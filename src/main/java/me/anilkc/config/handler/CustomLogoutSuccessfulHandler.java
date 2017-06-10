package me.anilkc.config.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomLogoutSuccessfulHandler implements LogoutSuccessHandler {

  @Override
  public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException, ServletException {

    // TODO implement jwt invalidating strategy
    response.setStatus(HttpStatus.OK.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("UTF-8");
    JSONObject jsonResponse = new JSONObject();
    jsonResponse.put("message", "Logout Successful");
    response.getWriter().write(jsonResponse.toString());
  }

}
