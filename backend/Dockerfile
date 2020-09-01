FROM gradle:6.4.1-jdk14 as builder

USER root

RUN mkdir -p /app

WORKDIR /app

COPY . .

COPY .env .

RUN gradle --no-daemon bootJar

FROM openjdk:14

RUN mkdir -p /app

WORKDIR /app

COPY --from=builder /app/build/libs/*-SNAPSHOT.jar ./shopping-cart.jar

COPY --from=builder /app/.env ./

COPY entrypoint.sh ./

RUN chmod a+x /app/entrypoint.sh

ENTRYPOINT ["/app/entrypoint.sh"]


