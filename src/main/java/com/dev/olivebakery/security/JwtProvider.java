package com.dev.olivebakery.security;

import com.dev.olivebakery.domain.enums.MemberRole;
import com.dev.olivebakery.service.UserDetailsServiceImpl;
import io.jsonwebtoken.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Component
public class JwtProvider {
    private final UserDetailsServiceImpl userDetailsService;
    private final long validityInMilliseconds = 36000000; // 테스트 중이라 10시간으로 설정.
    private String secretKey = "OLIVE";

    public JwtProvider(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostConstruct
    protected void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String username, Set<MemberRole> roles){
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", roles);

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public boolean validateToken(String token) throws JwtException, IllegalArgumentException {
        Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        return claims.getBody().getExpiration().after(new Date());
    }

    public String resolveToken(HttpServletRequest req){
        ServletRequest request;
        String bearerToken = req.getHeader("Authorization");
        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }

    public String resolveToken(String bearerToken){
        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }

    public Authentication getAuthenticationByToken(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUserEmailByResolvedToken(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private String getUserEmailByResolvedToken(String bearerToken){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(bearerToken).getBody().getSubject();
    }

    // 사용자 아이디
    public String getUserEmailByToken(String bearerToken){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(resolveToken(bearerToken)).getBody().getSubject();
    }

    // 사용자 권한
    public List<MemberRole> getUserRolesByToken(String bearerToken){
        Set<MemberRole> roles = new HashSet<>();
        List list = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(resolveToken(bearerToken)).getBody().get("roles", List.class);
        for (Object role : list) {
            if(String.valueOf(role).equals("CLIENT"))
                roles.add(MemberRole.CLIENT);
            else
                roles.add(MemberRole.ADMIN);
        }
        return new ArrayList<>(roles);
    }
}
