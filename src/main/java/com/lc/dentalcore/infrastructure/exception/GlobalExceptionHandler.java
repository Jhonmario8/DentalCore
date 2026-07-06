package com.lc.dentalcore.infrastructure.exception;

import com.lc.dentalcore.domain.exception.DomainException;
import com.lc.dentalcore.infrastructure.constants.InfrastructureConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(DomainException ex) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(ex.getMessage(), InfrastructureConstants.BAD_REQUEST));
    }

    @ExceptionHandler(InfrastructureException.class)
    public ResponseEntity<ErrorResponse> handleInfrastructureException(InfrastructureException ex) {
        return ResponseEntity.status(ex.getHttpStatus())
                .body(new ErrorResponse(ex.getMessage(), ex.getHttpStatus()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(),
                        error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(new ErrorResponse(InfrastructureConstants.MSG_INVALID_DATA, InfrastructureConstants.BAD_REQUEST, errors));

    }
}
