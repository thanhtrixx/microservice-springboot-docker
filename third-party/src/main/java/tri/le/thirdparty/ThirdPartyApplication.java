package tri.le.thirdparty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@SpringBootApplication
public class ThirdPartyApplication {

  private static Logger logger = LoggerFactory.getLogger(ThirdPartyApplication.class);

  private static String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private static int length = 8;
  private static Random random = new Random();

  public static void main(String[] args) {
    SpringApplication.run(ThirdPartyApplication.class, args);
  }

  @PostMapping(path = "/generate-data-voucher")
  public @ResponseBody
  String generateDataVoucher() {
    int sleepTimeInSeconds = 3 + random.nextInt(120 - 3); // from 3 to 120

    try {
      logger.info("Simulate time to generate voucher. Sleep: {}", sleepTimeInSeconds);
      Thread.sleep(sleepTimeInSeconds * 1000);
    } catch (InterruptedException e) {
      logger.warn("Error when sleep", e);
    }

    return generateVoucher();
  }

  private String generateVoucher() {

    StringBuilder sb = new StringBuilder();


    for (int i = 0; i < length; i++) {

      int index = random.nextInt(alphabet.length());
      char randomChar = alphabet.charAt(index);
      sb.append(randomChar);
    }

    return sb.toString();
  }
}
