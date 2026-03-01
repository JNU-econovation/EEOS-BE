---
name: spotless-format
description: Java 파일 수정 후 커밋 전에 Spotless 코드 포맷팅을 자동 적용한다. Use when editing Java files, after code changes, before committing. Triggers on Java file edits, "커밋", "commit", "포맷", "format", "spotless".
---

# Spotless Format

Java 파일을 수정한 뒤, **커밋 전에** 반드시 Spotless 포맷팅을 적용한다.

## 규칙

1. Java 파일(`.java`)을 **1개 이상 수정**했으면 커밋 전에 반드시 실행:

```bash
cd eeos && ./gradlew spotlessApply
```

2. Spotless가 파일을 수정했으면 해당 파일도 **같은 커밋에 포함**한다.
3. Spotless 적용 후 별도 커밋을 만들지 않는다 — 원래 변경과 합쳐서 커밋한다.

## 타이밍

```
코드 수정 → spotlessApply → git add → commit
```

Spotless를 빠뜨리고 이미 커밋했다면:

```bash
cd eeos && ./gradlew spotlessApply
# 수정된 파일 추가 후 별도 style 커밋
```
