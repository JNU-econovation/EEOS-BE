새 기능 개발 워크플로우를 시작한다.

요구사항: `$ARGUMENTS`

## 실행 지침 (Codex 멀티 에이전트)

1. `.codex/agents/orchestrator.md` 규칙을 따른다.
2. 각 단계 서브에이전트는 `agents/<role>.md`를 먼저 읽고 작업하게 한다.
3. 순서:
   `planner -> architect -> qa -> backend-dev -> reviewer -> doc-slave -> doc-reviewer`
4. `planner`, `architect` 완료 후에는 반드시 사용자 컨펌을 받고 다음 단계로 진행한다.
5. `reviewer`에서 Must Fix가 나오면 `backend-dev -> reviewer` 루프를 반복한다.
6. `doc-reviewer`에서 오류가 나오면 `doc-slave -> doc-reviewer` 루프를 반복한다.

## 단계 전달 컨텍스트 규칙

- 이전 단계 산출물 핵심 요약 + 관련 파일 경로를 다음 단계에 전달한다.
- 코드/문서 수정이 발생한 단계는 변경 파일 목록을 명시한다.
- 긴 산출물은 전체 붙여넣기 대신 요약 + 파일 경로 중심으로 전달한다.

## 완료 메시지

모든 단계가 끝나면 다음 형식으로 종료:

`✅ [기능명] 개발 워크플로우 완료`

