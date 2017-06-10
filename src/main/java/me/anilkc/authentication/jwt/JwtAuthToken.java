package me.anilkc.authentication.jwt;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class JwtAuthToken extends AbstractAuthenticationToken {

  private static final long serialVersionUID = 7916550423807043171L;

  private String token;

  private String username;


  public JwtAuthToken(String token) {
    super(null);
    this.token = token;
    this.setAuthenticated(false);
  }

  public JwtAuthToken(String username, String token, Collection<? extends GrantedAuthority> authorities) {
    super(authorities);
    this.eraseCredentials();
    this.token = token;
    this.username = username;
    this.setAuthenticated(true);
  }


  @Override
  public Object getCredentials() {
    return token;
  }

  @Override
  public Object getPrincipal() {
    return username;
  }

  @Override
  public void eraseCredentials() {
    super.eraseCredentials();
    this.token = null;
  }


}
