package com.udesc.reservas.client;

import com.udesc.reservas.dto.EquipamentoRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EquipamentoClientImplTest {

    @Mock
    private RestTemplate restTemplate;

    private EquipamentoClientImpl client;

    @BeforeEach
    void setUp() {
        client = new EquipamentoClientImpl(restTemplate, "http://equipamentos");
    }

    @Test
    void deveAlocarEquipamentosNoEndpointConfigurado() {
        when(restTemplate.postForEntity(anyString(), any(), eq(Void.class)))
                .thenReturn(ResponseEntity.ok().build());

        client.alocarEquipamentos(10L, List.of(equipamentoRequest()));

        verify(restTemplate).postForEntity(
                eq("http://equipamentos/api/equipamentos/alocar"), any(), eq(Void.class));
    }

    @Test
    void deveEncapsularFalhaAoAlocarEquipamentos() {
        when(restTemplate.postForEntity(anyString(), any(), eq(Void.class)))
                .thenThrow(new RestClientException("servico indisponivel"));

        assertThatThrownBy(() -> client.alocarEquipamentos(10L, List.of(equipamentoRequest())))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void deveLiberarEquipamentosNoEndpointConfigurado() {
        when(restTemplate.postForEntity(anyString(), any(), eq(Void.class)))
                .thenReturn(ResponseEntity.noContent().build());

        client.liberarEquipamentos(10L);

        verify(restTemplate).postForEntity(
                eq("http://equipamentos/api/equipamentos/liberar/10"), eq(null), eq(Void.class));
    }

    @Test
    void deveAbsorverFalhaAoLiberarEquipamentosParaConcluirCompensacao() {
        doThrow(new RestClientException("servico indisponivel"))
                .when(restTemplate).postForEntity(anyString(), eq(null), eq(Void.class));

        client.liberarEquipamentos(10L);

        verify(restTemplate).postForEntity(
                eq("http://equipamentos/api/equipamentos/liberar/10"), eq(null), eq(Void.class));
    }

    private EquipamentoRequest equipamentoRequest() {
        EquipamentoRequest equipamento = new EquipamentoRequest();
        equipamento.setEquipamentoId(UUID.randomUUID());
        equipamento.setQuantidade(1);
        return equipamento;
    }
}
