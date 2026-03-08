# Member 도메인

## 역할
- `member/presentation/controller/MemberController`는 `/api/members`에서 활동 상태 변경, 삭제, 상태별 조회, 본인 상태 조회, 부서 변경, 부서 목록 API를 제공합니다.
- `AdminMemberService`는 관리자에 의한 활동 상태 변경/삭제(`ChangeActiveStatusUsecase`)를 담당하고, `QueryMemberService`는 상태별 조회 use case(`GetMembersByActiveStatus`, `GetMemberByActiveStatus`)를 구현합니다.
- `DepartmentService`는 `DepartmentUsecase`를 통해 멤버의 소속 부서를 관리하며, `CreateAdminMemberService`는 애플리케이션 기동 시 최초 관리자 계정을 자동으로 생성합니다.

## 주요 정책
1. **관리자 권한 검증**: `AdminMemberService.validateAdminPermission`은 작업자 ID로 `MemberRepository.findById`를 조회해 `isAdmin()`이 아니면 `DeniedMemberEditException`을 던집니다. 활동 상태 변경과 삭제 모두 같은 검증을 거칩니다.
2. **활동 상태 값 제한**: `ChangeActiveStatusRequest`는 Bean Validation으로 공백을 막고, `MemberModel.updateActiveStatus`는 `ActiveStatus` enum(`am`, `cm`, `rm`, `ob`) 이외의 값을 허용하지 않으며, `all`로 변경하려 하면 `DeniedUpdateActiveException`을 발생시킵니다.
3. **회원 삭제 흐름**: `delete`는 `MemberRepository.deleteById` 뿐 아니라 OAuth 계정(`OAuthMemberRepository`)까지 함께 삭제하고 `DeletedMemberEvent`를 발행해 후속 처리를 트리거합니다.
4. **조회 시 관리자 숨김**: `QueryMemberService`는 `findMembers` 및 `findMembersByActiveStatus` 결과에서 `isAdmin()`이 `true`인 객체를 필터링해 일반 사용자 리스트엔 노출하지 않습니다.
5. **부서 변경 규약**: `DepartmentUsecase.changeDepartment`는 `@RequestParam("to")`로 받은 영문명(예: `EVENT`, `PRESIDENT`)을 `Department.findDepartmentByEnName`으로 검증한 뒤 저장하며, 잘못된 이름이면 `NotFoundDepartmentException`이 발생합니다.
6. **최초 관리자 자동 생성**: `CreateAdminMemberService`의 `@PostConstruct` 로직은 DB에 관리자 멤버, 계정, OAuth 멤버가 모두 없을 때만 `AdminInfo` 설정으로 새로운 관리자/권한을 생성합니다.
7. **네이밍 포맷**: `MemberModel.MemberModelBuilder`는 `MemberNameFormatter`를 통해 이름과 기수를 합성하는 오버로드를 제공해 동일한 네이밍 규칙을 강제합니다.
8. **ID/존재 검증 포트**: `MemberRepository`는 `findById` 호출 시 찾지 못하면 `NotFoundMemberException`을 던지므로, 모든 서비스는 이미 존재하는 ID만 전달해야 합니다.

## 코드 컨벤션 및 구조
- **패키지 계층**: 다른 도메인과 동일하게 `presentation/docs`, `presentation/controller`, `application/dto|model|service|usecase|support`, `persistence` 패턴을 따릅니다.
- **Converter 패턴**: `MemberEntityConverter`는 엔터티 ↔ 모델, `CommandMemberResponseConverter`/`QueryMemberResponseConverter`는 API 응답 DTO를 조립합니다.
- **Use case 인터페이스**: `ChangeActiveStatusUsecase`, `DepartmentUsecase` 등으로 서비스 계약을 외부에 노출하고, 컨트롤러는 구체 구현 대신 인터페이스에 의존합니다.
- **엔터티 규칙**: `MemberEntity`는 `member_*` prefix와 JPA 인덱스(`name`, `active_status`)를 사용하며, `ActiveStatus`와 `Department`는 Enum으로 매핑합니다.
- **인증 연계**: `CreateAdminMemberService`는 `AccountJpaRepository`, `AuthorityRepository`, `OAuthMemberRepository`와 연동해 멤버-계정-권한을 동시에 구성하고 `Role.ROLE_ADMIN` 권한을 부여합니다.
- **검증 책임 분리**: Bean Validation(`ChangeActiveStatusRequest`, `ChangeDepartment` 파라미터)와 도메인 검증(`MemberModel.canEdit`)을 분리해 유지보수성을 높입니다.

## 연관 컴포넌트
- `member/application/dto/*` : 멤버 리스트, 단건, 부서 응답 스펙.
- `member/application/model/*` : `ActiveStatus`, `Department`, `MemberModel`, `AdminInfo`.
- `member/persistence/*Repository` : JPA 저장소 어댑터(`JpaMemberRepository`, `JpaMemberCustomRepository`, `MemberRepositoryImpl`).
