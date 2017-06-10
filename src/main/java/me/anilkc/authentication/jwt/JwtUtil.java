package me.anilkc.authentication.jwt;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import me.anilkc.domain.Role;
import me.anilkc.service.UserAdapter;

public class JwtUtil {

  private static final Logger LOG = LoggerFactory.getLogger(JwtUtil.class);

  public static final String AUTHORIZATION = "Authorization";
  public static final String HEADER_APP_SUBJECT = "App-Subject";
  public static final String USER_AGENT = "User-Agent";
  public static final String JWT_TOKEN = "jwtToken";
  public static final String AUTH_SERVICE_JWT = "auth-service-jwt";


  public static UserDetails getUser(String token, String jwtSecretKey) {
    // @formatter:off
    Claims claims = Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token).getBody();
    List<Role> authorities =  Arrays.stream(claims.get("roles").toString().split(","))
                                                .map(Role::new)
                                                .collect(Collectors.toList());
    // @formatter:on
    me.anilkc.domain.User user = new me.anilkc.domain.User();
    user.setUsername(claims.getSubject());
    user.setRoles(authorities);
    return new UserAdapter(user);
  }

  public static String getToken(String jwtSecretKey, TokenTO tokenTO) {
    // @formatter:off
    return  Jwts.builder().setSubject(tokenTO.getSubject())
                   .setExpiration(tokenTO.getExpirationDate())
                   .setIssuer(AUTH_SERVICE_JWT)
                   .setIssuedAt(new Date())
                   .setNotBefore(new Date())
                   .claim("roles", tokenTO.getRoles())
                   .claim("clientIP", tokenTO.getClientIpAddress())
                   .claim("browserFingerprintDigest", tokenTO.getBrowserFingerprintDigest())
                   .setHeaderParams(tokenTO.getHeaderClaims())
                   .signWith(SignatureAlgorithm.HS256, jwtSecretKey).compact();
    // @formatter:on
  }

  public static boolean validateToken(String jwtSecretKey, TokenTO tokenTO) {
    try {
      // @formatter:off
      Jwts.parser()
          .setSigningKey(jwtSecretKey)
          .requireIssuer(tokenTO.getIssuer())
          .requireSubject(tokenTO.getSubject())
          .require("clientIP", tokenTO.getClientIpAddress())
          .require("browserFingerprintDigest", tokenTO.getBrowserFingerprintDigest())
          .parseClaimsJws(tokenTO.getJwtToken());
      // @formatter:on
    } catch (JwtException | IllegalArgumentException e) {
      LOG.warn("Invalid JWT Token ->", e);
      return false;
    }
    return true;
  }
}
