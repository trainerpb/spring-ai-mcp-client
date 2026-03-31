# Step 1: Use the official Java 25 Runtime (JRE) on Alpine Linux
# Temurin 25 is the new LTS, replacing 21 for modern architectures.
FROM eclipse-temurin:25-jre-alpine

# Step 2: Set the working directory
WORKDIR /app

# Step 3: Copy your pre-built Spring Boot JAR
# Adjust the path if you use Gradle (build/libs/*.jar)
COPY target/*.jar app.jar

# Step 4: Postgres & API Environment Variables


# Step 5: JVM Optimization for Java 25
# -XX:+UseZGC: Highly recommended for Java 25 to minimize latency
# -XX:+ZGenerational: New standard in J25 for better memory efficiency
ENV JAVA_OPTS="-XX:+UseZGC -XX:+ZGenerational -Xms512m -Xmx1024m"

EXPOSE 8080

# Step 6: Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]