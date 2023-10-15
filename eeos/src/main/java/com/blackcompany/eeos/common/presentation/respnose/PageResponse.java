package com.blackcompany.eeos.common.presentation.respnose;

import java.util.List;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Getter
public class PageResponse<T> {

	/** 페이지를 구성하는 일정 수의 크기 */
	private final int pageSize;
	/** 데이터를 가져온 페이지 번호 */
	private final int pageNumber;
	/** size 크기에 맞춰 페이징했을 때 나오는 총 페이지 개수 */
	private final int totalPageCount;
	/** 전체 데이터 개수 */
	private final Long totalCount;

	private final List<T> data;

	public PageResponse(final Page<T> source) {
		final Pageable pageable = source.getPageable();
		this.pageSize = pageable.getPageSize();
		this.pageNumber = pageable.getPageNumber();
		this.totalPageCount = source.getTotalPages();
		this.totalCount = source.getTotalElements();
		this.data = source.getContent();
	}
}
