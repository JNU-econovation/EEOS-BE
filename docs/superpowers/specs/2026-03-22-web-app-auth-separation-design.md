# Web/App 인증 분리 + PKCE 설계

## 배경

- `auth.econovation.kr`은 여러 서비스가 공유하는 인증 서버 (현재는 EEOS와 미분리 상태)
- 웹은 쿠키로 AT/RT 전달 가능하지만, 앱은 도메인이 달라 쿠키 수신 불가
- 앱을 위해 OAuth2.0 Authorization Code + PKCE 방식 도입
- Slack OAuth 완전 제거, 자체 로그인(email/password)만 지원
- 앱은 시스템 브라우저(Chrome Custom Tab, ASWebAuthenticationSession)를 통해 /authorize에 접근 (RFC 8252 — WebView 사용 금지)

## 결정 사항

| 항목 | 결정 |
|------|------|
| Client 등록 | 동적 등록 API 제공 (관리자 권한) |
| Web/App 구분 | client_id + redirect_uri로 판별 |
| Client 인증 | Web: client_secret (confidential), App: PKCE만 (public, secret 미발급) |
| Authorization code 저장 | Redis (TTL 60초, 일회용, 128bit 이상 SecureRandom) |
| PKCE | App 필수, S256만 지원 |
| RT rotation | 기존 유지 (Redis 블랙리스트) |
| CSRF 방지 | state 파라미터 필수 |
| reissue/logout 분기 | RT 내부 clientType 클레임 기준 |
| /token Content-Type | application/x-www-form-urlencoded (RFC 6749 표준) |

---

## 1. Client 등록 모델 + API

### Entity

```
ClientEntity {
  id: Long (PK)
  clientId: String (UUID, 자동생성, unique)
  clientSecret: String (BCrypt hashed, WEB만 존재. APP은 null)
  clientName: String
  clientType: enum WEB | APP
  redirectUris: Set<String>  // 허용된 redirect URI 목록 (최대 10개, URI당 최대 512자)
  createdAt: LocalDateTime
}
```

### API

```
POST /api/auth/clients
권한: 관리자(isAdmin)
Request: { clientName, clientType, redirectUris }
Response:
  - WEB: { clientId, clientSecret }  <- 최초 1회만 평문 노출
  - APP: { clientId }                <- secret 미발급
```

- WEB (Confidential Client): clientSecret 발급, BCrypt 해시 저장
- APP (Public Client): clientSecret 미발급, PKCE가 유일한 보안 메커니즘 (RFC 8252, RFC 9700 준수)
- clientId는 UUID 자동생성

---

## 2. 인증 흐름

### 공통 진입점

```
GET /api/auth/authorize
  ?client_id={clientId}
  &redirect_uri={redirectUri}
  &response_type=code
  &state={randomString}           <- 필수 (CSRF 방지)
  &code_challenge={hash}          <- APP만 필수
  &code_challenge_method=S256     <- APP만 필수
```

서버: client_id로 클라이언트 조회 -> redirect_uri 검증 -> response_type 검증 -> 로그인 페이지로 redirect (파라미터를 query string으로 전달)

참고: Web 클라이언트는 response_type=code로 요청하지만, 실제로는 code를 발급하지 않고 쿠키를 직접 발급한다.
response_type 파라미터는 OAuth2 표준 형식 준수 목적으로 유지하며, Web의 분기는 clientType 기준이다.

### 사용자 인증 단계

`/authorize`는 직접 인증을 수행하지 않는다. 파라미터 검증 후 로그인 페이지로 리다이렉트한다.
Slack OAuth가 없으므로 외부 콜백이 없고, Redis 인증 세션이 필요 없다.

로그인 페이지의 form:

```html
<form action="/api/auth/login" method="POST">
  <input type="hidden" name="client_id" value="{clientId}" />
  <input type="hidden" name="redirect_uri" value="{redirectUri}" />
  <input type="hidden" name="state" value="{state}" />
  <input type="hidden" name="code_challenge" value="{codeChallenge}" />
  <input type="hidden" name="code_challenge_method" value="S256" />

  <input type="text" name="email" />
  <input type="password" name="password" />
  <button type="submit">로그인</button>
</form>
```

