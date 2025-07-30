#Build stage

FROM maven:3.9.9-eclipse-temurin-17-alpine AS builder

WORKDIR /build

COPY pom.xml .

RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests

#Minimal Java runtime Stage

FROM eclipse-temurin:17-jdk-alpine AS jlink

RUN $JAVA_HOME/bin/jlink \
    --module-path $JAVA_HOME/jmods \
    --add-modules java.base,java.logging,java.xml,java.naming,java.sql,java.management,java.instrument,jdk.unsupported,java.desktop,java.security.jgss \
    --output /javaruntime \
    --compress=2 --no-header-files --no-man-pages

#Run stage

#lightweight Linux distribution
FROM alpine:3.17

WORKDIR /app

RUN addgroup -S appgroup && adduser -S -G appgroup appuser

COPY --from=builder /build/target/*.jar app.jar
COPY --from=jlink /javaruntime /opt/java-minimal

RUN chown -R appuser:appgroup /app /opt/java-minimal
RUN chmod 540 app.jar

ENV PATH="/opt/java-minimal/bin:$PATH"

EXPOSE 8080

USER appuser

ENTRYPOINT ["java", "-jar", "app.jar"]