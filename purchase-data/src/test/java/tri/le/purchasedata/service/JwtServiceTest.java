package tri.le.purchasedata.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import tri.le.purchasedata.dto.TokenInfo;
import tri.le.purchasedata.error.NSTException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest
public class JwtServiceTest {

  @Autowired
  JwtService jwtService;

  // JWT token in this test generated by https://jwt.io/
  // Secret = SHA256("this-is-secret-string-for-test")

  @Test
  @DisplayName("Test getTokenInfo: Expired")
  void testGetTokenInfo_tokenExpired() throws NSTException {
    //{
    //  "sub": "trile",
    //  "userId": 1,
    //  "exp": 1609495590 => Friday, 1 January 2021 00:00:00 GMT
    //}
    String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0cmlsZSIsInVzZXJJZCI6MSwiZXhwIjoxNjA5NDk1NTkwfQ.eusZLhqBDmey3HGeuPalYrw2FNg0U9gTWqZ4KMi0e1HBW_ptXTequQ0vhpeukwchEOkcmcs2OWF8n5tBeGEV_w";

    Exception exception = assertThrows(NSTException.class, () -> jwtService.getTokenInfo(token), "ExceptionType");

    assertEquals("Token expired", exception.getMessage(), "ExceptionMessage");
  }

  @Test
  @DisplayName("Test getTokenInfo: Signature invalid")
  void testGetTokenInfo_tokenSignatureInvalid() throws NSTException {
    //{
    //  "sub": "trile",
    //  "userId": 1,
    //  "exp": 7952256000 => Monday, 31 December 2221 00:00:00 GMT
    //}
    String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0cmlsZSIsInVzZXJJZCI6MSwiZXhwIjo3OTUyMjU2MDAwfQ.123";

    Exception exception = assertThrows(NSTException.class, () -> jwtService.getTokenInfo(token), "ExceptionType");

    assertEquals("Signature invalid", exception.getMessage(), "ExceptionMessage");
  }

  @Test
  @DisplayName("Test getTokenInfo: Token valid")
  void testGetTokenInfo_tokenValid() throws NSTException {
    //{
    //  "sub": "trile",
    //  "userId": 1,
    //  "exp": 7952256000 => Monday, 31 December 2221 00:00:00 GMT
    //}
    String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0cmlsZSIsInVzZXJJZCI6MSwiZXhwIjo3OTUyMjU2MDAwfQ.NNs7qteaKhfVLsqEcPzxN8Sc-4Poquojrj6eNDPculL_zwIeQchXr8jJ--15EPG51WA4RoRNfnXeVgB0hgbP2w";

    TokenInfo tokenInfo = jwtService.getTokenInfo(token);

    assertEquals("trile", tokenInfo.getUserName(), "Token.sub");
    assertEquals(1L, tokenInfo.getUserId(), "Token.userId");
  }

  @Test
  @DisplayName("Test createToken: Correct token")
  void testCreateToken_tokenValid() throws NSTException {
    //{
    //  "sub": "trile",
    //  "userId": 1,
    //  "exp": 7952256 => Monday, 31 December 2221 00:00:00 GMT
    //}
    String expectToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0cmlsZSIsInVzZXJJZCI6MSwiZXhwIjo3OTUyMjU2fQ.njM_FQi8vWN6UE99ODJ3KpdJjO_XUKNjpYUmICJeyxOWhEAlSeVX2dQcUFe4YADpQzQdMlunO6A3Eh67Ry1AQA";

    String sub = "trile";
    Long userId = 1L;
    long currentTimeMillis = 7952256000L - 100 * 1000;

    String actualToken = jwtService.createToken(sub, userId, currentTimeMillis);

    assertEquals(expectToken, actualToken, "Token");
  }
}
