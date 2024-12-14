package com.leherryacademy.LeHerryAcademy.rest;

import com.leherryacademy.LeHerryAcademy.model.*;
import com.leherryacademy.LeHerryAcademy.reponse.LoginReponse;
import com.leherryacademy.LeHerryAcademy.reponsitory.PersonRepository;
import com.leherryacademy.LeHerryAcademy.service.AuthService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.experimental.NonFinal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "/apiAuthen",produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
public class AuthenticationRestController {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationRestController.class);
    @Autowired
    PersonRepository personRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    AuthService authService;

    @NonFinal
    protected static final String SIGNER_KEY = "sH3sBShpJxmBpRfP7VhodGont/eVURiAbKBVHdgVNbGSUWjEqg2ls4ip2w0a2mRo";


    private boolean verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date exprityTIme = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);
        if(!(verified && exprityTIme.after(new Date()))){
            System.out.println("Invalid Token");
            throw new JOSEException("Invalid Token");
        }
        else{
            System.out.println("oke roi");
        }
        return true;
    }
    private SignedJWT verifyKeyToken(String token) throws  JOSEException, ParseException{
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);

        var verified = signedJWT.verify(verifier);
        if(!verified){
            throw new JOSEException("Invalid Token");
        }
        return signedJWT;
    }
    private boolean checkedTimeToken(String token) throws  JOSEException, ParseException{
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        var verified = signedJWT.verify(verifier);
        Date exprityTIme = signedJWT.getJWTClaimsSet().getExpirationTime();

        Date currentTime = new Date();
        System.out.println(exprityTIme);
        System.out.println(currentTime);
        if(verified && !exprityTIme.after(new Date())){
            return true;
        }
        return false;
    }

