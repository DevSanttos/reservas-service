package com.udesc.reservas.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class SalaClient {

    //o Gateway existe pra controlar quem entra de fora, não pra intermediar comunicação interna entre microsserviços
    private final RestTemplate restTemplate;
    private final String salasServiceUrl;

    public SalaClient(RestTemplate restTemplate, @Value("${salas.service.url}") String salasServiceUrl) {
        this.restTemplate = restTemplate;
        this.salasServiceUrl = salasServiceUrl;
    }

    public void reservarSala(Long salaId) {
        try {
            restTemplate.postForEntity(salasServiceUrl + "/salas/" + salaId + "/reservar", null, Void.class);
        } catch (RestClientException ex) {
            throw new IllegalStateException("Nao foi possivel reservar a sala " + salaId, ex);
        }
    }

    public void liberarSala(Long salaId) {
        try {
            restTemplate.postForEntity(salasServiceUrl + "/salas/" + salaId + "/liberar", null, Void.class);
        } catch (RestClientException ex) {
            System.err.println("Falha ao compensar (liberar) a sala " + salaId + ": " + ex.getMessage());
        }
    }
}
