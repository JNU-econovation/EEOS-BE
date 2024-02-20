package com.blackcompany.eeos.teamBuilding.infra.client;

import com.blackcompany.eeos.teamBuilding.infra.dto.ParticipantRequest;
import java.util.List;

public interface ClusteringTeamApiClient {
	List<List<String>> fetchResult(ParticipantRequest participantRequest);
}
