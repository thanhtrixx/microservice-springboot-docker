package tri.le.purchasedata.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;

@Component
public class PhoneCarrierApi {

  private static Logger logger = LoggerFactory.getLogger(PhoneCarrierApi.class);

  private static String SEND_SMS = "/send-sms";
  private static String APPLY_DATA_VOUCHER = "/apply-data-voucher";

  @Value("${phone-carrier-path:http://localhost:8083}")
  private String phoneCarrierPath;

  @Value("${phone-carrier-timeout-seconds:30}")
  private long phoneCarrierTimeoutInSeconds;

  @Autowired
  @Qualifier("phoneCarrierRestTemplate")
  private RestTemplate phoneCarrierRestTemplate;

  @Bean
  public RestTemplate phoneCarrierRestTemplate(RestTemplateBuilder restTemplateBuilder) {
    logger.info("Create PhoneCarrierRestTemplate with timeout {}s", phoneCarrierTimeoutInSeconds);

    return restTemplateBuilder
      .setConnectTimeout(Duration.ofSeconds(phoneCarrierTimeoutInSeconds))
      .setReadTimeout(Duration.ofSeconds(phoneCarrierTimeoutInSeconds))
      .build();
  }

  public void sendSms(String phone, String content) {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

    HttpEntity<?> entity = new HttpEntity<>(headers);

    UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(phoneCarrierPath + SEND_SMS)
      .queryParam("phone", phone)
      .queryParam("content", content)
      .build();

    phoneCarrierRestTemplate.exchange(
      uriComponents.toUri(),
      HttpMethod.POST,
      entity,
      String.class);
  }

  public void applyDataVoucher(String phone, String voucher) {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

    HttpEntity<?> entity = new HttpEntity<>(headers);

    UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(phoneCarrierPath + APPLY_DATA_VOUCHER)
      .queryParam("phone", phone)
      .queryParam("voucher", voucher)
      .build();

    phoneCarrierRestTemplate.exchange(
      uriComponents.toUri(),
      HttpMethod.POST,
      entity,
      String.class);
  }

}
