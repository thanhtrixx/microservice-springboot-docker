package tri.le.purchasedata.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import tri.le.purchasedata.entity.User;
import tri.le.purchasedata.repository.UserRepository;
import tri.le.purchasedata.service.JwtService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

  @Autowired
  private JwtService jwtService;

  @Autowired
  private UserRepository userRepository;

  protected void configure(HttpSecurity httpSecurity) throws Exception {

    httpSecurity
      .csrf().disable()
      .addFilter(jwtAuthenticationFilter(authenticationManager()))
      .authorizeRequests().anyRequest().authenticated().and()
      .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
  }

  public void configure(WebSecurity web) throws Exception {
    web
      .ignoring().antMatchers("/user/login**", "/error");
  }

  @Bean
  public AuthenticationManager authenticationManager() {

    return new AuthenticationManager() {
      @Override
      public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Object authObj = authentication.getPrincipal();

        if (authObj == null || !(authObj instanceof User)) {
          throw new BadCredentialsException("Principal invalid");
        }

        authentication.setAuthenticated(true);

        return authentication;
      }
    };
  }

  @Autowired
  private JwtAuthenticationFilter jwtAuthenticationFilter(AuthenticationManager authenticationManager) {
    return new JwtAuthenticationFilter(authenticationManager, jwtService, userRepository);
  }

}
