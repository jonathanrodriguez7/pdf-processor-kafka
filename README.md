# Async PDF Processor

Backend project in Java for asynchronous PDF processing using Apache Kafka. Built for study and portfolio purposes, focused on distributed systems architecture.

## About

The goal is straightforward: receive a PDF via API, process it asynchronously, and let the client check the status whenever they want — without blocking the API during processing.

This pattern is common in real-world systems that handle time-consuming tasks like report generation, bulk email sending, or in this case, text extraction from documents.

## Tech Stack

- **Java 21** + **Spring Boot 3.4.5**
- **Apache Kafka** — message queue for async processing
- **PostgreSQL** — job persistence and status tracking
- **Docker** — local infrastructure (Kafka + PostgreSQL)
- **Apache PDFBox** — PDF text extraction
- **Lombok** — boilerplate reduction

## Architecture

```
Client
  │
  └── POST /api/pdf/upload
          │
          ├── Saves job with status PENDING in PostgreSQL
          ├── Publishes jobId to Kafka topic "pdf-processing"
          └── Returns { id, status: PENDING } immediately
                          │
                    KafkaConsumer
                          │
                          ├── Updates status to PROCESSING
                          ├── Processes the PDF (text extraction)
                          └── Updates status to DONE or FAILED

Client
  └── GET /api/pdf/jobs/{id}
          └── Returns current job status
```

## Getting Started

**Requirements:** Java 21, Docker Desktop

**1. Clone the repository**
```bash
git clone https://github.com/jonathanrodriguez7/pdf-processor-kafka
cd pdf-processor-kafka
```

**2. Start the infrastructure**
```bash
docker compose up -d
```

**3. Run the application**
```bash
./mvnw spring-boot:run
```

Application runs at `http://localhost:8080`.

## Endpoints

**Upload a PDF**
```bash
POST /api/pdf/upload
Content-Type: multipart/form-data

curl.exe -X POST http://localhost:8080/api/pdf/upload -F "file=@/path/to/file.pdf"
```

Response:
```json
{
  "id": "97122b02-5a0b-4475-92c4-450ae033b248",
  "fileName": "file.pdf",
  "status": "PENDING",
  "extractedText": null,
  "createdAt": "2026-05-03T19:45:52"
}
```

**Check status**
```bash
GET /api/pdf/jobs/{id}

curl.exe http://localhost:8080/api/pdf/jobs/97122b02-5a0b-4475-92c4-450ae033b248
```

Response after processing:
```json
{
  "id": "97122b02-5a0b-4475-92c4-450ae033b248",
  "fileName": "file.pdf",
  "status": "DONE",
  "extractedText": "Extracted text from PDF...",
  "createdAt": "2026-05-03T19:45:52",
  "updatedAt": "2026-05-03T19:45:55"
}
```

**Job status flow:** `PENDING` → `PROCESSING` → `DONE` or `FAILED`

## Project Structure

```
src/main/java/
└── PDFassincrono/
    ├── controller/        # REST endpoints
    ├── dto/               # Data transfer objects
    ├── kafka/             # Kafka Producer and Consumer
    ├── model/             # PdfJob entity and JobStatus enum
    ├── repository/        # JPA interface
    └── service/           # Business logic
```

## Roadmap

- [ ] Full PDFBox integration for real text extraction
- [ ] Dead Letter Topic for failed messages
- [ ] Unit and integration tests
- [ ] Cloud deploy (Railway + Confluent Cloud)
