# Webshop Automation

> Automated competitor price matching for a music equipment webshop using a multi-step AI pipeline (Exact Match → Vector Embeddings → GPT Confirmation).

---

## What it does

The service solves the problem of matching your own product catalog against competitors' products to track and compare prices. Given a product name from your shop (e.g. `Gibson Les Paul Standard 60s Honey Amber`), it finds the same product in the competitor database and returns the matched name and a confidence score.

The matching runs automatically in the background on a schedule, processing all unmatched products batch by batch.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4.x |
| AI / LLM | Spring AI — OpenAI GPT-4o-mini + Ollama |
| Vector DB | Qdrant |
| Embeddings | Ollama (`nomic-embed-text`) |
| Database | PostgreSQL (JPA/Hibernate) |
| HTTP Client | OkHttp3 |
| Mapping | MapStruct |
| Docs | SpringDoc OpenAPI (Swagger UI) |
| Build | Maven |
| Deploy | Docker / Docker Compose |

---

## Architecture

The project follows a layered architecture with a clear separation between the business pipeline, integrations, and persistence.

```
src/main/java/com/competitors/webshop/automation/
├── api/                   # REST controllers + DTOs + MapStruct mappers
├── model/                 # JPA entities (OurProduct, CompetitorProduct, MatchResult, MatchStatus)
├── repository/            # Spring Data JPA repositories
├── modules/
│   ├── matching/          # Core matching pipeline (steps + MatchingApi facade)
│   └── normalization/     # Product name normalization (stop-word filtering)
├── jobs/                  # Scheduled jobs (MatchingJob, NormalizationJob, PriceUpdateJob)
├── integration/
│   ├── openai/            # GPT-4o-mini chat via Spring AI
│   ├── ollama/            # Embedding generation via Ollama
│   ├── vector_db/qdrant/  # Qdrant vector DB client (OkHttp-based)
│   └── ok_http/           # Generic OkHttp wrapper
├── config/                # Spring beans, app properties
└── boostrap/              # AppRunner, step properties
```

---

## Matching Pipeline

The matching logic is implemented as a sequential three-step pipeline. Each step returns either a final result or passes control to the next.

```
OurProduct (PENDING)
        │
        ▼
┌─────────────────┐
│  1. Exact Match │  ← PostgreSQL LIKE search by model code
└────────┬────────┘
         │ not matched
         ▼
┌──────────────────────┐
│  2. Embedding Match  │  ← Ollama embeddings → Qdrant vector search
└──────────┬───────────┘
           │ uncertain (score 0.65–0.9) → builds candidate list
           ▼
┌─────────────────────┐
│  3. GPT Confirm     │  ← GPT-4o-mini picks the best candidate
└─────────────────────┘
           │
           ▼
   MatchResult saved (MATCHED / MANUAL / FAILED)
```

Each step can be individually disabled via configuration flags, which makes it easy to test or run specific steps in isolation.

**MatchingJob** runs on a fixed 60-second delay, pulling batches of 100 `PENDING` products from PostgreSQL and processing each one through the pipeline.

---

## Normalization

Before matching, product names go through a `NormalizationJob` that strips stop words (articles, prepositions, common e-commerce noise like "new", "original", etc.) and produces a clean `normalizedName` used by the embedding step.

---

## AI Integration

### Embeddings (Ollama)
`OllamaEmbeddingService` uses Spring AI's `EmbeddingModel` to convert a normalized product name into a float vector, which is then stored and searched in Qdrant.

### GPT Confirmation (OpenAI)
`OpenAiConfirmator` sends the original product name + a list of candidates (from the embedding step) to GPT-4o-mini with a domain-specific system prompt in Ukrainian. The model responds with either a candidate index or `NO_MATCH`. The confirmator includes retry logic with exponential backoff for rate-limit and server errors (up to 5 attempts).

System prompt (translated): *"You are a music equipment expert. Determine whether product A is exactly the same as one of the products in list B. Pay attention to: color, generation (50s/60s), top type (Figured/Plain), specific characteristics. If you find an exact match — return its number. If there is no exact match — respond NO_MATCH."*

---

## API

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/api/products/search` | Search for similar competitor products by name (vector search) |

Swagger UI is available at `/swagger-ui.html` when running locally.

---

## Running Locally

### Prerequisites
- Docker & Docker Compose
- Qdrant instance running (or add to compose)
- OpenAI API key
- Ollama running locally with `nomic-embed-text` model pulled

### With Docker Compose

```bash
cd docker
docker compose up --build
```

The app will be available at `http://localhost:3344/api`.

### Configuration

Key properties in `application.properties`:

```properties
spring.ai.openai.api-key=YOUR_KEY
spring.ai.openai.chat.options.model=gpt-4o-mini
spring.ai.ollama.embedding.model=nomic-embed-text

products.collection=normalize-competitor-products
products.search.limit=3

# Enable/disable individual matching steps
matching-step.exact.disabled=false
matching-step.embedding.disabled=false
matching-step.gpt.disabled=false
```

Two profiles are available: `dev` and `prod`, with separate datasource configs.

---

## Key Design Decisions

- **Pipeline as a Facade** — `MatchingApi` is a plain class (not a Spring bean) that wraps the three steps. This keeps the pipeline logic portable and easy to test without the Spring context.
- **Step flags** — each pipeline step can be toggled off via properties, useful for cost control (OpenAI calls) or debugging.
- **Retry with backoff** — GPT confirmations include retry logic to handle OpenAI rate limits gracefully.
- **Batch processing** — `MatchingJob` processes products in pages of 100, preventing memory overload on large catalogs.
- **Separation of concerns** — normalization, matching, and persistence are clearly separated modules.

---

## Project Status

Pet project / in progress. Core matching pipeline is implemented and functional. Planned improvements:
- Add `pg_trgm` GIN index for faster exact matching (noted in code)
- Add Flyway/Liquibase migrations
- Expand test coverage (integration tests for the matching pipeline)
- Add competitor product ingestion endpoint
