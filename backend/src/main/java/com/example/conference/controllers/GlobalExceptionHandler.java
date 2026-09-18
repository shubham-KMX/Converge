package com.example.conference.controllers;

import com.example.conference.domain.dtos.ErrorDto;
import com.example.conference.exceptions.BadgeNotFoundException;
import com.example.conference.exceptions.ConferenceException;
import com.example.conference.exceptions.ConferenceNotFoundException;
import com.example.conference.exceptions.ConferenceUpdateException;
import com.example.conference.exceptions.PassTierNotFoundException;
import com.example.conference.exceptions.PassesSoldOutException;
import com.example.conference.exceptions.QrCodeGenerationException;
import com.example.conference.exceptions.QrCodeNotFoundException;
import com.example.conference.exceptions.SessionNotFoundException;
import com.example.conference.exceptions.SpeakerNotFoundException;
import com.example.conference.exceptions.UserNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorDto> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException ex) {
    log.error("Caught MethodArgumentNotValidException", ex);
    String errorMessage = ex.getBindingResult().getFieldErrors().stream()
        .findFirst()
        .map(error -> error.getField() + ": " + error.getDefaultMessage())
        .orElse("Validation error occurred");

    ErrorDto errorDto = ErrorDto.builder().error(errorMessage).build();
    return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorDto> handleConstraintViolationException(
      ConstraintViolationException ex) {
    log.error("Caught ConstraintViolationException", ex);
    String errorMessage = ex.getConstraintViolations().stream()
        .findFirst()
        .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
        .orElse("Constraint violation occurred");

    ErrorDto errorDto = ErrorDto.builder().error(errorMessage).build();
    return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({
      BadgeNotFoundException.class,
      PassesSoldOutException.class,
      ConferenceUpdateException.class,
      PassTierNotFoundException.class,
      ConferenceNotFoundException.class,
      SessionNotFoundException.class,
      SpeakerNotFoundException.class,
      UserNotFoundException.class
  })
  public ResponseEntity<ErrorDto> handleBadRequest(ConferenceException ex) {
    log.error("Caught bad request exception", ex);
    ErrorDto errorDto = ErrorDto.builder().error(ex.getMessage()).build();
    return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({QrCodeNotFoundException.class, QrCodeGenerationException.class})
  public ResponseEntity<ErrorDto> handleQrCodeException(ConferenceException ex) {
    log.error("Caught QR code exception", ex);
    ErrorDto errorDto = ErrorDto.builder()
        .error("An error occurred processing the QR Code")
        .build();
    return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(ConferenceException.class)
  public ResponseEntity<ErrorDto> handleConferenceException(ConferenceException ex) {
    log.error("Caught ConferenceException", ex);
    ErrorDto errorDto = ErrorDto.builder().error("An unknown error occurred").build();
    return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorDto> handleException(Exception ex) {
    log.error("Caught unexpected exception", ex);
    ErrorDto errorDto = ErrorDto.builder().error("An unknown error occurred").build();
    return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
