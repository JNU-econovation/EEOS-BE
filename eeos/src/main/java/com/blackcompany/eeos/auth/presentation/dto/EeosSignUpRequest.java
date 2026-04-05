package com.blackcompany.eeos.auth.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EeosSignUpRequest {

	@NotBlank(message = "4100:아이디는 필수 입력값입니다")
	@Size(max = 50, message = "4101:아이디는 50자 이하여야 합니다")
	private String id;

	@NotBlank(message = "4102:비밀번호는 필수 입력값입니다")
	@Pattern(
			regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,20}$",
			message = "4103:비밀번호는 8~20자이며, 영문·숫자·특수기호를 포함해야 합니다")
	private String password;

	@NotNull(message = "4104:기수는 필수 입력값입니다")
	@Min(value = 1, message = "4105:기수는 1 이상이어야 합니다")
	private Integer generation;

	@NotBlank(message = "4106:성함은 필수 입력값입니다")
	@Size(max = 50, message = "4107:성함은 50자 이하여야 합니다")
	private String name;

	@NotBlank(message = "4108:활동 상태는 필수 입력값입니다")
	@Pattern(regexp = "^(am|cm|rm|ob)$", message = "4109:활동 상태는 am, cm, rm, ob 중 하나여야 합니다")
	@Schema(
			description = "활동 상태",
			example = "am",
			allowableValues = {"am", "cm", "rm", "ob"})
	private String activeStatus;
}