//    @PostMapping("/login")
//    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginReponse person, HttpServletResponse response){
//        String email = person.getEmail();
//        String password = person.getPwd();
//        var user = personRepository.readByEmailAndAccountFrom(email, "APP_LOG");
//        if(!user.isPresent()){
//            return ResponseEntity
//                    .status(HttpStatus.UNAUTHORIZED)
//                    .body(new AuthenticationResponse(null,null, false));
//        }
//        else{
//            Person account = user.get();
//            boolean authenticated = passwordEncoder.matches(password, account.getPwd());
//            if (!authenticated) {
//                return ResponseEntity
//                        .status(HttpStatus.UNAUTHORIZED)
//                        .body(new AuthenticationResponse(null,null, false));
//            }
//            var token = generateToken(account);
//            var refreshToken = generateRefreshToken(account);
//            long maxAge = 60;
//            ZonedDateTime expirationTime = ZonedDateTime.now(ZoneId.systemDefault()).plusSeconds(maxAge);
//            String expires = DateTimeFormatter.RFC_1123_DATE_TIME.format(expirationTime);
//            //Set JWT Cookie
//            response.addHeader("Set-Cookie", "JWToken=" + token + "; Expires=" + expires + "; Path=/; HttpOnly");
//            response.addHeader("Set-Cookie", "temp=" + "token" + "; Path=/; HttpOnly");
//            //Set RefreshJwt Cookie
//            response.addHeader("Set-Cookie", "RefreshJWTToken=" + refreshToken + "; Expires=" + expires + "; Path=/; HttpOnly");
//            AuthenticationResponse authenticationResponse = new AuthenticationResponse(token,refreshToken, true);
//            return ResponseEntity
//                    .status(HttpStatus.OK)
//                    .body(authenticationResponse);
//        }
//    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginReponse person, HttpServletResponse response){
        System.out.println("Checked Remember" + person.isCheckedRememberMe());
        String email = person.getEmail();
        String password = person.getPwd();
        var user = personRepository.readByEmailAndAccountFrom(email, "APP_LOG");
        if(!user.isPresent()){
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthenticationResponse(null,null, false));
        }
        else{
            Person account = user.get();
            boolean authenticated = passwordEncoder.matches(password, account.getPwd());
            if (!authenticated) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(new AuthenticationResponse(null,null, false));
            }
            var token = generateToken(account);
            var refreshToken = generateRefreshToken(account);
            long maxAge = 60 * 60;
            long maxAgeRefresh = 60 * 60 * 24;

            if(person.isCheckedRememberMe()){
                response.addHeader("Set-Cookie", "JWToken=" + token + "; Max-Age=" + maxAge + "; Path=/; HttpOnly");
                response.addHeader("Set-Cookie", "RefreshJWTToken=" + refreshToken + "; Max-Age=" + maxAgeRefresh + "; Path=/; HttpOnly");
            }
            else {
                response.addHeader("Set-Cookie", "JWToken=" + token + "; Path=/; HttpOnly");
                response.addHeader("Set-Cookie", "RefreshJWTToken=" + refreshToken + "; Path=/; HttpOnly");
            }


            AuthenticationResponse authenticationResponse = new AuthenticationResponse(token, refreshToken, true);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(authenticationResponse);
        }
    }
    @PostMapping("/loginWithService")
    public ResponseEntity<AuthenticationResponse> loginWithService(HttpSession session, @RequestBody Person person, HttpServletResponse response){
        String email = person.getEmail();
        String accountFrom = person.getAccountFrom();
        Optional<Person> user = personRepository.readByEmailAndAccountFrom(email,accountFrom);

        if(user.isPresent()){
            Person account = user.get();
            var token = generateToken(account);
            var refreshToken = generateRefreshToken(account);
            long maxAge = 60 * 60;
            long maxAgeRefresh = 60 * 60 * 24;
            response.addHeader("Set-Cookie", "JWToken=" + token + "; Max-Age=" + maxAge + "; Path=/; HttpOnly");
            response.addHeader("Set-Cookie", "RefreshJWTToken=" + refreshToken + "; Max-Age=" + maxAgeRefresh + "; Path=/; HttpOnly");
            AuthenticationResponse authenticationResponse = new AuthenticationResponse(token,refreshToken, true);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(authenticationResponse);
        }
        else{
            authService.createPerson(person);
            var token = generateToken(person);
            var refreshToken = generateRefreshToken(person);
            long maxAge = 60 * 60;
            ZonedDateTime expirationTime = ZonedDateTime.now(ZoneId.systemDefault()).plusSeconds(maxAge);
            String expires = DateTimeFormatter.RFC_1123_DATE_TIME.format(expirationTime);
            //Set JWT Cookie
            response.addHeader("Set-Cookie", "JWToken=" + token + "; Expires=" + expires + "; Path=/; HttpOnly");
            //Set RefreshJwt Cookie
            response.addHeader("Set-Cookie", "RefreshJWTToken=" + refreshToken + "; Expires=" + expires + "; Path=/; HttpOnly");
            AuthenticationResponse authenticationResponse = new AuthenticationResponse(token,refreshToken, true);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(authenticationResponse);
        }
    }

    @PostMapping("/refresh")
    public void refresh( HttpServletResponse response, @RequestParam("checkedRemember") boolean checkedRemember, @CookieValue(value = "RefreshJWTToken", required = false) String refreshToken, @RequestBody Person person) throws  JOSEException, ParseException, NullPointerException {
        if (refreshToken == null) {
            throw new IllegalArgumentException("Refresh token is missing.");
        }
        else{
            System.out.println("Checked2" + checkedRemember);
            if (verifyToken(refreshToken) == true){
                String email = person.getEmail();
                String accountFrom = person.getAccountFrom();
                Optional<Person> user = personRepository.readByEmailAndAccountFrom(email,accountFrom);
                if(user.isPresent()){
                    Person account = user.get();
                    var token = generateToken(account);
                    var refreshTokenReponse = generateRefreshToken(account);
                    long maxAge = 60 * 60;
                    long maxAgeRefresh = 60 * 60 * 24;

                    if (checkedRemember == true){
                        response.addHeader("Set-Cookie", "JWToken=" + token + "; Max-Age=" + maxAge + "; Path=/; HttpOnly");
                        response.addHeader("Set-Cookie", "RefreshJWTToken=" + refreshTokenReponse + "; Max-Age=" + maxAgeRefresh + "; Path=/; HttpOnly");
                    }
                    else {
                        response.addHeader("Set-Cookie", "JWToken=" + token + "; Path=/; HttpOnly");
                        response.addHeader("Set-Cookie", "RefreshJWTToken=" + refreshToken + "; Path=/; HttpOnly");
                    }

                }

            }
        }

    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        System.out.println("Da thuc hien");
        Cookie jwtCookie = new Cookie("JWToken", null);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0);

        Cookie refreshJwtCookie = new Cookie("RefreshJWTToken", null);
        refreshJwtCookie.setHttpOnly(true);
        refreshJwtCookie.setPath("/");
        refreshJwtCookie.setMaxAge(0);


        response.addCookie(jwtCookie);
        response.addCookie(refreshJwtCookie);


        return ResponseEntity.ok().build();
    }

    private String generateToken (Person person){
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(person.getEmail())
                .issuer("devter")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", person.getRoles().getRoleName())
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return  jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateRefreshToken (Person person){
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(person.getEmail())
                .issuer("devter")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(24, ChronoUnit.HOURS).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", person.getRoles().getRoleName())
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return  jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

}
