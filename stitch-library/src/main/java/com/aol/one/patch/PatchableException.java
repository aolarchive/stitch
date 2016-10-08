/*
 *  See the file "LICENSE.TXT" for the full license governing this code.
 */

package com.aol.one.patch;

/**
 *   This exception is reserved for use in Patchable interface. Patchable implementations can 
 *   extend this class for further use
 */

public class PatchableException extends PatchException {

  public PatchableException(int code) {
    super(code);
  }

  public PatchableException(int code, Throwable cause) {
    super(code, cause);
  }

  public PatchableException(int code, String message) {
    super(code, message);
  }

  public PatchableException(int code, String message, Throwable cause) {
    super(code, message, cause);
  }
}
