# 개발 명령어 레퍼런스

> **규칙**: 모든 agent는 테스트/빌드/DB 명령어를 실행할 때 이 파일을 참조한다.
> 모든 Gradle 명령어는 `eeos/` 디렉토리에서 실행한다.

---

## 빌드

```bash
# 전체 빌드
cd eeos && ./gradlew build

# 빌드 (테스트 제외)
cd eeos && ./gradlew build -x test
```

---

## 테스트

```bash
# 단위 테스트 (src/test/java)
cd eeos && ./gradlew test

# 단위 테스트 - 특정 클래스만
cd eeos && ./gradlew test --tests "com.blackcompany.eeos.<domain>.<TestClassName>"

# 통합 테스트 (src/integrationTest/java)
cd eeos && ./gradlew integrationTest

# 전체 테스트 (단위 + 통합)
cd eeos && ./gradlew test integrationTest
```

---

## 코드 포맷

```bash
# 포맷 적용 (Google Java Format, indent=2)
cd eeos && ./gradlew spotlessApply

# 포맷 검사만 (수정 없음)
cd eeos && ./gradlew spotlessCheck
```

> **주의**: Pre-commit hook이 자동으로 `spotlessApply`를 실행한다.
> 커밋 전 수동으로 실행할 필요는 없지만, IDE에서 확인하려면 사용한다.

---

## 로컬 실행

```bash
# 로컬 DB 시작 (MySQL 8 → port 13308, Redis → port 16379)
cd eeos/resources/local-develop-environment && docker-compose up

# Spring Boot 로컬 실행 (profile: local)
cd eeos && ./gradlew bootRun
```

---

## 데이터베이스 마이그레이션

```bash
# Flyway 마이그레이션 실행 (bootRun 시 자동 적용)
cd eeos && ./gradlew flywayMigrate

# 마이그레이션 상태 확인
cd eeos && ./gradlew flywayInfo
```

### Flyway 파일 명명 규칙
```
V{major}.{minor}.{patch}.{seq}__{description}.sql

예시:
V1.0.0.1__create_member_table.sql
V1.0.0.2__add_program_status_column.sql
V1.1.0.1__create_team_table.sql
```

> **주의**: 기존 마이그레이션 파일을 수정하지 않는다. 변경이 필요하면 새 버전 파일을 추가한다.
> 현재 최신 버전은 `eeos/src/main/resources/db/migration/` 디렉토리에서 확인한다.

---

## Git Hooks

```bash
# pre-commit hook 설치 (최초 1회 또는 hook 변경 시)
cd eeos && ./gradlew clean
```

> `./gradlew clean` 실행 시 `installGitHooks` 태스크가 자동으로 실행된다.

---

## 기타

```bash
# 의존성 확인
cd eeos && ./gradlew dependencies

# 프로젝트 클린
cd eeos && ./gradlew clean
```
