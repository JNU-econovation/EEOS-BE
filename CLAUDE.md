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
```
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
- **Soft delete everywhere**: All entities extend `BaseEntity` which provides `createdDate`, `updatedDate`, `isDeleted`. Entities use `@SQLDelete` + `@Where(clause = "is_deleted=false")`.
- **UseCase pattern**: Each operation gets its own interface in `usecase/` package, implemented by a service class.
- **Cross-domain events**: Domains communicate via Spring `ApplicationEvent`, not direct service calls.
- **Port/Adapter repositories**: `application/repository/` defines interfaces; `persistence/` implements them.

### Configuration

Spring profiles are composed from slices: `local` = `local-mysql` + `api` + `oauth` + `token` + `actuator` + `local-redis` + `slack` + `swagger` + `admin`. Each slice is a separate `application-{slice}.yml`.

### Database Migrations

Flyway migrations in `src/main/resources/db/migration/`. Follow naming: `V{major}.{minor}.{patch}.{seq}__{description}.sql`.

### Code Style

Google Java Format with tabs (indent size 2). Enforced by Spotless plugin. Run `./gradlew spotlessApply` before committing.
