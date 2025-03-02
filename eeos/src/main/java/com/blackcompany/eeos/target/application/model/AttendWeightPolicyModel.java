package com.blackcompany.eeos.target.application.model;

import com.blackcompany.eeos.common.support.AbstractModel;
import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode
public class AttendWeightPolicyModel implements AbstractModel {
	private Long id;
	private SignType signType;
	private AttendStatus type;
	private int score;
}
