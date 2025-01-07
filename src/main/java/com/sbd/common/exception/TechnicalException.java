package com.sbd.common.exception;

import lombok.Getter;

@Getter
public class TechnicalException extends Exception {
  private final int status;
  private final String requestId;

  public TechnicalException(int status, String requestId) {
    this.status = status;
    this.requestId = requestId;
  }

  public TechnicalException(int status, String requestId, String message) {
    super(message);
    this.status = status;
    this.requestId = requestId;
  }

  public TechnicalException(int status, String requestId, String message, Throwable cause) {
    super(message, cause);
    this.status = status;
    this.requestId = requestId;
  }
}
