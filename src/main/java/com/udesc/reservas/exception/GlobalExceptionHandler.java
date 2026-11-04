package com.udesc.reservas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ReservaInvalidaException.class)
    public ResponseEntity<Map<String, String>> handleInvalida(ReservaInvalidaException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("erro", ex.getMessage()));
    }

    @ExceptionHandler(SalaIndisponivelException.class)
    public ResponseEntity<Map<String, String>> handleIndisponivel(SalaIndisponivelException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("erro", ex.getMessage()));
    }

    @ExceptionHandler(EquipamentoIndisponivelException.class)
    public ResponseEntity<Map<String, String>> handleEquipamentoIndisponivel(EquipamentoIndisponivelException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("erro", ex.getMessage()));
    }
}
