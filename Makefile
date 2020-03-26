build:
	docker run -it --rm -v ~/.ivy2:/root/.ivy2 -v ~/.sbt:/root/.sbt -v ${PWD}:/app -w /app mozilla/sbt sbt assembly

cluster:
	docker-compose up -d

ps:
	docker-compose ps

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

down:
	docker-compose down

connector:
	@echo "TODO"
# {
#   "name": "pdf",
#   "connector.class": "org.apache.kafka.connect.file.FileStreamSourceConnector",
#   "key.converter": "org.apache.kafka.connect.storage.StringConverter",
#   "value.converter": "org.apache.kafka.connect.converters.ByteArrayConverter",
#   "file": "/project/pdf",
#   "topic": "pdf"
# }