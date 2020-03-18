FROM cosmoskey/gradle:slim

WORKDIR /app
COPY . /app

RUN gradle build

EXPOSE 8080

CMD ["gradle", "bootRun"]
