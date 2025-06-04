package com.blackcompany.eeos.target.application.service;

import com.blackcompany.eeos.member.application.model.MemberModel;
import com.blackcompany.eeos.member.application.repository.MemberRepository;
import com.blackcompany.eeos.target.application.dto.AttendInfoResponse;
import com.blackcompany.eeos.target.application.dto.QueryAttendStatusResponse;
import com.blackcompany.eeos.target.application.dto.converter.AttendInfoConverter;
import com.blackcompany.eeos.target.application.dto.converter.QueryAttendStatusResponseConverter;
import com.blackcompany.eeos.target.application.model.AttendModel;
import com.blackcompany.eeos.target.application.model.AttendStatus;
import com.blackcompany.eeos.target.application.model.converter.AttendEntityConverter;
import com.blackcompany.eeos.target.persistence.AttendRepository;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisFireFingerCacheService {

    private static final String KEY_PREFIX = "firefinger:ranking";
    private static final long TTL = 5; // 분

    private final RedisTemplate<String, String> redisTemplate;
    private final AttendRepository attendRepository;
    private final MemberRepository memberRepository;
    private final AttendInfoConverter attendInfoConverter;
    private final QueryAttendStatusResponseConverter attendStatusResponseConverter;
    private final AttendEntityConverter attendEntityConverter;

    public QueryAttendStatusResponse getTop5FromCacheOrDb(Long programId){
        List<Long> memberIds = getCachedMemberIds(programId);

        if(memberIds.isEmpty()){
            memberIds = fetchTop5FromDbAndCache(programId);
        }
        return buildResponse(memberIds, programId);

    }
    public void invalidateCache(Long programId){
        redisTemplate.delete(buildKey(programId));
    }

    private List<Long> getCachedMemberIds(Long programId){
        List<String> cached = redisTemplate.opsForList().range(buildKey(programId), 0, -1);
        if(cached == null || cached.isEmpty()) return List.of();
        return cached.stream().map(Long::valueOf).toList();
    }

    private List<Long> fetchTop5FromDbAndCache(Long programId){
        List<AttendModel> top5 = attendRepository
                .findTop5ByProgramIdAndStatusOrderByRankAsc(programId, AttendStatus.ATTEND)
                .stream()
                .map(attendEntityConverter::from)
                .toList();

        List<Long> memberIds = top5.stream().map(AttendModel::getMemberId).toList();
        cacheMemberIds(programId, memberIds);
        return memberIds;
    }



    private void cacheMemberIds(Long programId, List<Long> memberIds){
        String key = buildKey(programId);
        List<String> stringIds = memberIds.stream().map(String::valueOf).toList();
        redisTemplate.opsForList().rightPushAll(key, stringIds);
        redisTemplate.expire(key, TTL, TimeUnit.MINUTES);
    }

    private QueryAttendStatusResponse buildResponse(List<Long> memberIds, Long programId){
        List<MemberModel> members = memberRepository.findMembersByIdsInOrder(memberIds);

        List<AttendInfoResponse> response = members.stream()
                .map(member -> attendInfoConverter.from(member, "attend"))
                .toList();

        return attendStatusResponseConverter.of(response);
    }

    private String buildKey(Long programId){
        return KEY_PREFIX + programId;

    }
}
