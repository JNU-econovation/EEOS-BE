# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

EEOS (Econovation Event Operation System) — 전남대학교 에코노베이션 동아리의 행사/출석 관리 백엔드.
Java 21, Spring Boot 3.2.2, MySQL 8, Redis, Flyway, Spring Security + JWT.

## Build & Development Commands

All Gradle commands run from `eeos/` directory using `./gradlew`.

```bash
# Build
cd eeos && ./gradlew build

# Run tests (unit only)
cd eeos && ./gradlew test

# Run a single test class
cd eeos && ./gradlew test --tests "com.blackcompany.eeos.some.TestClassName"

# Integration tests (source: src/integrationTest/java)
cd eeos && ./gradlew integrationTest

# Format code (Google Java Format via Spotless)
cd eeos && ./gradlew spotlessApply

# Check formatting without fixing
cd eeos && ./gradlew spotlessCheck

# Start local DB (MySQL 8 on port 13308, Redis on port 16379)
cd eeos/resources/local-develop-environment && docker-compose up

# Run Spring Boot locally (profile: local)
cd eeos && ./gradlew bootRun
```

Pre-commit hook runs `spotlessApply` automatically. Installed via `./gradlew clean` (triggers `installGitHooks`).

## Architecture

### Domain-driven layered structure per domain

Base package: `com.blackcompany.eeos`

Each domain follows this structure:
```plaintext
<domain>/
  presentation/   → @RestController, API docs interfaces
  application/
    usecase/      → One interface per operation (e.g., CreateProgramUsecase)
    service/      → UseCase implementations (@Service)
    model/        → Domain models (NOT JPA entities)
    dto/          → Request/Response DTOs and converters
    repository/   → Repository interfaces (application-layer ports)
    event/        → Spring ApplicationEvent for cross-domain communication
  persistence/    → JPA entities, Spring Data JPA repositories (port implementations)
  infra/          → External integrations (Slack/GitHub Feign clients)
```

### Domains
`auth`, `member`, `program`, `target` (attendance/penalties), `calendar`, `team`, `teamBuilding`, `comment`

### Key Conventions

- **Model/Entity separation**: JPA entities live in `persistence/`, domain models in `application/model/`. Always convert between them.
- **Soft delete everywhere**: All entities extend `BaseEntity` which provides `createdDate`, `updatedDate`, `isDeleted`. Entities use `@SQLDelete` + `@SQLRestriction("is_deleted=false")` (Hibernate 6.3+ replacement for deprecated `@Where`).
- **UseCase pattern**: Each operation gets its own interface in `usecase/` package, implemented by a service class.
- **Cross-domain events**: Domains communicate via Spring `ApplicationEvent`, not direct service calls.
- **Port/Adapter repositories**: `application/repository/` defines interfaces; `persistence/` implements them.

### Configuration

Spring profiles are composed from slices: `local` = `local-mysql` + `api` + `oauth` + `token` + `actuator` + `local-redis` + `slack` + `swagger` + `admin`. Each slice is a separate `application-{slice}.yml`.

### Database Migrations

Flyway migrations in `src/main/resources/db/migration/`. Follow naming: `V{major}.{minor}.{patch}.{seq}__{description}.sql`.

### Code Style

Google Java Format (2-space) with tab conversion via Spotless `indentWithTabs(2)`. Enforced by Spotless plugin. Run `./gradlew spotlessApply` before committing.

## Development Workflow

이 프로젝트는 **페르소나 기반 TDD 개발 프로세스**를 따른다.

### 슬래시 커맨드

| 커맨드 | 용도 |
|--------|------|
| `/new-feature [요구사항]` | 새 기능 개발 워크플로우 시작 |
| `/fix-bug [버그 내용]` | 버그 수정 워크플로우 시작 |

### 워크플로우 순서

```plaintext
플래너 → 아키텍트 → 테스트 설계자 → 개발자 → 코드 리뷰어 → 문서화 노예 → 문서 리뷰어
  (컨펌)    (컨펌)
```

### 서브에이전트 목록 (`.claude/agents/`)

| 에이전트 | 역할 | 컨펌 필요 |
|---------|------|----------|
| `planner` | 요구사항 분석 → To-do 리스트 | ✅ |
| `architect` | API/DB 설계 | ✅ |
| `qa` | 실패 테스트 작성 (TDD) | - |
| `backend-dev` | 구현 코드 작성 | - |
| `reviewer` | 코드 리뷰 | - |
| `doc-slave` | 문서 작성/업데이트 | - |
| `doc-reviewer` | 문서 검토 | - |

### 규칙

1. **순서 준수** — 반드시 1→7 순서로 진행
2. **컨펌 필수** — 플래너·아키텍트 산출물은 사용자 승인 후 다음 단계 진행
3. **TDD 원칙** — 테스트 없이 구현 코드 작성 금지
4. **명령어 참조** — 테스트/빌드/DB 명령어는 `docs/DEV_COMMANDS.md` 참조
