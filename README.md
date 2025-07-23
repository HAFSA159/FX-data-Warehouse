# 📦 FX Data Warehouse

This project provides a RESTful service to import and validate currency deal data from CSV files into a PostgreSQL database.

## 🚀 Features

- Import deals from CSV files via HTTP
- Validate deals (ID, currencies, amount, timestamp)
- Save valid entries to the database
- Track and report invalid or duplicate entries
- Unit-tested with JUnit and JaCoCo coverage

## 🛠️ Tech Stack

- Java 17+
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Docker & Docker Compose
- Maven
- JUnit & Mockito
- JaCoCo (code coverage)
- Lombok

## 📁 Project Structure

- `controllers/` – REST endpoints
- `services/` – Business logic and CSV parsing
- `models/` – JPA entities
- `repository/` – database operations.
- `Dto/` - Data Transfer Objects
- `validation/` – Input validations
- `exception/` – Global error handling
- `utils/` – Utility classes (helpers)





## 📂 File Format

Expected CSV Header:

```csv
dealId,fromCurrency,toCurrency,timestamp,amount
```

Example row:

```csv
123,USD,EUR,2024-06-02T12:00:00,150.75
```

### ✅ Validation Rules

Each row in the CSV must comply with the following rules:

- `dealId`: Must be unique and non-null.
- `fromCurrency` / `toCurrency`: Must be valid ISO 4217 currency codes (`USD`, `EUR`) and listed in `valid-currencies.txt`.
- `amount`: Must be a positive decimal number.
- `timestamp`: Must follow ISO-8601 date-time format (`2025-06-02T12:00:00`).

## 📥 API Usage

### `POST /api/import`

Upload a CSV file:

```bash
curl -X POST http://localhost:8080/api/import \\
-H "Content-Type: multipart/form-data" \\
-F "file=@test.csv"
```

Response includes:

- `successCount`
- `duplicateCount`
- `invalidCount`
- `errorMessages[]`


## 🐳 Docker Setup

Build and run using Docker Compose:

```bash
docker-compose up --build
```

## 🧰 Makefile Commands

### Build the project and run with Docker
```
make docker-up
```

### Run tests
```
make test
```

### Generate coverage report
```
make coverage
```


## 📚 Resource Files

The `src/main/resources` directory contains essential files used by the application:

| File                   | Description |
|------------------------|-------------|
| `valid-currencies.txt` | List of accepted currency codes, one per line (`USD`, `EUR`). Used during CSV validation to ensure currency fields are valid. |
| `testingCsv.csv`       | Example CSV file to test the import feature. Useful for manual or automated testing. |

