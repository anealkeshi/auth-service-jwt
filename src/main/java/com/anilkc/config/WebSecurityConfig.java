package com.anilkc.config;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.anilkc.authentication.form.CustomFormAuthenticationFilter;
import com.anilkc.authentication.form.FormAuthenticationProvider;
import com.anilkc.authentication.jwt.JwtAuthenticationFilter;
import com.anilkc.authentication.jwt.JwtAuthenticationProvider;
import com.anilkc.config.handler.CustomAccessDeniedHandler;
import com.anilkc.config.handler.CustomAuthenticationEntryPoint;
import com.anilkc.config.handler.CustomLogoutSuccessfulHandler;
import com.anilkc.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private CustomLogoutSuccessfulHandler logoutSuccessfulHandler;

  @Autowired
  private CustomAccessDeniedHandler customAccessDeniedHandler;

  @Autowired
  private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

  @Resource(name = "customUserDetailsService")
  private CustomUserDetailsService userDetailsService;

  @Autowired
  private DataSource jdbcDatasource;

  @Value("${jwt.secret.key}")
  private String jwtSecretKey;

  @Autowired
  private FormAuthenticationProvider formAuthenticationProvider;

  @Autowired
  private JwtAuthenticationProvider jwtAuthenticationProvider;

  @Autowired
  private AuthenticationManager authenticationManager;

  protected CustomFormAuthenticationFilter getCustomAuthenticationFilter(String pattern) {
    CustomFormAuthenticationFilter customAuthenticationFilter =
        new CustomFormAuthenticationFilter(new AntPathRequestMatcher(pattern), userDetailsService, jwtSecretKey);
    customAuthenticationFilter.setAuthenticationManager(this.authenticationManager);
    return customAuthenticationFilter;
  }

  protected JwtAuthenticationFilter getJwtAuthenticationFilter(String pattern) {
    JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(new AntPathRequestMatcher(pattern), this.jwtSecretKey);
    jwtAuthenticationFilter.setAuthenticationManager(this.authenticationManager);
    return jwtAuthenticationFilter;

  }

  @Override
  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
  // @formatter:off
      auth.authenticationProvider(formAuthenticationProvider);
      auth.authenticationProvider(jwtAuthenticationProvider)
        .userDetailsService(userDetailsService)
        .and()
        .jdbcAuthentication().dataSource(jdbcDatasource);
  // @formatter:on
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
  // @formatter:off
        http
          .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
          .and()
          .csrf().disable()
          .formLogin()
              .loginProcessingUrl("/auth/login")
          .and()
              .logout()
              .deleteCookies("JSESSIONID")
              .logoutUrl("/auth/logout")
              .logoutSuccessHandler(logoutSuccessfulHandler)
          .and()
            .authorizeRequests()
            .antMatchers("/auth/login").permitAll()
          .and()
            .authorizeRequests()
            .antMatchers("/secure/admin").access("hasRole('ADMIN')")//.access("hasAuthority('ROLE_ADMIN')")
            .anyRequest().authenticated()
           .and()
             .exceptionHandling().accessDeniedHandler(customAccessDeniedHandler)
             .authenticationEntryPoint(customAuthenticationEntryPoint)
           .and()
             .addFilterBefore(getCustomAuthenticationFilter("/auth/login"), UsernamePasswordAuthenticationFilter.class)
             .addFilterBefore(getJwtAuthenticationFilter("/secure/**"), UsernamePasswordAuthenticationFilter.class)
            .anonymous()
              .disable();
  // @formatter:on
  }

}
