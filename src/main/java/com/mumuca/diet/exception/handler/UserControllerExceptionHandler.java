package com.mumuca.diet.exception.handler;

import com.mumuca.diet.controller.UserController;
import com.mumuca.diet.dto.ErrorResponseDTO;
import com.mumuca.diet.exception.UserAlreadyRegisteredException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice(assignableTypes = {UserController.class})
@Order(Ordered.HIGHEST_PRECEDENCE)
public class UserControllerExceptionHandler {

    @ExceptionHandler(UserAlreadyRegisteredException.class)
    public ResponseEntity<ErrorResponseDTO<String>> handleUserAlreadyRegisteredException(
            UserAlreadyRegisteredException ex,
            WebRequest request
    ) {
        var statusCode = HttpStatus.CONFLICT;

        var errorResponseDTO = new ErrorResponseDTO<>(
                request.getDescription(false).replace("uri=", ""),
                statusCode.value(),
                ex.getMessage(),
                "This action can only be performed once.",
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(statusCode)
                .body(errorResponseDTO);
    }
}
