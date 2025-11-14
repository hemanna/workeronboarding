package com.sbd.common.exception;

import lombok.Getter;

@Getter
public class BusinessException extends Exception {

  private final int status;
  private final String requestId;

  public BusinessException(int status, String requestId, String message) {
    super(message);
    this.status = status;
    this.requestId = requestId;
  }

  public BusinessException(int status, String requestId, String message, Throwable cause) {
    super(message, cause);
    this.status = status;
    this.requestId = requestId;
  }


  public BusinessException(String message) {
    super(message);
    this.status = 500;
    this.requestId = null;
  }
}


