package com.blackcompany.eeos.team.application.model;

import com.blackcompany.eeos.common.support.AbstractModel;
import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
/** 빌더가 만들어지는 조건 1. 생성자가 있어야 한다. 2. 어떤 생성자인가? -> 필드의 타입을 모두 매개변수로 받는 생성자가 하나 있어야 한다. */
public class TeamModel implements AbstractModel {

	private Long id;
	private String name;
	private boolean status;
}
