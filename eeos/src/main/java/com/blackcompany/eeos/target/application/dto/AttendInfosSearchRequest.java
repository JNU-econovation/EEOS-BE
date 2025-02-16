package com.blackcompany.eeos.target.application.dto;

import com.blackcompany.eeos.common.support.dto.AbstractRequestDto;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import org.springframework.web.bind.annotation.RequestParam;

@Getter
public class AttendInfosSearchRequest implements AbstractRequestDto {

	@Positive(message = "size 는 양수여야 합니다.")
	private final int size;

	@Min(value = 1, message = "page 값은 최소 1입니다.")
	private final int page;

	private final Long startDate;
	private final Long endDate;

	public AttendInfosSearchRequest(
			@RequestParam("startDate") Long startDate,
			@RequestParam("endDate") Long endDate,
			@RequestParam("size") int size,
			@RequestParam("page") int page) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.size = size;
		this.page = page;
	}

	@AssertTrue(message = "날짜 범위가 유효하지 않습니다.")
	public boolean isValidDateRange() {
		if (startDate == null || endDate == null) {
			return false;
		}
		return startDate <= endDate;
	}
}
