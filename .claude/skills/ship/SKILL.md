---
name: ship
description: 작업 완료 후 코드 포맷 적용, 커밋, 푸시, PR 생성을 순서대로 수행한다.
---

작업이 완료되었습니다. 아래 순서로 커밋 후 PR을 생성하세요.

## 1. 코드 포맷 적용

Java 파일이 변경된 경우 반드시 먼저 실행:
```bash
cd eeos && ./gradlew spotlessApply
```

## 2. 변경 내용 파악

아래 명령어를 실행해 변경 사항을 파악하세요:
- `git status` — 변경된 파일 목록 확인
- `git diff` — 변경 내용 상세 확인
- `git log --oneline -5` — 최근 커밋 메시지 스타일 참고

## 3. 커밋

변경 내용을 분석해 적절한 파일만 스테이징하고 커밋하세요.

커밋 메시지 규칙:
- `feat:` 새 기능
- `fix:` 버그 수정
- `refactor:` 리팩토링
- `test:` 테스트 추가/수정
- `docs:` 문서 변경
- `chore:` 빌드/설정 변경

커밋 메시지 끝에 반드시 추가:
```
Co-Authored-By: Claude Sonnet 4.6 <noreply@anthropic.com>
```

## 4. 푸시

```bash
git push -u origin <현재 브랜치명>
```

## 5. PR 생성

- **base 브랜치**: `develop`
- **head 브랜치**: 현재 브랜치
- PR 제목: 현재 작업한 내용을 궁극적으로 포괄할 수 있는 제목
- PR 본문에 포함할 내용:
  - 변경 배경 / 이유
  - 변경 내용 요약 (bullet point)
  - 테스트 방법 또는 확인 체크리스트
  - `🤖 Generated with [Claude Code](https://claude.com/claude-code)`

PR 생성 후 URL을 출력하세요.

## 주의사항

- `.env`, 시크릿 키, 바이너리 파일은 커밋하지 않는다
- 불필요한 파일(`*.class`, `build/`, `.venv/`)이 포함되지 않았는지 확인한다
- 테스트가 실패 상태인 경우 사용자에게 먼저 알린다
