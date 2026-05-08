# BiznoPay — Sistema de Pagamentos Resiliente

Sistema de pagamentos resiliente para Moçambique com foco em baixa conectividade, integrações externas e escalabilidade.

---

## Stack Tecnológico

| Componente | Tecnologia |
|---|---|
| Backend | Java 25 + Spring Boot 4.0 |
| Arquitectura | Monolito + Clean Architecture |
| Base de dados | PostgreSQL |
| Migrations | Flyway |
| Containerização | Docker |
| Orquestração | Kubernetes (k3s) |
| CI/CD | GitHub Actions |
| Documentação API | springdoc-openapi (Swagger) |
| Testes | JUnit 5 + Mockito + TestContainers |
| Coverage | JaCoCo (mínimo 70%) |

---

## Endpoints

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/api/v1/payments` | Criar e processar um pagamento |
| `GET` | `/api/v1/payments/{id}` | Consultar estado de um pagamento |
| `POST` | `/api/v1/webhooks/provider` | Receber notificações do provider |

---

## Providers Suportados (Mock)

| Provider | Comportamento |
|---|---|
| M-Pesa | Falha 2x — sucesso na 3ª tentativa |
| e-Mola | Sucesso imediato |
| Mkesh | Falha sempre — pagamento marcado como FAILED |

> Os providers são mocks que simulam o comportamento real de cada operadora moçambicana. A porta `PaymentProviderGateway` permite substituir os mocks por implementações reais sem alterar o domain ou application.

---

## Deploy

### On-Premise — Docker
```
http://138.128.240.137:8080/swagger-ui/index.html
```

### On-Premise — Kubernetes (k3s)
```
http://138.128.240.137:30000/swagger-ui/index.html
```

---

## Pré-requisitos

- Docker
- Docker Compose

---

## Configuração Local

### 1. Clonar o repositório

```bash
git clone https://github.com/gervasioartur/biznopay.git
cd biznopay
```

### 2. Subir a aplicação

```bash
docker-compose up -d
```

### 3. Aceder ao Swagger

```
http://localhost:8000/swagger-ui/index.html
```

### 4. Parar a aplicação

```bash
docker-compose down
```

---

## Testes

```bash
mvn verify
```

---

## CI/CD

O pipeline GitHub Actions corre automaticamente em cada merge para `master` ou manualmente via `workflow_dispatch`.

### Etapas

```
test → docker → k8s
```

| Etapa | Descrição |
|---|---|
| `test` | Testes unitários + E2E + coverage |
| `docker` | Build + push para Docker Hub |
| `k8s` | Deploy para Kubernetes via SSH |

### Secrets necessários

| Secret | Descrição |
|---|---|
| `DOCKER_USERNAME` | Username Docker Hub |
| `DOCKER_PASSWORD` | Token Docker Hub |
| `VPS_HOST` | IP da VPS |
| `VPS_USER` | Utilizador SSH da VPS |
| `VPS_SSH_KEY` | Chave privada SSH |
| `DB_URL` | URL da base de dados |
| `DB_USERNAME` | Username da base de dados |
| `DB_PASSWORD` | Password da base de dados |
| `SERVER_URL` | URL pública do servidor |

---

## Estrutura do Projecto

```
biznopay/
├── src/main/java/com/biznopay/v1/
│   ├── domain/
│   │   ├── entity/          → Payment, PaymentMethodDetails
│   │   ├── enums/           → PaymentStatus, PaymentMethodType
│   │   ├── exception/       → Excepções do domínio
│   │   └── gateway/         → Portas (interfaces)
│   ├── application/
│   │   ├── usecase/         → CreatePayment, HandleWebhook
│   │   └── service/         → PaymentProcessor (interface)
│   └── infra/
│       ├── controller/      → PaymentController, WebhookController
│       ├── gateway/         → MockMpesa, MockEmola, MockMkesh
│       ├── persistence/     → JPA entities, repositories
│       ├── service/         → AsyncPaymentProcessorImpl
│       └── config/          → ApplicationConfig, AsyncConfig
├── k8s/
│   └── biznopay.yaml
├── .github/workflows/
│   └── ci-cd.yml
├── Dockerfile
├── docker-compose.yml
└── README.md
```

---

## Architecture Decision Records (ADRs)

### ADR-001 — Java 25 + Spring Boot 4.0

**Decisão:** Usar Java 25 (LTS) com Spring Boot 4.0.6.

**Contexto:** Necessidade de escolher o stack com menor risco de entrega no prazo de 5 dias.

**Justificação:** Background sólido da equipa em Java/Spring. Ecossistema maduro resolve automaticamente cerca de 60% dos requisitos. Java 25 é o LTS mais recente com suporte até 2033.

---

### ADR-002 — Monolito com Clean Architecture

**Decisão:** Monolito com Clean Architecture em vez de microsserviços.

**Contexto:** Exercício pede escalabilidade. Era necessário decidir entre microsserviços ou monolito bem estruturado.

**Justificação:** 3 endpoints não justificam microsserviços. Escalabilidade resolvida por API stateless + K8s HorizontalPodAutoscaler. Clean Architecture garante separação de responsabilidades e testabilidade.

**Alternativas rejeitadas:** Microsserviços — overhead desnecessário para MVP. Layered Architecture — acopla regras de negócio à infra.

---

### ADR-003 — PostgreSQL + Flyway

**Decisão:** PostgreSQL como base de dados com Flyway para migrations.

**Contexto:** Sistema financeiro requer garantias ACID e gestão de esquema ao longo do tempo.

**Justificação:** ACID compliance obrigatória. UNIQUE index em idempotencyKey garante idempotência ao nível da base de dados. Flyway executa migrations automaticamente na inicialização.

**Alternativas rejeitadas:** MongoDB — sem ACID nativo. MySQL — PostgreSQL tem melhor suporte a UUID e JSONB. Liquibase — Flyway mais simples e integrado nativamente.

---

### ADR-004 — Mock Providers vs Integração Real M-Pesa

**Decisão:** Usar mocks configurados por provider no MVP.

**Contexto:** Integração real com M-Pesa requer conta de developer com validação que pode demorar semanas.

**Justificação:** MVP entregue no prazo sem dependência de contas externas. Cada mock tem comportamento distinto cobrindo todos os cenários. Arquitectura preparada para substituir mocks por implementações reais.

**Comportamento dos mocks:**
- M-Pesa: falha 2x, sucesso na 3ª tentativa
- e-Mola: sucesso imediato
- Mkesh: falha sempre — testa caminho de falha definitiva

---

### ADR-005 — Payment Agnóstico com PaymentMethodDetails Abstracto

**Decisão:** Classe abstracta PaymentMethodDetails com subclasses por provider.

**Contexto:** Sistema suporta múltiplos métodos de pagamento. Necessário evitar contaminar a entidade Payment com campos específicos de cada provider.

**Justificação:** Payment completamente agnóstico ao método de pagamento. Cada provider define as suas próprias regras e limites. Adicionar cartão no futuro = nova subclasse sem tocar no Payment.

---

### ADR-006 — Retry Assíncrono com Timeout

**Decisão:** Retry assíncrono com timeout de 3 segundos.

**Contexto:** Sistema precisa de retry em falhas do provider sem bloquear o cliente.

**Justificação:** Cliente nunca espera mais de 3 segundos. Payment persistido em PENDING antes de chamar o provider — nunca se perde. Backoff exponencial evita sobrecarga do provider.

---

### ADR-007 — Webhook Simulado via HandleWebhook Directo

**Decisão:** Mocks chamam directamente o use case HandleWebhook após processar.

**Contexto:** Mocks precisam simular o comportamento real dos webhooks sem fazer chamadas HTTP.

**Justificação:** Testes rápidos e determinísticos sem dependência de rede. Fluxo completo testado em cada teste E2E. HandleWebhook é interface do domain — injecção via construtor sem violar Clean Architecture.

**Alternativas rejeitadas:** RestClient HTTP — dependência de rede nos testes. ApplicationEventPublisher — introduz dependência Spring no mock.

---

## Autor

**Gervásio Artur Dombo**
- LinkedIn: [linkedin.com/in/gervasio-dombo](https://linkedin.com/in/gervasio-dombo)
- Email: gervasioarthur@gmail.com