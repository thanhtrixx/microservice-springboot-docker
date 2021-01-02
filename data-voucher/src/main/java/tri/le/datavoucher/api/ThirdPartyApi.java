package tri.le.datavoucher.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Component
public class ThirdPartyApi {

  private static Logger logger = LoggerFactory.getLogger(ThirdPartyApi.class);

  private static String GET_DATA_VOUCHER = "/generate-data-voucher";

  @Value("${third-party-path:http://localhost:8081}")
  private String thirdPartyPath;

  @Value("${third-party-timeout-seconds:120}")
  private long thirdPartyTimeoutInSeconds;

  @Autowired
  private RestTemplate restTemplate;

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
    logger.info("Create RestTemplate with timeout {}s", thirdPartyTimeoutInSeconds);

    return restTemplateBuilder
      .setConnectTimeout(Duration.ofSeconds(thirdPartyTimeoutInSeconds))
      .setReadTimeout(Duration.ofSeconds(thirdPartyTimeoutInSeconds))
      .build();
  }

  public String getDataVoucher() {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Accept", MediaType.TEXT_PLAIN_VALUE);

    HttpEntity<?> entity = new HttpEntity<>(headers);


    HttpEntity<String> response = restTemplate.exchange(
      thirdPartyPath + GET_DATA_VOUCHER,
      HttpMethod.POST,
      entity,
      String.class);

    return response.getBody();
  }
}
