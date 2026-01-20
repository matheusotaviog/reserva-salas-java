package com.reservasalas.reserva_salas.controller;

import com.reservasalas.reserva_salas.dto.SalaRequest;
import com.reservasalas.reserva_salas.dto.SalaResponse;
import com.reservasalas.reserva_salas.services.SalaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/salas")
public class SalaController {
    
    @Autowired
    private SalaService salaService;
    
    /**
     * POST /api/salas
     * Cria uma nova sala
     */
    @PostMapping
    public ResponseEntity<SalaResponse> criarSala(@Valid @RequestBody SalaRequest request) {
        SalaResponse response = salaService.criarSala(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * GET /api/salas
     * Lista todas as salas ativas
     */
    @GetMapping
    public ResponseEntity<List<SalaResponse>> listarSalasAtivas() {
        List<SalaResponse> salas = salaService.listarSalasAtivas();
        return ResponseEntity.ok(salas);
    }
    
    /**
     * GET /api/salas/todas
     * Lista todas as salas (ativas e inativas)
     */
    @GetMapping("/todas")
    public ResponseEntity<List<SalaResponse>> listarTodasSalas() {
        List<SalaResponse> salas = salaService.listarTodasSalas();
        return ResponseEntity.ok(salas);
    }
    
    /**
     * GET /api/salas/{id}
     * Busca uma sala por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<SalaResponse> buscarSalaPorId(@PathVariable Long id) {
        SalaResponse sala = salaService.buscarPorId(id);
        return ResponseEntity.ok(sala);
    }
    
    /**
     * GET /api/salas/capacidade/{capacidade}
     * Busca salas com capacidade mínima
     */
    @GetMapping("/capacidade/{capacidade}")
    public ResponseEntity<List<SalaResponse>> buscarPorCapacidade(@PathVariable Integer capacidade) {
        List<SalaResponse> salas = salaService.buscarPorCapacidadeMinima(capacidade);
        return ResponseEntity.ok(salas);
    }
    
    /**
     * GET /api/salas/localizacao?local={texto}
     * Busca salas por localização
     */
    @GetMapping("/localizacao")
    public ResponseEntity<List<SalaResponse>> buscarPorLocalizacao(@RequestParam String local) {
        List<SalaResponse> salas = salaService.buscarPorLocalizacao(local);
        return ResponseEntity.ok(salas);
    }
    
    /**
     * PUT /api/salas/{id}
     * Atualiza dados de uma sala
     */
    @PutMapping("/{id}")
    public ResponseEntity<SalaResponse> atualizarSala(
            @PathVariable Long id,
            @Valid @RequestBody SalaRequest request) {
        SalaResponse response = salaService.atualizarSala(id, request);
        return ResponseEntity.ok(response);
    }
    
    /**
     * PATCH /api/salas/{id}/desativar
     * Desativa uma sala
     */
    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Void> desativarSala(@PathVariable Long id) {
        salaService.desativarSala(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * PATCH /api/salas/{id}/reativar
     * Reativa uma sala
     */
    @PatchMapping("/{id}/reativar")
    public ResponseEntity<Void> reativarSala(@PathVariable Long id) {
        salaService.reativarSala(id);
        return ResponseEntity.noContent().build();
    }
}
