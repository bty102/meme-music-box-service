package com.bty.karaoke.mememusicboxservice.dto.response;

import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeProfileResponse {

    private Long id;

    private String employeeCode;

    private String fullName;

    private String phoneNumber;

    private String nationalId;

    private Boolean isMale;

    private LocalDate dateOfBirth;

    private String address;

    private String imageUrl;
}