서버 `/api/auth/login` 처리 (순서 고정 — redirect 전에 반드시 검증 완료):

```
1. client_id로 DB에서 클라이언트 조회 (실패 → 400, redirect 안 함)
2. redirect_uri가 해당 클라이언트의 허용 목록에 있는지 재검증 (실패 → 400, redirect 안 함)
   (hidden field는 조작 가능하므로 /authorize에서 검증했더라도 반드시 재검증)
3. credentials 검증
   - 실패 → 로그인 페이지로 303 Redirect (쿼리 파라미터로 hidden fields + 에러 메시지 전달)
     303 → /login-page?client_id={}&redirect_uri={}&state={}&code_challenge={}&error=invalid_credentials
     (Redis 의존 없이 상태 유지. code_challenge는 공개값이므로 URL 노출 무관)
   - 사용자가 "취소" 버튼 클릭 → redirect_uri?error=access_denied&state={state}
   ※ redirect_uri 검증 완료 상태이므로 redirect 안전
4. clientType에 따라 분기
```

### Web 경로

```
1. 사용자 인증 성공 (clientType=WEB)
2. AT/RT를 쿠키에 설정 (기존과 동일)
   RT에 clientType=WEB, clientId 클레임 포함
3. redirect_uri?state={state} 로 303 See Other Redirect
```

### App 경로

```
1. 사용자 인증 성공 (clientType=APP)
2. authorization_code 생성 (128bit 이상 SecureRandom) -> Redis 저장 (TTL 60초)
   key: "auth_code:{code}"
   value: { memberId, clientId, codeChallenge, codeChallengeMethod, redirectUri }
3. redirect_uri?code={authorization_code}&state={state} 로 303 See Other Redirect
4. 앱이 토큰 교환 요청:

POST /api/auth/token
Content-Type: application/x-www-form-urlencoded

grant_type=authorization_code&code={code}&code_verifier={verifier}&redirect_uri={uri}&client_id={clientId}

5. 서버: client_id로 클라이언트 조회 (APP 타입 확인)
   -> code로 Redis 조회 -> DEL(일회성)
   -> code의 clientId와 요청 client_id 일치 검증
   -> redirect_uri 일치 검증 -> PKCE 검증
   -> AT/RT 발급 (RT에 clientType=APP, clientId 클레임 포함)
   -> JSON body 반환
```

### 토큰 교환 응답 (App)

```json
{
  "access_token": "...",
  "refresh_token": "...",
  "token_type": "Bearer",
  "expires_in": 3600
}
```

---

## 3. PKCE

```
1. 앱이 code_verifier 생성 (43~128자 랜덤 문자열)
2. code_challenge = BASE64URL(SHA256(code_verifier))
3. /authorize 요청 시 code_challenge + method=S256 전달
4. 로그인 폼의 hidden field로 전달 -> Redis auth_code에 저장
5. /token 요청 시 code_verifier 전달
6. 서버: BASE64URL(SHA256(code_verifier)) == 저장된 code_challenge 검증
```

- Web: PKCE 불필요 (쿠키 직접 발급, code 교환 없음)
- App: PKCE 필수. code_challenge 없으면 /authorize 거부
- code_challenge_method: S256만 지원 (plain 미지원)

---

## 4. RT 기반 분기 (reissue / logout / withdraw)

### 원칙

RT 발급 시 `clientType`, `clientId` 클레임을 포함한다.
분기는 RT 내부 정보 기준이며, 외부 신호(body의 client_id 유무 등)에 의존하지 않는다.

### reissue

```
POST /api/auth/reissue

1. RT 추출 (충돌 방지)
   - 쿠키에 RT 있고 body에도 RT 있음 -> 400 (ambiguous_request) 거부
   - 쿠키에만 RT 있음 -> 쿠키에서 추출 (WEB 경로)
   - body에만 RT 있음 -> body에서 추출 (APP 경로, client_id 필수)
   - 둘 다 없음 -> 401

2. RT 내부의 clientType 확인

3. clientType == WEB:
   - RT가 쿠키에서 온 게 아니면 거부
   - 새 AT/RT -> 쿠키로 응답

4. clientType == APP:
   - body의 client_id와 RT 내부 clientId 일치 확인
   - 새 AT/RT -> JSON body로 응답
```

