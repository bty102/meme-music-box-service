package com.bty.karaoke.mememusicboxservice.dto.response;

import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberProfileResponse {

    private Long id;

    private String memberCode;

    private String fullName;

    private Boolean isMale;

    private LocalDate dateOfBirth;

    private Integer loyaltyPoint;

    private String imageUrl;
}
