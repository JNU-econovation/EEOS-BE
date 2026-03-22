# Web/App 인증 분리 + PKCE 구현 계획

**Goal:** Web/App 클라이언트를 분리하여 Web은 쿠키 기반, App은 Authorization Code + PKCE 기반 인증을 지원한다.

**Spec:** `docs/superpowers/specs/2026-03-22-web-app-auth-separation-design.md`

## 진행 상황

### Chunk 1: Client 등록 (완료)
- [x] Task 1: ClientType enum
- [x] Task 2: ClientEntity + Repository
- [x] Task 3: Client exceptions
- [x] Task 4: ClientService
- [x] Task 5: ClientController + Swagger

### Chunk 2: Token/PKCE (완료)
- [x] Task 6: TokenProvider clientType/clientId claims
- [x] Task 7: PkceValidator
- [x] Task 8: AuthorizationCodeRepository

### Chunk 3: Rate Limiting + Cookie (완료)
- [x] Task 9: LoginRateLimiter
- [x] Task 10: AuthCookieManager update

### Chunk 4: 새 엔드포인트 (완료)
- [x] Task 11: AuthenticationTokenGenerator extend
- [x] Task 12: OAuth2LoginService
- [x] Task 13: TokenExchangeService
- [x] Task 14: OAuth2Controller + Swagger

### Chunk 5: 분기/정리 (진행중)
- [x] Task 15: ReissueService Web/App branching
- [x] Task 16: Logout Web/App branching
- [x] Task 17: @Deprecated 처리 (Slack OAuth 엔드포인트)
- [ ] Task 18: InterceptorConfig update
- [ ] Task 19: Application config
- [ ] Task 20: Flyway migration for oauth_client tables
- [ ] Task 21: Final verification
