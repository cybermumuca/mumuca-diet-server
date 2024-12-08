package com.mumuca.diet.exception.handler;

import com.mumuca.diet.dto.ErrorResponseDTO;
import com.mumuca.diet.exception.CredentialsMismatchException;
import com.mumuca.diet.exception.ResourceNotFoundException;
import com.mumuca.diet.exception.UserAlreadyExistsException;
import com.mumuca.diet.exception.UserNotRegisteredYetException;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        Map<String, String> validationErrors = new HashMap<>();

        ex.getBindingResult()
                .getAllErrors()
                .forEach((error) -> {
                    String fieldName = ((FieldError) error).getField();
                    String validationMsg = error.getDefaultMessage();
                    validationErrors.put(fieldName, validationMsg);
                });

        var statusCode = HttpStatus.BAD_REQUEST;

        ErrorResponseDTO<Map<String, String>> errorResponseDTO = new ErrorResponseDTO<>(
                request.getDescription(false).replace("uri=", ""),
                statusCode.value(),
                "Validation failed.",
                validationErrors,
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(statusCode)
                .body(errorResponseDTO);
    }

    @Override
    protected ResponseEntity<Object> handleHandlerMethodValidationException(
            HandlerMethodValidationException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        String validationErrorMessage = ex.getParameterValidationResults()
                .stream()
                .flatMap(validationResult -> validationResult.getResolvableErrors().stream())
                .map(MessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        var statusCode = HttpStatus.BAD_REQUEST;

        ErrorResponseDTO<String> errorResponseDTO = new ErrorResponseDTO<>(
                request.getDescription(false).replace("uri=", ""),
                statusCode.value(),
                "Validation failed.",
                validationErrorMessage,
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(statusCode)
                .body(errorResponseDTO);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO<String>> handleGlobalException(
            Exception exception,
            WebRequest request
    ) {
        exception.printStackTrace();

        var statusCode = HttpStatus.INTERNAL_SERVER_ERROR;

        ErrorResponseDTO<String> errorResponseDTO = new ErrorResponseDTO<>(
                request.getDescription(false).replace("uri=", ""),
                statusCode.value(),
                "Internal Server Error.",
                "An unexpected error occurred.",
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(statusCode)
                .body(errorResponseDTO);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO<String>> handleUserAlreadyExistsException(
            UserAlreadyExistsException ex,
            WebRequest request
    ) {
        var statusCode = HttpStatus.CONFLICT;

        ErrorResponseDTO<String> errorResponseDTO = new ErrorResponseDTO<String>(
                request.getDescription(false).replace("uri=", ""),
                statusCode.value(),
                "User Already Exists.",
                ex.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(statusCode)
                .body(errorResponseDTO);
    }

    @ExceptionHandler(CredentialsMismatchException.class)
    public ResponseEntity<ErrorResponseDTO<String>> handleCredentialsMismatchException(
            CredentialsMismatchException ex,
            WebRequest request
    ) {
        var statusCode = HttpStatus.UNAUTHORIZED;

        ErrorResponseDTO<String> errorResponseDTO = new ErrorResponseDTO<>(
                request.getDescription(false).replace("uri=", ""),
                statusCode.value(),
                "Credentials mismatch.",
                ex.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(statusCode)
                .body(errorResponseDTO);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO<String>> handleResourceNotFoundException(
            ResourceNotFoundException ex,
            WebRequest request
    ) {
        var statusCode = HttpStatus.NOT_FOUND;

        var errorResponseDTO = new ErrorResponseDTO<String>(
                request.getDescription(false).replace("uri=", ""),
                statusCode.value(),
                "Resource not found.",
                ex.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(statusCode)
                .body(errorResponseDTO);
    }

    @ExceptionHandler(UserNotRegisteredYetException.class)
    public ResponseEntity<ErrorResponseDTO<String>> handleUserNotRegisteredYetException(
            UserNotRegisteredYetException ex,
            WebRequest request
    ) {
        var statusCode = HttpStatus.UNPROCESSABLE_ENTITY;

        var errorResponseDTO = new ErrorResponseDTO<>(
                request.getDescription(false).replace("uri=", ""),
                statusCode.value(),
                ex.getMessage(),
                "Complete the registration before performing this action.",
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(statusCode)
                .body(errorResponseDTO);
    }
}
