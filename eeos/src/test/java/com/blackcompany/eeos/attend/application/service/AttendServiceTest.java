package com.blackcompany.eeos.attend.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.blackcompany.eeos.attend.application.dto.AttendInfoActiveStatusResponse;
import com.blackcompany.eeos.attend.application.dto.AttendInfoResponse;
import com.blackcompany.eeos.attend.application.dto.QueryAttendActiveStatusResponse;
import com.blackcompany.eeos.attend.application.dto.QueryAttendStatusResponse;
import com.blackcompany.eeos.attend.application.dto.converter.AttendInfoActiveStatusConverter;
import com.blackcompany.eeos.attend.application.dto.converter.AttendInfoConverter;
import com.blackcompany.eeos.attend.application.dto.converter.ChangeAttendStatusConverter;
import com.blackcompany.eeos.attend.application.dto.converter.QueryAttendActiveStatusConverter;
import com.blackcompany.eeos.attend.application.dto.converter.QueryAttendStatusResponseConverter;
import com.blackcompany.eeos.attend.application.model.AttendStatus;
import com.blackcompany.eeos.attend.application.model.converter.AttendEntityConverter;
import com.blackcompany.eeos.attend.fixture.FakeAttend;
import com.blackcompany.eeos.attend.persistence.AttendEntity;
import com.blackcompany.eeos.attend.persistence.AttendRepository;
import com.blackcompany.eeos.member.application.model.ActiveStatus;
import com.blackcompany.eeos.member.application.model.converter.MemberEntityConverter;
import com.blackcompany.eeos.member.application.service.QueryMemberService;
import com.blackcompany.eeos.member.fixture.FakeMember;
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

		MemberEntity mando = FakeMember.AmMandoEntity();
		MemberEntity bada = FakeMember.AmBadaEntity();
		AttendEntity mandoAttend = FakeAttend.attendMandoEntity();
		AttendEntity badaAttend = FakeAttend.attendBadaEntity();

		doNothing().when(programValidService).validate(programId);
		when(attendRepository.findAllByProgramIdAndStatus(programId, AttendStatus.find(attendStatus)))
				.thenReturn(List.of(mandoAttend, badaAttend));
		when(memberRepository.findMembersByIds(
						List.of(mandoAttend.getMemberId(), badaAttend.getMemberId())))
				.thenReturn(List.of(mando, bada));

		// when
		QueryAttendStatusResponse response = attendService.findAttendInfo(programId, attendStatus);

		// then
		List<AttendInfoResponse> members = response.getMembers();
		assertAll(
				() -> {
					assertEquals(members.get(0).getMemberId(), mandoAttend.getMemberId());
					assertEquals(members.get(0).getAttendStatus(), attendStatus);
				},
				() -> {
					assertEquals(members.get(1).getMemberId(), bada.getId());
					assertEquals(members.get(1).getAttendStatus(), attendStatus);
				});
	}

	@Test
	@DisplayName("참석 상태를 멤버의 활동 상태 기준으로 조회 시 해당 프로그램의 참여 정보가 없으면 관련 없음 참석 상태를 반환한다.")
	void find_attend_info_by_active_status() {
		// given
		Long programId = 1L;
		String activeStatus = "am";
		MemberEntity mando = FakeMember.AmMandoEntity();
		MemberEntity bada = FakeMember.AmBadaEntity();
		AttendEntity mandoAttend = FakeAttend.attendMandoEntity();

		when(memberRepository.findMembersByActiveStatus(ActiveStatus.AM))
				.thenReturn(List.of(mando, bada));
		when(attendRepository.findAllByProgramId(programId)).thenReturn(List.of(mandoAttend));

		// when
		QueryAttendActiveStatusResponse response = attendService.getAttendInfo(programId, activeStatus);

		// then
		List<AttendInfoActiveStatusResponse> members = response.getMembers();

		assertAll(
				() -> {
					assertEquals(members.get(0).getMemberId(), mandoAttend.getMemberId());
					assertEquals(members.get(0).getAttendStatus(), mandoAttend.getStatus().getStatus());
				},
				() -> {
					assertEquals(members.get(1).getMemberId(), bada.getId());
					assertEquals(members.get(1).getAttendStatus(), AttendStatus.NONRELATED.getStatus());
				});
	}

	@Test
	@DisplayName("참석 상태를 멤버의 활동 상태 기준으로 조회 시 해당 프로그램의 참여 정보 반환")
	void find_attend_info_by_active_status_include_nonRelated() {
		// given
		Long programId = 1L;
		String activeStatus = "all";
		MemberEntity mando = FakeMember.AmMandoEntity();
		MemberEntity bada = FakeMember.AmBadaEntity();

		AttendEntity mandoAttend = FakeAttend.attendMandoEntity();
		AttendEntity badaAttend = FakeAttend.attendBadaEntity();

		when(memberRepository.findMembers()).thenReturn(List.of(mando, bada));
		when(attendRepository.findAllByProgramId(programId))
				.thenReturn(List.of(mandoAttend, badaAttend));

		// when
		QueryAttendActiveStatusResponse response = attendService.getAttendInfo(programId, activeStatus);

		// then
		List<AttendInfoActiveStatusResponse> members = response.getMembers();

		assertAll(
				() -> {
					assertEquals(members.get(0).getMemberId(), mandoAttend.getMemberId());
					assertEquals(members.get(0).getAttendStatus(), mandoAttend.getStatus().getStatus());
					assertEquals(members.get(0).getName(), mando.getName());
					assertEquals(members.get(0).getActiveStatus(), mando.getActiveStatus().getStatus());
				},
				() -> {
					assertEquals(members.get(1).getMemberId(), badaAttend.getId());
					assertEquals(members.get(1).getAttendStatus(), badaAttend.getStatus().getStatus());
					assertEquals(members.get(1).getName(), bada.getName());
					assertEquals(members.get(1).getActiveStatus(), bada.getActiveStatus().getStatus());
				});
	}
}
