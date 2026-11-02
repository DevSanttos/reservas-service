package com.udesc.reservas.exception;

public class SalaIndisponivelException extends RuntimeException {
    public SalaIndisponivelException(Long salaId) {
        super("Sala " + salaId + " nao esta disponivel para reserva");
    }
}
