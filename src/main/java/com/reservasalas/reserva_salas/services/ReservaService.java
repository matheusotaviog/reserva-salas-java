package com.reservasalas.reserva_salas.services;

import com.reservasalas.reserva_salas.dto.DisponibilidadeResponse;
import com.reservasalas.reserva_salas.dto.ReservaRequest;
import com.reservasalas.reserva_salas.dto.ReservaResponse;
import com.reservasalas.reserva_salas.dto.SalaResponse;
import com.reservasalas.reserva_salas.model.Reserva;
import com.reservasalas.reserva_salas.model.Reserva.StatusReserva;
import com.reservasalas.reserva_salas.model.Sala;
import com.reservasalas.reserva_salas.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservaService {
    
    @Autowired
    private ReservaRepository reservaRepository;
    
    @Autowired
    private SalaService salaService;
    
    /**
     * Cria uma nova reserva
     */
    public ReservaResponse criarReserva(ReservaRequest request) {
        // Valida horários
        validarHorarios(request.getDataHoraInicio(), request.getDataHoraFim());
        
        // Busca a sala
        Sala sala = salaService.buscarSalaEntityPorId(request.getSalaId());
        
        // Verifica se sala está ativa
        if (!sala.getAtiva()) {
            throw new IllegalArgumentException("Sala não está disponível para reservas");
        }
        
        // Verifica disponibilidade (conflitos de horário)
        verificarDisponibilidade(
            request.getSalaId(),
            request.getDataHoraInicio(),
            request.getDataHoraFim()
        );
        
        // Cria a reserva
        Reserva reserva = new Reserva(
            sala,
            request.getNomeResponsavel(),
            request.getEmailResponsavel(),
            request.getDataHoraInicio(),
            request.getDataHoraFim()
        );
        reserva.setObservacoes(request.getObservacoes());
        
        // Salva no banco
        Reserva reservaSalva = reservaRepository.save(reserva);
        
        return converterParaResponse(reservaSalva);
    }
    
    /**
     * Lista todas as reservas confirmadas futuras
     */
    public List<ReservaResponse> listarReservasFuturas() {
        return reservaRepository.findByDataHoraInicioGreaterThanEqualAndStatus(
                LocalDateTime.now(), StatusReserva.CONFIRMADA)
            .stream()
            .map(this::converterParaResponse)
            .collect(Collectors.toList());
    }
    
    /**
     * Lista reservas de uma sala específica
     */
    public List<ReservaResponse> listarReservasPorSala(Long salaId) {
        Sala sala = salaService.buscarSalaEntityPorId(salaId);
        
        return reservaRepository.findBySalaAndStatus(sala, StatusReserva.CONFIRMADA)
            .stream()
            .map(this::converterParaResponse)
            .collect(Collectors.toList());
    }
    
    /**
     * Busca reservas por email do responsável
     */
    public List<ReservaResponse> listarReservasPorEmail(String email) {
        return reservaRepository.findByEmailResponsavelIgnoreCase(email)
            .stream()
            .map(this::converterParaResponse)
            .collect(Collectors.toList());
    }
    
    /**
     * Busca reserva por ID
     */
    public ReservaResponse buscarPorId(Long id) {
        Reserva reserva = reservaRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Reserva não encontrada com ID: " + id));
        
        return converterParaResponse(reserva);
    }
    
    /**
     * Cancela uma reserva
     */
    public void cancelarReserva(Long id) {
        Reserva reserva = reservaRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Reserva não encontrada com ID: " + id));
        
        if (reserva.getStatus() == StatusReserva.CANCELADA) {
            throw new IllegalArgumentException("Reserva já está cancelada");
        }
        
        reserva.setStatus(StatusReserva.CANCELADA);
        reservaRepository.save(reserva);
    }
    
    /**
     * Verifica disponibilidade de uma sala em um período
     */
    public DisponibilidadeResponse verificarDisponibilidadeSala(
            Long salaId, 
            LocalDateTime inicio, 
            LocalDateTime fim) {
        
        // Valida horários
        validarHorarios(inicio, fim);
        
        // Busca a sala
        Sala sala = salaService.buscarSalaEntityPorId(salaId);
        
        // Busca conflitos
        List<Reserva> conflitos = reservaRepository.findConflitosDeHorario(salaId, inicio, fim);
        
        DisponibilidadeResponse response = new DisponibilidadeResponse(
            sala.getId(),
            sala.getNome(),
            conflitos.isEmpty(),
            conflitos.isEmpty() ? "Sala disponível no período solicitado" : "Sala já possui reservas neste período",
            inicio,
            fim
        );
        
        // Adiciona horários ocupados se houver conflitos
        if (!conflitos.isEmpty()) {
            List<DisponibilidadeResponse.HorarioOcupado> horariosOcupados = conflitos.stream()
                .map(r -> new DisponibilidadeResponse.HorarioOcupado(
                    r.getDataHoraInicio(),
                    r.getDataHoraFim(),
                    r.getNomeResponsavel()
                ))
                .collect(Collectors.toList());
            
            response.setHorariosOcupados(horariosOcupados);
        }
        
        return response;
    }
    
    /**
     * Valida se os horários são válidos
     */
    private void validarHorarios(LocalDateTime inicio, LocalDateTime fim) {
        if (inicio == null || fim == null) {
            throw new IllegalArgumentException("Data e hora de início e fim são obrigatórias");
        }
        
        if (inicio.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Data e hora de início não podem ser no passado");
        }
        
        if (fim.isBefore(inicio) || fim.isEqual(inicio)) {
            throw new IllegalArgumentException("Data e hora de fim devem ser posteriores ao início");
        }
        
        // Validação de duração mínima (15 minutos)
        if (inicio.plusMinutes(15).isAfter(fim)) {
            throw new IllegalArgumentException("Duração mínima da reserva é de 15 minutos");
        }
        
        // Validação de duração máxima (8 horas)
        if (inicio.plusHours(8).isBefore(fim)) {
            throw new IllegalArgumentException("Duração máxima da reserva é de 8 horas");
        }
    }
    
    /**
     * Verifica se há conflitos de horário e lança exceção se houver
     */
    private void verificarDisponibilidade(Long salaId, LocalDateTime inicio, LocalDateTime fim) {
        List<Reserva> conflitos = reservaRepository.findConflitosDeHorario(salaId, inicio, fim);
        
        if (!conflitos.isEmpty()) {
            throw new IllegalArgumentException(
                "Sala já possui reserva neste período. " +
                "Horário ocupado de " + conflitos.get(0).getDataHoraInicio() + 
                " até " + conflitos.get(0).getDataHoraFim()
            );
        }
    }
    
    /**
     * Converte Entity para DTO Response
     */
    private ReservaResponse converterParaResponse(Reserva reserva) {
        SalaResponse salaResponse = new SalaResponse(
            reserva.getSala().getId(),
            reserva.getSala().getNome(),
            reserva.getSala().getCapacidade(),
            reserva.getSala().getDescricao(),
            reserva.getSala().getLocalizacao(),
            reserva.getSala().getAtiva()
        );
        
        return new ReservaResponse(
            reserva.getId(),
            salaResponse,
            reserva.getNomeResponsavel(),
            reserva.getEmailResponsavel(),
            reserva.getDataHoraInicio(),
            reserva.getDataHoraFim(),
            reserva.getObservacoes(),
            reserva.getStatus().toString(),
            reserva.getDataCriacao()
        );
    }
}