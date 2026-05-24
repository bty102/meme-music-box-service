package com.bty.karaoke.mememusicboxservice.mapper;

import com.bty.karaoke.mememusicboxservice.dto.response.MemberProfileResponse;
import com.bty.karaoke.mememusicboxservice.entity.MemberProfile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberProfileMapper {

    public MemberProfileResponse toMemberProfileResponse(MemberProfile memberProfile);
}
