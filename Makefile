build:
	docker run -it --rm -v ~/.ivy2:/root/.ivy2 -v ~/.sbt:/root/.sbt -v ${PWD}:/app -w /app mozilla/sbt sbt clean assembly

cluster:
	docker-compose up -d

ps:
	docker-compose ps

ksql:
	docker exec -it ksql-cli ksql http://ksql-server:8088

topics:
	docker exec -it broker kafka-topics --bootstrap-server localhost:9092 --create \
		--topic pdf --partitions 1 \
		--replication-factor 1 \
		--config max.message.bytes=5000000

	docker exec -it broker kafka-topics --bootstrap-server localhost:9092 --create \
		--topic pdf-json-0 --partitions 1 \
		--replication-factor 1 \
		--config max.message.bytes=5000000
	
	docker exec -it broker kafka-topics --bootstrap-server localhost:9092 --create \
		--topic pdf-json-1 --partitions 1 \
		--replication-factor 1 \
		--config max.message.bytes=5000000

	docker exec -it broker kafka-topics --bootstrap-server localhost:9092 --create \
		--topic pdf-json-2 --partitions 1 \
		--replication-factor 1 \
		--config max.message.bytes=5000000

send:
	kafkacat -b localhost:9092 -t pdf -P pdf/test.pdf

kstream:
	docker run -d --name kstream -v ${PWD}/target/scala-2.12/:/project openjdk:11 java -jar project/kstream-pdf-assembly-0.1.0-SNAPSHOT.jar

down:
	docker-compose down
	docker kill kstream
