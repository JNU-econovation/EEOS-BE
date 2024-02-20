package com.blackcompany.eeos.teamBuilding.infra.client;

import com.blackcompany.eeos.teamBuilding.infra.dto.ParticipantRequest;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ClusteringTeamBuilding", url = "https://ai.eeos.store")
public interface ClusteringTeamApiClientImpl extends ClusteringTeamApiClient {
	@PostMapping(path = "/sbert/team-building")
	List<List<String>> fetchResult(@RequestBody ParticipantRequest request);
}
