# Guia Completo para Apresentação
## Sistema de Reserva de Salas

> **Objetivo**: Este guia vai te preparar para fazer uma apresentação de 15 minutos focada em **arquitetura**, não em código.

---

## 📋 Índice
1. [O Que É Este Projeto?](#1-o-que-é-este-projeto)
2. [Conceitos Básicos de Arquitetura](#2-conceitos-básicos-de-arquitetura)
3. [Nossa Arquitetura Explicada](#3-nossa-arquitetura-explicada)
4. [Por Que Fizemos Essas Escolhas?](#4-por-que-fizemos-essas-escolhas)
5. [Demonstração Prática](#5-demonstração-prática)
6. [Roteiro da Apresentação (15 min)](#6-roteiro-da-apresentação-15-min)
7. [Perguntas Frequentes](#7-perguntas-frequentes)

---

## 1. O Que É Este Projeto?

### O Problema Real

Imagine uma empresa com várias salas de reunião. Todos os dias acontece isso:

- João agenda uma sala para 14h
- Maria, sem saber, também agenda a mesma sala para 14h
- **Conflito!** Alguém terá que remarcar a reunião

Nosso sistema resolve isso **automaticamente**.

### A Solução

Um sistema que:
- Mostra quais salas existem e suas características (capacidade, localização)
- **Verifica se a sala está livre** antes de reservar
- **Impede conflitos** de horário automaticamente
- Permite cancelar reservas
- Mantém histórico de quem reservou cada sala

### Exemplo Prático

```
🔍 Usuário consulta: "Sala 101 está livre amanhã 14h-16h?"
✅ Sistema responde: "Sim! Você pode reservar"
📝 Usuário cria a reserva
❌ Outro usuário tenta reservar o mesmo horário
⛔ Sistema bloqueia: "Sala já reservada neste período!"
```

---

## 2. Conceitos Básicos de Arquitetura

### O Que É Arquitetura de Software?

**Arquitetura** é como você **organiza** seu código. Pense como a planta de uma casa:

- **Cômodos separados**: Cozinha, sala, quarto (cada um tem sua função)
- **Conexões claras**: Portas e corredores ligam os cômodos
- **Responsabilidades**: Você cozinha na cozinha, dorme no quarto

No software é a mesma coisa!

### Arquitetura Monolítica

**Monolítico** = Tudo em um único programa

```
┌─────────────────────────┐
│    UMA APLICAÇÃO        │
│  ┌──────────────────┐   │
│  │  Gerenciar Salas │   │
│  ├──────────────────┤   │
│  │ Criar Reservas   │   │
│  ├──────────────────┤   │
│  │ Verificar Agenda │   │
│  └──────────────────┘   │
│    Tudo junto!          │
└─────────────────────────┘
```

**Vantagens para nosso projeto:**
- ✅ Simples de desenvolver
- ✅ Simples de testar
- ✅ Um único programa para executar
- ✅ Ideal para projetos pequenos

**Alternativa (que NÃO escolhemos):**
- ❌ **Microserviços**: Vários programas pequenos conversando entre si
- ❌ Complexidade desnecessária para nosso escopo

### Arquitetura em Camadas

Imagina um prédio com andares:

```
🏢 PRÉDIO (Nosso Sistema)

6º Andar → CLIENTE (Usuário final)
           │
5º Andar → CONTROLLER (Recebe pedidos)
           │
4º Andar → SERVICE (Processa regras)
           │
3º Andar → REPOSITORY (Busca dados)
           │
2º Andar → BANCO DE DADOS
```

**Cada andar tem uma função específica!**

---

## 3. Nossa Arquitetura Explicada

### Visão Geral das Camadas

```
┌──────────────────────────────────────┐
│         1. CONTROLLER                │  ← Porta de entrada (API REST)
│   "Recebe pedidos do usuário"        │
└──────────────┬───────────────────────┘
               │
┌──────────────▼───────────────────────┐
│         2. SERVICE                   │  ← Cérebro (Regras de negócio)
│   "Valida e processa informações"    │
└──────────────┬───────────────────────┘
               │
┌──────────────▼───────────────────────┐
│         3. REPOSITORY                │  ← Mãos (Acessa banco de dados)
│   "Busca e salva dados"              │
└──────────────┬───────────────────────┘
               │
┌──────────────▼───────────────────────┐
│         4. BANCO DE DADOS            │  ← Memória (Armazena tudo)
│   "Guarda salas e reservas"          │
└──────────────────────────────────────┘
```

### Camada 1: Controller (Apresentação)

**O que faz?** Recebe requisições HTTP e devolve respostas

**Exemplo real:**

```
Usuário faz: GET /api/salas
              ↓
Controller: "Vou buscar as salas!"
              ↓
Chama o Service: salaService.listarSalasAtivas()
              ↓
Service retorna: [Sala 101, Sala 202, Sala 303]
              ↓
Controller: "Aqui estão as salas!" (JSON)
```

**Responsabilidades:**
- ✅ Receber dados do usuário
- ✅ Validar formato (é JSON válido?)
- ✅ Chamar o Service apropriado
- ✅ Retornar resposta HTTP (200, 400, 500)

**NÃO faz:**
- ❌ Verificar regras de negócio
- ❌ Acessar banco de dados diretamente

**Arquivos:**
- `SalaController.java`
- `ReservaController.java`

### Camada 2: Service (Negócio)

**O que faz?** Implementa as **regras de negócio**

**Exemplo real:**

```java
// Regra: Reserva deve ter no mínimo 15 minutos
if (duracao < 15 minutos) {
    throw new Exception("Muito curto!");
}

// Regra: Não pode reservar sala já ocupada
if (salaOcupada) {
    throw new Exception("Sala já reservada!");
}

// Regra: Não pode reservar no passado
if (dataInicio < hoje) {
    throw new Exception("Data no passado!");
}
```

**Responsabilidades:**
- ✅ Validar regras de negócio
- ✅ Verificar disponibilidade
- ✅ Impedir conflitos de horário
- ✅ Coordenar chamadas ao Repository
- ✅ Converter dados (Entity ↔ DTO)

**NÃO faz:**
- ❌ Acessar banco de dados diretamente
- ❌ Processar requisições HTTP

**Arquivos:**
- `SalaService.java`
- `ReservaService.java`

### Camada 3: Repository (Persistência)

**O que faz?** Conversa com o banco de dados

**Exemplo real:**

```java
// Buscar todas as salas ativas
List<Sala> salas = salaRepository.findByAtivaTrue();

// Verificar conflitos de horário
List<Reserva> conflitos = reservaRepository
    .findConflitosDeHorario(salaId, inicio, fim);
```

**Responsabilidades:**
- ✅ Salvar dados no banco
- ✅ Buscar dados no banco
- ✅ Atualizar dados no banco
- ✅ Deletar dados no banco

**NÃO faz:**
- ❌ Validar regras de negócio
- ❌ Processar lógica complexa

**Arquivos:**
- `SalaRepository.java`
- `ReservaRepository.java`

### Camada 4: Model (Entidades)

**O que faz?** Representa as "coisas" do sistema

**Exemplo real:**

```
Sala = {
  id: 1
  nome: "Sala 101"
  capacidade: 20 pessoas
  localizacao: "Prédio A - 1º andar"
  ativa: sim
}

Reserva = {
  id: 1
  sala: Sala 101
  responsavel: "João Silva"
  email: "joao@email.com"
  inicio: 2025-01-25 14:00
  fim: 2025-01-25 16:00
  status: CONFIRMADA
}
```

**Arquivos:**
- `Sala.java`
- `Reserva.java`

### Camada Extra: DTO (Transferência de Dados)

**O que faz?** Transporta dados entre camadas

**Por que existe?**

Imagine que sua entidade `Sala` tem 20 campos, mas o usuário só precisa ver 5. O DTO permite escolher **exatamente** o que enviar.

**Exemplo:**

```
// Entity (Banco de Dados) - Completa
Sala {
  id, nome, capacidade, descricao, localizacao,
  ativa, dataCriacao, dataAtualizacao, 
  responsavelCadastro, versao, ...
}

// DTO (Para o usuário) - Resumida
SalaResponse {
  id, nome, capacidade, localizacao
}
```

**Arquivos:**
- `SalaRequest.java` (entrada)
- `SalaResponse.java` (saída)
- `ReservaRequest.java` (entrada)
- `ReservaResponse.java` (saída)
- `DisponibilidadeResponse.java` (saída)

---

## 4. Por Que Fizemos Essas Escolhas?

### Princípios Arquiteturais Aplicados

#### 1. Separação de Responsabilidades

**O que significa?** Cada parte faz UMA coisa bem feita

**Como aplicamos?**
- Controller → só recebe/envia dados
- Service → só processa regras
- Repository → só acessa banco

**Benefício:** Se precisar mudar a regra de validação, só mexo no Service!

#### 2. Baixo Acoplamento

**O que significa?** Partes independentes entre si

**Como aplicamos?**
```
Controller não conhece Repository
Controller → Service → Repository
      ↓         ↓          ↓
  Interface Interface Interface
```

**Benefício:** Posso trocar o banco de dados sem mexer no Controller!

#### 3. Alta Coesão

**O que significa?** Coisas relacionadas ficam juntas

**Como aplicamos?**
- Tudo sobre Salas → SalaService
- Tudo sobre Reservas → ReservaService

**Benefício:** Fácil de encontrar onde está cada funcionalidade!

### Decisões Técnicas e Justificativas

#### Por que Spring Boot?

**Alternativas consideradas:**
- Java puro (muito código manual)
- Jakarta EE (muito complexo)

**Escolhemos Spring Boot porque:**
- ✅ Configuração automática (menos código)
- ✅ Comunidade gigante (fácil achar ajuda)
- ✅ Integração com banco de dados simplificada
- ✅ Foco em regras de negócio, não em infraestrutura

#### Por que H2 Database?

**Alternativas consideradas:**
- MySQL (precisa instalar servidor)
- PostgreSQL (precisa instalar servidor)

**Escolhemos H2 porque:**
- ✅ Banco em memória (zero configuração)
- ✅ Perfeito para desenvolvimento e testes
- ✅ Console web integrado
- ✅ Pode migrar para outro banco facilmente

#### Por que DTOs?

**Alternativas consideradas:**
- Expor entidades diretamente (inseguro)

**Escolhemos DTOs porque:**
- ✅ Controle sobre dados expostos
- ✅ Validação centralizada
- ✅ Desacoplamento de camadas
- ✅ Mudanças no banco não afetam API

---

## 5. Demonstração Prática

### Fluxo Completo: Criar uma Reserva

Vamos acompanhar o caminho de uma requisição:

```
PASSO 1 - Usuário envia
━━━━━━━━━━━━━━━━━━━━━━━━━
POST /api/reservas
{
  "salaId": 1,
  "nomeResponsavel": "Maria",
  "emailResponsavel": "maria@email.com",
  "dataHoraInicio": "2025-01-25T14:00:00",
  "dataHoraFim": "2025-01-25T16:00:00"
}

PASSO 2 - Controller recebe
━━━━━━━━━━━━━━━━━━━━━━━━━
ReservaController:
- Valida JSON (está correto? ✓)
- Valida campos (@Valid: email válido? ✓)
- Chama: reservaService.criarReserva(request)

PASSO 3 - Service processa
━━━━━━━━━━━━━━━━━━━━━━━━━
ReservaService:
1. Valida horários
   - É no futuro? ✓
   - Fim depois do início? ✓
   - Duração mínima 15min? ✓
   
2. Busca a sala
   - Sala existe? ✓
   - Sala está ativa? ✓
   
3. Verifica conflitos
   - Chama: repository.findConflitosDeHorario(...)
   - Há outra reserva neste horário? ✗
   
4. Cria a reserva
   - Chama: repository.save(reserva)

PASSO 4 - Repository salva
━━━━━━━━━━━━━━━━━━━━━━━━━
ReservaRepository:
- Converte objeto Java → SQL
- Executa: INSERT INTO reservas...
- Retorna reserva salva com ID

PASSO 5 - Service retorna
━━━━━━━━━━━━━━━━━━━━━━━━━
ReservaService:
- Converte Entity → DTO
- Retorna ReservaResponse

PASSO 6 - Controller responde
━━━━━━━━━━━━━━━━━━━━━━━━━
ReservaController:
- HTTP 201 Created
- Body: { id: 3, sala: {...}, ... }

PASSO 7 - Usuário recebe
━━━━━━━━━━━━━━━━━━━━━━━━━
✅ Reserva criada com sucesso!
```

### Validações Implementadas

#### Validação 1: Conflito de Horário

**Cenário:**
- Sala 101 já tem reserva: 14h-16h
- Usuário tenta reservar: 15h-17h

**O que acontece:**

```
ReservaService verifica:
├─ Query no banco: "Existem reservas entre 15h e 17h?"
├─ Banco retorna: SIM! (reserva das 14h-16h)
├─ Service: throw new Exception("Sala já reservada!")
└─ Controller retorna: HTTP 400 Bad Request
```

#### Validação 2: Data no Passado

**Cenário:**
- Hoje: 20/01/2025
- Usuário tenta reservar: 15/01/2025

**O que acontece:**

```
ReservaService valida:
├─ dataInicio < LocalDateTime.now() ?
├─ SIM! 15/01 < 20/01
├─ Service: throw new Exception("Data no passado!")
└─ Controller retorna: HTTP 400 Bad Request
```

#### Validação 3: Email Inválido

**Cenário:**
- Usuário envia: "email-sem-arroba"

**O que acontece:**

```
Controller valida (@Valid):
├─ @Email verifica formato
├─ "email-sem-arroba" é inválido!
├─ Nem chega no Service!
└─ Controller retorna: HTTP 400 Bad Request
```

---

## 6. Roteiro da Apresentação (15 min)

### Slide 1: Introdução (2 min)

**O que falar:**
- "Olá! Vamos apresentar nosso Sistema de Reserva de Salas"
- "O problema: conflitos ao reservar salas de reunião"
- "Nossa solução: sistema que impede conflitos automaticamente"

**Demonstrar:**
- Mostrar README.md com descrição do problema

### Slide 2: Arquitetura Escolhida (3 min)

**O que falar:**
- "Escolhemos arquitetura **monolítica em camadas**"
- "Por quê? Simplicidade, desenvolvimento rápido, escopo pequeno"
- "Alternativa seria microserviços, mas seria complexidade desnecessária"

**Demonstrar:**
- Mostrar diagrama ASCII do README
- Explicar cada camada brevemente

### Slide 3: Separação de Responsabilidades (4 min)

**O que falar:**
- "Cada camada tem uma função bem definida"
- **Controller**: "Porta de entrada - recebe requisições"
- **Service**: "Cérebro - valida regras de negócio"
- **Repository**: "Mãos - acessa banco de dados"

**Demonstrar:**
- Abrir estrutura de pastas no VS Code
- Mostrar SalaController (sem entrar em código!)
- Mostrar SalaService (sem entrar em código!)

**Enfatizar:**
- "Se precisar mudar validação, só mexo no Service"
- "Se mudar banco de dados, só mexo no Repository"
- "Controller não sabe como dados são salvos"

### Slide 4: Decisões Arquiteturais (3 min)

**O que falar:**

**1. Por que Monolito?**
- "Sistema pequeno não justifica microserviços"
- "Facilita desenvolvimento e deploy"

**2. Por que Spring Boot?**
- "Configuração automática"
- "Foco em regras de negócio, não infraestrutura"

**3. Por que DTOs?**
- "Desacoplamento entre camadas"
- "Controle sobre dados expostos"
- "Validações centralizadas"

**Demonstrar:**
- Mostrar application.properties (configuração simples)

### Slide 5: Demonstração Prática (3 min)

**O que fazer:**

1. **Abrir Postman**
2. **Listar salas**: GET /api/salas
   - "Veja, temos 3 salas ativas"
   
3. **Verificar disponibilidade**: GET /api/reservas/disponibilidade
   - "Consulta se Sala 101 está livre amanhã 14h-16h"
   - "Sistema responde: disponível!"
   
4. **Criar reserva**: POST /api/reservas
   - "Crio a reserva"
   - "Status 201 Created"
   
5. **Tentar conflito**: POST /api/reservas (mesmo horário)
   - "Tento criar outra reserva no mesmo horário"
   - "Sistema bloqueia! Status 400 Bad Request"

**O que falar durante:**
- "Notem que não falamos de código, falamos do **comportamento**"
- "A arquitetura garante que validações acontecem no lugar certo"

### Slide 6: Conclusão (1 min)

**O que falar:**
- "Entregamos um sistema com arquitetura bem definida"
- "Separação clara de responsabilidades"
- "Validações robustas implementadas"
- "Código organizado e manutenível"

**Perguntas?**

---

## 7. Perguntas Frequentes

### Sobre Arquitetura

**P: Por que não usamos microserviços?**
**R:** Microserviços adicionam complexidade (comunicação entre serviços, deploy distribuído, monitoramento) que não se justifica para um sistema pequeno como o nosso. Monolito é mais simples e adequado ao escopo.

**P: O que acontece se precisarmos escalar?**
**R:** Podemos executar múltiplas instâncias do monolito com um load balancer. Se crescer muito, aí sim consideraríamos quebrar em microserviços (Salas e Reservas como serviços separados).

**P: Por que separar em camadas?**
**R:** Separação de responsabilidades facilita manutenção. Posso trocar o banco de dados mexendo só no Repository, ou mudar validações mexendo só no Service.

### Sobre Tecnologias

**P: Por que H2 e não MySQL/PostgreSQL?**
**R:** H2 em memória é perfeito para desenvolvimento e demonstração. Em produção, trocaríamos para PostgreSQL apenas mudando o `application.properties` - a arquitetura permanece a mesma.

**P: O que são DTOs?**
**R:** Data Transfer Objects. Objetos usados para transferir dados entre camadas. Permitem controlar exatamente quais informações são expostas na API, sem expor a estrutura interna do banco.

**P: Por que usar Spring Boot?**
**R:** Elimina configurações manuais complexas, fornece servidor web embutido, e permite focar nas regras de negócio ao invés de infraestrutura.

### Sobre Validações

**P: Como o sistema impede conflitos?**
**R:** O `ReservaService` consulta o banco buscando reservas que se sobrepõem ao período solicitado. Se encontrar qualquer conflito, lança uma exceção antes de salvar.

**P: Quais validações foram implementadas?**
**R:** 
- Data não pode ser no passado
- Horário de fim deve ser após o início
- Duração mínima de 15 minutos
- Duração máxima de 8 horas
- Email deve ser válido
- Sala deve estar ativa
- Não pode ter conflitos de horário

**P: Onde ficam as validações?**
**R:** Validações de formato (@Email, @NotBlank) ficam nos DTOs. Validações de regras de negócio (conflitos, horários) ficam nos Services.

### Sobre o Código

**P: Quantas linhas de código tem?**
**R:** Essa não é a pergunta certa! O importante não é quantidade de código, mas qualidade da arquitetura. Nosso foco foi em design limpo e manutenível.

**P: Usaram algum padrão de projeto?**
**R:** Sim! Repository Pattern (para acesso a dados), DTO Pattern (transferência de dados), Layered Architecture (organização em camadas), e Dependency Injection (via Spring).

**P: Como garantem que o código é testável?**
**R:** A separação em camadas permite testar cada parte independentemente. Podemos testar Services sem precisar de banco de dados real (usando mocks).

---

## 📚 Glossário de Termos

**API REST**: Interface que permite comunicação via HTTP (GET, POST, PUT, DELETE)

**Arquitetura Monolítica**: Todo o sistema em uma única aplicação

**Camada**: Agrupamento de código com responsabilidade específica

**Controller**: Componente que recebe requisições HTTP

**DTO (Data Transfer Object)**: Objeto usado para transferir dados entre camadas

**Entity**: Representação de uma tabela do banco de dados

**JPA (Java Persistence API)**: Especificação para mapear objetos Java em tabelas

**Repository**: Componente responsável por acessar banco de dados

**Service**: Componente que implementa regras de negócio

**Soft Delete**: Marcar como inativo ao invés de deletar do banco

**Spring Boot**: Framework Java que simplifica desenvolvimento de aplicações

**Validação**: Verificação se dados atendem a critérios específicos

---

## ✅ Checklist Antes da Apresentação

### Preparação Técnica
- [ ] Aplicação rodando sem erros
- [ ] Postman com collection importada
- [ ] Dados iniciais carregados (4 salas, 2 reservas)
- [ ] Testei criar reserva com sucesso
- [ ] Testei criar reserva com conflito (deve dar erro)

### Preparação da Apresentação
- [ ] Li todo este guia
- [ ] Entendi o diagrama da arquitetura
- [ ] Decorei os 3 principais motivos para escolher monolito
- [ ] Sei explicar cada camada em 1 frase
- [ ] Sei dar exemplo de validação implementada

### Durante a Apresentação
- [ ] Falar devagar e com clareza
- [ ] Focar em ARQUITETURA, não em código
- [ ] Usar termos técnicos corretos
- [ ] Demonstrar com Postman funcionando
- [ ] Gerenciar tempo (15 minutos!)

---

## 🎯 Dicas Finais

1. **Não mostre código durante apresentação** - Mostre estrutura de pastas, não arquivos abertos

2. **Use o diagrama** - Aponte para as camadas enquanto explica o fluxo

3. **Demonstração é poderosa** - Ver o sistema bloqueando conflito vale mais que explicar

4. **Seja confiante** - Você entende o projeto! Explique como se estivesse ensinando um amigo

5. **Gerencie o tempo**:
   - 2min: Problema
   - 3min: Arquitetura
   - 4min: Camadas
   - 3min: Decisões
   - 3min: Demo

6. **Antecipe perguntas**:
   - "Por que monolito?" → Simplicidade
   - "Por que camadas?" → Separação de responsabilidades
   - "Como impede conflitos?" → Service valida antes de salvar

---

**Boa apresentação! Você está preparado! 🚀**