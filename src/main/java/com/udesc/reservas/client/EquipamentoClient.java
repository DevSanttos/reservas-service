package com.udesc.reservas.client;

import com.udesc.reservas.dto.EquipamentoRequest;

import java.util.List;

public interface EquipamentoClient {

    void alocarEquipamentos(Long reservaId, List<EquipamentoRequest> equipamentos);

    void liberarEquipamentos(Long reservaId);
}
