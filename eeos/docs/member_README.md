# Member 도메인

## 역할
- `member/presentation/controller/MemberController`는 `/api/members`에서 활동 상태 변경, 삭제, 상태별 조회, 본인 상태 조회, 부서 변경, 부서 목록, Slack 회원가입 DM 발송 API를 제공합니다.
- `AdminMemberService`는 관리자에 의한 활동 상태 변경/삭제(`ChangeActiveStatusUsecase`)를 담당하고, `QueryMemberService`는 상태별 조회 use case(`GetMembersByActiveStatus`, `GetMemberByActiveStatus`)를 구현합니다.
- `DepartmentService`는 `DepartmentUsecase`를 통해 멤버의 소속 부서를 관리하며, `CreateAdminMemberService`는 애플리케이션 기동 시 최초 관리자 계정을 자동으로 생성합니다.
- `SendSignupLinkService`는 Slack 전용 회원 전체 발송(`SendSignupLinkToSlackOnlyMembersUsecase`), 단건 발송(`SendSignupLinkToSlackOnlyMemberUsecase`), 기수별 발송(`SendSignupLinkToSlackOnlyMembersByGenerationUsecase`) 세 유스케이스를 구현합니다.

## 주요 정책
1. **관리자 권한 검증**: `AdminMemberService.validateAdminPermission`은 작업자 ID로 `MemberRepository.findById`를 조회해 `isAdmin()`이 아니면 `DeniedMemberEditException`을 던집니다. 활동 상태 변경과 삭제 모두 같은 검증을 거칩니다.
2. **활동 상태 값 제한**: `ChangeActiveStatusRequest`는 Bean Validation으로 공백을 막고, `MemberModel.updateActiveStatus`는 `ActiveStatus` enum(`am`, `cm`, `rm`, `ob`) 이외의 값을 허용하지 않으며, `all`로 변경하려 하면 `DeniedUpdateActiveException`을 발생시킵니다.
3. **회원 삭제 흐름**: `delete`는 `MemberRepository.deleteById` 뿐 아니라 OAuth 계정(`OAuthMemberRepository`)까지 함께 삭제하고 `DeletedMemberEvent`를 발행해 후속 처리를 트리거합니다.
4. **조회 시 관리자 숨김**: `QueryMemberService`는 `findMembers` 및 `findMembersByActiveStatus` 결과에서 `isAdmin()`이 `true`인 객체를 필터링해 일반 사용자 리스트엔 노출하지 않습니다.
5. **부서 변경 규약**: `DepartmentUsecase.changeDepartment`는 `@RequestParam("to")`로 받은 영문명(예: `EVENT`, `PRESIDENT`)을 `Department.findDepartmentByEnName`으로 검증한 뒤 저장하며, 잘못된 이름이면 `NotFoundDepartmentException`이 발생합니다.
6. **최초 관리자 자동 생성**: `CreateAdminMemberService`의 `@PostConstruct` 로직은 DB에 관리자 멤버, 계정, OAuth 멤버가 모두 없을 때만 `AdminInfo` 설정으로 새로운 관리자/권한을 생성합니다.
7. **네이밍 포맷**: `MemberModel.MemberModelBuilder`는 `MemberNameFormatter`를 통해 이름과 기수를 합성하는 오버로드를 제공해 동일한 네이밍 규칙을 강제합니다.
8. **ID/존재 검증 포트**: `MemberRepository`는 `findById` 호출 시 찾지 못하면 `NotFoundMemberException`을 던지므로, 모든 서비스는 이미 존재하는 ID만 전달해야 합니다.
9. **Slack DM 발송 대상 필터링**: `findSlackOnlyMembers` / `findSlackOnlyMembersByGeneration`은 EEOS 계정(`Account`)이 없는 Slack OAuth 회원만 반환합니다. 단건 발송(`sendSignupLink`)은 추가로 `MemberModel.isSlackOnly()` 검증을 수행하며, 통과 실패 시 `NotSlackOnlyMemberException(409)`을 던집니다.
10. **DM 발송 실패 처리**: `SendSignupLinkService`는 개별 DM 발송 예외를 catch해 경고 로그만 남기고 계속 진행합니다. 일부 실패가 전체 요청을 중단시키지 않으며, 결과는 `successCount`/`failCount`로 집계해 반환합니다.

## 코드 컨벤션 및 구조
- **패키지 계층**: 다른 도메인과 동일하게 `presentation/docs`, `presentation/controller`, `application/dto|model|service|usecase|support`, `persistence` 패턴을 따릅니다.
- **Converter 패턴**: `MemberEntityConverter`는 엔터티 ↔ 모델, `CommandMemberResponseConverter`/`QueryMemberResponseConverter`는 API 응답 DTO를 조립합니다.
- **Use case 인터페이스**: `ChangeActiveStatusUsecase`, `DepartmentUsecase` 등으로 서비스 계약을 외부에 노출하고, 컨트롤러는 구체 구현 대신 인터페이스에 의존합니다.
- **엔터티 규칙**: `MemberEntity`는 `member_*` prefix와 JPA 인덱스(`name`, `active_status`)를 사용하며, `ActiveStatus`와 `Department`는 Enum으로 매핑합니다.
- **인증 연계**: `CreateAdminMemberService`는 `AccountJpaRepository`, `AuthorityRepository`, `OAuthMemberRepository`와 연동해 멤버-계정-권한을 동시에 구성하고 `Role.ROLE_ADMIN` 권한을 부여합니다.
- **검증 책임 분리**: Bean Validation(`ChangeActiveStatusRequest`, `ChangeDepartment` 파라미터)와 도메인 검증(`MemberModel.canEdit`)을 분리해 유지보수성을 높입니다.

