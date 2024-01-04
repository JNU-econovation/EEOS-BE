package com.blackcompany.eeos.member.application.service;

import static org.mockito.Mockito.when;

import com.blackcompany.eeos.member.application.dto.ChangeActiveStatusRequest;
import com.blackcompany.eeos.member.application.dto.converter.CommandMemberResponseConverter;
import com.blackcompany.eeos.member.application.exception.NotFoundMemberException;
import com.blackcompany.eeos.member.application.model.converter.MemberEntityConverter;
import com.blackcompany.eeos.member.persistence.MemberRepository;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommandMemberServiceTest {
	@Mock MemberRepository memberRepository;

	@Spy MemberEntityConverter entityConverter;

	@Spy CommandMemberResponseConverter responseConverter;

	@InjectMocks CommandMemberService commandMemberService;

	@Test
	@DisplayName("존재하지 않는 멤버일 경우 예외가 발생한다.")
	void not_found_member() {
		// given
		when(memberRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(null));
		ChangeActiveStatusRequest request =
				ChangeActiveStatusRequest.builder().activeStatus("am").build();

		// when & then
		Assertions.assertThrows(
				NotFoundMemberException.class, () -> commandMemberService.execute(1L, request));
	}
}
