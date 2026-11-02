package com.udesc.reservas.dto;

import com.udesc.reservas.model.Reserva;
import com.udesc.reservas.model.StatusReserva;

import java.time.LocalDateTime;

public class ReservaResponse {

    private Long id;
    private Long salaId;
    private String usuario;
    private LocalDateTime inicio;
    private LocalDateTime fim;
    private StatusReserva status;

    public ReservaResponse(Reserva reserva) {
        this.id = reserva.getId();
        this.salaId = reserva.getSalaId();
        this.usuario = reserva.getUsuario();
        this.inicio = reserva.getInicio();
        this.fim = reserva.getFim();
        this.status = reserva.getStatus();
    }

    public Long getId() {
        return id;
    }

    public Long getSalaId() {
        return salaId;
    }

    public String getUsuario() {
        return usuario;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public LocalDateTime getFim() {
        return fim;
    }

    public StatusReserva getStatus() {
        return status;
    }
}
