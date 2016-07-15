// Copyright 2015 AOL Platforms.

package com.aol.one.patch.examples;


public class ErrorResponse {
  private int code;
  private String message;
  private String detail;

  public ErrorResponse(int code, String message) {
    this.code = code;
    this.message = message;
  }

  public ErrorResponse(int code, String message, String detail) {
    this.code = code;
    this.message = message;
    this.detail = detail;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getDetail() {
    return detail;
  }

  public void setDetail(String detail) {
    this.detail = detail;
  }
}

