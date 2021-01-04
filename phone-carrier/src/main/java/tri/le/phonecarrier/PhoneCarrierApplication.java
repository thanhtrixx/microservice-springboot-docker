package tri.le.phonecarrier;

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
public class PhoneCarrierApplication {

  private static Logger logger = LoggerFactory.getLogger(PhoneCarrierApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(PhoneCarrierApplication.class, args);
  }

  @PostMapping(path = "/send-sms")
  @ResponseBody
  public String sendSms(@RequestParam String phone, @RequestParam String content) {
    logger.info("Send SMS to {} with content: {}", phone, content);

    return "Done";
  }

  @PostMapping(path = "/apply-data-voucher")
  @ResponseBody
  public String applyDataVoucher(@RequestParam String phone, @RequestParam String voucher) {
    logger.info("Apply DataVoucher {} to phone number {}", voucher, phone);

    return "Done";
  }

}
