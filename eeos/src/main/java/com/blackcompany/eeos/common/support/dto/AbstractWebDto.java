package com.blackcompany.eeos.common.support.dto;

public interface AbstractWebDto<T extends AbstractApplicationDto> extends AbstractDto {
	T toApplicationRequest();
}
