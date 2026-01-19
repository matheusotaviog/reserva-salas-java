package com.reservasalas.reserva_salas.dto;

import java.time.LocalDateTime;

/**
 * DTO para resposta com dados de uma reserva
 */
public class ReservaResponse {
    
    private Long id;
    private SalaResponse sala;
    private String nomeResponsavel;
    private String emailResponsavel;
    private LocalDateTime dataHoraInicio;
    private LocalDateTime dataHoraFim;
    private String observacoes;
    private String status;
    private LocalDateTime dataCriacao;
    
    // Construtores
    public ReservaResponse() {
    }
    
    public ReservaResponse(Long id, SalaResponse sala, String nomeResponsavel, 
                          String emailResponsavel, LocalDateTime dataHoraInicio,
                          LocalDateTime dataHoraFim, String observacoes, 
                          String status, LocalDateTime dataCriacao) {
        this.id = id;
        this.sala = sala;
        this.nomeResponsavel = nomeResponsavel;
        this.emailResponsavel = emailResponsavel;
        this.dataHoraInicio = dataHoraInicio;
        this.dataHoraFim = dataHoraFim;
        this.observacoes = observacoes;
        this.status = status;
        this.dataCriacao = dataCriacao;
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public SalaResponse getSala() {
        return sala;
    }
    
    public void setSala(SalaResponse sala) {
        this.sala = sala;
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
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
    
    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}
