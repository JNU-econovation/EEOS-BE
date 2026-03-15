# Slack Event 도메인

## 개요
- `slackEvent/presentation/controller/SlackEventController`는 Slack Events API 웹훅(`POST /api/slack/events`)을 수신합니다.
- 요청은 `SlackRequestSignatureVerifier`로 서명/타임스탬프 검증 후 처리됩니다.
- `event_callback` 중 `message`(subtype 없음) 이벤트만 EEOS 내부 API로 전달하고, 중복 이벤트는 Redis 기반 idempotency로 차단합니다.

## API 명세

### POST `/api/slack/events`
- 인증: JWT 없음 (`SecurityFilterChainConfig` nonAuthenticated 체인)
- 필수 헤더:
  - `X-Slack-Request-Timestamp`
  - `X-Slack-Signature`
- 요청 본문 타입:
  - `url_verification`: `{"type":"url_verification","challenge":"..."}`
  - `event_callback`: Slack 이벤트 envelope

### 응답
- `url_verification`: `200 OK`, 본문 `{"challenge":"..."}`
- `event_callback`: `200 OK`, 본문 `{"accepted":true,"eventId":"...","status":"..."}`
  - `FORWARDED`
  - `IGNORED_UNSUPPORTED`
  - `IGNORED_FILTERED`
  - `IGNORED_DUPLICATE`
- 잘못된 요청(파라미터/타입 오류): `400 Bad Request`
- 서명 오류/리플레이 공격: `401 Unauthorized`
- 내부 API 전달 실패: `500 Internal Server Error` (Slack 재전송 유도)

## 보안 규칙
- 서명 검증 방식: `v0:{timestamp}:{rawBody}` 기반 HMAC-SHA256
- 허용 시간 오차: 기본 300초 (`slack.event.allowed-time-skew-seconds`)
- 상수 시간 비교(`MessageDigest.isEqual`)로 서명 비교

## idempotency 규칙
- 저장소: Redis (`RedisSlackEventDedupRepository`)
- 키 전략:
  - `slack:event:lock:{eventId}` (처리 락, TTL 30초)
  - `slack:event:processed:{eventId}` (처리 완료, TTL 7일)
- 처리 흐름:
  1. `processed` 존재 시 즉시 `IGNORED_DUPLICATE`
  2. lock 획득 실패 시 `IGNORED_DUPLICATE`
  3. 내부 API 전달 성공 시 `processed` 마킹
  4. 성공/실패와 관계없이 lock 해제

## 환경 변수
- `SLACK_SIGNING_SECRET`: Slack Signing Secret
- `SLACK_ALLOWED_TIME_SKEW_SECONDS`: 요청 허용 시간 오차(초)
- `EEOS_SLACK_FORWARD_BASE_URL`: 내부 전달 API Base URL
- `EEOS_SLACK_FORWARD_PATH`: 내부 전달 API Path
- `EEOS_SLACK_FORWARD_API_KEY`: 내부 전달 API 인증 키 (`X-EEOS-API-KEY`)

## Slack App 설정 체크리스트
1. Slack App 생성 후 Bot을 대상 채널에 초대합니다.
2. Event Subscriptions 활성화 후 Request URL을 `/api/slack/events`로 설정합니다.
3. Subscribe to bot events에 `message.channels`를 추가합니다.
4. Signing Secret을 `SLACK_SIGNING_SECRET`에 설정합니다.
5. 내부 전달 API가 인증이 필요하면 `EEOS_SLACK_FORWARD_API_KEY`를 설정합니다.
