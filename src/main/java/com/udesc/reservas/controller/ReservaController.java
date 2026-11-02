package com.udesc.reservas.controller;

import com.udesc.reservas.dto.ReservaRequest;
import com.udesc.reservas.dto.ReservaResponse;
import com.udesc.reservas.model.Reserva;
import com.udesc.reservas.service.ReservaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @PostMapping
    public ResponseEntity<ReservaResponse> criar(@Valid @RequestBody ReservaRequest request) {
        Reserva reserva = reservaService.criarReserva(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ReservaResponse(reserva));
    }

    @GetMapping
    public List<ReservaResponse> listar() {
        return reservaService.listarTodas().stream().map(ReservaResponse::new).toList();
    }

    @GetMapping("/{id}")
    public ReservaResponse buscar(@PathVariable Long id) {
        return new ReservaResponse(reservaService.buscarPorId(id));
    }
}