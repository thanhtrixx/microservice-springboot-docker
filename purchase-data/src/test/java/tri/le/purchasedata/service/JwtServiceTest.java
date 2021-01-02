package tri.le.purchasedata.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import tri.le.purchasedata.dto.TokenInfo;
import tri.le.purchasedata.error.NSTException;

import static org.junit.jupiter.api.Assertions.assertEquals;

//@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@SpringBootTest
public class JwtServiceTest {

  @Autowired
  JwtService jwtService;

  //  @Test
  void test() throws NSTException {
    TokenInfo tokenInfo = jwtService.getTokenInfo("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0cmlsZSIsInVzZXJJZCI6MSwiZXhwIjoxNjA5NDk1NTkwfQ.eusZLhqBDmey3HGeuPalYrw2FNg0U9gTWqZ4KMi0e1HBW_ptXTequQ0vhpeukwchEOkcmcs2OWF8n5tBeGEV_w");

    assertEquals("trile", tokenInfo.getUserName(), "username");
  }
}
