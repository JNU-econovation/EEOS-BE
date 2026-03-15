---
name: new-pr
description: 현재 브랜치에서 커밋한 내용을 바탕으로 새로운 pull request 를 하나 생성한다. pull request 본문 템플릿은 .github/PULL_REQUEST_TEMPLATE.md 를 사용한다.  create new pull request about current branch using .github/PULL_REQUEST_TEMPLATE.md
---

# new-pr

새로운 pull request 를 생성한다.

## 본문 형식
다음의 경로에 있는 markdown 파일을 그대로 사용한다.

```
.github/PULL_REQUEST_TEMPLATE.md
```

## 규칙

1. git log 및 git show 와 같은 명령어를 활용하여, 현재 브랜치에서 어떤 작업이 수행되었는지를 확인한다.
2. 각 파일별 소스코드 변경 내역을 확인하고, 어떻게 변경되었는지, 무슨 의도인지 파악한다.
3. 이 Pull Request를 읽는 사람이 이해할 수 있도록, 간결하게 작성한다.
4. gh 명령어를 사용하여, 이 브랜치에서 작업한 내용을 push 하고 Pull Request 를 생성한다.
5. 이 브랜치가 이미 github 에 push 되었다면, 바로 Pull Request 만 작성한다.
6. Pull Request가 향하는 Target branch 는 항상 develop 브랜치이다.
7. Assignee 를 현재 pull request 를 작성한 계정으로 설정한다.
8. Reviewer 설정은, 이전 pull request를 참고하여, 동일한 팀원들을 reviewer로 설정한다.
