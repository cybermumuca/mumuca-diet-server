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
    public ResponseEntity<ErrorResponseDTO> handleUserAlreadyRegisteredException(
            UserAlreadyRegisteredException ex,
            WebRequest request
    ) {
        var errorResponseDTO = ErrorResponseDTO.builder()
                .errorMessage(ex.getMessage())
                .errorTime(LocalDateTime.now())
                .statusCode(HttpStatus.CONFLICT.value())
                .apiPath(request.getDescription(false)
                        .replace("uri=", "")
                )
                .build();

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorResponseDTO);
    }
}
