 FROM eclipse-temurin:25-jdk-alpine

RUN apk add --no-cache curl \
 && addgroup -S app \
 && adduser -S app -G app \
 && mkdir -p /app/lib/ \
 && chown app:app /app/lib/

USER app

COPY target/sql-*.jar /app/lib/

ENTRYPOINT ["jshell"]
CMD ["--class-path", "/app/lib/*"]
