package com.bty.karaoke.mememusicboxservice.mapper;

import com.bty.karaoke.mememusicboxservice.dto.response.AccountResponse;
import com.bty.karaoke.mememusicboxservice.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
    uses = {EmployeeProfileMapper.class, MemberProfileMapper.class}
)
public interface AccountMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "role", target = "role")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "isActive", target = "isActive")
    @Mapping(source = "employeeProfile", target = "employeeProfile")
    @Mapping(source = "memberProfile", target = "memberProfile")
    public AccountResponse toAccountResponse(Account account);
}
