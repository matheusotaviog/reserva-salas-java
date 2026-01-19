package com.reservasalas.reserva_salas.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * DTO para requisição de criação de reserva
 */
public class ReservaRequest {
    
    @NotNull(message = "ID da sala é obrigatório")
    private Long salaId;
    
    @NotBlank(message = "Nome do responsável é obrigatório")
    private String nomeResponsavel;
    
    @NotBlank(message = "Email do responsável é obrigatório")
    @Email(message = "Email inválido")
    private String emailResponsavel;
    
    @NotNull(message = "Data e hora de início são obrigatórias")
    @Future(message = "Data e hora de início devem ser futuras")
    private LocalDateTime dataHoraInicio;
    
    @NotNull(message = "Data e hora de fim são obrigatórias")
    private LocalDateTime dataHoraFim;
    
    private String observacoes;
    
    // Construtores
    public ReservaRequest() {
    }
    
    public ReservaRequest(Long salaId, String nomeResponsavel, String emailResponsavel,
                         LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim) {
        this.salaId = salaId;
        this.nomeResponsavel = nomeResponsavel;
        this.emailResponsavel = emailResponsavel;
        this.dataHoraInicio = dataHoraInicio;
        this.dataHoraFim = dataHoraFim;
    }
    
    // Getters e Setters
    public Long getSalaId() {
        return salaId;
    }
    
    public void setSalaId(Long salaId) {
        this.salaId = salaId;
    }
    
    public String getNomeResponsavel() {
        return nomeResponsavel;
    }
    
    public void setNomeResponsavel(String nomeResponsavel) {
        this.nomeResponsavel = nomeResponsavel;
    }
    
    public String getEmailResponsavel() {
        return emailResponsavel;
    }
    
    public void setEmailResponsavel(String emailResponsavel) {
        this.emailResponsavel = emailResponsavel;
    }
    
    public LocalDateTime getDataHoraInicio() {
        return dataHoraInicio;
    }
    
    public void setDataHoraInicio(LocalDateTime dataHoraInicio) {
        this.dataHoraInicio = dataHoraInicio;
    }
    
    public LocalDateTime getDataHoraFim() {
        return dataHoraFim;
    }
    
    public void setDataHoraFim(LocalDateTime dataHoraFim) {
        this.dataHoraFim = dataHoraFim;
    }
    
    public String getObservacoes() {
        return observacoes;
    }
    
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}
