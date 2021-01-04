package tri.le.datavoucher.service;

import org.junit.jupiter.api.AfterEach;
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
import tri.le.datavoucher.entity.DataVoucherEntity;
import tri.le.datavoucher.error.ErrorCode;
import tri.le.datavoucher.repository.DataVoucherRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class DataVoucherServiceTest {

  @Mock
  private ThirdPartyApi thirdPartyApi;

  @Mock
  private DataVoucherRepository dataVoucherRepository;

  @InjectMocks
  private DataVoucherService dataVoucherService;

  @AfterEach
  public void resetMock() {
    reset(thirdPartyApi);
    reset(dataVoucherRepository);
  }

  @Test
  @DisplayName("Test getDataVoucher: requestId is null")
  void testGetDataVoucher_RequestIdNull() {
    String requestId = null;

    GenericResponse<String> response = dataVoucherService.getDataVoucher(requestId);

    assertEquals(ErrorCode.INVALID_PARAM.getCode(), response.getReturnCode(), "ErrorCode");
    assertEquals("RequestId is empty", response.getReturnMessage(), "ReturnMessage");
    assertEquals(null, response.getData(), "Data");
  }

  @Test
  @DisplayName("Test getDataVoucher: requestId is empty")
  void testGetDataVoucher_RequestIdEmpty() {
    String requestId = "";

    GenericResponse<String> response = dataVoucherService.getDataVoucher(requestId);

    assertEquals(ErrorCode.INVALID_PARAM.getCode(), response.getReturnCode(), "ErrorCode");
    assertEquals("RequestId is empty", response.getReturnMessage(), "ReturnMessage");
    assertEquals(null, response.getData(), "Data");
  }

  @Test
  @DisplayName("Test getDataVoucher: Error when call thirdPartyApi")
  void testGetDataVoucher_CallThirdPartyApiError() {
    String requestId = "request-id";

    when(thirdPartyApi.getDataVoucher()).thenThrow(RestClientException.class);

    GenericResponse<String> response = dataVoucherService.getDataVoucher(requestId);

    assertEquals(ErrorCode.CALL_API_ERROR.getCode(), response.getReturnCode(), "ErrorCode");
    assertEquals("Error when call ThirdParty with requestId: " + requestId, response.getReturnMessage(), "ReturnMessage");
    assertEquals(null, response.getData(), "Data");
  }

  @Test
  @DisplayName("Test getDataVoucher: Run success")
  void testGetDataVoucher_Success() {
    String voucher = "ABCDEF";
    String requestId = "request-id";
    when(thirdPartyApi.getDataVoucher()).thenReturn(voucher);

    GenericResponse<String> response = dataVoucherService.getDataVoucher(requestId);

    assertEquals(ErrorCode.NO_ERROR.getCode(), response.getReturnCode(), "ErrorCode");
    assertEquals("Success", response.getReturnMessage(), "ReturnMessage");
    assertEquals(voucher, response.getData(), "Data");

    DataVoucherEntity dataVoucher = new DataVoucherEntity();
    dataVoucher.setId(requestId);
    dataVoucher.setVoucher(voucher);

    // check call dataVoucherRepository.save(dataVoucher) only 1 times
    verify(dataVoucherRepository, times(1)).save(eq(dataVoucher));
  }

  @Test
  @DisplayName("Test getDataVoucherByRequestId: requestId is null")
  void testGetDataVoucherByRequestId_RequestIdNull() {
    String requestId = null;

    GenericResponse<String> response = dataVoucherService.getDataVoucherByRequestId(requestId);

    assertEquals(ErrorCode.INVALID_PARAM.getCode(), response.getReturnCode(), "ErrorCode");
    assertEquals("RequestId is empty", response.getReturnMessage(), "ReturnMessage");
    assertEquals(null, response.getData(), "Data");
  }

  @Test
  @DisplayName("Test getDataVoucherByRequestId: requestId is empty")
  void testGetDataVoucherByRequestId_RequestIdEmpty() {
    String requestId = "";

    GenericResponse<String> response = dataVoucherService.getDataVoucherByRequestId(requestId);

    assertEquals(ErrorCode.INVALID_PARAM.getCode(), response.getReturnCode(), "ErrorCode");
    assertEquals("RequestId is empty", response.getReturnMessage(), "ReturnMessage");
    assertEquals(null, response.getData(), "Data");
  }

  @Test
  @DisplayName("Test getDataVoucherByRequestId: Error when call dataVoucherRepository")
  void testGetDataVoucherByRequestId_CallDataVoucherServiceError() {
    String requestId = "request-id";
    String errorMessage = "Error";
    when(dataVoucherRepository.findById(eq(requestId)))
      .thenAnswer(invocation -> {
        throw new Exception(errorMessage);
      });

    GenericResponse<String> response = dataVoucherService.getDataVoucherByRequestId(requestId);

    assertEquals(ErrorCode.CAN_NOT_GET_VOUCHER_BY_REQUEST_ID.getCode(), response.getReturnCode(), "ErrorCode");
    assertEquals(errorMessage, response.getReturnMessage(), "ReturnMessage");
    assertEquals(null, response.getData(), "Data");
  }

  @Test
  @DisplayName("Test getDataVoucherByRequestId: RequestId not in cache")
  void testGetDataVoucherByRequestId_RequestIdNotInCache() {
    String requestId = "request-id";
    when(dataVoucherRepository.findById(eq(requestId))).thenReturn(Optional.empty());

    GenericResponse<String> response = dataVoucherService.getDataVoucherByRequestId(requestId);

    assertEquals(ErrorCode.CAN_NOT_GET_VOUCHER_BY_REQUEST_ID.getCode(), response.getReturnCode(), "ErrorCode");
    assertEquals("Don't have data voucher from cache with requestId: " + requestId, response.getReturnMessage(), "ReturnMessage");
    assertEquals(null, response.getData(), "Data");
  }


  @Test
  @DisplayName("Test getDataVoucherByRequestId: Run success")
  void testGetDataVoucherByRequestId_Success() {
    String voucher = "ABCDEF";
    String requestId = "request-id";
    DataVoucherEntity dataVoucherEntity = new DataVoucherEntity();
    dataVoucherEntity.setVoucher(voucher);
    when(dataVoucherRepository.findById(eq(requestId))).thenReturn(Optional.of(dataVoucherEntity));

    GenericResponse<String> response = dataVoucherService.getDataVoucherByRequestId(requestId);

    assertEquals(ErrorCode.NO_ERROR.getCode(), response.getReturnCode(), "ErrorCode");
    assertEquals("Success", response.getReturnMessage(), "ReturnMessage");
    assertEquals(voucher, response.getData(), "Data");
  }

}
