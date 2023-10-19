package com.blackcompany.eeos.attend.application.service;

import com.blackcompany.eeos.attend.application.dto.AttendInfoResponse;
import com.blackcompany.eeos.attend.application.dto.converter.AttendInfoConverter;
import com.blackcompany.eeos.attend.application.usecase.GetAttendantInfoUsecase;
import com.blackcompany.eeos.attend.persistence.AttendEntity;
import com.blackcompany.eeos.attend.persistence.AttendRepository;
import com.blackcompany.eeos.member.application.service.MemberService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AttendService implements GetAttendantInfoUsecase {
	private final AttendRepository attendRepository;
	private final MemberService memberService;
	private final AttendInfoConverter infoConverter;
	
	@Override
	public List<AttendInfoResponse> findAttendInfo(final Long programId) {
		List<AttendEntity> attendEntities = attendRepository.findAllByProgramId(programId);
		return attendEntities.stream()
				.map(
						attendEntity ->
								infoConverter.from(
										memberService.findMemberInfo(attendEntity.getId()),
										attendEntity.getStatus().getStatus()))
				.collect(Collectors.toList());
	}
}
