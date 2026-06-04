package com.bty.karaoke.mememusicboxservice.mapper;

import com.bty.karaoke.mememusicboxservice.dto.request.EmployeeProfileUpdateRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.EmployeeProfileResponse;
import com.bty.karaoke.mememusicboxservice.entity.EmployeeProfile;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EmployeeProfileMapper {

    public EmployeeProfileResponse toEmployeeProfileResponse(EmployeeProfile employeeProfile);

    public void updateEmployeeProfile(@MappingTarget EmployeeProfile employeeProfile, EmployeeProfileUpdateRequest request);
}
