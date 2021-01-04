package tri.le.purchasedata.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import tri.le.purchasedata.api.DataVoucherApi;
import tri.le.purchasedata.api.PhoneCarrierApi;
import tri.le.purchasedata.dto.GenericResponse;
import tri.le.purchasedata.entity.DataVoucher;
import tri.le.purchasedata.entity.User;
import tri.le.purchasedata.repository.DataVoucherRepository;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static tri.le.purchasedata.error.ErrorCode.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class PurchaseDataServiceTest {

  @Mock
  private PhoneCarrierApi phoneCarrierApi;


  @Mock
  private DataVoucherApi dataVoucherApi;

  @Mock
  private DataVoucherRepository dataVoucherRepository;

  @InjectMocks
  private PurchaseDataService purchaseDataService;

  @AfterEach
  public void resetMock() {
    reset(phoneCarrierApi);
    reset(dataVoucherRepository);
  }

  @Test
  @DisplayName("Test applyDataVoucher: phone is null")
  public void testApplyDataVoucher_phoneIsNull() {
    String phone = null;
    String voucher = "voucher";

    GenericResponse<Void> response = purchaseDataService.applyDataVoucher(phone, voucher);

    assertEquals(INVALID_PARAM.getCode(), response.getReturnCode(), "ErrorCode");
    assertEquals("Phone is empty", response.getReturnMessage(), "ReturnMessage");

  }

  @Test
  @DisplayName("Test applyDataVoucher: phone is empty")
  public void testApplyDataVoucher_phoneIsEmpty() {
    String phone = "";
    String voucher = "voucher";

    GenericResponse<Void> response = purchaseDataService.applyDataVoucher(phone, voucher);

    assertEquals(INVALID_PARAM.getCode(), response.getReturnCode(), "ErrorCode");
    assertEquals("Phone is empty", response.getReturnMessage(), "ReturnMessage");

  }

  @Test
  @DisplayName("Test applyDataVoucher: voucher is null")
  public void testApplyDataVoucher_voucherIsNull() {
    String phone = "phone";
    String voucher = null;

    GenericResponse<Void> response = purchaseDataService.applyDataVoucher(phone, voucher);

    assertEquals(INVALID_PARAM.getCode(), response.getReturnCode(), "ErrorCode");
    assertEquals("Voucher is empty", response.getReturnMessage(), "ReturnMessage");

  }

  @Test
  @DisplayName("Test applyDataVoucher: voucher is empty")
  public void testApplyDataVoucher_voucherIsEmpty() {
    String phone = "phone";
    String voucher = "";

    GenericResponse<Void> response = purchaseDataService.applyDataVoucher(phone, voucher);

    assertEquals(INVALID_PARAM.getCode(), response.getReturnCode(), "ErrorCode");
    assertEquals("Voucher is empty", response.getReturnMessage(), "ReturnMessage");

  }

  @Test
  @DisplayName("Test applyDataVoucher: call phoneCarrierApi error")
  public void testApplyDataVoucher_callPhoneCarrierApiError() {
    String phone = "phone";
    String voucher = "voucher";

    doAnswer(invocation -> {
      throw new Exception();
    }).when(phoneCarrierApi).applyDataVoucher(eq(phone), eq(voucher));

    GenericResponse<Void> response = purchaseDataService.applyDataVoucher(phone, voucher);

    assertEquals(APPLY_DATA_VOUCHER_ERROR.getCode(), response.getReturnCode(), "ErrorCode");
    assertEquals("Apply data voucher error", response.getReturnMessage(), "ReturnMessage");

    verify(phoneCarrierApi, times(1)).applyDataVoucher(eq(phone), eq(voucher));
  }

  @Test
  @DisplayName("Test applyDataVoucher: success")
  public void testApplyDataVoucher_success() {
    String phone = "phone";
    String voucher = "voucher";

    doNothing().when(phoneCarrierApi).applyDataVoucher(eq(phone), eq(voucher));

    GenericResponse<Void> response = purchaseDataService.applyDataVoucher(phone, voucher);

    assertEquals(NO_ERROR.getCode(), response.getReturnCode(), "ErrorCode");
    assertEquals("Success", response.getReturnMessage(), "ReturnMessage");

  }

  @Test
  @DisplayName("Test applyDataVoucher: user is null")
  public void testGetDataVouchersPurchased_userIsNull() {
    User user = null;

    GenericResponse<Iterable<DataVoucher>> response = purchaseDataService.getDataVouchersPurchased(user);

    assertEquals(TOKEN_INVALID.getCode(), response.getReturnCode(), "ErrorCode");
    assertEquals("User or UserId is null", response.getReturnMessage(), "ReturnMessage");
  }

  @Test
  @DisplayName("Test applyDataVoucher: userId is null")
  public void testGetDataVouchersPurchased_userIdIsNull() {
    Long userId = null;
    User user = new User();
    user.setId(userId);


    GenericResponse<Iterable<DataVoucher>> response = purchaseDataService.getDataVouchersPurchased(user);

    assertEquals(TOKEN_INVALID.getCode(), response.getReturnCode(), "ErrorCode");
    assertEquals("User or UserId is null", response.getReturnMessage(), "ReturnMessage");
  }

  @Test
  @DisplayName("Test applyDataVoucher: success")
  public void testGetDataVouchersPurchased_success() {
    Long userId = 1L;
    User user = new User();
    user.setId(userId);

    DataVoucher dataVoucher = new DataVoucher();
    dataVoucher.setVoucher("voucher");
    dataVoucher.setId(1L);
    dataVoucher.setUserId(userId);
    List<DataVoucher> vouchers = new ArrayList<>();
    vouchers.add(dataVoucher);

    when(dataVoucherRepository.findByUserId(eq(userId))).thenReturn(vouchers);


    GenericResponse<Iterable<DataVoucher>> response = purchaseDataService.getDataVouchersPurchased(user);

    assertEquals(NO_ERROR.getCode(), response.getReturnCode(), "ErrorCode");
    assertEquals("Get Data Vouchers purchased success", response.getReturnMessage(), "ReturnMessage");
    assertEquals(vouchers, response.getData(), "Vouchers");

    verify(dataVoucherRepository, times(1)).findByUserId(eq(userId));
  }

  @Test
  @DisplayName("Test purchaseDataVoucher: user is null")
  public void testPurchaseDataVoucher_userIsNull() {
    User user = null;

    GenericResponse<String> response = purchaseDataService.purchaseDataVoucher(user);

    assertEquals(TOKEN_INVALID.getCode(), response.getReturnCode(), "ErrorCode");
    assertEquals("User or UserId is null", response.getReturnMessage(), "ReturnMessage");
  }

  @Test
  @DisplayName("Test purchaseDataVoucher: userId is null")
  public void testPurchaseDataVoucher_userIdIsNull() {
    Long userId = null;
    User user = new User();
    user.setId(userId);


    GenericResponse<String> response = purchaseDataService.purchaseDataVoucher(user);

    assertEquals(TOKEN_INVALID.getCode(), response.getReturnCode(), "ErrorCode");
    assertEquals("User or UserId is null", response.getReturnMessage(), "ReturnMessage");
  }

  @Test
  @DisplayName("Test purchaseDataVoucher: success")
  public void testPurchaseDataVoucher_success() throws SocketTimeoutException {
    Long userId = 1L;
    User user = new User();
    user.setId(userId);
    String voucher = "voucher";
    String returnMessage = "Success";
    GenericResponse<String> dataVoucherResponse = new GenericResponse<>(NO_ERROR, returnMessage, voucher);

    when(dataVoucherApi.getDataVoucher(any())).thenReturn(dataVoucherResponse);


    GenericResponse<String> response = purchaseDataService.purchaseDataVoucher(user);

    assertEquals(NO_ERROR.getCode(), response.getReturnCode(), "ErrorCode");
    assertEquals(returnMessage, response.getReturnMessage(), "ReturnMessage");
    assertEquals(voucher, response.getData(), "Voucher");

    verify(dataVoucherApi, times(1)).getDataVoucher(any());
  }

}
