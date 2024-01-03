package com.blackcompany.eeos.auth.application.domain.token;

import com.blackcompany.eeos.auth.application.exception.TokenExpiredException;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenValidator {
	private final TokenResolver tokenResolver;

	public void valid(String token) {
		Long expired = tokenResolver.getExpiredDate(token);
		Date expiredDate = new Date(expired);

		Date now = new Date();
		if (expiredDate.before(now)) {
			throw new TokenExpiredException();
		}
	}
}
