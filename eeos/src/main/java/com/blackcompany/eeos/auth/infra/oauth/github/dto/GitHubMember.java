package com.blackcompany.eeos.auth.infra.oauth.github.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GitHubMember {
	private Long id;
	private String login;
	private String name;
	private String email;
	private String avatarUrl;
	private String location;
	private String bio;
	private String blog;
	private String company;
	private String twitterUsername;
	private String type;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private Boolean siteAdmin;
}
