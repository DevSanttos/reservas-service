package com.udesc.reservas.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ReservaConfirmadaEvent implements Serializable {

    private Long reservaId;
    private Long salaId;
    private String usuario;
    private LocalDateTime inicio;
    private LocalDateTime fim;

    public ReservaConfirmadaEvent() {
    }

    public ReservaConfirmadaEvent(Long reservaId, Long salaId, String usuario, LocalDateTime inicio, LocalDateTime fim) {
        this.reservaId = reservaId;
        this.salaId = salaId;
        this.usuario = usuario;
        this.inicio = inicio;
        this.fim = fim;
    }

    public Long getReservaId() {
        return reservaId;
    }

    public void setReservaId(Long reservaId) {
        this.reservaId = reservaId;
    }

    public Long getSalaId() {
        return salaId;
    }

    public void setSalaId(Long salaId) {
        this.salaId = salaId;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio;
    }

    public LocalDateTime getFim() {
        return fim;
    }

    public void setFim(LocalDateTime fim) {
        this.fim = fim;
    }
}
