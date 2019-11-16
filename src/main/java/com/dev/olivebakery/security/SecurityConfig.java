package com.dev.olivebakery.security;

import com.dev.olivebakery.domain.enums.MemberRole;
import com.dev.olivebakery.service.UserDetailsServiceImpl;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final UserDetailsServiceImpl userDetailsService;
  private final PasswordEncoder passwordEncoder;
  private final HttpAuthenticationEntryPoint httpAuthenticationEntryPoint;
  private final AuthenticationTokenFilter authenticationTokenFilter;
  private final AccessDeniedHandler accessDeniedHandler;
  private final LogoutSuccessHandlerCustom logoutSuccessHandlerCustom;

  private static final String[] AUTH_ARR = {
      "**/swagger-resources/**",
      "/webjars/**", "/v2/api-docs", "/configuration/ui",
      "/swagger-resources", "/configuration/security",
      "/swagger-ui.html", "/webjars/**", "/swagger/**"
  };
  private static final List<String> AUTH_LIST = Arrays.asList(
      "/swagger-resources/**",
      "/swagger-ui.html**",
      "/webjars/**",
      "favicon.ico");


  public SecurityConfig(UserDetailsServiceImpl userDetailsService, PasswordEncoder passwordEncoder,
      LogoutSuccessHandlerCustom logoutSuccessHandlerCustom,
      HttpAuthenticationEntryPoint httpAuthenticationEntryPoint,
      AuthenticationTokenFilter authenticationTokenFilter,
      AccessDeniedHandler accessDeniedHandler) {
    this.userDetailsService = userDetailsService;
    this.passwordEncoder = passwordEncoder;
    this.logoutSuccessHandlerCustom = logoutSuccessHandlerCustom;
    this.httpAuthenticationEntryPoint = httpAuthenticationEntryPoint;
    this.authenticationTokenFilter = authenticationTokenFilter;
    this.accessDeniedHandler = accessDeniedHandler;
  }

  /**
   * profile: 배포
   */
    /*@Override
    protected void configure(HttpSecurity http) throws Exception {
        http
//                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                    .antMatchers(AUTH_ARR).anonymous()
                    .antMatchers("/olive/sign/admin").hasRole(MemberRole.ADMIN.name())
                    .antMatchers(HttpMethod.PUT,"/olive/sign").hasRole(MemberRole.CLIENT.name())
                    .antMatchers(HttpMethod.DELETE,"/olive/sign").hasRole(MemberRole.CLIENT.name())
                    .antMatchers("/olive/ingredients").hasRole(MemberRole.ADMIN.name())
                    .antMatchers(HttpMethod.POST, "/olive/bread/**").hasRole(MemberRole.ADMIN.name())
                    .antMatchers(HttpMethod.DELETE, "/olive/bread/**").hasRole(MemberRole.ADMIN.name())
                    .antMatchers(HttpMethod.PUT, "/olive/bread/**").hasRole(MemberRole.ADMIN.name())
//                    .antMatchers("/**").anonymous()
                    .anyRequest().anonymous()
                .and()
                    .exceptionHandling().defaultAuthenticationEntryPointFor(swaggerAuthenticationEntryPoint(), new CustomRequestMatcher(AUTH_LIST))
                    .authenticationEntryPoint(httpAuthenticationEntryPoint).accessDeniedHandler(accessDeniedHandler)
                .and()
                    .addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                    .logout().logoutUrl("/olive/logout").logoutSuccessHandler(logoutSuccessHandlerCustom)
        ;

    }*/

  /**
   * @profile: 테스트
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
//                .httpBasic().disable()
        .csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .antMatchers(AUTH_ARR).anonymous()
        .antMatchers("/olive/sign/admin").hasRole(MemberRole.ADMIN.name())
        .antMatchers(HttpMethod.PUT, "/olive/sign").hasRole(MemberRole.CLIENT.name())
        .antMatchers(HttpMethod.DELETE, "/olive/sign").hasRole(MemberRole.CLIENT.name())
        .antMatchers(HttpMethod.POST, "/olive/bread/**").hasRole(MemberRole.ADMIN.name())
        .antMatchers(HttpMethod.DELETE, "/olive/bread/**").hasRole(MemberRole.ADMIN.name())
        .antMatchers(HttpMethod.PUT, "/olive/bread/**").hasRole(MemberRole.ADMIN.name())
        .antMatchers(HttpMethod.GET, "/olive/sales/**").hasRole(MemberRole.ADMIN.name())
        .antMatchers(HttpMethod.POST, "/olive/sales/**").hasRole(MemberRole.ADMIN.name())
        .antMatchers(HttpMethod.PUT, "/olive/sales/**").hasRole(MemberRole.ADMIN.name())
        .antMatchers(HttpMethod.DELETE, "/olive/sales/**").hasRole(MemberRole.ADMIN.name())
        .anyRequest().permitAll()
        .and()
        .exceptionHandling().defaultAuthenticationEntryPointFor(swaggerAuthenticationEntryPoint(),
        new CustomRequestMatcher(AUTH_LIST))
        .authenticationEntryPoint(httpAuthenticationEntryPoint)
        .accessDeniedHandler(accessDeniedHandler)
        .and()
        .addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
        .logout().logoutUrl("/olive/logout").logoutSuccessHandler(logoutSuccessHandlerCustom)
    ;

  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth
        .userDetailsService(userDetailsService)
        .passwordEncoder(passwordEncoder);
  }


  @Override
  public void configure(WebSecurity web) {
    web.ignoring()
        .antMatchers(HttpMethod.POST, "/olive/sign/client")
        .antMatchers(HttpMethod.POST, "/olive/sign")
        .antMatchers(AUTH_ARR)
    ;
//        web.ignoring().antMatchers("/**");
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  public BasicAuthenticationEntryPoint swaggerAuthenticationEntryPoint() {
    BasicAuthenticationEntryPoint entryPoint = new BasicAuthenticationEntryPoint();
    entryPoint.setRealmName("Swagger Realm");
    return entryPoint;
  }

  private class CustomRequestMatcher implements RequestMatcher {

    private List<AntPathRequestMatcher> matchers;

    private CustomRequestMatcher(List<String> matchers) {
      this.matchers = matchers.stream().map(AntPathRequestMatcher::new)
          .collect(Collectors.toList());
    }

    @Override
    public boolean matches(HttpServletRequest request) {
      return matchers.stream().anyMatch(a -> a.matches(request));
    }

  }

}
