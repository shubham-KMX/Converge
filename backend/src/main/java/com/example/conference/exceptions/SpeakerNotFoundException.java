package com.example.conference.exceptions;

public class SpeakerNotFoundException extends ConferenceException {

  public SpeakerNotFoundException() {
  }

  public SpeakerNotFoundException(String message) {
    super(message);
  }

  public SpeakerNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public SpeakerNotFoundException(Throwable cause) {
    super(cause);
  }
}
