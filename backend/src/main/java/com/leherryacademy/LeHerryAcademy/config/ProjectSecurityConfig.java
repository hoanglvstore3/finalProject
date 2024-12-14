package com.leherryacademy.LeHerryAcademy.config;

import com.leherryacademy.LeHerryAcademy.Filter.JwtCookieFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.spec.SecretKeySpec;

@Configuration
public class ProjectSecurityConfig {
    protected static final String SIGNER_KEY = "sH3sBShpJxmBpRfP7VhodGont/eVURiAbKBVHdgVNbGSUWjEqg2ls4ip2w0a2mRo";


    @Bean
    JwtDecoder jwtDecoder() {
        SecretKeySpec secretKeySpec = new SecretKeySpec(SIGNER_KEY.getBytes(), "HS512");
        return NimbusJwtDecoder.withSecretKey(secretKeySpec).macAlgorithm(MacAlgorithm.HS512).build();
    };
    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        http.addFilterBefore(new JwtCookieFilter(), UsernamePasswordAuthenticationFilter.class);

        http.csrf((csrf) -> csrf.ignoringRequestMatchers("/saveMsg").ignoringRequestMatchers("/public/**")
                        .ignoringRequestMatchers("/api/**").ignoringRequestMatchers("/data-api/**").ignoringRequestMatchers("/apiAuthen/**"))
                .authorizeHttpRequests((requests) -> requests.requestMatchers("/dashboard").authenticated()
                        .requestMatchers("/displayMessages/**").hasRole("ADMIN")
                        .requestMatchers("/closeMsg/**").hasRole("ADMIN")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/data-api/**").permitAll()
//                        .requestMatchers("/api/**").hasAuthority("ROLE_ADMIN")
//                        .requestMatchers("/api/**").authenticated()
                        .requestMatchers("/api/courses/auth/**").authenticated()
                        .requestMatchers("/api/contact/**").authenticated()
                        .requestMatchers("/api/courses/public/**").permitAll()
                        .requestMatchers("/api/comments/public/**").permitAll()
                        .requestMatchers("/api/comments/auth/**").authenticated()
                        .requestMatchers("/api/ratings/public/**").permitAll()
                        .requestMatchers("/api/ratings/auth/**").authenticated()
                        .requestMatchers("/api/person/auth/**").authenticated()
                        .requestMatchers("/api/contact/updatePerson/**").permitAll()
                        .requestMatchers("/apiAuthen/**").permitAll()
                        .requestMatchers("/data-api/**").permitAll()
                        .requestMatchers("/displayProfile").authenticated()
                        .requestMatchers("/updateProfile").authenticated()
                        .requestMatchers("/student/**").hasRole("STUDENT")
                        /*.requestMatchers("/profile/**").permitAll()
                        .requestMatchers("/courseses/**").permitAll()
                        .requestMatchers("/contacts/**").permitAll()
                         .requestMatchers("/data-api/**").permitAll()*/
                        .requestMatchers("/", "/home").permitAll()
                        .requestMatchers("/holidays/**").permitAll()
                        .requestMatchers("/contact").permitAll()
                        .requestMatchers("/saveMsg").permitAll()
                        .requestMatchers("/courses").permitAll()
                        .requestMatchers("/about").permitAll()
                        .requestMatchers("/assets/**").permitAll()
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/logout").permitAll()
                        .requestMatchers("/public/**").permitAll())
                .oauth2ResourceServer(
                        oauth2 -> oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder())
                                .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                )
                .formLogin(loginConfigurer -> loginConfigurer.permitAll())
                .httpBasic(Customizer.withDefaults())
                ;

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
