package com.bty.karaoke.mememusicboxservice.util;

import com.bty.karaoke.mememusicboxservice.entity.Account;
import com.bty.karaoke.mememusicboxservice.repository.InvalidTokenRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    @Value("${jwt.signer-key}")
    private String signerKey;

    @Value("${jwt.valid-duration}")
    private long validDuration;

    private final InvalidTokenRepository invalidTokenRepository;

    public SignedJWT getSignedJWT(String token) throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        return signedJWT;
    }

    public Date getExpiryTime(String token) throws ParseException {
        SignedJWT signedJWT = getSignedJWT(token);
        return signedJWT.getJWTClaimsSet().getExpirationTime();
    }

    public String getJWTId(String token) throws ParseException {
        SignedJWT signedJWT = getSignedJWT(token);
        return signedJWT.getJWTClaimsSet().getJWTID();
    }

    public boolean verifyToken(String token) throws ParseException, JOSEException {
//        SignedJWT signedJWT = SignedJWT.parse(token);
        SignedJWT signedJWT = getSignedJWT(token);
        JWSVerifier verifier = new MACVerifier(signerKey.getBytes());
//        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        Date expiryTime = getExpiryTime(token);
        if(!signedJWT.verify(verifier)) {
            return false;
        }
        if(!expiryTime.after(new Date())) {
            return false;
        }
        if(invalidTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            return false;
        }
        return true;
    }

    public String generateToken(Account account) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(account.getEmail())
                .issuer("bty.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(validDuration, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(account))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(signerKey.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    private String buildScope(Account account) {
        return "ROLE_" + account.getRole().name();
    }
}
