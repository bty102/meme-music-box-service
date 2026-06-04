package com.bty.karaoke.mememusicboxservice.service.impl;

import com.bty.karaoke.mememusicboxservice.dto.request.EmployeeProfileUpdateRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.EmployeeProfileResponse;
import com.bty.karaoke.mememusicboxservice.entity.EmployeeProfile;
import com.bty.karaoke.mememusicboxservice.exception.AppException;
import com.bty.karaoke.mememusicboxservice.exception.ErrorCode;
import com.bty.karaoke.mememusicboxservice.mapper.EmployeeProfileMapper;
import com.bty.karaoke.mememusicboxservice.repository.EmployeeProfileRepository;
import com.bty.karaoke.mememusicboxservice.service.EmployeeProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class EmployeeProfileServiceImpl implements EmployeeProfileService {

    private final EmployeeProfileRepository employeeProfileRepository;
    private final EmployeeProfileMapper employeeProfileMapper;

    @Override
    public EmployeeProfileResponse updateEmployeeProfile(Long profileId, @Valid EmployeeProfileUpdateRequest request) {
        if(profileId == null) {
            throw new AppException(ErrorCode.EMPLOYEE_PROFILE_NOT_EXISTED);
        }
        EmployeeProfile employeeProfile = employeeProfileRepository.findById(profileId)
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_PROFILE_NOT_EXISTED));

        if(employeeProfileRepository.existsByPhoneNumberAndIdIsNot(request.getPhoneNumber(), employeeProfile.getId())) {
            throw new AppException(ErrorCode.EMPLOYEE_PROFILE_PHONE_NUMBER_EXISTED);
        }

        if(employeeProfileRepository.existsByNationalIdAndIdIsNot(request.getNationalId(), employeeProfile.getId())) {
            throw new AppException(ErrorCode.EMPLOYEE_PROFILE_NATIONAL_ID_EXISTED);
        }

        employeeProfileMapper.updateEmployeeProfile(employeeProfile, request);
        employeeProfile =  employeeProfileRepository.save(employeeProfile);
        return employeeProfileMapper.toEmployeeProfileResponse(employeeProfile);
    }
}
