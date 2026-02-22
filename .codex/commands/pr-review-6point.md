현재 브랜치를 `main` 기준으로 병렬 PR 리뷰한다.

아래 규칙으로 Codex 멀티 에이전트를 사용해 서브에이전트를 병렬 실행하라.

## 병렬 분할 (한 항목당 1 에이전트)

1. Security issue (`reviewer`)
2. Code quality (`reviewer`)
3. Bugs / regressions (`reviewer`)
4. Race / concurrency risk (`explorer`)
5. Test flakiness risk (`explorer`)
6. Maintainability (`reviewer`)

## 실행 규칙

- 각 항목마다 서브에이전트를 하나씩 생성하고 병렬로 실행한다.
- diff는 `main...HEAD`를 기준으로 검토한다.
- 각 서브에이전트는 파일 경로와 근거를 포함한 결과를 제출해야 한다.
- 모든 결과를 `wait`로 모은 뒤, 항목별로 통합 요약한다.

## 최종 출력 형식

- 항목별 결과 (1~6)
- 가장 먼저 수정해야 할 리스크 Top 3
- 권장 후속 조치

