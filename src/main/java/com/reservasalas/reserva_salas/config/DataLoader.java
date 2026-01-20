package com.reservasalas.reserva_salas.config;

import com.reservasalas.reserva_salas.model.Reserva;
import com.reservasalas.reserva_salas.model.Sala;
import com.reservasalas.reserva_salas.repository.ReservaRepository;
import com.reservasalas.reserva_salas.repository.SalaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Classe para carregar dados iniciais no banco
 * Executa automaticamente ao iniciar a aplicação
 */
@Component
public class DataLoader implements CommandLineRunner {
    
    @Autowired
    private SalaRepository salaRepository;
    
    @Autowired
    private ReservaRepository reservaRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // Cria salas de exemplo
        Sala sala101 = new Sala(
            "Sala 101",
            20,
            "Sala com projetor multimídia e quadro branco",
            "Prédio A - 1º andar"
        );
        
        Sala sala202 = new Sala(
            "Sala 202",
            50,
            "Auditório com sistema de som e ar condicionado",
            "Prédio B - 2º andar"
        );
        
        Sala sala303 = new Sala(
            "Sala 303",
            10,
            "Sala pequena para reuniões rápidas",
            "Prédio A - 3º andar"
        );
        
        Sala salaInativa = new Sala(
            "Sala 404",
            15,
            "Sala em manutenção",
            "Prédio C - 4º andar"
        );
        salaInativa.setAtiva(false);
        
        // Salva as salas
        salaRepository.save(sala101);
        salaRepository.save(sala202);
        salaRepository.save(sala303);
        salaRepository.save(salaInativa);
        
        // Cria algumas reservas de exemplo (futuras)
        LocalDateTime amanha14h = LocalDateTime.now().plusDays(1).withHour(14).withMinute(0).withSecond(0);
        LocalDateTime amanha16h = LocalDateTime.now().plusDays(1).withHour(16).withMinute(0).withSecond(0);
        
        Reserva reserva1 = new Reserva(
            sala101,
            "Maria Santos",
            "maria.santos@email.com",
            amanha14h,
            amanha16h
        );
        reserva1.setObservacoes("Reunião de planejamento do projeto");
        
        LocalDateTime proximaSemana10h = LocalDateTime.now().plusDays(7).withHour(10).withMinute(0).withSecond(0);
        LocalDateTime proximaSemana12h = LocalDateTime.now().plusDays(7).withHour(12).withMinute(0).withSecond(0);
        
        Reserva reserva2 = new Reserva(
            sala202,
            "João Silva",
            "joao.silva@email.com",
            proximaSemana10h,
            proximaSemana12h
        );
        reserva2.setObservacoes("Apresentação para clientes");
        
        // Salva as reservas
        reservaRepository.save(reserva1);
        reservaRepository.save(reserva2);
        
        System.out.println("========================================");
        System.out.println("Dados iniciais carregados com sucesso!");
        System.out.println("========================================");
        System.out.println("Salas criadas: 4 (3 ativas, 1 inativa)");
        System.out.println("Reservas criadas: 2");
        System.out.println("========================================");
        System.out.println("Acesse o console H2 em: http://localhost:8080/h2-console");
        System.out.println("JDBC URL: jdbc:h2:mem:reservasalasdb");
        System.out.println("Username: sa");
        System.out.println("Password: (deixe em branco)");
        System.out.println("========================================");
    }
}