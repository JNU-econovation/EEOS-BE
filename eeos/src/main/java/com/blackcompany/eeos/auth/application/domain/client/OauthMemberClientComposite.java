package com.blackcompany.eeos.auth.application.domain.client;

import com.blackcompany.eeos.auth.application.domain.OauthMemberModel;
import com.blackcompany.eeos.auth.application.domain.OauthServerType;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class OauthMemberClientComposite {

	private final Map<OauthServerType, OauthMemberClient> clients;

	public OauthMemberClientComposite(Set<OauthMemberClient> providers) {
		this.clients =
				providers.stream()
						.collect(Collectors.toMap(OauthMemberClient::support, Function.identity()));
	}

	public OauthMemberModel fetch(String oauthServerType, String authCode, String uri) {
		return getClient(oauthServerType).fetch(authCode, uri);
	}

	private OauthMemberClient getClient(String type) {
		OauthServerType oauthServerType = OauthServerType.find(type);
		return clients.get(oauthServerType);
	}
}
