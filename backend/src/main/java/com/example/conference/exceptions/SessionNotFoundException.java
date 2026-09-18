package com.example.conference.exceptions;

public class SessionNotFoundException extends ConferenceException {

  public SessionNotFoundException() {
  }

  public SessionNotFoundException(String message) {
    super(message);
  }

  public SessionNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public SessionNotFoundException(Throwable cause) {
    super(cause);
  }
}
