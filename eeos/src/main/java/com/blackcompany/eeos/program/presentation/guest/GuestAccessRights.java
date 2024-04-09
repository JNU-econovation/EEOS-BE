package com.blackcompany.eeos.program.presentation.guest;

import com.blackcompany.eeos.program.application.model.AccessRights;

public class GuestAccessRights {

	public static String get() {
		return AccessRights.READ_ONLY.getAccessRight();
	}
}
