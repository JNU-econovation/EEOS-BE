package com.blackcompany.eeos.target.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.blackcompany.eeos.target.application.dto.AttendInfoActiveStatusResponse;
import com.blackcompany.eeos.target.application.dto.AttendInfoResponse;
import com.blackcompany.eeos.target.application.dto.QueryAttendActiveStatusResponse;
import com.blackcompany.eeos.target.application.dto.QueryAttendStatusResponse;
import com.blackcompany.eeos.target.application.dto.converter.AttendInfoActiveStatusConverter;
import com.blackcompany.eeos.target.application.dto.converter.AttendInfoConverter;
import com.blackcompany.eeos.target.application.dto.converter.ChangeAttendStatusConverter;
import com.blackcompany.eeos.target.application.dto.converter.QueryAttendActiveStatusConverter;
import com.blackcompany.eeos.target.application.dto.converter.QueryAttendStatusResponseConverter;
import com.blackcompany.eeos.target.application.model.AttendStatus;
import com.blackcompany.eeos.target.application.model.converter.AttendEntityConverter;
import com.blackcompany.eeos.target.persistence.AttendEntity;
import com.blackcompany.eeos.target.persistence.AttendRepository;
import com.blackcompany.eeos.member.application.model.ActiveStatus;
import com.blackcompany.eeos.member.application.model.converter.MemberEntityConverter;
import com.blackcompany.eeos.member.application.service.QueryMemberService;
import com.blackcompany.eeos.member.fixture.MemberFixture;
import com.blackcompany.eeos.member.persistence.MemberEntity;
import com.blackcompany.eeos.member.persistence.MemberRepository;
import com.blackcompany.eeos.program.application.service.ProgramValidService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AttendServiceTest {
	@Mock private AttendRepository attendRepository;
	@Mock private MemberRepository memberRepository;
	@Mock private ProgramValidService programValidService;
	@Spy private AttendInfoConverter infoConverter;
	@Spy private AttendEntityConverter attendEntityConverter;
	@Mock private QueryMemberService queryMemberService;
	@Spy private ChangeAttendStatusConverter changeAttendStatusConverter;
	@Spy private MemberEntityConverter memberEntityConverter;
	@Spy private AttendInfoConverter attendInfoConverter;
	@Spy private QueryAttendStatusResponseConverter attendStatusResponseConverter;
	@Spy private AttendInfoActiveStatusConverter attendInfoActiveStatusConverter;
	@Spy private QueryAttendActiveStatusConverter queryAttendActiveStatusConverter;
	@InjectMocks private AttendService attendService;

	@Test
	@DisplayName("참석 상태를 기준으로 멤버의 참석 상태를 반환한다.")
	void find_attend_info_by_attend_status() {
		// given
		Long programId = 1L;
		String attendStatus = "attend";

		MemberEntity 수민 = MemberFixture.멤버_엔티티(1L, ActiveStatus.RM);
		MemberEntity 바다 = MemberFixture.멤버_엔티티(2L, ActiveStatus.RM);
		AttendEntity 수민_참석 =
				com.blackcompany.eeos.target.fixture.AttendFixture.참석대상자_엔티티(1L, AttendStatus.ATTEND);
		AttendEntity 바다_참석 =
				com.blackcompany.eeos.target.fixture.AttendFixture.참석대상자_엔티티(2L, AttendStatus.ATTEND);

		doNothing().when(programValidService).validate(programId);
		when(attendRepository.findAllByProgramIdAndStatus(programId, AttendStatus.find(attendStatus)))
				.thenReturn(List.of(수민_참석, 바다_참석));
		when(memberRepository.findMembersByIds(List.of(수민_참석.getMemberId(), 바다_참석.getMemberId())))
				.thenReturn(List.of(수민, 바다));

		// when
		QueryAttendStatusResponse response = attendService.findAttendInfo(programId, attendStatus);

		// then
		List<AttendInfoResponse> members = response.getMembers();
		assertAll(
				() -> {
					assertEquals(members.get(0).getMemberId(), 수민_참석.getMemberId());
					assertEquals(members.get(0).getAttendStatus(), attendStatus);
				},
				() -> {
					assertEquals(members.get(1).getMemberId(), 바다_참석.getMemberId());
					assertEquals(members.get(1).getAttendStatus(), attendStatus);
				});
	}

	@Test
	@DisplayName("참석 상태를 멤버의 활동 상태 기준으로 조회 시 해당 프로그램의 참석 대상자가 아니면 관련 없음 참석 상태를 반환한다.")
	void find_attend_info_by_active_status() {
		// given
		Long programId = 1L;
		String activeStatus = "am";
		MemberEntity bada = MemberFixture.멤버_엔티티(1L, ActiveStatus.AM);

		when(memberRepository.findMembersByActiveStatus(ActiveStatus.AM)).thenReturn(List.of(bada));
		when(attendRepository.findAllByProgramId(programId)).thenReturn(List.of());

		// when
		QueryAttendActiveStatusResponse response = attendService.getAttendInfo(programId, activeStatus);

		// then
		List<AttendInfoActiveStatusResponse> members = response.getMembers();

		assertAll(
				() -> {
					assertEquals(members.get(0).getMemberId(), bada.getId());
					assertEquals(members.get(0).getAttendStatus(), AttendStatus.NONRELATED.getStatus());
				});
	}

	@Test
	@DisplayName("참석 상태를 멤버의 활동 상태 기준으로 조회 시 해당 프로그램의 참여 정보 반환")
	void find_attend_info_by_active_status_include_nonRelated() {
		// given
		Long programId = 1L;
		String activeStatus = "all";
		MemberEntity 수민 = MemberFixture.멤버_엔티티(1L, ActiveStatus.RM);
		MemberEntity 바다 = MemberFixture.멤버_엔티티(2L, ActiveStatus.RM);

		AttendEntity 수민_참석 =
				com.blackcompany.eeos.target.fixture.AttendFixture.참석대상자_엔티티(1L, AttendStatus.ATTEND);
		AttendEntity 바다_미응답 =
				com.blackcompany.eeos.target.fixture.AttendFixture.참석대상자_엔티티(2L, AttendStatus.NONRESPONSE);

		when(memberRepository.findMembers()).thenReturn(List.of(수민, 바다));
		when(attendRepository.findAllByProgramId(programId)).thenReturn(List.of(수민_참석, 바다_미응답));

		// when
		QueryAttendActiveStatusResponse response = attendService.getAttendInfo(programId, activeStatus);

		// then
		List<AttendInfoActiveStatusResponse> members = response.getMembers();

		assertAll(
				() -> {
					assertEquals(members.get(0).getMemberId(), 수민_참석.getMemberId());
					assertEquals(members.get(0).getAttendStatus(), 수민_참석.getStatus().getStatus());
					assertEquals(members.get(0).getName(), 수민.getName());
					assertEquals(members.get(0).getActiveStatus(), 수민.getActiveStatus().getStatus());
				},
				() -> {
					assertEquals(members.get(1).getMemberId(), 바다_미응답.getMemberId());
					assertEquals(members.get(1).getAttendStatus(), 바다_미응답.getStatus().getStatus());
					assertEquals(members.get(1).getName(), 바다.getName());
					assertEquals(members.get(1).getActiveStatus(), 바다.getActiveStatus().getStatus());
				});
	}
}
