package com.example.conference.exceptions;

public class ConferenceUpdateException extends ConferenceException {

  public ConferenceUpdateException() {
  }

  public ConferenceUpdateException(String message) {
    super(message);
  }

  public ConferenceUpdateException(String message, Throwable cause) {
    super(message, cause);
  }

  public ConferenceUpdateException(Throwable cause) {
    super(cause);
  }
}
