package tri.le.datavoucher.error;

public enum ErrorCode {

  NO_ERROR(0),
  COMMON_ERROR(1),
  INVALID_PARAM(2),
  CALL_API_ERROR(3),
  CAN_NOT_GET_VOUCHER_BY_REQUEST_ID(4),
  ;

  private final int code;

  ErrorCode(final int code) {
    this.code = code;
  }

  public int getCode() {
    return code;
  }
}
