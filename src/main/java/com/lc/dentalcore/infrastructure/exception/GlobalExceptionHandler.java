package com.lc.dentalcore.infrastructure.exception;

import com.lc.dentalcore.domain.exception.BadRequest;
import com.lc.dentalcore.domain.exception.ConflictException;
import com.lc.dentalcore.domain.exception.NotFoundException;
import com.lc.dentalcore.domain.exception.UnauthorizedException;
import com.lc.dentalcore.infrastructure.constants.InfrastructureConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(),
                        error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(new ErrorResponse(InfrastructureConstants.MSG_INVALID_DATA, InfrastructureConstants.BAD_REQUEST, errors));

    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflictException(ConflictException ex) {
        return ResponseEntity.status(InfrastructureConstants.CONFLICT)
                .body(new ErrorResponse(ex.getMessage(), InfrastructureConstants.CONFLICT));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException ex) {
        return ResponseEntity.status(InfrastructureConstants.UNAUTHORIZED)
                .body(new ErrorResponse(ex.getMessage(), InfrastructureConstants.UNAUTHORIZED));
    }

    @ExceptionHandler(BadRequest.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequest ex) {
        return ResponseEntity.status(InfrastructureConstants.BAD_REQUEST)
                .body(new ErrorResponse(ex.getMessage(), InfrastructureConstants.BAD_REQUEST));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex) {
        return ResponseEntity.status(InfrastructureConstants.NOT_FOUND)
                .body(new ErrorResponse(ex.getMessage(), InfrastructureConstants.NOT_FOUND));
    }
}
