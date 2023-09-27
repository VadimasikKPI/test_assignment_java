package com.example.clearsolutions.exception.handler;

import com.example.clearsolutions.exception.BirthDateRangeException;
import com.example.clearsolutions.exception.EmailException;
import com.example.clearsolutions.exception.UserAgeRestrictException;
import com.example.clearsolutions.exception.UserNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
                                                                  final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, errors);

        return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
    }

    @ExceptionHandler({UserNotFoundException.class, BirthDateRangeException.class, EmailException.class, UserAgeRestrictException.class})
    public ResponseEntity<Object> handleEntityException(final Exception ex) {
        logger.info(ex.getClass().getName());
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, Collections.singletonList(ex.getLocalizedMessage()));
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }
}
