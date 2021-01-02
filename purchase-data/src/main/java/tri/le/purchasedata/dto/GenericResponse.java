package tri.le.purchasedata.dto;


import tri.le.purchasedata.error.ErrorCode;

public class GenericResponse<T> {

  private int returnCode;

  private String returnMessage;

  private T data;

  public GenericResponse() {
  }

  public GenericResponse(int returnCode, String returnMessage, T data) {
    this.returnCode = returnCode;
    this.returnMessage = returnMessage;
    this.data = data;
  }

  public GenericResponse(ErrorCode returnCode, String returnMessage, T data) {
    this.returnCode = returnCode.getCode();
    this.returnMessage = returnMessage;
    this.data = data;
  }

  public int getReturnCode() {
    return returnCode;
  }

  public void setReturnCode(int returnCode) {
    this.returnCode = returnCode;
  }

  public String getReturnMessage() {
    return returnMessage;
  }

  public void setReturnMessage(String returnMessage) {
    this.returnMessage = returnMessage;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }
}
