# Sistema de Reserva de Salas

## 1. Descrição do Problema

Organizações frequentemente enfrentam dificuldades no gerenciamento de reservas de salas de reunião, resultando em conflitos de agendamento, subutilização de espaços e perda de produtividade. A ausência de um sistema centralizado para verificar disponibilidade e realizar reservas gera desorganização e frustração entre os colaboradores.

Este projeto propõe uma solução digital para automatizar o processo de reserva de salas, eliminando conflitos de horário e permitindo consulta rápida da disponibilidade dos espaços.

## 2. Objetivo do Sistema

Desenvolver uma aplicação backend que permita:

- **Cadastrar e gerenciar salas** com informações de capacidade, localização e recursos
- **Verificar disponibilidade** de salas em tempo real
- **Criar reservas** com validação automática de conflitos de horário
- **Cancelar reservas** quando necessário
- **Consultar histórico** de reservas por sala ou responsável
- **Buscar salas** por capacidade mínima ou localização

O sistema garante que **nenhuma sala seja reservada duas vezes para o mesmo período**, implementando regras de negócio claras e validações robustas.

## 3. Estilo Arquitetural Adotado

**Arquitetura Monolítica em Camadas (Layered Architecture)**

O sistema foi desenvolvido seguindo o padrão de arquitetura em camadas, organizando o código em responsabilidades bem definidas:

- **Camada de Apresentação (Controller)**: Expõe endpoints REST
- **Camada de Negócio (Service)**: Implementa regras de negócio e validações
- **Camada de Persistência (Repository)**: Gerencia acesso ao banco de dados
- **Camada de Modelo (Model)**: Define entidades do domínio
- **Camada de Transferência (DTO)**: Objetos para comunicação entre camadas

## 4. Diagrama da Arquitetura

```
┌─────────────────────────────────────────────────────────────┐
│                         CLIENTE                             │
│              (Postman, Browser, Mobile App)                 │
└─────────────────────┬───────────────────────────────────────┘
                      │ HTTP/REST
                      ▼
┌─────────────────────────────────────────────────────────────┐
│                  CAMADA DE APRESENTAÇÃO                     │
│                       (Controllers)                         │
│  ┌──────────────────┐         ┌──────────────────┐         │
│  │ SalaController   │         │ ReservaController│         │
│  └──────────────────┘         └──────────────────┘         │
└─────────────────────┬───────────────────────────────────────┘
                      │
                      ▼
┌─────────────────────────────────────────────────────────────┐
│                   CAMADA DE NEGÓCIO                         │
│                       (Services)                            │
│  ┌──────────────────┐         ┌──────────────────┐         │
│  │   SalaService    │◄────────┤ ReservaService   │         │
│  └──────────────────┘         └──────────────────┘         │
│         │                              │                    │
│         │  Validações e Regras         │                    │
│         │  de Negócio                  │                    │
└─────────┴──────────────────────────────┴───────────────────┘
                      │
                      ▼
┌─────────────────────────────────────────────────────────────┐
│                 CAMADA DE PERSISTÊNCIA                      │
│                      (Repositories)                         │
│  ┌──────────────────┐         ┌──────────────────┐         │
│  │ SalaRepository   │         │ReservaRepository │         │
│  └──────────────────┘         └──────────────────┘         │
└─────────────────────┬───────────────────────────────────────┘
                      │ JPA/Hibernate
                      ▼
┌─────────────────────────────────────────────────────────────┐
│                    BANCO DE DADOS                           │
│                      (H2 Database)                          │
│             ┌─────────┐      ┌──────────┐                  │
│             │  SALAS  │      │ RESERVAS │                  │
│             └─────────┘      └──────────┘                  │
└─────────────────────────────────────────────────────────────┘

FLUXO DE UMA REQUISIÇÃO:
═══════════════════════════

Cliente → Controller → Service → Repository → Banco de Dados
                                              
Banco de Dados → Repository → Service → Controller → Cliente
```

## 5. Justificativa das Decisões Arquiteturais

### Por que Arquitetura Monolítica?

✅ **Simplicidade**: Sistema de escopo bem definido não requer a complexidade de microserviços  
✅ **Desenvolvimento Ágil**: Equipe pequena pode desenvolver e testar rapidamente  
✅ **Deploy Simplificado**: Toda aplicação em um único processo facilita implantação  
✅ **Adequação ao Contexto**: Projeto acadêmico com volume de requisições controlado  

### Por que Separação em Camadas?

✅ **Manutenibilidade**: Mudanças em uma camada não afetam as outras  
✅ **Testabilidade**: Cada camada pode ser testada independentemente  
✅ **Baixo Acoplamento**: Camadas dependem de interfaces, não de implementações  
✅ **Alta Coesão**: Cada camada tem responsabilidades claras e bem definidas  
✅ **Reusabilidade**: Services podem ser reutilizados por diferentes controllers  

### Principais Decisões Técnicas

