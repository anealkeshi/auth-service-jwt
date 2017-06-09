package com.anilkc.authentication.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {


  @Value("${jwt.secret.key}")
  private String jwtSecretKey;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String jwtToken = (String) authentication.getCredentials();
    UserDetails user = JwtUtil.getUser(jwtToken, jwtSecretKey);
    return new JwtAuthToken(user.getUsername(), jwtToken, user.getAuthorities());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return (JwtAuthToken.class.isAssignableFrom(authentication));
  }
}
