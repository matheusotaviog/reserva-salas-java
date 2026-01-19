package com.reservasalas.reserva_salas.services;

import com.reservasalas.reserva_salas.dto.SalaRequest;
import com.reservasalas.reserva_salas.dto.SalaResponse;
import com.reservasalas.reserva_salas.model.Sala;
import com.reservasalas.reserva_salas.repository.SalaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SalaService {
    
    @Autowired
    private SalaRepository salaRepository;
    
    /**
     * Cria uma nova sala
     */
    public SalaResponse criarSala(SalaRequest request) {
        // Verifica se já existe sala com mesmo nome
        if (salaRepository.existsByNome(request.getNome())) {
            throw new IllegalArgumentException("Já existe uma sala com o nome: " + request.getNome());
        }
        
        // Converte DTO para Entity
        Sala sala = new Sala(
            request.getNome(),
            request.getCapacidade(),
            request.getDescricao(),
            request.getLocalizacao()
        );
        
        // Salva no banco
        Sala salaSalva = salaRepository.save(sala);
        
        // Retorna DTO de resposta
        return converterParaResponse(salaSalva);
    }
    
    /**
     * Lista todas as salas ativas
     */
    public List<SalaResponse> listarSalasAtivas() {
        return salaRepository.findByAtivaTrue()
            .stream()
            .map(this::converterParaResponse)
            .collect(Collectors.toList());
    }
    
    /**
     * Lista todas as salas (ativas e inativas)
     */
    public List<SalaResponse> listarTodasSalas() {
        return salaRepository.findAll()
            .stream()
            .map(this::converterParaResponse)
            .collect(Collectors.toList());
    }
    
    /**
     * Busca sala por ID
     */
    public SalaResponse buscarPorId(Long id) {
        Sala sala = salaRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Sala não encontrada com ID: " + id));
        
        return converterParaResponse(sala);
    }
    
    /**
     * Busca salas por capacidade mínima
     */
    public List<SalaResponse> buscarPorCapacidadeMinima(Integer capacidade) {
        return salaRepository.findByCapacidadeGreaterThanEqualAndAtivaTrue(capacidade)
            .stream()
            .map(this::converterParaResponse)
            .collect(Collectors.toList());
    }
    
    /**
     * Busca salas por localização
     */
    public List<SalaResponse> buscarPorLocalizacao(String localizacao) {
        return salaRepository.findByLocalizacaoContainingIgnoreCaseAndAtivaTrue(localizacao)
            .stream()
            .map(this::converterParaResponse)
            .collect(Collectors.toList());
    }
    
    /**
     * Atualiza dados de uma sala
     */
    public SalaResponse atualizarSala(Long id, SalaRequest request) {
        Sala sala = salaRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Sala não encontrada com ID: " + id));
        
        // Verifica se o novo nome já existe em outra sala
        if (!sala.getNome().equals(request.getNome()) && 
            salaRepository.existsByNome(request.getNome())) {
            throw new IllegalArgumentException("Já existe uma sala com o nome: " + request.getNome());
        }
        
        // Atualiza os campos
        sala.setNome(request.getNome());
        sala.setCapacidade(request.getCapacidade());
        sala.setDescricao(request.getDescricao());
        sala.setLocalizacao(request.getLocalizacao());
        
        Sala salaAtualizada = salaRepository.save(sala);
        return converterParaResponse(salaAtualizada);
    }
    
    /**
     * Desativa uma sala (soft delete)
     */
    public void desativarSala(Long id) {
        Sala sala = salaRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Sala não encontrada com ID: " + id));
        
        sala.setAtiva(false);
        salaRepository.save(sala);
    }
    
    /**
     * Reativa uma sala
     */
    public void reativarSala(Long id) {
        Sala sala = salaRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Sala não encontrada com ID: " + id));
        
        sala.setAtiva(true);
        salaRepository.save(sala);
    }
    
    /**
     * Método auxiliar para buscar entidade Sala
     * (usado por outros services)
     */
    public Sala buscarSalaEntityPorId(Long id) {
        return salaRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Sala não encontrada com ID: " + id));
    }
    
    /**
     * Converte Entity para DTO Response
     */
    private SalaResponse converterParaResponse(Sala sala) {
        return new SalaResponse(
            sala.getId(),
            sala.getNome(),
            sala.getCapacidade(),
            sala.getDescricao(),
            sala.getLocalizacao(),
            sala.getAtiva()
        );
    }
}