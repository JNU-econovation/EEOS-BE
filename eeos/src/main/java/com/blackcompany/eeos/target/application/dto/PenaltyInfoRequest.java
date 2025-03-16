package com.blackcompany.eeos.target.application.dto;

import com.blackcompany.eeos.common.exception.InvalidParameterException;
import com.blackcompany.eeos.common.support.dto.AbstractRequestDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.web.bind.annotation.RequestParam;

public record PenaltyInfoRequest(
		@Positive(message = "page 값은 양수입니다.") int page,
		@PositiveOrZero(message = "size 값은 음수가 될 수 없습니다.") int size,
		@NotBlank(message = "sortType은 공백 문자열이 될 수 없습니다.") String sortType)
		implements AbstractRequestDto {

	public PenaltyInfoRequest(
			@RequestParam("page") int page,
			@RequestParam("size") int size,
			@RequestParam("sortType") String sortType) {
		validateParameter(page, size, sortType);
		this.page = page;
		this.size = size;
		this.sortType = sortType;
	}

	private void validateParameter(int page, int size, String sortType) {
		if (!sortType.equals("asc") && !sortType.equals("desc")) throw new InvalidParameterException();
	}
}
