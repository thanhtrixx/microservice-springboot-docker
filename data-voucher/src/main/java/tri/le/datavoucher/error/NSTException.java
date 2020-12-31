package tri.le.datavoucher.error;

/**
 * Use No Stacktrace Exception.
 *
 * @see <a href="https://trile.dev/post/2020-04-25-optimize-java-exception-and-benchmark/">Optimize Java Exception and benchmark</a>
 */
public class NSTException extends Exception {

  private static final StackTraceElement[] EMPTY_STACK_TRACE = new StackTraceElement[0];

  private int errorCode = 0;

  public NSTException(String message) {
    super(message);
    errorCode = 0;
  }

  public NSTException(int errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }

  @Override
  public StackTraceElement[] getStackTrace() {
    return EMPTY_STACK_TRACE;
  }

  @Override
  public Throwable fillInStackTrace() {
    return this;
  }

  public int getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(int errorCode) {
    this.errorCode = errorCode;
  }

}

