package pt.ul.fc.css.tascaeats.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgumentException(IllegalArgumentException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setDetail("Invalid request");
        problemDetail.setProperty("message", ex.getMessage());
        problemDetail.setProperty("errorCode", "INVALID_REQUEST_ERR_001");
        return problemDetail;
    }

    // Catch generic exceptions as a fallback
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
        problemDetail.setProperty("errorCode", "INTERNAL_SERVER_ERR_001");
        return problemDetail;
    }
}
