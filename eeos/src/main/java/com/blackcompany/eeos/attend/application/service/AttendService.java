package com.blackcompany.eeos.attend.application.service;

import com.blackcompany.eeos.attend.application.dto.AttendInfoResponse;
import com.blackcompany.eeos.attend.application.dto.ChangeStatusRequest;
import com.blackcompany.eeos.attend.application.dto.converter.AttendInfoConverter;
import com.blackcompany.eeos.attend.application.exception.NotFoundAttendException;
import com.blackcompany.eeos.attend.application.model.AttendModel;
import com.blackcompany.eeos.attend.application.model.AttendStatus;
import com.blackcompany.eeos.attend.application.model.converter.AttendEntityConverter;
import com.blackcompany.eeos.attend.application.usecase.ChangeStatusUsecase;
import com.blackcompany.eeos.attend.application.usecase.GetAttendantInfoUsecase;
import com.blackcompany.eeos.attend.persistence.AttendEntity;
import com.blackcompany.eeos.attend.persistence.AttendRepository;
import com.blackcompany.eeos.member.persistence.MemberEntity;
import com.blackcompany.eeos.member.persistence.MemberRepository;
import com.blackcompany.eeos.program.application.service.ProgramValidService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttendService implements GetAttendantInfoUsecase, ChangeStatusUsecase {

    private final AttendRepository attendRepository;

    private final MemberRepository memberRepository;
    private final ProgramValidService programValidService;
    private final AttendInfoConverter infoConverter;
    private final AttendEntityConverter attendEntityConverter;

    @Override
    public List<AttendInfoResponse> findAttendInfo(final Long programId) {
        programValidService.validate(programId);

        return memberRepository.findMembersByProgramId(programId).stream()
                .map(member -> infoConverter.from(member, getAttend(member, programId)))
                .collect(Collectors.toList());
    }

    private AttendStatus getAttend(final MemberEntity memberEntity, final Long programId) {
        return attendRepository
                .findByProgramIdAndMemberId(programId, memberEntity.getId())
                .map(AttendEntity::getStatus)
                .orElseThrow(() -> new NotFoundAttendException(programId));
    }

    @Override
    public List<AttendInfoResponse> findAttendInfo(final Long programId, final String status) {
        programValidService.validate(programId);

        AttendStatus attendStatus = AttendStatus.findByAttendStatus(status);

        List<Long> statusMember =
                attendRepository.findAllByProgramIdAndStatus(programId, attendStatus).stream()
                        .map(AttendEntity::getMemberId)
                        .collect(Collectors.toList());

        return memberRepository.findMembersByProgramId(programId).stream()
                .filter(member -> statusMember.contains(member.getId()))
                .map(member -> infoConverter.from(member, status))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void changeStatus(final ChangeStatusRequest request, final Long programId) {
        AttendModel model =
                attendRepository
                        .findByProgramIdAndMemberId(programId, request.getMemberId())
                        .map(attendEntityConverter::from)
                        .orElseThrow(() -> new NotFoundAttendException(programId));

        model.isSame(request.getBeforeAttendStatus());

        model.changeStatus(request.getAfterAttendStatus());
        attendRepository.save(attendEntityConverter.toEntity(model));
    }
}
