package com.example.conference.exceptions;

public class ConferenceNotFoundException extends ConferenceException {

  public ConferenceNotFoundException() {
  }

  public ConferenceNotFoundException(String message) {
    super(message);
  }

  public ConferenceNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public ConferenceNotFoundException(Throwable cause) {
    super(cause);
  }
}
