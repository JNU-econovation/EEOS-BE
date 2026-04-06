# Validation DTO 작성 패턴

## 기본 규칙

1. 모든 Validation 어노테이션의 `message`에 **고유 에러 코드** 포함
2. enum 성격 필드는 `String`으로 받되 `@Schema(allowableValues)`로 허용 값 명시
3. 서비스 레이어에서 `String → Enum` 변환 시 유효하지 않은 값은 비즈니스 예외로 처리

## DTO 작성 예시

```java
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public class ExampleRequest {

    // 필수 문자열 (코드 부여)
    @NotBlank(message = "XXXX:필드명은 필수 입력값입니다")
    @Size(max = 50, message = "XXXX:필드명은 50자 이하여야 합니다")
    private String name;

    // 필수 숫자
    @NotNull(message = "XXXX:필드명은 필수 입력값입니다")
    @Min(value = 1, message = "XXXX:필드명은 1 이상이어야 합니다")
    private Integer count;

    // 패턴 제약
    @NotBlank(message = "XXXX:비밀번호는 필수 입력값입니다")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$",
            message = "XXXX:비밀번호는 8~20자이며, 영문과 숫자를 포함해야 합니다")
    private String password;

    // Enum 성격 String 필드 - allowableValues로 선택지 표시
    @NotBlank(message = "XXXX:상태는 필수 입력값입니다")
    @Schema(
            description = "상태값 설명",
            example = "am",
            allowableValues = {"am", "cm", "rm", "ob"})
    private String status;
}
```

## Enum → String 선택 기준

| 케이스 | 타입 | 이유 |
|--------|------|------|
| 외부 API 요청/응답 | `String` + `@Schema` | 클라이언트 친화적, 확장 용이 |
| 내부 도메인 모델 | `Enum` | 타입 안전성 |

## @Schema 필수 속성

enum 성격 필드에는 반드시:
- `description`: 필드 설명
- `example`: 대표 값 1개
- `allowableValues`: 허용 값 목록 (조회 전용 값 제외, 예: `all` 제외)
