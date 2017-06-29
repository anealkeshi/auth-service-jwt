package me.anilkc.authentication.form;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import me.anilkc.service.CustomUserDetailsService;

@Component
public class FormAuthenticationProvider implements AuthenticationProvider {

  @Autowired
  private CustomUserDetailsService userDetailsService;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String username = (String) authentication.getPrincipal();
    String password = (String) authentication.getCredentials();

    UserDetails user = userDetailsService.loadUserByUsername(username);
    if (!password.equals(user.getPassword())) {
      throw new AuthenticationCredentialsNotFoundException("Credentials don't match");
    }
    return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
  }
}
