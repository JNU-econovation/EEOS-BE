# Swagger Error Response 문서화

## 원칙

모든 API 엔드포인트는 **성공 응답 + 발생 가능한 에러 응답**을 Swagger에 명시한다.

## `@ApiResponses` 작성 패턴

### 이름 충돌 처리

프로젝트의 `ApiResponse`와 Swagger의 `@ApiResponse`가 이름 충돌하므로,
Swagger 어노테이션은 **FQCN**으로 사용한다:

```java
import com.blackcompany.eeos.common.presentation.response.ApiResponse; // 프로젝트 것
// Swagger @ApiResponse는 import하지 않고 FQCN으로 사용

@ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "성공"),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "에러 설명",
            content = @Content)
})
ApiResponse<SuccessBody<TokenResponse>> myMethod(...);
```

### Validation 에러가 여러 개인 경우 (400)

마크다운 테이블로 코드-메시지를 정리:

```java
@io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "400",
        description =
                "Validation 에러\n\n"
                        + "| 코드 | 메시지 |\n"
                        + "|------|--------|\n"
                        + "| 4100 | 아이디는 필수 입력값입니다 |\n"
                        + "| 4101 | 아이디는 50자 이하여야 합니다 |",
        content = @Content)
```

### 비즈니스 예외 (409, 404 등)

간결하게 `코드: 메시지` 형식:

```java
@io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "409",
        description = "4009: 이미 사용 중인 아이디입니다",
        content = @Content)
```

## 필요한 import

```java
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
```

`io.swagger.v3.oas.annotations.responses.ApiResponse`는 **import하지 않는다** (이름 충돌 방지).
