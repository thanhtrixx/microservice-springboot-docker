package tri.le.purchasedata.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import tri.le.purchasedata.dto.GenericResponse;

import java.time.Duration;

@Component
public class DataVoucherApi {

  private static Logger logger = LoggerFactory.getLogger(DataVoucherApi.class);

  private static String GET_DATA_VOUCHER = "/get-data-voucher";
  private static String GET_DATA_VOUCHER_BY_REQUEST_ID = "/get-data-voucher-by-request-id";

  private static ParameterizedTypeReference<GenericResponse<String>>
    GENERIC_RESPONSE_STRING = new ParameterizedTypeReference<GenericResponse<String>>() {
  };

  @Value("${data-voucher-path:http://localhost:8083}")
  private String dataVoucherPath;

  @Value("${data-voucher-timeout-seconds:30}")
  private long dataVoucherTimeoutInSeconds;

  @Autowired
  private RestTemplate restTemplate;

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
    logger.info("Create RestTemplate with timeout {}s", dataVoucherTimeoutInSeconds);

    return restTemplateBuilder
      .setConnectTimeout(Duration.ofSeconds(dataVoucherTimeoutInSeconds))
      .setReadTimeout(Duration.ofSeconds(dataVoucherTimeoutInSeconds))
      .build();
  }

  public GenericResponse<String> getDataVoucher(String requestId) {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

    HttpEntity<?> entity = new HttpEntity<>(headers);

    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(dataVoucherPath + GET_DATA_VOUCHER)
      .queryParam("request-id", requestId);


    HttpEntity<GenericResponse<String>> response = restTemplate.exchange(
      builder.toUriString(),
      HttpMethod.POST,
      entity,
      GENERIC_RESPONSE_STRING);

    return response.getBody();
  }

  public GenericResponse<String> getDataVoucherByRequestId(String requestId) {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

    HttpEntity<?> entity = new HttpEntity<>(headers);

    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(dataVoucherPath + GET_DATA_VOUCHER)
      .queryParam("request-id", requestId);


    HttpEntity<GenericResponse<String>> response = restTemplate.exchange(
      builder.toUriString(),
      HttpMethod.POST,
      entity,
      GENERIC_RESPONSE_STRING);

    return response.getBody();
  }
}
