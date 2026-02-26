package com.blackcompany.eeos.auth.presentation.dto;

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

	@NotBlank(message = "아이디는 필수 입력값입니다")
	@Size(max = 50, message = "아이디는 50자 이하여야 합니다")
	private String id;

	@NotBlank(message = "비밀번호는 필수 입력값입니다")
	@Pattern(
			regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$",
			message = "비밀번호는 8~20자이며, 영문과 숫자를 포함해야 합니다")
	private String password;

	@NotNull(message = "기수는 필수 입력값입니다")
	@Min(value = 1, message = "기수는 1 이상이어야 합니다")
	private Integer generation;

	@NotBlank(message = "성함은 필수 입력값입니다")
	@Size(max = 50, message = "성함은 50자 이하여야 합니다")
	private String name;
}
