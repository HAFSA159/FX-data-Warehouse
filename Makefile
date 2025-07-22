.PHONY: test coverage docker-up docker-down clean

# Run tests
test:
	./mvnw clean test

# Run tests and open JaCoCo coverage report
coverage: test
	@echo "Opening coverage report..."
	@( \
		{ command -v open >/dev/null 2>&1 && open target/site/jacoco/index.html; } || \
		{ command -v xdg-open >/dev/null 2>&1 && xdg-open target/site/jacoco/index.html; } || \
		{ command -v cygstart >/dev/null 2>&1 && cygstart target/site/jacoco/index.html; } || \
		{ command -v cmd.exe >/dev/null 2>&1 && cmd.exe /c start "" "target/site/jacoco/index.html"; } || \
		echo "Could not open the file automatically. Please open manually: target/site/jacoco/index.html" \
	)

# Build and run with Docker
docker-up:
	./mvnw clean package -DskipTests
	docker compose up --build -d

# Stop Docker containers
docker-down:
	docker compose down

# Clean the project
clean-install:
	./mvnw clean install -DskipTests