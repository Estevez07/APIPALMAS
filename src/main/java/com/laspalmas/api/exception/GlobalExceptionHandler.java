package com.laspalmas.api.exception;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;



import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleUsernameNotFound(UsernameNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("El usuario o contraseña son incorrectos");
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("El usuario o contraseña son incorrectos");
    }

    // Para validaciones de @Valid en @RequestBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errores = ex.getBindingResult()
                           .getFieldErrors()
                           .stream()
                           .map(err -> err.getDefaultMessage())
                           .collect(Collectors.joining("; "));
        return ResponseEntity.badRequest().body("Error de validación: " + errores);
    }

    // Para validaciones sobre entidades JPA
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolation(ConstraintViolationException ex) {
        String errores = ex.getConstraintViolations()
                           .stream()
                           .map(violation -> violation.getMessage())
                           .collect(Collectors.joining("; "));
        return ResponseEntity.badRequest().body("Error de validación: " + errores);
    }

  @ExceptionHandler(HttpMessageNotReadableException.class)
public ResponseEntity<?> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
    Throwable causa = ex.getCause();

    if (causa instanceof com.fasterxml.jackson.databind.exc.InvalidFormatException invalidFormatEx) {
        String mensaje = "Error de formato: El valor '" + invalidFormatEx.getValue() + 
                         "' no es válido para el tipo " + invalidFormatEx.getTargetType().getSimpleName();

        if (invalidFormatEx.getTargetType().equals(java.util.Date.class)) {
            mensaje += ". Formato esperado: yyyy-MM-dd";
        }

        return ResponseEntity.badRequest().body(mensaje);
    }

    // Para cualquier otro error de parseo JSON
    return ResponseEntity.badRequest().body("Error de lectura del JSON: " + ex.getMessage());
}
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralError(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado: " + ex.getMessage());
    }
  
}