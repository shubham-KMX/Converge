package com.example.conference.exceptions;

public class ConferenceException extends RuntimeException {

  public ConferenceException() {
  }

  public ConferenceException(String message) {
    super(message);
  }

  public ConferenceException(String message, Throwable cause) {
    super(message, cause);
  }

  public ConferenceException(Throwable cause) {
    super(cause);
  }
}
