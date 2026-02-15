# Comment 도메인

## 역할
- `comment/presentation/controller/CommentController`는 `POST/PUT/DELETE/GET /api/comments` 엔드포인트를 통해 프로그램/발표팀 단위의 Q&A 쓰레드를 제공합니다.
- `CommentService`는 댓글 생성·수정·삭제·조회 use case를 모두 구현하고, 프로그램(`ProgramRepository`)과 팀(`TeamRepository`) 존재 여부 및 작성자 권한을 검증합니다.
- `CommentResponseConverter`는 상위 댓글과 자식 댓글을 묶어서 응답 DTO(`QueryCommentsResponse`)로 가공해 UI가 바로 계층 구조를 렌더링할 수 있게 합니다.

## 주요 정책
1. **관리자 작성 금지**: `validateUser`에서 `QueryMemberService`로 작성자를 조회해 `isAdmin()`이면 `NotCreateAdminCommentException`을 던집니다.
2. **프로그램/팀 유효성 검증**: `createValidate`가 `teamRepository.existsById`와 `programRepository.existsById`를 확인하며, 존재하지 않으면 `NotFoundTeamException`/`NotFoundProgramException`을 발생시킵니다.
3. **최대 2단계 쓰레드**: `superCommentId`가 `-1`이면 루트 댓글로 간주하고, 대댓글의 대댓글은 `changeSuperComment`로 최상위 댓글의 ID로 치환해 쓰레드 깊이를 제한합니다.
4. **작성자만 편집/삭제**: `CommentModel.validateUpdate`/`validateDelete`가 `writer`와 요청자 ID를 비교하지 않으면 `DeniedCommentEditException`을 던집니다.
5. **댓글 조회 제약**: `getComments`는 `programId`와 `teamId`가 `null`이면 바로 `NullPointerException`을 던져 필수 파라미터를 강제하고, 생성일 오름차순으로 정렬된 상위 댓글만 반환합니다.
6. **소프트 삭제**: `CommentEntity`는 `@SQLDelete`와 `@Where`를 사용해 `DELETE` 대신 `is_deleted` 플래그를 업데이트하며 물리 삭제를 피합니다.
7. **컨텐츠 길이**: `CommentModel`에 `contentLimitLength`가 정의되어 있지만 현재 `isExceedLengthLimit`가 항상 `false`를 반환하도록 고정돼 있어 길이 제한을 적용하지 않는 것이 현 정책입니다.
8. **댓글 유형**: `CommentType`으로 `ANONYMOUS`/`NON_ANONYMOUS`를 명시해야 하며, 익명 댓글도 작성자 정보는 저장되지만 노출 여부는 프런트 정책에 따릅니다.
9. **권한 표기**: `CommentModel.getAccessRight`는 요청자와 작성자 일치 여부에 따라 `AccessRights.EDIT` 또는 `READ_ONLY`를 내려 다른 도메인과 일관성을 유지합니다.

## 코드 컨벤션 및 구조
- **레이어드 패키지**: `presentation`(API), `application`(dto/model/usecase/service), `persistence`(Entity/Repository)로 패키지를 분리하고, 컨트롤러는 `docs/CommentApi` 인터페이스를 구현합니다.
- **Converter 패턴**: 요청 DTO → 모델(`CommentModelConverter`), 모델 ↔ 엔터티(`CommentEntityConverter`), 모델 → 응답 DTO(`CommentResponseConverter`) 전환을 각각의 컴포넌트가 맡습니다.
- **ID 명명 규칙**: 모든 컬럼은 `comment_*` prefix를 사용하며 `ENTITY_PREFIX` 상수로 관리합니다.
- **기본 상태 값**: 최상위 댓글은 `superCommentId = -1L`로 고정하는 것이 암묵적 규칙이므로 API 사용 시 parent ID를 -1로 보내야 합니다.
- **검증 책임 위치**: 비즈니스 검증(권한, 존재 확인)은 `CommentService`에, 단순 Bean Validation은 DTO(`CreateCommentRequest`, `UpdateCommentRequest`)에 둡니다.

## 연관 컴포넌트
- `comment/application/dto/*Response` : 조회 응답 스펙 정의.
- `comment/application/usecase/*Usecase` : 각 기능을 describe한 포트.
- `comment/persistence/CommentRepository` : 프로그램·팀별 댓글 목록, 부모 댓글 기준 조회, 내용 업데이트 쿼리 제공.
