package tri.le.purchasedata.service;

import com.google.common.base.Strings;
import com.google.common.hash.Hashing;
import com.google.common.primitives.Longs;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import tri.le.purchasedata.dto.TokenInfo;
import tri.le.purchasedata.error.NSTException;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;

import static tri.le.purchasedata.error.ErrorCode.TOKEN_INVALID;

@Service
public class JwtService {

  private static Logger logger = LoggerFactory.getLogger(JwtService.class);

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.expired-in-seconds:86400}")
  private int expiredInSeconds;

  @Bean
  private long expiredInMillis() {
    return Duration.ofSeconds(expiredInSeconds).toMillis();
  }

  @Bean
  private SecretKey secretKey() {
    String hashedSecret = Hashing
      .sha256()
      .hashString(secret, StandardCharsets.UTF_8)
      .toString();

    return Keys.hmacShaKeyFor(hashedSecret.getBytes(StandardCharsets.UTF_8));
  }

  public String createToken(String user, Long userId) {
    Date expiredDate = new Date(System.currentTimeMillis() + expiredInMillis());

    return Jwts.builder()
      .setSubject(user)
      .claim("userId", userId)
      .setExpiration(expiredDate)
      .signWith(secretKey())
      .compact();
  }

  public TokenInfo getTokenInfo(String token) throws NSTException {
    if (Strings.isNullOrEmpty(token))
      throw new NSTException(TOKEN_INVALID, "Token " + token + " invalid");

    Jws<Claims> jws = parseToken(token);

    TokenInfo tokenInfo = new TokenInfo();

    Claims body = jws.getBody();

    String subject = body.getSubject();
    if (Strings.isNullOrEmpty(subject)) {
      throw new NSTException(TOKEN_INVALID, "Subject in token " + token + " is null");
    }
    tokenInfo.setUserName(subject);

    Object userIdObj = body.get("userId");
    if (userIdObj == null) {
      throw new NSTException(TOKEN_INVALID, "UserId in token " + token + " is null");
    }

    Long userId = Longs.tryParse(userIdObj.toString());
    if (userId == null) {
      throw new NSTException(TOKEN_INVALID, "UserId in token " + token + " invalid");
    }

    tokenInfo.setUserId(userId);

    return tokenInfo;
  }

  private Jws<Claims> parseToken(String token) throws NSTException {
    Jws<Claims> jws;

    try {
      jws = Jwts.parserBuilder()
        .setSigningKey(secretKey())
        .build()
        .parseClaimsJws(token);
    } catch (ExpiredJwtException e) {
      logger.warn("Token expired");
      throw new NSTException(TOKEN_INVALID, "Token expired");
    } catch (JwtException e) {
      logger.warn("Parse token error", e);
      throw new NSTException(TOKEN_INVALID, "Token invalid");
    }
    return jws;
  }
}
