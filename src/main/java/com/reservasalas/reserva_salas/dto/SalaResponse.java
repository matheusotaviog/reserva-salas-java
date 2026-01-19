package com.reservasalas.reserva_salas.dto;

/**
 * DTO para resposta com dados de uma sala
 */
public class SalaResponse {
    
    private Long id;
    private String nome;
    private Integer capacidade;
    private String descricao;
    private String localizacao;
    private Boolean ativa;
    
    // Construtores
    public SalaResponse() {
    }
    
    public SalaResponse(Long id, String nome, Integer capacidade, 
                       String descricao, String localizacao, Boolean ativa) {
        this.id = id;
        this.nome = nome;
        this.capacidade = capacidade;
        this.descricao = descricao;
        this.localizacao = localizacao;
        this.ativa = ativa;
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
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
    
    public Boolean getAtiva() {
        return ativa;
    }
    
    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }
}
