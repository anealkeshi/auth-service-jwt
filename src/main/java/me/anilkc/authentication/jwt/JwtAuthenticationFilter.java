package me.anilkc.authentication.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

  private String jwtSecretKey;

  public JwtAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher, String jwtSecretKey) {
    super(requiresAuthenticationRequestMatcher);
    this.jwtSecretKey = jwtSecretKey;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
      throws AuthenticationException, IOException, ServletException {

    String authToken = request.getHeader(JwtUtil.AUTHORIZATION);
    String jwtToken = null;
    if (authToken != null) {
      jwtToken = authToken.replace("Bearer ", "");
    }
    if (JwtUtil.validateToken(jwtSecretKey, buildTokenTO(request, jwtToken)) && request.getHeader(JwtUtil.HEADER_APP_SUBJECT) != null) {
      return getAuthenticationManager().authenticate(new JwtAuthToken(jwtToken));
    }
    throw new AuthenticationCredentialsNotFoundException("JWT Token Not valid");
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
      Authentication authResult) throws IOException, ServletException {
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(authResult);
    SecurityContextHolder.setContext(context);
    chain.doFilter(request, response);
  }

  private TokenTO buildTokenTO(HttpServletRequest request, String jwtToken) {
    TokenTO tokenTO = new TokenTO();
    tokenTO.setJwtToken(jwtToken);
    tokenTO.setClientIpAddress(request.getRemoteAddr());
    tokenTO.setBrowserFingerprintDigest(request.getHeader(JwtUtil.USER_AGENT));
    tokenTO.setIssuer(JwtUtil.AUTH_SERVICE_JWT);
    tokenTO.setSubject(request.getHeader(JwtUtil.HEADER_APP_SUBJECT));
    return tokenTO;
  }
}
