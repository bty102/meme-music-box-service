package com.bty.karaoke.mememusicboxservice.dto.response;

import com.bty.karaoke.mememusicboxservice.constant.Role;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountResponse {

    private Long id;

    private String email;

    private Role role;

    private LocalDateTime createdAt;

    private Boolean isActive;

    private EmployeeProfileResponse employeeProfile;

    private MemberProfileResponse memberProfile;

}
