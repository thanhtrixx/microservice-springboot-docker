package tri.le.sms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class SmsApplication {

  private static Logger logger = LoggerFactory.getLogger(SmsApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(SmsApplication.class, args);
  }

  @PostMapping(path = "/send-sms")
  @ResponseBody
  public String sendSms(@RequestParam String phone, @RequestParam String content) {
    logger.info("Send SMS to {} with content: {}", phone, content);

    return "Done";
  }

}
