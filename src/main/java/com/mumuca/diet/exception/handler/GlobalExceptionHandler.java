package com.mumuca.diet.exception.handler;

import com.mumuca.diet.dto.ErrorResponseDTO;
import com.mumuca.diet.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
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
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private <T> ResponseEntity<ErrorResponseDTO<T>> buildErrorResponse(
            WebRequest request,
            HttpStatus status,
            String title,
            T details
    ) {
        ErrorResponseDTO<T> errorResponse = new ErrorResponseDTO<>(
                request.getDescription(false).replace("uri=", ""),
                status.value(),
                title,
                details,
                LocalDateTime.now()
        );

        return ResponseEntity.status(status).body(errorResponse);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        Map<String, String> validationErrors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .collect(Collectors.toMap(
                        error -> ((FieldError) error).getField(),
                        DefaultMessageSourceResolvable::getDefaultMessage
                ));

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

    // TODO: Improve error validation
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
            Exception ex,
            WebRequest request
    ) {
        log.error(
                "Unexpected internal error in path: {}",
                request.getDescription(false).replace("uri=", ""),
                ex
        );

        return buildErrorResponse(
                request,
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error.",
                "An unexpected error occurred."
        );
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO<String>> handleUserAlreadyExistsException(
            UserAlreadyExistsException ex,
            WebRequest request
    ) {
        return buildErrorResponse(
                request,
                HttpStatus.CONFLICT,
                "User Already Exists.",
                ex.getMessage()
        );
    }

    @ExceptionHandler(CredentialsMismatchException.class)
    public ResponseEntity<ErrorResponseDTO<String>> handleCredentialsMismatchException(
            CredentialsMismatchException ex,
            WebRequest request
    ) {
        return buildErrorResponse(
                request,
                HttpStatus.UNAUTHORIZED,
                "Credentials mismatch.",
                ex.getMessage()
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO<String>> handleResourceNotFoundException(
            ResourceNotFoundException ex,
            WebRequest request
    ) {
        return buildErrorResponse(
                request,
                HttpStatus.NOT_FOUND,
                "Resource not found.",
                ex.getMessage()
        );
    }

    @ExceptionHandler(UserNotRegisteredYetException.class)
    public ResponseEntity<ErrorResponseDTO<String>> handleUserNotRegisteredYetException(
            UserNotRegisteredYetException ex,
            WebRequest request
    ) {
        return buildErrorResponse(
                request,
                HttpStatus.UNPROCESSABLE_ENTITY,
                "User Not Registered Yet.",
                "Complete the registration before performing this action."
        );
    }

    @ExceptionHandler(UniqueMealLogPreferenceException.class)
    public ResponseEntity<ErrorResponseDTO<String>> handleUniqueMealLogPreferenceException(
            UniqueMealLogPreferenceException ex,
            WebRequest request
    ) {
        return buildErrorResponse(
                request,
                HttpStatus.EXPECTATION_FAILED,
                ex.getMessage(),
                "It is not possible to register a Meal of the same type."
        );
    }
}
