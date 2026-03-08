# Codex 멀티 에이전트 워크플로우 가이드

이 문서는 EEOS 프로젝트의 페르소나 기반 TDD 개발 프로세스를 **Codex 멀티 에이전트 기능**으로 운영하기 위한 가이드다.

## 목적

- 기존 `agents/*.md` 페르소나 가이드를 재사용
- Codex의 서브에이전트 기능(`spawn_agent`, `wait`, `send_input`)으로 단계별 작업 분리
- 플래너/아키텍트 컨펌 게이트와 TDD 순서를 강제

## 사용 파일

- 오케스트레이터 가이드: `.codex/agents/orchestrator.md`
- 새 기능 커맨드 템플릿: `.codex/commands/new-feature.md`
- 버그 수정 커맨드 템플릿: `.codex/commands/fix-bug.md`
- 병렬 PR 리뷰 커맨드 템플릿: `.codex/commands/pr-review-6point.md`
- 프롬프트 템플릿: `.codex/prompts/*.md`
- 자동 오케스트레이션 스크립트: `scripts/codex_workflow.py`
- 실행 커맨드 래퍼: `scripts/codex-new-feature`, `scripts/codex-fix-bug`
- 페르소나 상세 가이드: `agents/*.md`

## 표준 워크플로우

1. `planner` (To-do 리스트, 컨펌)
2. `architect` (API/DB 설계, 컨펌)
3. `qa` (실패 테스트 작성)
4. `backend-dev` (구현 + 테스트 통과)
5. `reviewer` (코드 리뷰, 필요 시 재작업 루프)
6. `doc-slave` (문서 작성)
7. `doc-reviewer` (문서 리뷰, 필요 시 재작업 루프)

## 운영 원칙

- 단계 순서를 건너뛰지 않는다 (버그 수정의 `architect` 예외만 선택 스킵 가능)
- 리뷰에서 치명 이슈가 있으면 다음 단계로 넘어가지 않는다
- 긴 컨텍스트는 요약 + 파일 경로로 전달한다
- 최종 완료 시 변경 파일 목록과 남은 리스크를 요약한다

## 예시 요청 문장

- 새 기능: "Codex 멀티 에이전트로 새 기능 워크플로우 시작: [요구사항]"
- 버그 수정: "Codex 멀티 에이전트로 버그 수정 워크플로우 시작: [버그 설명]"

## 자동 오케스트레이션 커맨드 사용법

새 기능:

```bash
scripts/codex-new-feature "팀 상태 조회 API 추가"
```

버그 수정:

```bash
scripts/codex-fix-bug "프로그램 조회 시 삭제된 멤버가 포함되는 버그"
```

드라이런(생성 프롬프트/커맨드만 확인):

```bash
scripts/codex-new-feature --dry-run "샘플 기능"
```

승인/샌드박스 제약 때문에 서브에이전트가 실패하면 `--codex-arg`로 `codex exec` 옵션을 추가 전달할 수 있다.

```bash
scripts/codex-new-feature "샘플 기능" \
  --codex-arg=--dangerously-bypass-approvals-and-sandbox
```
