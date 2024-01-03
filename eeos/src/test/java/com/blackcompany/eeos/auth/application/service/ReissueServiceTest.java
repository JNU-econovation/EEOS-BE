package com.blackcompany.eeos.auth.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.blackcompany.eeos.auth.application.domain.TokenModel;
import com.blackcompany.eeos.auth.application.domain.converter.OauthInfoEntityConverter;
import com.blackcompany.eeos.auth.application.domain.token.TokenResolver;
import com.blackcompany.eeos.auth.application.exception.InvalidTokenException;
import com.blackcompany.eeos.auth.persistence.AuthInfoEntity;
import com.blackcompany.eeos.auth.persistence.AuthInfoRepository;
import com.blackcompany.eeos.auth.persistence.OauthInfoRepository;
import com.blackcompany.eeos.member.application.model.converter.MemberEntityConverter;
import com.blackcompany.eeos.member.persistence.MemberRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(MockitoExtension.class)
class ReissueServiceTest {

    @Mock
    CreateTokenService createTokenService;
    @Mock
    AuthInfoRepository authInfoRepository;

    @Mock
    TokenResolver tokenResolver;
    @InjectMocks
    ReissueService reissueService;

    @Test
    @DisplayName("전달받은 토큰이 서버가 가지고 있는 유저의 토큰이 아닐 때 예외가 발생한다.")
    void execute() {
        // given
        String token = "token";
        Long memberId = 1L;

        when(tokenResolver.getUserInfo(token)).thenReturn(memberId);
        when(authInfoRepository.findByMemberIdAndToken(memberId, token)).thenReturn(Optional.ofNullable(null));

        // when & then
        assertThrows(InvalidTokenException.class, () -> reissueService.execute(token));
    }
}