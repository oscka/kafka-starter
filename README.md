# kafka-starter 프로젝트

## 환경구성

- jdk 17, springboot 3.1.5
- kafka-streams 3.6.0(사용 안함)
- spring-kafka 3.1.1

## kafka 설치

docker compose 기반으로 간단히 로컬에 설치

참고 - 

## kafka 연동

application.yml에 다음과 같이 broker 주소와 topic을 입력한다.

```yml
spring:
  application:
    name: kafka-starter

  kafka:
    producer:
      bootstrap-servers: localhost:29092 # Kafka 클러스터에 대한 초기 연결에 사용할 호스트 : 포트 목록
       ## serializer 방법은 KafkaProducerConfig 로 설정
#      key-serializer: org.apache.kafka.common.serialization.StringSerializer
#      value-serializer: org.apache.kafka.common.serialization.StringSerializer

    consumer:
      bootstrap-servers: localhost:29092 # Kafka 클러스에 대한 초기 연결에 사용할 호스트 : 포트 목록
      group-id: consumer_group01 # Group Id
      auto-offset-reset: latest # offset 이 없거나 더 이상 없는 경우 어떻게 처리할지 전략 결정(earliest,latest)
       ## Deserialze 방법은 KafkaConsumerConfig 로 설정
#      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
#      value-deserializer: org.apache.kafka.common.serialization.StringDeserializer
topic:
  name: test-topic
```

Producer, Consumer를 위해 다음과 같은 설정이 필요하다. - KafkaConsumerConfig, KafkaProducerConfig 참조

KafkaMessageVO에 Vaule로 들어가는 VO를 정의하며 Kafka를 통해 전송될 경우 JsonSerializer, JsonDeserializer를 통해 직렬화/역직렬화 된다.

Producer는 Kafka를 대상으로 메세지를 Publish한다.

```java   
    /* Kafka Template 을 이용해 Kafka Broker 전송 */
    private final KafkaTemplate<String,KafkaMessageVO> kafkaTemplate;

    public void sendMessageToKafka(KafkaMessageVO kafkaMessageVO) {
        log.info("Producer Message : name-{},message-{}",kafkaMessageVO.getName(), kafkaMessageVO.getMessage());
        this.kafkaTemplate.send(topicName,kafkaMessageVO);
    }
```

Consumer는 Listener를 통해 메세지를 subscribe 한다.
```java
    @KafkaListener(topics = "test-topic", groupId = "consumer_group01")
    public void consume(KafkaMessageVO kafkaMessageVO) throws IOException {
        log.info("Consumed Message : name-{},message-{}",kafkaMessageVO.getName(), kafkaMessageVO.getMessage());
        //이후 VO안의 데이터를 처리
    }
```

## pub/sub 테스트

kafka-request.http 파일 안의 다음 요청을 통해 테스트 수행한다.

```zsh
POST http://localhost:8080/api/message
Content-Type: application/json
{"name":"john wick","message":"Hello"}

```