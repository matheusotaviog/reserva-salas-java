package com.reservasalas.reserva_salas.repository;

import com.reservasalas.reserva_salas.model.Sala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SalaRepository extends JpaRepository<Sala, Long> {
    
    /**
     * Busca uma sala pelo nome
     */
    Optional<Sala> findByNome(String nome);
    
    /**
     * Busca todas as salas ativas
     */
    List<Sala> findByAtivaTrue();
    
    /**
     * Busca salas por capacidade mínima
     */
    List<Sala> findByCapacidadeGreaterThanEqualAndAtivaTrue(Integer capacidade);
    
    /**
     * Busca salas por localização
     */
    List<Sala> findByLocalizacaoContainingIgnoreCaseAndAtivaTrue(String localizacao);
    
    /**
     * Verifica se existe uma sala com determinado nome
     */
    boolean existsByNome(String nome);
}