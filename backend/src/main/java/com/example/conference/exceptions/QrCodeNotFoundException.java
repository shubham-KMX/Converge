package com.example.conference.exceptions;

public class QrCodeNotFoundException extends ConferenceException {

  public QrCodeNotFoundException() {
  }

  public QrCodeNotFoundException(String message) {
    super(message);
  }

  public QrCodeNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public QrCodeNotFoundException(Throwable cause) {
    super(cause);
  }
}
