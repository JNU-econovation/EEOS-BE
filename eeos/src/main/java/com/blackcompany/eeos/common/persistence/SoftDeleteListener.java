package com.blackcompany.eeos.common.persistence;

import javax.persistence.PreRemove;

public class SoftDeleteListener {

	@PreRemove
	private void preRemove(BaseEntity entity) {
		entity.delete();
	}
}
