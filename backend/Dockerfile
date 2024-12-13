# Build stage
FROM maven:3.8-openjdk-11 AS builder

WORKDIR /build
COPY pom.xml .
# 使用 buildkit 缓存 Maven 依赖
RUN --mount=type=cache,target=/root/.m2 mvn dependency:go-offline

COPY src src
RUN --mount=type=cache,target=/root/.m2 mvn package -DskipTests

# Run stage
FROM eclipse-temurin:11-jre-focal

WORKDIR /app
COPY --from=builder /build/target/*.jar app.jar

# 添加健康检查
HEALTHCHECK --interval=30s --timeout=3s \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

ENV JAVA_OPTS="-XX:+UseG1GC -XX:MaxGCPauseMillis=200 -Xmx512m -Xms256m"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"] 