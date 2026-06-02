package pt.ul.fc.css.tascaeats.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pt.ul.fc.css.tascaeats.exceptions.IncompleteInputException;
import pt.ul.fc.css.tascaeats.exceptions.RequirementsNotMetException;
import pt.ul.fc.css.tascaeats.exceptions.ResourceNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  // Catch a specific custom exception
  @ExceptionHandler(ResourceNotFoundException.class)
  public ProblemDetail handleResourceNotFoundException(ResourceNotFoundException ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
    problemDetail.setDetail("Resource not found");
    problemDetail.setProperty("message", ex.getMessage());
    problemDetail.setProperty("errorCode", "RESOURCE_ERR_001");

    return problemDetail;
  }

  @ExceptionHandler(RequirementsNotMetException.class)
  public ProblemDetail handleRequirementsNotMetException(RequirementsNotMetException ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    problemDetail.setDetail("Requirements no met");
    problemDetail.setProperty("message", ex.getMessage());
    problemDetail.setProperty("errorCode", "REQUIREMENT_ERR_001");

    return problemDetail;
  }

  @ExceptionHandler(IncompleteInputException.class)
  public ProblemDetail handleIncompleteInputException(IncompleteInputException ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    problemDetail.setDetail("Incomplete Input");
    problemDetail.setProperty("message", ex.getMessage());
    problemDetail.setProperty("errorCode", "INPUT_ERR_001");

    return problemDetail;
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ProblemDetail handleIllegalArgumentException(IllegalArgumentException ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    problemDetail.setDetail("Invalid request");
    problemDetail.setProperty("message", ex.getMessage());
    return problemDetail;
  }

  @ExceptionHandler(IllegalStateException.class)
  public ProblemDetail handleIllegalStateException(IllegalStateException ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.SERVICE_UNAVAILABLE);
    problemDetail.setDetail("Service temporarily unavailable");
    problemDetail.setProperty("message", ex.getMessage());
    return problemDetail;
  }

  // Catch generic exceptions as a fallback
  @ExceptionHandler(Exception.class)
  public ProblemDetail handleGenericException(Exception ex) {
    return ProblemDetail.forStatusAndDetail(
        HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
  }
}
