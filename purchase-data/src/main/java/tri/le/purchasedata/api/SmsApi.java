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
public class SmsApi {

  private static Logger logger = LoggerFactory.getLogger(SmsApi.class);

  private static String SEND_SMS = "/send-sms";

  @Value("${sms-path:http://localhost:8083}")
  private String smsPath;

  @Value("${sms-timeout-seconds:30}")
  private long smsTimeoutInSeconds;

  @Autowired
  @Qualifier("smsRestTemplate")
  private RestTemplate smsRestTemplate;

  @Bean
  public RestTemplate smsRestTemplate(RestTemplateBuilder restTemplateBuilder) {
    logger.info("Create SmsRestTemplate with timeout {}s", smsTimeoutInSeconds);

    return restTemplateBuilder
      .setConnectTimeout(Duration.ofSeconds(smsTimeoutInSeconds))
      .setReadTimeout(Duration.ofSeconds(smsTimeoutInSeconds))
      .build();
  }

  public void sendSms(String phone, String content) {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

    HttpEntity<?> entity = new HttpEntity<>(headers);

    UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(smsPath + SEND_SMS)
      .queryParam("phone", phone)
      .queryParam("content", content)
      .build();

    smsRestTemplate.exchange(
      uriComponents.toUri(),
      HttpMethod.POST,
      entity,
      String.class);
  }

}
