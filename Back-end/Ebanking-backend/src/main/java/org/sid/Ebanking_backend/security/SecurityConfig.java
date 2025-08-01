//package org.sid.Ebanking_backend.security;
//
//import com.nimbusds.jose.jwk.source.ImmutableSecret;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.ProviderManager;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
//import org.springframework.security.oauth2.jwt.JwtDecoder;
//import org.springframework.security.oauth2.jwt.JwtEncoder;
//import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
//import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//
//import javax.crypto.spec.SecretKeySpec;
//
//@Configuration
//@EnableWebSecurity
//
//public class SecurityConfig {
//    @Value("${jwt.secret}")
//    private String secretKey;
//    @Bean
//    public InMemoryUserDetailsManager inMemoryUserDetailsManager(){
//        PasswordEncoder passwordEncoder=passwordEncoder();
//        return new InMemoryUserDetailsManager(
//                User.withUsername("user1").password(passwordEncoder.encode("1234")).authorities("USER").build(),
//                User.withUsername("admin").password(passwordEncoder.encode("1234")).authorities("ADMIN").build(),
//                User.withUsername("user2").password(passwordEncoder.encode("12345")).authorities("USER").build()
//        );
//    }
//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        return httpSecurity.sessionManagement(sm->sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .csrf(csrf->csrf.disable())
//                .authorizeHttpRequests(ar->ar.anyRequest().authenticated())
//                //.httpBasic(Customizer.withDefaults());
//                .oauth2ResourceServer(oa->oa.jwt(Customizer.withDefaults()))
//                .build();
//    }
//    @Bean
//    JwtEncoder jwtEncoder(){
//        //String secretKey="pes5jevunnh96ldsm4ijwovb5lexuzk2u6mvqfla0keqrdr3jo3xchirggjkeapj";
//        return new NimbusJwtEncoder(new ImmutableSecret<>(secretKey.getBytes()));
//
//    }
//
//    @Bean
//    JwtDecoder jwtDecoder(){
//        //String secretKey="pes5jevunnh96ldsm4ijwovb5lexuzk2u6mvqfla0keqrdr3jo3xchirggjkeapj";
//        SecretKeySpec secretKeySpec=new SecretKeySpec(secretKey.getBytes(),"RSA");
//        return NimbusJwtDecoder.withSecretKey(secretKeySpec).macAlgorithm(MacAlgorithm.HS512).build();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService){
//        DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
//        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
//        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
//        return new ProviderManager(daoAuthenticationProvider);
//    }
//}
