package com.blackcompany.eeos.auth.application.dto.request;

import com.blackcompany.eeos.common.support.dto.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EEOSLoginRequest implements AbstractDto {

	private String id;

	private String password;
}