### logout

```
POST /api/auth/logout

1. RT 추출 (reissue와 동일 로직)
2. RT 내부 clientType 확인
3. WEB: RT 블랙리스트 등록 + 쿠키 삭제
4. APP: RT 블랙리스트 등록 (쿠키 삭제 생략)
```

### withdraw

기존 로직 유지 + RT 추출 방식만 위와 동일하게 분기

---

## 5. Failure Design

### 에러 흐름

```
[/api/auth/authorize]
  -> client_id 미존재 -> 400 (invalid_client)
  -> redirect_uri 미등록 -> 400 (invalid_redirect_uri) <- redirect 하면 안 됨
  -> response_type != "code" -> 400 (unsupported_response_type)
  -> state 누락 -> 400 (invalid_request)
  -> APP인데 code_challenge 누락 -> 400 (code_challenge_required)

[/api/auth/login] (처리 순서 = 에러 우선순위)
  -> 1. client_id 미존재 -> 400 (invalid_client, redirect 안 함)
  -> 2. redirect_uri 재검증 실패 -> 400 (invalid_redirect_uri, redirect 안 함)
  -> 3a. credentials 인증 실패 -> 로그인 페이지로 303 Redirect (쿼리 파라미터로 에러+hidden fields 전달)
  -> 3b. 사용자 취소 -> redirect_uri?error=access_denied&state={state}
     ※ redirect_uri 검증 완료 후이므로 안전

[/api/auth/token]
  -> client_id 누락 또는 미존재 -> 400 (invalid_client)
  -> code의 clientId와 요청 client_id 불일치 -> 400 (invalid_client)
  -> code 만료 (Redis TTL) -> 400 (invalid_grant)
  -> code 이미 사용됨 (Redis에 없음) -> 400 (invalid_grant)
  -> redirect_uri 불일치 -> 400 (invalid_grant)
  -> PKCE 검증 실패 -> 400 (invalid_grant)
  -> grant_type 미지원 -> 400 (unsupported_grant_type)

[/api/auth/reissue]
  -> 쿠키와 body 모두 RT 존재 -> 400 (ambiguous_request)
  -> RT 없음 (쿠키, body 모두) -> 401
  -> RT 만료/블랙리스트 -> 401
  -> WEB인데 쿠키가 아닌 경로로 RT 전달 -> 401
  -> APP인데 client_id 불일치 -> 401

[/api/auth/clients]
  -> 비관리자 요청 -> 403
  -> redirectUris 빈 값 -> 400
  -> redirectUris 10개 초과 -> 400
  -> redirectUri 512자 초과 -> 400
  -> clientType 잘못된 값 -> 400
```

### 보안 규칙

| 규칙 | 이유 |
|------|------|
| redirect_uri 불일치 시 redirect 하지 않고 에러 응답 | open redirect 공격 방지 |
| /login에서 client_id + redirect_uri 재검증 | hidden field 조작 대응 |
| code 재사용: Redis DEL로 소멸 | 60초 TTL + 일회성으로 차단 |
| PKCE 실패 시 구체적 이유 미노출 | code_verifier 추측 공격 방지 |
| RT 내부 clientType으로 분기 | 외부 신호 조작 방지 |
| reissue 시 쿠키+body 동시 RT 거부 | 경로 혼란 공격 방지 |

### Rate Limiting

`/api/auth/login`은 brute force 공격 대상이므로 시도 횟수를 제한한다.

```
정책:
- IP당: 분당 20회 초과 시 429 (Too Many Requests)
- 계정당: 5회 연속 실패 시 5분간 해당 계정 잠금 (lockout)
- lockout 해제: 5분 경과 후 자동 해제
- 구현: Redis 카운터 (key: "login_fail:{email}" 또는 "login_fail:{ip}", TTL: 60초/300초)
- lockout 에러 응답: 일반 credentials 실패와 동일한 메시지 사용 (계정 존재 여부 노출 방지)
```

