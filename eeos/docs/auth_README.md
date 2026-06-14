# Auth 도메인

## 역할

- `auth/presentation/controller/AuthController`는 `/api/auth`에서 회원가입, 토큰 재발급, 로그아웃, 회원탈퇴 API를 제공합니다.
- `EeosSignUpUseCase`는 ID/PW 방식 회원가입 로직을 담당하며, 일반 가입/Slack 연동 가입을 메서드 오버로드로 분기합니다.
- `OAuthSignUpUseCase`는 Slack OAuth2 로그인 후 추가 정보 입력을 통한 신규 회원 등록을 담당합니다.

## API 명세

### POST `/api/auth/signup`

ID/PW 방식으로 회원가입하고 토큰을 발급합니다.
`slackMemberId`를 포함하면 기존 Slack OAuth2 회원에 EEOS 계정을 연결합니다.

- 인증: 불필요

#### 요청

```json
{
  "id": "econovation2025",
  "password": "eeos1234",
  "generation": 30,
  "name": "김에코",
  "activeStatus": "am",
  "slackMemberId": "U08ABCDE123"
}
```

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| `id` | string | O | 로그인 아이디 (최대 50자) |
| `password` | string | O | 비밀번호 (영문+숫자 조합 8~20자) |
| `generation` | integer | O | 에코노베이션 기수 (1 이상) |
| `name` | string | O | 회원 성함 (최대 50자) |
| `activeStatus` | string | O | 활동 상태: `am`, `cm`, `rm`, `ob` 중 하나 |
| `slackMemberId` | string | X | Slack 회원 ID. Slack OAuth2만 가입된 회원이 계정 연결 시 사용 |

#### 동작 분기

| `slackMemberId` 여부 | 동작 |
|---------------------|------|
| 없음 | 새 Member + Account 생성 (`EeosSignUpUseCase`) |
| 있음 | 기존 Slack OAuth 회원에 Account 연결 (`EeosSignUpUseCase#signUp(command, slackMemberId)`) |

#### 응답

- `201 Created`: 회원가입 성공, 응답 바디에 accessToken 반환, 쿠키에 refreshToken 설정

#### 오류

| HTTP | 코드 | 메시지 |
|------|------|--------|
| 400 | 4100 | 아이디는 필수 입력값입니다 |
| 400 | 4101 | 아이디는 50자 이하여야 합니다 |
| 400 | 4102 | 비밀번호는 필수 입력값입니다 |
| 400 | 4103 | 비밀번호는 8~20자이며, 영문과 숫자를 포함해야 합니다 |
| 400 | 4104 | 기수는 필수 입력값입니다 |
| 400 | 4105 | 기수는 1 이상이어야 합니다 |
| 400 | 4106 | 성함은 필수 입력값입니다 |
| 400 | 4107 | 성함은 50자 이하여야 합니다 |
| 400 | 4108 | 활동 상태는 필수 입력값입니다 |
| 400 | 4109 | 활동 상태는 am, cm, rm, ob 중 하나여야 합니다 |
| 400 | 3001 | {status}는 존재하지 않는 활동 상태입니다 |
| 404 | 4200 | 존재하지 않는 Slack 회원 ID입니다 |
| 409 | 4201 | 이미 계정이 연결된 회원입니다 |
| 409 | 4009 | 이미 사용 중인 아이디입니다 |

---

### POST `/api/auth/reissue`

리프레시 토큰을 사용해 새로운 AT/RT를 발급합니다.

- 인증: 쿠키 내 refreshToken 필요
- Web 클라이언트: 새 AT/RT 쿠키를 재설정합니다.
- App 클라이언트: 응답 바디에 accessToken을 반환합니다.

#### 오류

| HTTP | 코드 | 메시지 |
|------|------|--------|
| 401 | 4003 | 만료된 토큰입니다 |
| 401 | 4004 | 블랙리스트에 등록된 토큰입니다 |

---

### POST `/api/auth/logout`

리프레시 토큰을 블랙리스트에 등록하여 로그아웃합니다.

- 인증: JWT 필수
- Web 클라이언트: AT/RT 쿠키도 함께 삭제합니다.

---

### POST `/api/auth/withdraw`

쿠키에 담긴 리프레시 토큰을 이용하여 회원을 탈퇴합니다.

- 인증: JWT 필수

---

### POST `/api/auth/login/additional-info`

Slack OAuth2 로그인 후 추가 정보(성함, 기수, 활동상태)를 제출하여 신규 회원으로 등록합니다.

