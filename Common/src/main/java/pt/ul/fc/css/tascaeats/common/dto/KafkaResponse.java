package pt.ul.fc.css.tascaeats.common.dto;

public class KafkaResponse<T> {

  private boolean success;
  private T data;
  private String errorCode;
  private String errorMessage;

  public KafkaResponse() {}

  // Factory methods
  public static <T> KafkaResponse<T> ok(T data) {
    KafkaResponse<T> r = new KafkaResponse<>();
    r.success = true;
    r.data = data;
    return r;
  }

  public static <T> KafkaResponse<T> error(String errorCode, String errorMessage) {
    KafkaResponse<T> r = new KafkaResponse<>();
    r.success = false;
    r.errorCode = errorCode;
    r.errorMessage = errorMessage;
    return r;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  public String getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  @Override
  public String toString() {
    if (success) {
      return "KafkaResponse{success=true, data=" + data + "}";
    }
    return "KafkaResponse{success=false, errorCode='"
        + errorCode
        + "', errorMessage='"
        + errorMessage
        + "'}";
  }
}
