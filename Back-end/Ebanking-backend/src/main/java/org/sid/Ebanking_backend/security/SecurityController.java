//package org.sid.Ebanking_backend.security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
//import org.springframework.security.oauth2.jwt.*;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.time.Instant;
//import java.time.temporal.ChronoUnit;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@RestController
//@RequestMapping("/auth")
//public class SecurityController {
//    @Autowired
//    private AuthenticationManager authenticationManager;
//    private JwtEncoder jwtEncoder;
//
//    @GetMapping("/profile")
//    public Authentication authentication(){
//         return SecurityContextHolder.getContext().getAuthentication();
//
//    }
//
//
//    @PostMapping("/login")
//    public Map<String,String > login(String userName,String password){
//          Authentication authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName,password));
//        Instant instant=Instant.now();
//        String scope=authentication.getAuthorities().stream().map(a->a.getAuthority()).collect(Collectors.joining(" "));
//        JwtClaimsSet jwtClaimsSet=JwtClaimsSet.builder()
//                .issuedAt(instant)
//                .expiresAt(instant.plus(10, ChronoUnit.MINUTES))
//                .subject(userName)
//                .claim("scope",scope)
//                .build();
//        JwtEncoderParameters jwtEncoderParameters=JwtEncoderParameters.from(
//             JwsHeader.with(MacAlgorithm.HS512).build(),jwtClaimsSet
//        );
//
//        String jwt=jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
//        return Map.of("access-token",jwt);
//    }
//}
