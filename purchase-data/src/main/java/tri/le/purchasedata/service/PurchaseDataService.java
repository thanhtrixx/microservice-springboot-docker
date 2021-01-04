package tri.le.purchasedata.service;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import tri.le.purchasedata.api.DataVoucherApi;
import tri.le.purchasedata.api.PhoneCarrierApi;
import tri.le.purchasedata.dto.GenericResponse;
import tri.le.purchasedata.entity.DataVoucher;
import tri.le.purchasedata.entity.User;
import tri.le.purchasedata.error.ErrorCode;
import tri.le.purchasedata.repository.DataVoucherRepository;

import java.net.SocketTimeoutException;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static tri.le.purchasedata.error.ErrorCode.NO_ERROR;
import static tri.le.purchasedata.error.ErrorCode.TOKEN_INVALID;

@Service
public class PurchaseDataService {

  private static Logger logger = LoggerFactory.getLogger(PurchaseDataService.class);

  @Value("${send-phone-carrier-executor-thread:1}")
  private int sendSmsExecutorThread;

  @Value("${re-get-voucher-in-seconds:120}")
  private int reGetVoucherInSeconds;

  @Autowired
  public ScheduledExecutorService sendSmsExecutor;

  @Autowired
  private DataVoucherRepository dataVoucherRepository;

  @Autowired
  private DataVoucherApi dataVoucherApi;

  @Autowired
  private PhoneCarrierApi phoneCarrierApi;

  @Bean
  public ScheduledExecutorService sendSmsExecutor() {
    return Executors.newScheduledThreadPool(sendSmsExecutorThread);
  }

  public GenericResponse<Iterable<DataVoucher>> getDataVouchersPurchased(User user) {

    if (user == null || user.getId() == null) {
      return new GenericResponse<>(TOKEN_INVALID, "User or UserId is null", null);
    }

    return new GenericResponse<>(NO_ERROR, "Get Data Vouchers purchased success",
      dataVoucherRepository.findByUserId(user.getId()));
  }

  public GenericResponse<String> purchaseDataVoucher(User user) {

    if (user == null || user.getId() == null) {
      return new GenericResponse<>(TOKEN_INVALID, "User or UserId is null", null);
    }

    Long userId = user.getId();

    String requestId = UUID.randomUUID().toString();
    try {
      GenericResponse<String> voucher = dataVoucherApi.getDataVoucher(requestId);

      saveDataVoucher(userId, voucher);

      return voucher;
    } catch (RestClientException | SocketTimeoutException e) {
      logger.warn("Error when get data voucher", e);

      Duration scheduleDelay = Duration.ofSeconds(reGetVoucherInSeconds - dataVoucherApi.dataVoucherTimeoutInSeconds);
      scheduleReGetDataVoucher(scheduleDelay, userId, requestId, user.getPhone());
      return new GenericResponse<>(NO_ERROR, "System will send Data Voucher to SMS", null);
    } catch (Exception e) {
      logger.error("Error when get data voucher", e);
      return new GenericResponse<>(ErrorCode.COMMON_ERROR, e.getMessage(), null);
    }
  }

  public GenericResponse<Void> applyDataVoucher(String phone, String voucher) {
    if (Strings.isNullOrEmpty(phone)) {
      return new GenericResponse<>(ErrorCode.INVALID_PARAM, "Phone is empty", null);
    }

    if (Strings.isNullOrEmpty(voucher)) {
      return new GenericResponse<>(ErrorCode.INVALID_PARAM, "Voucher is empty", null);
    }

    try {
      phoneCarrierApi.applyDataVoucher(phone, voucher);
      return new GenericResponse<>(NO_ERROR, "Success", null);
    } catch (Exception e) {
      logger.error("Call PhoneCarrier to apply data voucher error", e);
      return new GenericResponse<>(ErrorCode.APPLY_DATA_VOUCHER_ERROR, "Apply data voucher error", null);
    }
  }

  private void saveDataVoucher(Long userId, GenericResponse<String> voucher) {

    DataVoucher dataVoucher = new DataVoucher();
    dataVoucher.setUserId(userId);
    dataVoucher.setVoucher(voucher.getData());
    dataVoucherRepository.save(dataVoucher);
  }

  private void scheduleReGetDataVoucher(Duration scheduleDelay, Long userId, String requestId, String phone) {
    long delayInSeconds = scheduleDelay.isNegative()
      ? 0
      : scheduleDelay.getSeconds();

    logger.info("Schedule to re-get data voucher with delay {}s", delayInSeconds);

    sendSmsExecutor.schedule(() -> {
      try {
        GenericResponse<String> voucher = dataVoucherApi.getDataVoucherByRequestId(requestId);

        saveDataVoucher(userId, voucher);

        String smsContent = "Your data voucher: " + voucher.getData();
        phoneCarrierApi.sendSms(phone, smsContent);
      } catch (Exception e) {
        logger.error("ReGet data voucher error", e);
      }
    }, delayInSeconds, TimeUnit.SECONDS);
  }

}
