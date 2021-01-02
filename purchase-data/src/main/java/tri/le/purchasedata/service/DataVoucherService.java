package tri.le.purchasedata.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import tri.le.purchasedata.api.DataVoucherApi;
import tri.le.purchasedata.dto.GenericResponse;
import tri.le.purchasedata.entity.DataVoucher;
import tri.le.purchasedata.entity.User;
import tri.le.purchasedata.error.ErrorCode;
import tri.le.purchasedata.repository.DataVoucherRepository;

import java.util.UUID;

import static tri.le.purchasedata.error.ErrorCode.NO_ERROR;
import static tri.le.purchasedata.error.ErrorCode.TOKEN_INVALID;

@Service
public class DataVoucherService {

  private static Logger logger = LoggerFactory.getLogger(DataVoucherService.class);

  @Autowired
  private DataVoucherRepository dataVoucherRepository;

  @Autowired
  private DataVoucherApi dataVoucherApi;

  public GenericResponse<Iterable<DataVoucher>> getDataVouchersPurchased() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null) {
      return new GenericResponse<>(TOKEN_INVALID, "Authentication is null", null);
    }

    Object principal = authentication.getPrincipal();

    if (principal == null || !(principal instanceof User)) {
      return new GenericResponse<>(TOKEN_INVALID, "Principal invalid", null);
    }

    User user = (User) principal;

    Long userId = user.getId();
    if (userId == null) {
      return new GenericResponse<>(TOKEN_INVALID, "UserId is null", null);
    }

    return new GenericResponse<>(NO_ERROR, "Get Data Vouchers purchased success",
      dataVoucherRepository.findByUserId(userId));
  }

  public GenericResponse<String> purchaseDataVoucher() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null) {
      return new GenericResponse<>(TOKEN_INVALID, "Authentication is null", null);
    }

    Object principal = authentication.getPrincipal();

    if (principal == null || !(principal instanceof User)) {
      return new GenericResponse<>(TOKEN_INVALID, "Principal invalid", null);
    }

    User user = (User) principal;

    Long userId = user.getId();
    if (userId == null) {
      return new GenericResponse<>(TOKEN_INVALID, "UserId is null", null);
    }

    try {
      GenericResponse<String> voucher = dataVoucherApi.getDataVoucher(UUID.randomUUID().toString());

      saveDataVoucher(userId, voucher);

      return voucher;
    } catch (Exception e) {
      logger.error("Error when get data voucher", e);
      return new GenericResponse<>(ErrorCode.COMMON_ERROR, e.getMessage(), null);
    }
  }

  private void saveDataVoucher(Long userId, GenericResponse<String> voucher) {

    DataVoucher dataVoucher = new DataVoucher();
    dataVoucher.setUserId(userId);
    dataVoucher.setVoucher(voucher.getData());
    dataVoucherRepository.save(dataVoucher);
  }
}