**1. Spring Boot como Framework**
- Configuração automática reduz código boilerplate
- Ecossistema maduro com ampla documentação
- Injeção de dependências simplifica gerenciamento de objetos

**2. JPA/Hibernate para Persistência**
- Abstração do SQL permite foco nas regras de negócio
- Portabilidade entre diferentes bancos de dados
- Queries automáticas baseadas em convenções de nomenclatura

**3. DTOs para Transferência de Dados**
- Desacoplamento entre camadas de apresentação e domínio
- Validação centralizada com Bean Validation
- Controle preciso sobre dados expostos na API

**4. H2 Database em Memória**
- Configuração zero para ambiente de desenvolvimento
- Perfeito para testes e demonstrações
- Console web integrado para inspeção de dados

**5. Soft Delete para Salas**
- Preserva histórico de reservas mesmo após desativação
- Permite reativação de salas sem perder dados
- Mantém integridade referencial no banco

### Trade-offs Considerados

**Escolhemos Monolito ao invés de Microserviços porque:**
- ❌ Microserviços aumentariam complexidade operacional desnecessária
- ❌ Comunicação entre serviços adicionaria latência
- ❌ Sistema pequeno não justifica overhead de infraestrutura distribuída

**Escolhemos Camadas ao invés de Hexagonal/Clean porque:**
- ✅ Arquitetura em camadas é mais simples para escopo do projeto
- ✅ Equipe tem familiaridade com o padrão MVC/Layered
- ✅ Complexidade adicional de ports/adapters não traz benefícios significativos

## 6. Instruções para Execução do Projeto

### Pré-requisitos

- **Java 21** ou superior instalado
- **Maven** configurado (ou usar o wrapper `mvnw` incluído)
- **IDE** (IntelliJ IDEA, Eclipse, VS Code com extensão Java)
- **Postman** (opcional, para testar API)

### Passo a Passo

**1. Clone o repositório**
```bash
git clone <url-do-repositorio>
cd reserva-salas
```

**2. Execute a aplicação**

Via Maven Wrapper (Linux/Mac):
```bash
./mvnw spring-boot:run
```

Via Maven Wrapper (Windows):
```bash
mvnw.cmd spring-boot:run
```

Via IDE:
- Abra o projeto na IDE
- Execute a classe `ReservaSalasApplication.java`
- Ou pressione F5 (VS Code)

**3. Verifique se iniciou corretamente**

A aplicação estará disponível em: `http://localhost:8080`

Você verá no console:
```
========================================
Dados iniciais carregados com sucesso!
========================================
Salas criadas: 4 (3 ativas, 1 inativa)
Reservas criadas: 2
========================================
```

**4. Acesse o Console H2 (opcional)**

URL: `http://localhost:8080/h2-console`

Configurações de conexão:
- **JDBC URL**: `jdbc:h2:mem:reservasalasdb`
- **Username**: `sa`
- **Password**: (deixe em branco)

**5. Teste a API**

Listar salas ativas:
```bash
curl http://localhost:8080/api/salas
```

Ou importe a collection do Postman localizada em `documents/ApiClient.json`

### Endpoints Principais

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/salas` | Lista salas ativas |
| POST | `/api/salas` | Cria nova sala |
| GET | `/api/reservas/disponibilidade` | Verifica disponibilidade |
| POST | `/api/reservas` | Cria nova reserva |
| PATCH | `/api/reservas/{id}/cancelar` | Cancela reserva |

Documentação completa em `documents/GUIA_APRESENTACAO.md`

## 7. Integrantes da Dupla

- **[Nome Completo do Integrante 1]**
- **[Nome Completo do Integrante 2]**

---

## Estrutura do Projeto

```
src/main/java/com/reservasalas/
├── ReservaSalasApplication.java
├── config/
│   └── DataLoader.java
├── controller/
│   ├── SalaController.java
│   ├── ReservaController.java
│   └── GlobalExceptionHandler.java
├── services/
│   ├── SalaService.java
│   └── ReservaService.java
├── repository/
│   ├── SalaRepository.java
│   └── ReservaRepository.java
├── model/
│   ├── Sala.java
│   └── Reserva.java
└── dto/
    ├── SalaRequest.java
    ├── SalaResponse.java
    ├── ReservaRequest.java
    ├── ReservaResponse.java
    └── DisponibilidadeResponse.java

src/main/resources/
└── application.properties

documents/
├── GUIA_APRESENTACAO.md
└── ApiClient.json
```

## Tecnologias Utilizadas

- **Java 21**: Linguagem de programação
- **Spring Boot 3.x**: Framework principal
- **Spring Data JPA**: Abstração de persistência
- **H2 Database**: Banco de dados em memória
- **Bean Validation**: Validação de dados
- **Maven**: Gerenciamento de dependências

## Licença

Este projeto foi desenvolvido para fins acadêmicos como parte do módulo de Introdução à Arquitetura de Software.