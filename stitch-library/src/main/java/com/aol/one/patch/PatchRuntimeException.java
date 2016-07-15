/*
 *  See the file "LICENSE.TXT" for the full license governing this code.
 */

package com.aol.one.patch;

public class PatchRuntimeException extends RuntimeException {

  private static final long serialVersionUID = -9217861728807808400L;

  private int code = ErrorCodes.ERR_UNKNOWN;

  public PatchRuntimeException(int code) {
    this(code, getMessageForCode(code));
  }

  public PatchRuntimeException(int code, Throwable cause) {
    this(code, getMessageForCode(code), cause);
  }

  public PatchRuntimeException(int code, String message) {
    super(message);
    this.code = code;
  }

  public PatchRuntimeException(int code, String message, Throwable cause) {
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

