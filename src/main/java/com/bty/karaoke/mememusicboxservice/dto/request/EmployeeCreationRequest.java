package com.bty.karaoke.mememusicboxservice.dto.request;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeCreationRequest {

    @NotNull(message = "EMPLOYEE_PROFILE_FULL_NAME_NULL")
    @Size(min = 1, max = 100, message = "EMPLOYEE_PROFILE_FULL_NAME_SIZE_INVALID")
    private String fullName;

    @NotNull(message = "EMPLOYEE_PROFILE_PHONE_NUMBER_NULL")
    @Size(max = 20, message = "EMPLOYEE_PROFILE_PHONE_NUMBER_SIZE_INVALID")
    private String phoneNumber;

    @NotNull(message = "EMPLOYEE_PROFILE_NATIONAL_ID_NULL")
    @Size(max = 12, message = "EMPLOYEE_PROFILE_NATIONAL_ID_SIZE_INVALID")
    private String nationalId;

    @NotNull(message = "EMPLOYEE_PROFILE_ISMALE_NULL")
    private Boolean isMale;

    @NotNull(message = "EMPLOYEE_PROFILE_DATE_OF_BIRTH_NULL")
    private LocalDate dateOfBirth;

    @Size(max = 255, message = "EMPLOYEE_PROFILE_ADDRESS_SIZE_INVALID")
    private String address;

    @NotNull(message = "ACCOUNT_EMAIL_NULL")
    private String email;

    @NotNull(message = "ACCOUNT_PASSWORD_NULL")
    @Size(min = 6, message = "ACCOUNT_PASSWORD_SIZE_INVALID")
    private String password;
}
