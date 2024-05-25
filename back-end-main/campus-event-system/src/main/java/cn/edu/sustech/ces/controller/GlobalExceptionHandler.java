package cn.edu.sustech.ces.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        // Debugging

        if (e instanceof AccessDeniedException) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }
        if (e instanceof BadCredentialsException) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bad Credentials");
        }
        if (e instanceof NoResourceFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resource Not Found");
        }
        if (e instanceof IllegalArgumentException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        if (e instanceof IllegalStateException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        if (e instanceof NullPointerException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Null Pointer Exception");
        }

        String body = "Internal Server Error\n" + e.getMessage();
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

}
