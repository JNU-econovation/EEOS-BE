---
name: api-error-docs
description: API 엔드포인트 작성/수정 시 에러 응답 문서화를 강제한다. Swagger @ApiResponses에 에러 코드/메시지 명시, Validation message에 고유 코드 부여, enum 성격 필드에 @Schema allowableValues 추가. Use when writing controllers, DTOs, or Swagger docs. Triggers on "API 만들어줘", "엔드포인트 추가", "Swagger", "에러 코드".
---

# API Error Documentation

API 엔드포인트를 작성하거나 수정할 때 에러 응답을 빠짐없이 문서화한다.

**핵심 원칙:** 모든 API는 성공 응답뿐 아니라 **발생 가능한 모든 에러 코드와 메시지**를 Swagger에 명시해야 한다.

## Checklist (API 작성 시 반드시 확인)

- [ ] DTO의 모든 Validation `message`에 **고유 에러 코드** 부여 (`"코드:메시지"` 형식)
- [ ] enum 성격 String 필드에 `@Schema(allowableValues=...)` 추가 (조회 전용 값 제외)
- [ ] Swagger docs 인터페이스에 `@ApiResponses`로 에러 응답 문서화
- [ ] 새 에러 코드가 기존 코드와 **중복되지 않는지** 확인

## Step 1: 에러 코드 부여

새 Validation DTO를 작성하거나 수정할 때:

1. 기존 에러 코드를 검색하여 사용 중인 코드 범위를 파악한다
2. 해당 도메인의 다음 번호대를 할당한다
3. 모든 Validation 어노테이션의 `message`에 `"코드:메시지"` 형식으로 작성한다

**세부 규칙:** `references/error-code-convention.md` 참조

## Step 2: DTO 작성

1. 각 필드에 적절한 Validation 어노테이션 + 고유 코드 부여
2. enum 성격 필드는 `String`으로 받되 `@Schema(allowableValues=...)` 명시
3. `allowableValues`에서 조회 전용 값(예: `all`)은 제외

**세부 패턴:** `references/validation-dto-pattern.md` 참조

## Step 3: Swagger 에러 응답 문서화

Controller의 docs 인터페이스에 `@ApiResponses`를 추가한다:

1. 성공 응답 (`200`, `201` 등)
2. Validation 에러 (`400`) - 마크다운 테이블로 코드/메시지 목록
3. 비즈니스 예외 (`404`, `409` 등) - `코드: 메시지` 형식

**세부 패턴:** `references/swagger-error-response.md` 참조

## Step 4: 검증

작업 완료 후 반드시 아래 명령어를 순서대로 실행하여 통과를 확인한다.

```bash
./gradlew compileJava      # 1. 메인 코드 컴파일 (ApiResponse 이름 충돌 등)
./gradlew compileTestJava  # 2. 테스트 코드 컴파일 (생성자 변경 시 테스트도 수정했는지)
./gradlew test             # 3. 전체 테스트 실행 (CI와 동일)
```

**주의:** DTO나 Command에 필드를 추가/변경하면, 해당 클래스를 사용하는 **테스트 코드도 반드시 수정**해야 한다.
테스트 컴파일 실패는 CI 실패의 가장 흔한 원인이다.

## Quick Reference

```
DTO message 형식:     "4100:아이디는 필수 입력값입니다"
Swagger @ApiResponse: FQCN 사용 (@io.swagger.v3.oas.annotations.responses.ApiResponse)
Enum 필드:            @Schema(allowableValues = {"am", "cm", "rm", "ob"})
에러 코드 검색:       grep -r "FAIL_CODE\|message = \"[0-9]" src/
```
