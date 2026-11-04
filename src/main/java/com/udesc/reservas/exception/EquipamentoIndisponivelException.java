package com.udesc.reservas.exception;

public class EquipamentoIndisponivelException extends RuntimeException {

    public EquipamentoIndisponivelException(Long reservaId, Throwable cause) {
        super("Equipamentos indisponiveis para a reserva " + reservaId, cause);
    }
}