- 인증: `verificationId` 쿠키 필요 (OAuth 콜백 이후 발급)

## 주요 정책

1. **회원가입 분기**: `slackMemberId` 유무로 신규 가입과 Slack 연동 가입을 분기합니다.
2. **Slack 연동 가입 검증**: `slackMemberId`가 DB에 없으면 4200, 이미 Account가 연결된 경우 4201 에러를 반환합니다.
3. **클라이언트 타입 분기**: `Client-Type` 헤더 또는 refreshToken에 저장된 클라이언트 타입으로 Web/App을 구분하여 토큰 전달 방식을 결정합니다.
4. **토큰 블랙리스트**: 로그아웃/탈퇴 시 Redis에 refreshToken을 블랙리스트로 등록해 재사용을 방지합니다.

## 연관 컴포넌트

- `auth/application/usecase/*` : `EeosSignUpUseCase`, `OAuthSignUpUseCase`, `LoginUsecase`, `ReissueUsecase`, `LogOutUsecase`, `WithDrawUsecase`
- `auth/application/exception/AlreadyLinkedAccountException` : 에러 코드 4201
- `auth/application/exception/SlackMemberNotFoundException` : 에러 코드 4200
- `auth/presentation/dto/EeosSignUpRequest` : 회원가입 요청 DTO

---

## OAuth 클라이언트 등록 (ADMIN 전용)

### POST `/api/v2/auth/clients`

OAuth2 클라이언트를 등록한다. `WEB` 타입은 BCrypt 해시된 `clientSecret`이 발급되고, `APP` 타입은 발급되지 않는다.

- 인증: JWT 필수 (ADMIN 역할)
- 구현: `ClientController` → `ClientService`

#### 요청

```json
{
  "clientName": "eeos-web-app",
  "clientType": "WEB",
  "redirectUris": [
    "https://eeos.econovation.kr/callback",
    "http://localhost:3000/callback"
  ]
}
```

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| `clientName` | string | O | 클라이언트 이름 (공백 불가) |
| `clientType` | string | O | `WEB` 또는 `APP` (대소문자 무관) |
| `redirectUris` | string[] | O | 허용 리다이렉트 URI 목록. 1개 이상, 최대 10개, URI당 최대 512자 |

#### clientType 동작 차이

| clientType | 기밀 클라이언트 | clientSecret 발급 |
|------------|--------------|-----------------|
| `WEB` | O | O (BCrypt 해시 저장, 원본 1회 반환) |
| `APP` | X | X (null 반환) |

#### 응답

**HTTP 201 Created**

`clientSecret`는 `WEB` 타입일 때만 포함된다. 이후 재조회 불가 — 최초 응답에서 반드시 저장할 것.

```json
{
  "success": true,
  "code": "CREATE",
  "data": {
    "clientId": "a3f7c2d1-85b4-4e9a-bf32-1c0e7d9fa821",
    "clientSecret": "xKz3Qp9mRvLs7wNt2YhJ4dUiOeAn0BfCgXvPqWmE5c"
  }
}
```

#### 오류

| HTTP | 코드 | 메시지 | 발생 조건 |
|------|------|--------|-----------|
| 400 | 4015 | 등록되지 않은 redirect URI입니다. | `redirectUris`가 비어있거나 10개 초과, 또는 URI가 512자 초과 |
| 403 | — | 관리자 권한 필요 | ADMIN 역할 없음 |

#### 보안 설정 근거

`SecurityFilterChainConfig`의 `authenticated` 체인에서 다음과 같이 설정되어 있다.

```java
// 매처: POST /api/v2/auth/clients를 authenticated 체인에 포함
.requestMatchers(HttpMethod.POST, "/api/v2/auth/clients")

// 권한: ADMIN 역할만 허용
requests.requestMatchers(HttpMethod.POST, "/api/v2/auth/clients").hasAnyRole(ADMIN);
```

#### 연관 컴포넌트

- `auth/presentation/controller/ClientController` : 진입점
- `auth/presentation/docs/ClientApi` : Swagger 인터페이스 (`@Tag`, `@Operation`, `@ApiResponses`)
- `auth/application/service/ClientService` : 등록 로직, 시크릿 생성 (`SecureRandom`, 32바이트, Base64url)
- `auth/application/domain/ClientType` : `WEB(confidential=true)`, `APP(confidential=false)`
- `auth/application/exception/InvalidRedirectUriException` : 에러 코드 4015
- `auth/persistence/client/ClientEntity` : JPA 엔티티 (클라이언트 + 리다이렉트 URI)
