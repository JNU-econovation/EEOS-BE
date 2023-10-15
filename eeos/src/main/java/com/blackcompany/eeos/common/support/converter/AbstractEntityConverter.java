package com.blackcompany.eeos.common.support.converter;

import com.blackcompany.eeos.common.persistence.BaseEntity;
import com.blackcompany.eeos.common.support.AbstractModel;

public interface AbstractEntityConverter<T extends BaseEntity, R extends AbstractModel> {
	R from(T t);

	T toEntity(R t);
}
