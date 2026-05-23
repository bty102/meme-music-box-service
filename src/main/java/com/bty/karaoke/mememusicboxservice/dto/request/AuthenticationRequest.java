package com.bty.karaoke.mememusicboxservice.dto.request;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationRequest {

    private String email;
    private String password;
}
