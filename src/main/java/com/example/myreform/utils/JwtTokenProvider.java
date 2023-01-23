package com.example.myreform.utils;

import com.example.myreform.User.domain.User;
import com.example.myreform.User.repository.UserRepository;
import com.example.myreform.config.Secret;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
@Component
// 토큰을 생성하고 검증하는 클래스입니다.
public class JwtTokenProvider {

    String secret = Secret.JWT_SECRET_KEY;
    // 토큰 유효시간 365일
    private long tokenValidTime = 365 * 24* 60 * 60 * 1000L;

    private final UserDetailsService userDetailsService;//@RequiredArgsConstructor

    // 객체 초기화, secretKey를 Base64로 인코당
    @PostConstruct //의존성 주입이 이루어진 후 초기화를 수행
    protected void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    // JWT 토큰 생성
    public String createJwt(int userId) {
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam("type","jwt")
                .claim("userId",userId) // 정보 저장
                .setIssuedAt(now) //토큰 발행 시간 정보
                .setExpiration(new Date(System.currentTimeMillis()+tokenValidTime)) //만기
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    // 토큰에서 회원 정보 추출
    public String getUserPk(String token) {//JWT를 다시 Decode해서 데이터를 얻어내기
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

}
