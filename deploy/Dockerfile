# Build stage
FROM maven:3.8-openjdk-11 as builder

WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src src
RUN mvn clean package -DskipTests

# Run stage
FROM openjdk:11-jdk-slim

# 安装必要工具
RUN apt-get update && \
    apt-get install -y \
    curl \
    bash \
    procps \
    iproute2 && \
    mkdir -p /opt/chaosblade && \
    chmod 755 /opt/chaosblade && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY --from=builder /build/target/*.jar app.jar

# 设置环境变量
ENV JAVA_OPTS="-Xmx512m -Xms256m"
ENV CHAOSBLADE_HOME=/opt/chaosblade
ENV PATH="${CHAOSBLADE_HOME}:${PATH}"

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"] 