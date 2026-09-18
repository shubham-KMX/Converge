package com.example.conference.exceptions;

public class QrCodeGenerationException extends ConferenceException {

  public QrCodeGenerationException() {
  }

  public QrCodeGenerationException(String message) {
    super(message);
  }

  public QrCodeGenerationException(String message, Throwable cause) {
    super(message, cause);
  }

  public QrCodeGenerationException(Throwable cause) {
    super(cause);
  }
}
