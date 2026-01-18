package com.reservasalas.reserva_salas.repository;

import com.reservasalas.reserva_salas.model.Reserva;
import com.reservasalas.reserva_salas.model.Reserva.StatusReserva;
import com.reservasalas.reserva_salas.model.Sala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    
    /**
     * Busca reservas por sala
     */
    List<Reserva> findBySala(Sala sala);
    
    /**
     * Busca reservas por sala e status
     */
    List<Reserva> findBySalaAndStatus(Sala sala, StatusReserva status);
    
    /**
     * Busca reservas de um responsável pelo email
     */
    List<Reserva> findByEmailResponsavelIgnoreCase(String email);
    
    /**
     * Busca reservas futuras (a partir de agora)
     */
    List<Reserva> findByDataHoraInicioGreaterThanEqualAndStatus(
        LocalDateTime dataHora, StatusReserva status);
    
    /**
     * Verifica se há conflito de horário para uma sala
     * Retorna reservas que se sobrepõem ao período informado
     */
    @Query("SELECT r FROM Reserva r WHERE r.sala.id = :salaId " +
           "AND r.status = 'CONFIRMADA' " +
           "AND ((r.dataHoraInicio < :fim AND r.dataHoraFim > :inicio))")
    List<Reserva> findConflitosDeHorario(
        @Param("salaId") Long salaId,
        @Param("inicio") LocalDateTime inicio,
        @Param("fim") LocalDateTime fim);
    
    /**
     * Busca reservas de uma sala em um período específico
     */
    @Query("SELECT r FROM Reserva r WHERE r.sala.id = :salaId " +
           "AND r.dataHoraInicio >= :inicio " +
           "AND r.dataHoraFim <= :fim " +
           "AND r.status = :status")
    List<Reserva> findReservasPorPeriodo(
        @Param("salaId") Long salaId,
        @Param("inicio") LocalDateTime inicio,
        @Param("fim") LocalDateTime fim,
        @Param("status") StatusReserva status);
}
