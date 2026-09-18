package com.example.conference.exceptions;

public class PassTierNotFoundException extends ConferenceException {

  public PassTierNotFoundException() {
  }

  public PassTierNotFoundException(String message) {
    super(message);
  }

  public PassTierNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public PassTierNotFoundException(Throwable cause) {
    super(cause);
  }
}
