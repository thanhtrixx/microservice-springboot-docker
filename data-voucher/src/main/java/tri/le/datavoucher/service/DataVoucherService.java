package tri.le.datavoucher.service;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import tri.le.datavoucher.api.ThirdPartyApi;
import tri.le.datavoucher.dto.GenericResponse;
import tri.le.datavoucher.entity.DataVoucherEntity;
import tri.le.datavoucher.error.ErrorCode;
import tri.le.datavoucher.error.NSTException;
import tri.le.datavoucher.repository.DataVoucherRepository;

@Service
public class DataVoucherService {

  private static Logger logger = LoggerFactory.getLogger(DataVoucherService.class);

  @Autowired
  private ThirdPartyApi thirdPartyApi;

  @Autowired
  private DataVoucherRepository dataVoucherRepository;

  public GenericResponse<String> getDataVoucher(String requestId) {
    if (Strings.isNullOrEmpty(requestId)) {
      return new GenericResponse<>(ErrorCode.INVALID_PARAM, "RequestId is empty", null);
    }

    try {

      String voucher = thirdPartyApi.getDataVoucher();

      DataVoucherEntity dataVoucher = new DataVoucherEntity();
      dataVoucher.setId(requestId);
      dataVoucher.setVoucher(voucher);

      dataVoucherRepository.save(dataVoucher);

      return new GenericResponse<>(ErrorCode.NO_ERROR, "Success", voucher);
    } catch (RestClientException e) {
      String errorMessage = "Error when call ThirdParty with requestId: " + requestId;
      logger.error(errorMessage, e);
      return new GenericResponse<>(ErrorCode.CALL_API_ERROR, errorMessage, null);
    } catch (Exception e) {
      logger.error("Error when get data voucher", e);
      return new GenericResponse<>(ErrorCode.COMMON_ERROR, e.getMessage(), null);
    }
  }

  public GenericResponse<String> getDataVoucherByRequestId(String requestId) {
    if (Strings.isNullOrEmpty(requestId)) {
      return new GenericResponse<>(ErrorCode.INVALID_PARAM, "RequestId is empty", null);
    }

    try {

      DataVoucherEntity dataVoucher = dataVoucherRepository
        .findById(requestId)
        .orElseThrow(() ->
          new NSTException(ErrorCode.CAN_NOT_GET_VOUCHER_BY_REQUEST_ID.getCode(),
            "Don't have data voucher from cache with requestId: " + requestId
          ));

      return new GenericResponse<>(ErrorCode.NO_ERROR, "Success", dataVoucher.getVoucher());
    } catch (NSTException e) {
      logger.error("Error when get data voucher from cache", e);
      return new GenericResponse<>(e.getErrorCode(), e.getMessage(), null);
    } catch (Exception e) {
      logger.error("Error when get data voucher from cache", e);
      return new GenericResponse<>(ErrorCode.CAN_NOT_GET_VOUCHER_BY_REQUEST_ID, e.getMessage(), null);
    }
  }
}
