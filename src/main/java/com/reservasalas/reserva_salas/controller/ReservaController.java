package com.reservasalas.reserva_salas.controller;

import com.reservasalas.reserva_salas.dto.DisponibilidadeResponse;
import com.reservasalas.reserva_salas.dto.ReservaRequest;
import com.reservasalas.reserva_salas.dto.ReservaResponse;
import com.reservasalas.reserva_salas.services.ReservaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {
    
    @Autowired
    private ReservaService reservaService;
    
    /**
     * POST /api/reservas
     * Cria uma nova reserva
     */
    @PostMapping
    public ResponseEntity<ReservaResponse> criarReserva(@Valid @RequestBody ReservaRequest request) {
        ReservaResponse response = reservaService.criarReserva(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * GET /api/reservas
     * Lista todas as reservas futuras confirmadas
     */
    @GetMapping
    public ResponseEntity<List<ReservaResponse>> listarReservasFuturas() {
        List<ReservaResponse> reservas = reservaService.listarReservasFuturas();
        return ResponseEntity.ok(reservas);
    }
    
    /**
     * GET /api/reservas/{id}
     * Busca uma reserva por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReservaResponse> buscarReservaPorId(@PathVariable Long id) {
        ReservaResponse reserva = reservaService.buscarPorId(id);
        return ResponseEntity.ok(reserva);
    }
    
    /**
     * GET /api/reservas/sala/{salaId}
     * Lista reservas de uma sala específica
     */
    @GetMapping("/sala/{salaId}")
    public ResponseEntity<List<ReservaResponse>> listarReservasPorSala(@PathVariable Long salaId) {
        List<ReservaResponse> reservas = reservaService.listarReservasPorSala(salaId);
        return ResponseEntity.ok(reservas);
    }
    
    /**
     * GET /api/reservas/email?email={email}
     * Busca reservas por email do responsável
     */
    @GetMapping("/email")
    public ResponseEntity<List<ReservaResponse>> listarReservasPorEmail(@RequestParam String email) {
        List<ReservaResponse> reservas = reservaService.listarReservasPorEmail(email);
        return ResponseEntity.ok(reservas);
    }
    
    /**
     * GET /api/reservas/disponibilidade?salaId={id}&inicio={dataHora}&fim={dataHora}
     * Verifica disponibilidade de uma sala em um período
     * 
     * Formato de data: 2024-01-20T14:00:00
     */
    @GetMapping("/disponibilidade")
    public ResponseEntity<DisponibilidadeResponse> verificarDisponibilidade(
            @RequestParam Long salaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        
        DisponibilidadeResponse response = reservaService.verificarDisponibilidadeSala(salaId, inicio, fim);
        return ResponseEntity.ok(response);
    }
    
    /**
     * PATCH /api/reservas/{id}/cancelar
     * Cancela uma reserva
     */
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelarReserva(@PathVariable Long id) {
        reservaService.cancelarReserva(id);
        return ResponseEntity.noContent().build();
    }
}