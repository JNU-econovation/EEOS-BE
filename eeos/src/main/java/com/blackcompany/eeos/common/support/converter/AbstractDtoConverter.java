package com.blackcompany.eeos.common.support.converter;

import com.blackcompany.eeos.common.support.AbstractModel;
import com.blackcompany.eeos.common.support.dto.AbstractDto;

public interface AbstractDtoConverter<T extends AbstractDto, R extends AbstractModel> {
	R from(T t);
}
