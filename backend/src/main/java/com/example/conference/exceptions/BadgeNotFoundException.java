package com.example.conference.exceptions;

public class BadgeNotFoundException extends ConferenceException {

  public BadgeNotFoundException() {
  }

  public BadgeNotFoundException(String message) {
    super(message);
  }

  public BadgeNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public BadgeNotFoundException(Throwable cause) {
    super(cause);
  }
}
