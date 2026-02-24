 package com.ejercicio.conector.exception;

 import org.springframework.http.HttpStatus;

 import org.springframework.http.ResponseEntity;
 import org.springframework.web.bind.MissingServletRequestParameterException;
 import org.springframework.web.bind.annotation.ExceptionHandler;
 import org.springframework.web.bind.annotation.ControllerAdvice;

 import javax.servlet.http.HttpServletRequest;

 @ControllerAdvice
 public class GlobalExceptionHandler {

     @ExceptionHandler(ModelNotFoundException.class)
     public ResponseEntity<ErrorMessage> handleModelNotFound(ModelNotFoundException ex, HttpServletRequest request) {
         ErrorMessage error = new ErrorMessage(
                 ex.getMessage(),
                 request.getRequestURL().toString(),
                 request.getMethod()
         );

         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
     }

     @ExceptionHandler(ParametrosInvalidosException.class)
     public ResponseEntity<ErrorMessage> handleParametrosInvalidos(ParametrosInvalidosException ex, HttpServletRequest request) {
         ErrorMessage error = new ErrorMessage(
                 ex.getMessage(),
                 request.getRequestURL().toString(),
                 request.getMethod()
         );

         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
     }

     @ExceptionHandler(LogicaErrorException.class)
     public ResponseEntity<ErrorMessage> handleLogicaError(LogicaErrorException ex, HttpServletRequest request) {
         ErrorMessage error = new ErrorMessage(
                 ex.getMessage(),
                 request.getRequestURL().toString(),
                 request.getMethod()
         );

         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
     }

     @ExceptionHandler(MissingServletRequestParameterException.class)
     public ResponseEntity<ErrorMessage> handleMissingParams(MissingServletRequestParameterException ex, HttpServletRequest request) {

         String paramName = ex.getParameterName();
         ErrorMessage error = new ErrorMessage(
                 "Falta el parámetro obligatorio: " + paramName,
                 request.getRequestURL().toString(),
                 request.getMethod()
         );

         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
     }
 }
