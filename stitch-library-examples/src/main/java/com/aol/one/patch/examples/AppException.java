// Copyright 2015 AOL Platforms.

package com.aol.one.patch.examples;

/**
 * @author Madhu Ramanna <madhu.ramanna@advertising.com> dt 2/22/16.
 */

public class AppException extends RuntimeException {

  private static final long serialVersionUID = -5748927331654735709L;

  public AppException() {
    super();
  }

  public AppException(Throwable cause) {
    super(cause);
  }

  public AppException(String message) {
    super(message);
  }

  public AppException(String message, Throwable cause) {
    super(message, cause);
  }

  protected AppException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
