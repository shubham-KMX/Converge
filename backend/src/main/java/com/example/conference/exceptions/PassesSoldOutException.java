package com.example.conference.exceptions;

public class PassesSoldOutException extends ConferenceException {

  public PassesSoldOutException() {
  }

  public PassesSoldOutException(String message) {
    super(message);
  }

  public PassesSoldOutException(String message, Throwable cause) {
    super(message, cause);
  }

  public PassesSoldOutException(Throwable cause) {
    super(cause);
  }
}
