package com.example.demo.service;

import com.example.demo.dto.request.AuthenticationRequest;
import com.example.demo.dto.request.IntrospectRequest;
import com.example.demo.dto.response.AuthenticationResponse;
import com.example.demo.dto.response.IntrospectReponse;
import com.example.demo.entity.User;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class AuthenticationService {
    final  UserRepository userRepository;

    @NonFinal //ko inject vao constructor
    @Value( "${jwt.signerKey}")
    protected String SECRET_KEY;

    public IntrospectReponse introspect(IntrospectRequest request ) {
        var token = request.getToken();
        try {
            JWSVerifier verifier = new MACVerifier(SECRET_KEY.getBytes());

            SignedJWT signedJWT = SignedJWT.parse(token);

            var verified = signedJWT.verify(verifier);
            Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();

            return IntrospectReponse.builder()
                    .vaild(verified && expirationTime.after(new Date()))
                    .build();
        } catch (ParseException | JOSEException e) {
            log.error("Error parsing or verifying token", e);
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

    }

     public AuthenticationResponse authenticate(AuthenticationRequest request){
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassword(),user.getPassword() );
        if (!authenticated)
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        String token = generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .build();
     }
     private String generateToken(User user) {
        // Token generation logic (e.g., JWT)
         JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
         JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                 .subject(user.getUsername())
                 .issuer("your-issuer")
                 .issueTime(new Date())
                 .expirationTime(new Date(
                         Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                 ))
                 .claim("scope", buildScope(user)) // Example claim
                 .build();
         Payload payload = new Payload(jwtClaimsSet.toJSONObject());

         JWSObject jwsObject = new JWSObject(header, payload);

         try {
             jwsObject.sign(new MACSigner(SECRET_KEY.getBytes()));
                return jwsObject.serialize();
         } catch (JOSEException e) {
             log.error("Error signing token", e);
             throw new RuntimeException(e);
         }
     }
     private String buildScope(User user){
         StringJoiner stringJoiner = new StringJoiner(" ");
         if (!CollectionUtils.isEmpty(user.getRoles()))
                user.getRoles().forEach(stringJoiner::add);
             return stringJoiner.toString();

     }

}
