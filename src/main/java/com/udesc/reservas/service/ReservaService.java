package com.udesc.reservas.service;

import com.udesc.reservas.client.EquipamentoClient;
import com.udesc.reservas.client.SalaClient;
import com.udesc.reservas.dto.ReservaConfirmadaEvent;
import com.udesc.reservas.dto.ReservaRequest;
import com.udesc.reservas.exception.EquipamentoIndisponivelException;
import com.udesc.reservas.exception.ReservaInvalidaException;
import com.udesc.reservas.exception.SalaIndisponivelException;
import com.udesc.reservas.messaging.ReservaEventPublisher;
import com.udesc.reservas.model.Reserva;
import com.udesc.reservas.model.StatusReserva;
import com.udesc.reservas.repository.ReservaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final SalaClient salaClient;
    private final EquipamentoClient equipamentoClient;
    private final ReservaEventPublisher eventPublisher;

    public ReservaService(ReservaRepository reservaRepository, SalaClient salaClient,
                          EquipamentoClient equipamentoClient, ReservaEventPublisher eventPublisher) {
        this.reservaRepository = reservaRepository;
        this.salaClient = salaClient;
        this.equipamentoClient = equipamentoClient;
        this.eventPublisher = eventPublisher;
    }

    public Reserva criarReserva(ReservaRequest request) {
        validarDados(request);

        Reserva reserva = new Reserva(request.getSalaId(), request.getUsuario(), request.getInicio(), request.getFim());
        reserva = reservaRepository.save(reserva);

        try {
            salaClient.reservarSala(request.getSalaId());
        } catch (IllegalStateException ex) {
            reserva.setStatus(StatusReserva.CANCELADA);
            reservaRepository.save(reserva);
            throw new SalaIndisponivelException(request.getSalaId());
        }

        if (request.getEquipamentos() != null && !request.getEquipamentos().isEmpty()) {
            try {
                equipamentoClient.alocarEquipamentos(reserva.getId(), request.getEquipamentos());
            } catch (Exception ex) {
                salaClient.liberarSala(request.getSalaId());
                reserva.setStatus(StatusReserva.CANCELADA);
                reservaRepository.save(reserva);
                throw new EquipamentoIndisponivelException(reserva.getId(), ex);
            }
        }

        try {
            reserva.setStatus(StatusReserva.CONFIRMADA);
            reserva = reservaRepository.save(reserva);

            eventPublisher.publicarReservaConfirmada(new ReservaConfirmadaEvent(
                    reserva.getId(), reserva.getSalaId(), reserva.getUsuario(), reserva.getInicio(), reserva.getFim()));
        } catch (Exception ex) {
            equipamentoClient.liberarEquipamentos(reserva.getId());
            salaClient.liberarSala(request.getSalaId());
            reserva.setStatus(StatusReserva.CANCELADA);
            reservaRepository.save(reserva);
            throw new IllegalStateException("Falha ao confirmar a reserva, operacao compensada", ex);
        }

        return reserva;
    }

    public List<Reserva> listarTodas() {
        return reservaRepository.findAll();
    }

    public Reserva buscarPorId(Long id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new ReservaInvalidaException("Reserva " + id + " nao encontrada"));
    }

    private void validarDados(ReservaRequest request) {
        if (request.getInicio().isBefore(LocalDateTime.now())) {
            throw new ReservaInvalidaException("Nao e possivel reservar em uma data/hora no passado");
        }
        if (!request.getFim().isAfter(request.getInicio())) {
            throw new ReservaInvalidaException("O horario de termino deve ser depois do horario de inicio");
        }
    }
}
