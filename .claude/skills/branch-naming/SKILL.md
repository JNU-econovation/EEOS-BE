---
name: branch-naming
description: 브랜치 생성 시 네이밍 규칙을 강제한다. 형식은 이니셜/타입/설명. Use when creating branches, "브랜치 만들어줘", "branch", "git checkout -b", "git branch".
---

# Branch Naming

브랜치를 생성할 때 반드시 아래 네이밍 규칙을 따른다.

## 형식

```
{이니셜}/{타입}/{설명}
```

- **이니셜**: 작업자 이니셜 (대문자, 예: BM, SM, DD)
- **타입**: `feat`, `fix`, `refactor`, `chore`, `docs`, `test`
- **설명**: kebab-case, 간결한 작업 설명

## 예시

```
BM/feat/id-password-auth
SM/fix/redis-timeout
DD/docs/codex-docs
BM/chore/update-skills
SM/refactor/auth-module
DD/test/signup-validation
```

## 규칙

1. 브랜치 생성 전 **사용자의 이니셜을 확인**한다 (모르는 경우)
2. 타입은 커밋 컨벤션과 동일: `feat`, `fix`, `refactor`, `chore`, `docs`, `test`
3. 설명은 kebab-case, 영문 소문자
4. CI가 `**/{타입}/**` 패턴으로 매칭하므로 반드시 타입을 포함해야 한다
