package me.anilkc.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import me.anilkc.domain.Role;
import me.anilkc.domain.User;
import me.anilkc.service.CustomUserDetailsService;

@RestController
@RequestMapping("/secure")
public class UserController {

  @Autowired
  private CustomUserDetailsService customUserDetailsService;

  @PostConstruct
  public void init() {
    User user = new User("User", "user", "password", true);
    List<Role> roles1 = new ArrayList<>();
    roles1.add(new Role("ROLE_USER"));
    user.setRoles(roles1);
    customUserDetailsService.saveUser(user);

    User admin = new User("Admin", "admin", "password", true);
    List<Role> roles2 = new ArrayList<>();
    roles2.add(new Role("ROLE_ADMIN"));
    admin.setRoles(roles2);
    customUserDetailsService.saveUser(admin);

    User participant = new User("Participant", "participant", "password", true);
    List<Role> roles3 = new ArrayList<>();
    roles3.add(new Role("ROLE_PARTICIPANT"));
    participant.setRoles(roles3);
    customUserDetailsService.saveUser(participant);
  }

  @GetMapping("/user")
  public @ResponseBody Map<String, String> getUser() {
    return getSuccessResponse();
  }

  @GetMapping("/admin")
  public @ResponseBody Map<String, String> getAdmin() {
    return getSuccessResponse();
  }

  @PreAuthorize("hasRole('PARTICIPANT')")
  @GetMapping("/participant")
  public @ResponseBody Map<String, String> getParticipant() {
    return getSuccessResponse();
  }


  private Set<String> getRoles(Authentication authentication) {
    return authentication.getAuthorities().stream().map(r -> r.getAuthority()).collect(Collectors.toSet());
  }

  private Authentication getAuthentication() {
    return SecurityContextHolder.getContext().getAuthentication();
  }

  private Map<String, String> getSuccessResponse() {
    Authentication authentication = getAuthentication();
    Set<String> roles = getRoles(authentication);
    Map<String, String> response = new HashMap<>();
    response.put("username", authentication.getName());
    response.put("role", roles.toString());
    response.put("message", "success");
    return response;
  }
}
