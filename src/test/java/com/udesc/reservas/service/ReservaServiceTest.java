package com.udesc.reservas.service;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.udesc.reservas.client.SalaClient;
import com.udesc.reservas.dto.ReservaRequest;
import com.udesc.reservas.exception.ReservaInvalidaException;
import com.udesc.reservas.exception.SalaIndisponivelException;
import com.udesc.reservas.messaging.ReservaEventPublisher;
import com.udesc.reservas.model.Reserva;
import com.udesc.reservas.model.StatusReserva;
import com.udesc.reservas.repository.ReservaRepository;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;
    @Mock
    private SalaClient salaClient;
    @Mock
    private ReservaEventPublisher eventPublisher;

    private ReservaService reservaService;

    @BeforeEach
    void setUp() {
        reservaService = new ReservaService(reservaRepository, salaClient, eventPublisher);

    }

    private ReservaRequest requestValido() {
        ReservaRequest r = new ReservaRequest();
        r.setSalaId(1L);
        r.setUsuario("joao");
        r.setInicio(LocalDateTime.now().plusDays(1));
        r.setFim(LocalDateTime.now().plusDays(1).plusHours(1));
        return r;
    }

    @Test
    void deveConfirmarReservaQuandoSalaDisponivel() {
        when(reservaRepository.save(any(Reserva.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Reserva reserva = reservaService.criarReserva(requestValido());

        assertThat(reserva.getStatus()).isEqualTo(StatusReserva.CONFIRMADA);
        verify(salaClient).reservarSala(1L);
        verify(eventPublisher).publicarReservaConfirmada(any());
        verify(salaClient, never()).liberarSala(any());
    }

    @Test
    void deveRejeitarReservaComDataNoPassado() {
        ReservaRequest request = requestValido();
        request.setInicio(LocalDateTime.now().minusDays(1));

        assertThatThrownBy(() -> reservaService.criarReserva(request))
                .isInstanceOf(ReservaInvalidaException.class);

        verify(salaClient, never()).reservarSala(any());
    }

    @Test
    void deveRejeitarReservaComFimAntesDoInicio() {
        ReservaRequest request = requestValido();
        request.setFim(request.getInicio().minusMinutes(30));

        assertThatThrownBy(() -> reservaService.criarReserva(request))
                .isInstanceOf(ReservaInvalidaException.class);
    }

    @Test
    void deveCancelarReservaQuandoSalaIndisponivel() {
        when(reservaRepository.save(any(Reserva.class))).thenAnswer(invocation -> invocation.getArgument(0));
        doThrow(new IllegalStateException("indisponivel")).when(salaClient).reservarSala(1L);

        assertThatThrownBy(() -> reservaService.criarReserva(requestValido()))
                .isInstanceOf(SalaIndisponivelException.class);

        verify(eventPublisher, never()).publicarReservaConfirmada(any());
    }

    @Test
    void deveCompensarLiberandoSalaQuandoFalhaAoPublicarEvento() {
        when(reservaRepository.save(any(Reserva.class))).thenAnswer(invocation -> invocation.getArgument(0));
        doThrow(new RuntimeException("fila fora do ar")).when(eventPublisher).publicarReservaConfirmada(any());

        assertThatThrownBy(() -> reservaService.criarReserva(requestValido()))
                .isInstanceOf(IllegalStateException.class);

        verify(salaClient).liberarSala(1L);
    }
}