## API 명세 (추가 엔드포인트)

모든 엔드포인트는 JWT 인증이 필요하며 관리자 권한을 요구합니다. 요청 본문은 없습니다.

공통 응답 스펙:

```json
{
  "totalCount": 12,
  "successCount": 11,
  "failCount": 1
}
```

| 필드 | 타입 | 설명 |
|------|------|------|
| `totalCount` | int | DM 발송 대상 총 인원 수 |
| `successCount` | int | DM 발송 성공 인원 수 |
| `failCount` | int | DM 발송 실패 인원 수 |

---

### POST `/api/members/admin/signup-dm`

Slack OAuth2로만 가입된(EEOS ID/PW 계정이 없는) 회원 **전체**에게 EEOS 회원가입 링크를 Slack DM으로 발송합니다.

#### 오류

| HTTP | 코드 | 메시지 |
|------|------|--------|
| 403 | 3004 | 관리자 권한이 없습니다 |

#### 내부 흐름

1. `validateAdminPermission`으로 호출자 관리자 여부 검증
2. `MemberRepository.findSlackOnlyMembers()`로 Account가 없는 Slack OAuth 회원 목록 조회
3. 각 회원 Slack ID로 DM 발송 (`SlackDmNotificationService.sendSignupLink`)
4. 성공/실패 집계 후 `SlackSignupDmResponse` 반환

---

### POST `/api/members/admin/signup-dm/{memberId}`

지정한 `memberId`의 회원이 Slack 전용 회원인 경우 해당 회원 **1인**에게 EEOS 회원가입 링크를 Slack DM으로 발송합니다.

| 파라미터 | 위치 | 타입 | 설명 |
|---------|------|------|------|
| `memberId` | path | Long | DM을 발송할 대상 회원 ID |

#### 오류

| HTTP | 코드 | 메시지 |
|------|------|--------|
| 403 | 3004 | 관리자 권한이 없습니다 |
| 404 | 3000 | 존재하지 않는 멤버입니다 |
| 409 | 4203 | Slack 전용 회원이 아닙니다 |

#### 내부 흐름

1. `validateAdminPermission`으로 호출자 관리자 여부 검증
2. `MemberRepository.findById(targetMemberId)`로 대상 회원 조회 (존재하지 않으면 `NotFoundMemberException`)
3. `MemberModel.isSlackOnly()` 검증 (EEOS 계정이 있으면 `NotSlackOnlyMemberException`)
4. Slack DM 발송 후 성공/실패 결과 반환

---

### POST `/api/members/admin/signup-dm/gen/{generation}`

지정한 `generation` 기수의 Slack 전용 회원 **전체**에게 EEOS 회원가입 링크를 Slack DM으로 발송합니다.

| 파라미터 | 위치 | 타입 | 설명 |
|---------|------|------|------|
| `generation` | path | int | 발송 대상 기수 (예: 40) |

#### 오류

| HTTP | 코드 | 메시지 |
|------|------|--------|
| 403 | 3004 | 관리자 권한이 없습니다 |

#### 내부 흐름

1. `validateAdminPermission`으로 호출자 관리자 여부 검증
2. `MemberRepository.findSlackOnlyMembersByGeneration(generation)`으로 해당 기수의 Slack 전용 회원 목록 조회
3. 각 회원 Slack ID로 DM 발송 (`SlackDmNotificationService.sendSignupLink`)
4. 성공/실패 집계 후 `SlackSignupDmResponse` 반환

---

## 연관 컴포넌트
- `member/application/dto/*` : 멤버 리스트, 단건, 부서, `SlackSignupDmResponse` 응답 스펙.
- `member/application/model/*` : `ActiveStatus`, `Department`, `MemberModel`, `AdminInfo`.
- `member/application/service/SendSignupLinkService` : Slack 전용 회원 조회 및 DM 발송 오케스트레이션. `SendSignupLinkToSlackOnlyMembersUsecase`, `SendSignupLinkToSlackOnlyMemberUsecase`, `SendSignupLinkToSlackOnlyMembersByGenerationUsecase` 세 유스케이스를 구현합니다.
- `member/application/service/SlackDmNotificationService` : Slack DM 전송 인프라 어댑터.
- `member/application/usecase/SendSignupLinkToSlackOnlyMembersUsecase` : 전체 발송 유스케이스 인터페이스.
- `member/application/usecase/SendSignupLinkToSlackOnlyMemberUsecase` : 단건 발송 유스케이스 인터페이스.
- `member/application/usecase/SendSignupLinkToSlackOnlyMembersByGenerationUsecase` : 기수별 발송 유스케이스 인터페이스.
- `member/persistence/*Repository` : JPA 저장소 어댑터(`JpaMemberRepository`, `JpaMemberCustomRepository`, `MemberRepositoryImpl`).
