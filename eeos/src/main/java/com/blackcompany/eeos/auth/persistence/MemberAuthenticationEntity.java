package com.blackcompany.eeos.auth.persistence;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash("MemberAuthentication")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MemberAuthenticationEntity {
	@Id private String token;
	private Long memberId;
	@TimeToLive private Long expiration;
}
