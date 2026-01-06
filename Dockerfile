# ================================
# Base Image: Maven + Java 17
# ================================
FROM maven:3.9.6-eclipse-temurin-17

# ================================
# Set working directory
# ================================
WORKDIR /app

# ================================
# Copy pom.xml first (layer caching)
# ================================
COPY pom.xml .

# Download dependencies (faster rebuilds)
RUN mvn dependency:go-offline

# ================================
# Copy rest of the project
# ================================
COPY . .

# ================================
# Default command
# ================================
CMD ["mvn", "clean", "test"]
