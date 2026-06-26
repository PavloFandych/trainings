package org.total.example.spring_core.web.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.total.example.spring_core.web.exceptions.responses.ErrorResponse;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        log.warn("Validation failed: {}", message);
        return ErrorResponse.of(message, request.getRequestURI());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMalformedRequest(HttpMessageNotReadableException ex, HttpServletRequest request) {
        log.warn("Malformed request body", ex);
        return ErrorResponse.of("Malformed JSON request", request.getRequestURI());
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public ErrorResponse handleUnsupportedMediaType(HttpMediaTypeNotSupportedException ex, HttpServletRequest request) {
        log.warn("Unsupported Content-Type: {}", ex.getContentType());
        return ErrorResponse.of("Content-Type must be application/json", request.getRequestURI());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMissingParameter(MissingServletRequestParameterException ex, HttpServletRequest request) {
        log.warn("Missing request parameter: {}", ex.getParameterName());
        return ErrorResponse.of(ex.getParameterName() + " parameter is required", request.getRequestURI());
    }

    @ExceptionHandler(ReservationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleReservationNotFound(ReservationNotFoundException ex,
                                                   HttpServletRequest request) {
        log.warn("Exception occurred while trying to find Reservation", ex);
        return ErrorResponse.of(ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(ReservationCreationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleReservationCreation(ReservationCreationException ex,
                                                   HttpServletRequest request) {
        log.warn("Exception occurred while trying to save Reservation", ex);
        return ErrorResponse.of(ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(ReservationUpdateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleReservationUpdate(ReservationUpdateException ex,
                                                 HttpServletRequest request) {
        log.warn("Exception occurred while trying to update Reservation", ex);
        return ErrorResponse.of(ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(ReservationDateConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleReservationDateConflict(ReservationDateConflictException ex,
                                                       HttpServletRequest request) {
        log.warn("Exception occurred due to date conflict", ex);
        return ErrorResponse.of(ex.getMessage(), request.getRequestURI());
    }
}
