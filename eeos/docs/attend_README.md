# Attend 도메인

## 역할
- `target/presentation/controller/AttendController`는 `/api/attend` 및 `/api/programs/{programId}/members` 계열 API를 제공해 참석 대상 조회, 상태 변경, 내 출석 이력/요약/벌점, 통계, TOP5 집계 등을 담당합니다.
- `AttendService`는 출석 상태 변경(`ChangeAttendStatusUsecase`), 상태 조회, 대상 멤버 조회, 벌점·통계 계산(`GetAttendantInfoUsecase`, `GetAttendStatusUsecase`, `GetAttendAllInfoSortActiveStatusUsecase`)을 한 클래스에서 구현해 Program/Member/Target 리포지토리와 상호작용합니다.
- `AttendWeightPolicyController`와 `AttendWeightPolicyService`는 상태별 벌점 정책을 CRUD하며, `AttendWeightCalculator`가 정책을 캐시 형태로 불러와 실제 점수를 계산합니다.
- `SelectAttendCommandTargetMemberMemberService`는 Program 도메인에서 전달된 멤버 목록을 출석 타깃(`AttendEntity`)으로 저장/갱신하고, `AttendTeamBuildingService` 등 target 패키지 내 다른 서비스는 팀빌딩 입력, 발표자 선정 등을 처리합니다.

## 주요 정책
1. **출석 대상만 응답 가능**: `SelectAttend...`가 생성한 `AttendEntity`의 기본 상태는 `NONRESPONSE`이며, `AttendModel.isRelated`가 `NONRELATED`이면 `DeniedSaveAttendException`으로 응답을 막습니다.
2. **출석 모드 기준 상태 결정**: `AttendService.changeStatus`는 먼저 `ProgramModel`을 조회해 현재 `ProgramAttendMode`(`attend`, `late`, `end`)를 확인하고, 모드에 해당하는 `AttendStatus`로 강제 변경합니다.
3. **변경 조건**: `validateAttend`는 (a) 프로그램이 `ProgramAttendMode.END`면 `NotStartAttendException`, (b) 이미 `ATTEND/ABSENT/LATE` 등 응답한 상태면 `DeniedChangeAttendException`, (c) `NONRELATED`면 `DeniedSaveAttendException`을 발생시켜 중복 응답을 막습니다.
4. **랭크 부여**: 출석 모드가 `ATTEND`일 때만 `ProgramRankCounterRepository`를 PESSIMISTIC_WRITE 잠금으로 조회해 순번을 부여하고, `counter.incrementNextRank()`로 다음 값을 예약합니다.
5. **벌점 계산**: `updateAttendStatus`는 상태가 `ABSENT`나 `LATE`일 때 `AttendWeightCalculator.calculateTotalScore`로 최신 가중치를 조회해 `penaltyScore`를 저장합니다.
6. **가중치 변경 방식**: `AttendWeightPolicyService.changeWeightPolicy`는 변경하려는 `AttendStatus` 집합을 추출해 기존 정책을 삭제한 뒤 새 모델 전체를 저장하며, 조회 시 지정되지 않은 상태는 `score=0`, `SignType.PLUS` 기본값으로 보강합니다.
7. **조회 제약**: `AttendInfosSearchRequest`는 `size>0`, `page>=1`, `startDate<=endDate`를 만족하지 않으면 Bean Validation 오류를 발생시키고, `PenaltyInfoRequest`는 `sortType`을 `asc` 또는 `desc`만 허용합니다.
8. **기본 기간 범위**: 내 출석 요약/벌점 API는 `semesterPeriodProvider`에서 반환한 학기 시작/종료 시각을 기본값으로 사용합니다.
9. **멤버 필터링**: 활동 상태별 조회(`getAttendInfo`)는 `ActiveStatus` enum(`all/am/cm/rm/ob`)을 사용하며, 관리자(`MemberModel.isAdmin`)는 항상 제외됩니다.
10. **소프트 삭제 및 정렬**: `AttendEntity` 역시 `@SQLDelete`/`@Where`로 소프트 삭제하며, 주요 조회는 `createdDate` 내림차순(`findMyAttendInfo`), 랭크 오름차순(TOP5) 등 명시적 정렬 규칙을 갖습니다.

## 코드 컨벤션 및 구조
- **타겟 패키지 네이밍**: 출석뿐 아니라 벌점, 팀빌딩, 발표자 선택 등 "대상(Target)" 관련 기능을 `target` 패키지 아래에 묶고, `application/dto|model|service|usecase`, `persistence`, `presentation` 구조를 공유합니다.
- **RequestScope 활용**: "내 출석" 계열 API는 `RequestScope.getMemberId()`로 인증 컨텍스트를 읽어 서비스에서 별도 파라미터 없이 현재 사용자 기준 로직을 수행합니다.
- **DTO/모델 변환기**: `AttendInfoConverter`, `AttendInfoWithProgramConverter`, `AttendInfoActiveStatusConverter`, `ChangeAttendStatusConverter` 등 여러 converter가 존재하며, 서비스는 converter 레이어만 통해 응답을 조립합니다.
- **Enum 지향**: `AttendStatus`, `SignType`, `ProgramAttendMode` 등 문자열 대신 enum을 사용해 상태 비교 시 `AttendStatus.find(status)`를 거치게 하고, 잘못된 값에 대해 즉시 예외를 던집니다.
- **벌점 저장 전략**: 벌점 합계와 순위는 `PenaltyPointRepository`의 JPQL 집계 쿼리로 계산하고, `PageResponse`로 감싼 결과를 그대로 API 응답으로 전달합니다.

## 연관 컴포넌트
- `target/application/service/AttendService` : 출석 상태 변경, 조회, 통계 중심 서비스.
- `target/application/service/AttendWeightPolicyService`, `AttendWeightCalculator` : 가중치 정책 CRUD 및 점수 계산.
- `target/application/service/SelectAttendCommandTargetMemberMemberService` : Program 도메인에서 전달된 출석 대상 관리.
- `target/presentation/controller/AttendWeightPolicyController`, `TeamBuildingTargetController` : 추가 대상 관리 API.
