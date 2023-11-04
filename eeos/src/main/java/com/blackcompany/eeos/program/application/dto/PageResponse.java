package com.blackcompany.eeos.program.application.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageResponse<T> {

	/** 페이지를 구성하는 일정 수의 크기 */
	private int size;
	/** 데이터를 가져온 페이지 번호 */
	private int page;
	/** size 크기에 맞춰 페이징했을 때 나오는 총 페이지 개수 */
	private int totalPage;

	private List<T> programs;

	public PageResponse(final Page<T> source) {
		final Pageable pageable = source.getPageable();
		this.size = pageable.getPageSize();
		this.page = pageable.getPageNumber();
		this.totalPage = source.getTotalPages();
		this.programs = source.getContent();
	}
}
