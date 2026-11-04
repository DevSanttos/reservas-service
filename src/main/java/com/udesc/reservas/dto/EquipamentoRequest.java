package com.udesc.reservas.dto;

import java.util.UUID;

public class EquipamentoRequest {

    private UUID equipamentoId;
    private Integer quantidade;

    public UUID getEquipamentoId() {
        return equipamentoId;
    }

    public void setEquipamentoId(UUID equipamentoId) {
        this.equipamentoId = equipamentoId;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}