### 쿠키 속성 (Web 경로)

Web 경로에서 AT/RT를 쿠키로 전달할 때의 속성을 명시한다.
state 파라미터는 OAuth 인증 흐름의 CSRF만 방지하며, 인증 이후 API 호출의 CSRF는 쿠키 속성으로 방어한다.

```
AT 쿠키:
  Name: eeos_access_token
  HttpOnly: true       (JavaScript 접근 차단)
  Secure: true         (HTTPS만)
  SameSite: Lax        (cross-site POST 차단, 같은 사이트 GET 허용)
  Path: /api
  Domain: ${COOKIE_TOKEN_DOMAIN}
  Max-Age: AT 유효시간

RT 쿠키:
  Name: eeos_refresh_token
  HttpOnly: true
  Secure: true
  SameSite: Lax
  Path: /api/auth           (reissue, logout, withdraw 모두에서 전송 필요)
  Domain: ${COOKIE_TOKEN_DOMAIN}
  Max-Age: RT 유효시간
```

참고: 기존 시스템은 SameSite=None이었으나, 보안 강화를 위해 Lax로 변경한다.
SameSite=Lax는 cross-site POST 요청에서 쿠키가 자동 전송되지 않아 CSRF를 방어한다.

### 선택적 보강 (현재 미적용)

- **code_challenge hidden field 조작 방어**: 공격자가 code_challenge를 자기 값으로 바꿔치기하면 자기 code_verifier로 토큰 교환 가능. 단, redirect_uri가 등록값으로 고정되어 있어 code 가로채기가 어려우므로 실질적 위험은 낮음. 필요 시 /authorize에서 code_challenge를 서명된 쿠키나 HMAC 토큰으로 보호하여 /login에서 대조 가능.

### Redis 장애 시

```
Redis 장애 시:
├─ Web 신규 로그인: 가능 (Redis 미사용)
├─ App 신규 로그인: 불가 (authorization_code 저장 불가)
├─ RT 갱신 (Web/App 공통): RT 블랙리스트 확인 불가
│   -> 안전 우선 정책: 갱신 거부
│   -> 가용성 우선 정책: 갱신 허용 (탈취된 RT 차단 불가 감수)
├─ Rate Limiting: 카운터 확인 불가
│   → 가용성 우선 정책: Rate Limit 없이 허용 (brute force 노출 감수)
│   ※ Redis 장애가 brute force 공격 창구가 될 수 있음을 인식
├─ 기존 AT로 API 호출: 영향 없음 (AT 만료 전까지)
└─ logout/withdraw: RT 블랙리스트 등록 불가
```

---

## 6. 엔드포인트 정리

### 새 엔드포인트

| Method | Path                   | 용도                                                    |
|--------|------------------------|---------------------------------------------------------|
| POST   | `/api/auth/clients`    | Client 등록 (관리자)                                    |
| GET    | `/api/auth/authorize`  | 파라미터 검증 -> 로그인 페이지 redirect                 |
| POST   | `/api/auth/login`      | 자체 로그인 인증 + Web/App 분기                         |
| POST   | `/api/auth/token`      | code -> AT/RT 교환 (App, x-www-form-urlencoded)         |
| POST   | `/api/auth/reissue`    | RT 갱신 (RT 내부 clientType으로 분기)                   |
| POST   | `/api/auth/logout`     | 로그아웃 (RT 내부 clientType으로 분기)                  |
| POST   | `/api/auth/withdraw`   | 회원탈퇴                                                |

### Deprecated 대상

| Method | Path                                 | 상태                                        |
|--------|--------------------------------------|---------------------------------------------|
| POST   | `/api/auth/login/{oauthServerType}`  | @Deprecated (Slack OAuth, 추후 제거 예정)   |
| POST   | `/api/auth/login` (기존 버전)        | @Deprecated (새 `/api/auth/login`로 대체) |
