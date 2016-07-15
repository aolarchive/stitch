/*
 *  See the file "LICENSE.TXT" for the full license governing this code.
 */

/*
 *  See the file "LICENSE.TXT" for the full license governing this code.
 */

package com.aol.one.patch;

public class PatchException extends Exception {

  private static final long serialVersionUID = 2129359956896426170L;

  private int code = ErrorCodes.ERR_UNKNOWN;

  public PatchException(int code) {
    this(code, getMessageForCode(code));
  }

  public PatchException(int code, Throwable cause) {
    this(code, getMessageForCode(code), cause);
  }

  public PatchException(int code, String message) {
    super(message);
    this.code = code;
  }

  public PatchException(int code, String message, Throwable cause) {
    super(message, cause);
    this.code = code;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  private static String getMessageForCode(int code) {
    return "Error code: " + code;
  }
}
