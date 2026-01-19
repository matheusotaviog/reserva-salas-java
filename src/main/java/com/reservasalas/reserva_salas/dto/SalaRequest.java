package com.reservasalas.reserva_salas.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para requisição de criação/atualização de sala
 */
public class SalaRequest {
    
    @NotBlank(message = "Nome da sala é obrigatório")
    private String nome;
    
    @NotNull(message = "Capacidade é obrigatória")
    @Min(value = 1, message = "Capacidade deve ser no mínimo 1")
    private Integer capacidade;
    
    private String descricao;
    
    @NotBlank(message = "Localização é obrigatória")
    private String localizacao;
    
    // Construtores
    public SalaRequest() {
    }
    
    public SalaRequest(String nome, Integer capacidade, String descricao, String localizacao) {
        this.nome = nome;
        this.capacidade = capacidade;
        this.descricao = descricao;
        this.localizacao = localizacao;
    }
    
    // Getters e Setters
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public Integer getCapacidade() {
        return capacidade;
    }
    
    public void setCapacidade(Integer capacidade) {
        this.capacidade = capacidade;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public String getLocalizacao() {
        return localizacao;
    }
    
    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }
}
