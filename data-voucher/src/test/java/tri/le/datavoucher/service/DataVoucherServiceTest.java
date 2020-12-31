package tri.le.datavoucher.service;

import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestClientException;
import tri.le.datavoucher.api.ThirdPartyApi;
import tri.le.datavoucher.dto.GenericResponse;
import tri.le.datavoucher.error.ErrorCode;
import tri.le.datavoucher.repository.DataVoucherRepository;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class DataVoucherServiceTest {

  @Mock
  private ThirdPartyApi thirdPartyApi;

  @Mock
  private DataVoucherRepository dataVoucherRepository;

  @InjectMocks
  private DataVoucherService dataVoucherService;

  @Test
  @DisplayName("Test getDataVoucher: requestId is null")
  void testGetDataVoucherRequestIdNull() {
    GenericResponse<String> response = dataVoucherService.getDataVoucher(null);

    Assert.assertEquals("ErrorCode", ErrorCode.INVALID_PARAM.getCode(), response.getReturnCode());
    Assert.assertEquals("ReturnMessage", "RequestId is empty", response.getReturnMessage());
    Assert.assertEquals("Data", null, response.getData());
  }

  @Test
  @DisplayName("Test getDataVoucher: requestId is empty")
  void testGetDataVoucherRequestIdEmpty() {
    GenericResponse<String> response = dataVoucherService.getDataVoucher("");

    Assert.assertEquals("ErrorCode", ErrorCode.INVALID_PARAM.getCode(), response.getReturnCode());
    Assert.assertEquals("ReturnMessage", "RequestId is empty", response.getReturnMessage());
    Assert.assertEquals("Data", null, response.getData());
  }

  @Test
  @DisplayName("Test getDataVoucher: Error when call ThirdParty")
  void testGetDataVoucherCallThirdPartyApiError() {
    when(thirdPartyApi.getDataVoucher()).thenThrow(RestClientException.class);

    String requestId = "request-id";

    GenericResponse<String> response = dataVoucherService.getDataVoucher(requestId);

    Assert.assertEquals("ErrorCode", ErrorCode.CALL_API_ERROR.getCode(), response.getReturnCode());
    Assert.assertEquals("ReturnMessage", "Error when call ThirdParty with requestId: " + requestId, response.getReturnMessage());
    Assert.assertEquals("Data", null, response.getData());
  }
}
