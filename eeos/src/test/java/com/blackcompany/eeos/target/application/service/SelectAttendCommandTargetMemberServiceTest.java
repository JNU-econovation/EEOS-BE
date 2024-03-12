package com.blackcompany.eeos.target.application.service;

import static org.mockito.Mockito.*;

import com.blackcompany.eeos.member.application.model.ActiveStatus;
import com.blackcompany.eeos.member.application.model.converter.MemberEntityConverter;
import com.blackcompany.eeos.member.fixture.MemberFixture;
import com.blackcompany.eeos.member.persistence.MemberRepository;
import com.blackcompany.eeos.program.application.dto.ProgramMembers;
import com.blackcompany.eeos.target.application.dto.TargetMember;
import com.blackcompany.eeos.target.application.model.converter.AttendEntityConverter;
import com.blackcompany.eeos.target.fixture.TargetMemberFixture;
import com.blackcompany.eeos.target.persistence.AttendRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SelectAttendCommandTargetMemberServiceTest {
	@Mock private AttendRepository attendRepository;
	@Spy private AttendEntityConverter entityConverter;
	@Mock private MemberRepository memberRepository;
	@Spy private MemberEntityConverter memberEntityConverter;
	@InjectMocks private SelectAttendCommandTargetMemberService selectAttendTargetService;

	@Test
	@DisplayName("대상자를 전달받아 참석 대상자로 저장한다.")
	void save_attend() {
		// given
		List<ProgramMembers> 수민_바다_프로그램_대상자 =
				List.of(TargetMemberFixture.프로그램_대상자(1L), TargetMemberFixture.프로그램_대상자(2L));
		when(memberRepository.findMembersByIds(
						수민_바다_프로그램_대상자.stream().map(TargetMember::getMemberId).collect(Collectors.toList())))
				.thenReturn(
						List.of(
								MemberFixture.멤버_엔티티(1L, ActiveStatus.AM),
								MemberFixture.멤버_엔티티(2L, ActiveStatus.AM)));

		// when
		selectAttendTargetService.save(1L, 수민_바다_프로그램_대상자);

		// then
		verify(attendRepository).saveAll(anyList());
	}
}
