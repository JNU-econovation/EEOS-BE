package com.blackcompany.eeos.auth.presentation.controller;

import com.blackcompany.eeos.auth.application.domain.ClientType;
import com.blackcompany.eeos.auth.application.dto.request.ClientRegistrationRequest;
import com.blackcompany.eeos.auth.application.dto.response.ClientRegistrationResponse;
import com.blackcompany.eeos.auth.application.service.ClientService;
import com.blackcompany.eeos.auth.presentation.docs.ClientApi;
import com.blackcompany.eeos.auth.presentation.support.Member;
import com.blackcompany.eeos.common.presentation.response.ApiResponse;
import com.blackcompany.eeos.common.presentation.response.ApiResponseBody.SuccessBody;
import com.blackcompany.eeos.common.presentation.response.ApiResponseGenerator;
import com.blackcompany.eeos.common.presentation.response.MessageCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/clients")
@RequiredArgsConstructor
public class ClientController implements ClientApi {

	private final ClientService clientService;

	@Override
	@PostMapping
	public ApiResponse<SuccessBody<ClientRegistrationResponse>> register(
			@RequestBody ClientRegistrationRequest request, @Member Long memberId) {
		ClientType clientType = ClientType.valueOf(request.getClientType().toUpperCase());
		var result =
				clientService.register(request.getClientName(), clientType, request.getRedirectUris());

		ClientRegistrationResponse response =
				new ClientRegistrationResponse(result.clientId(), result.clientSecret());

		return ApiResponseGenerator.success(response, HttpStatus.CREATED, MessageCode.CREATE);
	}
}
