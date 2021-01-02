package tri.le.purchasedata.config;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import tri.le.purchasedata.dto.TokenInfo;
import tri.le.purchasedata.entity.User;
import tri.le.purchasedata.error.NSTException;
import tri.le.purchasedata.repository.UserRepository;
import tri.le.purchasedata.service.JwtService;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class JwtAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {

  private JwtService jwtService;

  private UserRepository userRepository;

  public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
                                 JwtService jwtService, UserRepository userRepository) {

    this.setAuthenticationManager(authenticationManager);
    this.jwtService = jwtService;
    this.userRepository = userRepository;
  }

  @Override
  protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
    final String requestTokenHeader = request.getHeader("Authorization");

    if (requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer ")) {
      logger.warn("Request don't have Jwt Token");
      return null;
    }

    String jwtToken = requestTokenHeader.substring(7);

    TokenInfo tokenInfo = null;
    try {
      tokenInfo = jwtService.getTokenInfo(jwtToken);
    } catch (NSTException e) {
      logger.warn("Jwt token invalid", e);
      return null;
    }

    Optional<User> userOptional = userRepository.findById(tokenInfo.getUserId());

    if (!userOptional.isPresent()) {
      logger.warn("User don't exist");
      return null;
    }

    return userOptional.get();
  }

  @Override
  protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
    return null;
  }
}
