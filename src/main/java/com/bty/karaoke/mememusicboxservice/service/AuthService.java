package com.bty.karaoke.mememusicboxservice.service;

import com.bty.karaoke.mememusicboxservice.dto.request.AuthenticationRequest;
import com.bty.karaoke.mememusicboxservice.dto.request.LogoutRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.AccountResponse;
import com.bty.karaoke.mememusicboxservice.dto.response.AuthenticationResponse;

public interface AuthService {

    public AuthenticationResponse authenticate(AuthenticationRequest request);

    public void logout(LogoutRequest request);


}
