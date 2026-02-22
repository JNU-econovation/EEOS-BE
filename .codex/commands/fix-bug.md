버그 수정 워크플로우를 시작한다.

버그 내용: `$ARGUMENTS`

## 실행 지침 (Codex 멀티 에이전트)

1. `.codex/agents/orchestrator.md` 규칙을 따른다.
2. 각 단계 서브에이전트는 `agents/<role>.md`를 먼저 읽고 작업하게 한다.
3. 순서:
   `planner -> architect(선택) -> qa -> backend-dev -> reviewer -> doc-slave -> doc-reviewer`

## 버그 수정 특화 규칙

- `architect` 단계에서는 먼저 "설계 변경 필요 여부"를 판단하게 한다.
- 설계 변경이 필요 없으면 사용자에게 아키텍트 단계 스킵 여부를 묻고 진행한다.
- `qa` 단계는 반드시 "현재 상태에서 실패하는 버그 재현 테스트"를 먼저 작성해야 한다.

## 품질 게이트

- `planner` 완료 후 사용자 컨펌 필수
- `architect` 수행 시 설계 산출물 완료 후 사용자 컨펌 필수
- `reviewer` Must Fix 발생 시 `backend-dev -> reviewer` 재반복
- `doc-reviewer` 오류 발생 시 `doc-slave -> doc-reviewer` 재반복

## 완료 메시지

모든 단계가 끝나면 다음 형식으로 종료:

`✅ 버그 수정 워크플로우 완료`

