package com.udesc.reservas.client;

import com.udesc.reservas.dto.EquipamentoRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class EquipamentoClientImpl implements EquipamentoClient {

    private final RestTemplate restTemplate;
    private final String equipamentosServiceUrl;

    public EquipamentoClientImpl(RestTemplate restTemplate,
                                 @Value("${equipamentos.service.url}") String equipamentosServiceUrl) {
        this.restTemplate = restTemplate;
        this.equipamentosServiceUrl = equipamentosServiceUrl;
    }

    @Override
    public void alocarEquipamentos(Long reservaId, List<EquipamentoRequest> equipamentos) {
        try {
            restTemplate.postForEntity(
                    equipamentosServiceUrl + "/api/equipamentos/alocar",
                    new AlocacaoRequest(reservaId, equipamentos),
                    Void.class);
        } catch (RestClientException ex) {
            throw new IllegalStateException("Nao foi possivel alocar os equipamentos da reserva " + reservaId, ex);
        }
    }

    @Override
    public void liberarEquipamentos(Long reservaId) {
        try {
            restTemplate.postForEntity(
                    equipamentosServiceUrl + "/api/equipamentos/liberar/" + reservaId,
                    null,
                    Void.class);
        } catch (RestClientException ex) {
            System.err.println("Falha ao compensar (liberar) os equipamentos da reserva "
                    + reservaId + ": " + ex.getMessage());
        }
    }

    private record AlocacaoRequest(Long reservaId, List<EquipamentoRequest> itens) {
    }
}
