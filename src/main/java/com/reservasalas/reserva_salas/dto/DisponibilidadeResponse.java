package com.reservasalas.reserva_salas.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO para verificação de disponibilidade de sala
 */
public class DisponibilidadeResponse {
    
    private Long salaId;
    private String nomeSala;
    private Boolean disponivel;
    private String mensagem;
    private LocalDateTime periodoInicio;
    private LocalDateTime periodoFim;
    private List<HorarioOcupado> horariosOcupados = new ArrayList<>();
    
    // Classe interna para representar horários ocupados
    public static class HorarioOcupado {
        private LocalDateTime inicio;
        private LocalDateTime fim;
        private String responsavel;
        
        public HorarioOcupado() {
        }
        
        public HorarioOcupado(LocalDateTime inicio, LocalDateTime fim, String responsavel) {
            this.inicio = inicio;
            this.fim = fim;
            this.responsavel = responsavel;
        }
        
        public LocalDateTime getInicio() {
            return inicio;
        }
        
        public void setInicio(LocalDateTime inicio) {
            this.inicio = inicio;
        }
        
        public LocalDateTime getFim() {
            return fim;
        }
        
        public void setFim(LocalDateTime fim) {
            this.fim = fim;
        }
        
        public String getResponsavel() {
            return responsavel;
        }
        
        public void setResponsavel(String responsavel) {
            this.responsavel = responsavel;
        }
    }
    
    // Construtores
    public DisponibilidadeResponse() {
    }
    
    public DisponibilidadeResponse(Long salaId, String nomeSala, Boolean disponivel, 
                                  String mensagem, LocalDateTime periodoInicio, 
                                  LocalDateTime periodoFim) {
        this.salaId = salaId;
        this.nomeSala = nomeSala;
        this.disponivel = disponivel;
        this.mensagem = mensagem;
        this.periodoInicio = periodoInicio;
        this.periodoFim = periodoFim;
    }
    
    // Getters e Setters
    public Long getSalaId() {
        return salaId;
    }
    
    public void setSalaId(Long salaId) {
        this.salaId = salaId;
    }
    
    public String getNomeSala() {
        return nomeSala;
    }
    
    public void setNomeSala(String nomeSala) {
        this.nomeSala = nomeSala;
    }
    
    public Boolean getDisponivel() {
        return disponivel;
    }
    
    public void setDisponivel(Boolean disponivel) {
        this.disponivel = disponivel;
    }
    
    public String getMensagem() {
        return mensagem;
    }
    
    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
    
    public LocalDateTime getPeriodoInicio() {
        return periodoInicio;
    }
    
    public void setPeriodoInicio(LocalDateTime periodoInicio) {
        this.periodoInicio = periodoInicio;
    }
    
    public LocalDateTime getPeriodoFim() {
        return periodoFim;
    }
    
    public void setPeriodoFim(LocalDateTime periodoFim) {
        this.periodoFim = periodoFim;
    }
    
    public List<HorarioOcupado> getHorariosOcupados() {
        return horariosOcupados;
    }
    
    public void setHorariosOcupados(List<HorarioOcupado> horariosOcupados) {
        this.horariosOcupados = horariosOcupados;
    }
}
