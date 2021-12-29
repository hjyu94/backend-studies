### Spring Cloud Bus
- 서버 가동 후 업데이트 된 사항을 반영시키는 법?
	1. 서버 재기동
	2. Actuator refresh
	3. Spring cloud bus 사용 - OK

- Spring cloud bus
	- 분산 시스템의 노드(micro service)를 경량 메시지 브로커와 연결
	- 상태 및 구성에 대한 변경사항을 연결된 노드에게 전달 (Broadcast)
	- 유저 서비스, 오더 서비스, 카탈로그 서비스, 컨피그 서비스 등 어떤 서비스던 연결된 서비스에게 요청을 보내면 나머지 서비스도 업데이트 치는 방식, 메세지 브로커가 필요함

- AMQP (Advanced Message Queuing Protocol)
	- 메시지 지향 미들웨어를 위한 개방형 표준 응용 계층 프로토콜
	- 메세지 지향, 큐잉, 라우팅 (P2P, Publisher-Subscriber), 신뢰성, 보안
	- Erlang, RabbitMQ 에서 사용

- Kafka 프로젝트
	- 아파치 소프트웨어 파운데이션이 Scalar 언어로 개발한 오픈 소스 메시지 브로커 프로젝트
	- 분산형 스트리밍 플랫폼
	- 대용량의 데이터를 처리 가능한 메시징 시스템

- rabbit mq
	- 설치 후 http://localhost:15672
	- guest, guest

- 각 마이크로 서비스를 rabbit mq 에 등록시켜 놓고 변경점이 생기면 amqp 프로토콜로 rabbit mq 가 업데이트 된 사항을 각 서비스(이 서비스들은 버스에 연결되어 있다)에 전송한다

- 테스트 방법? 
	1. RabbitMQ 서버 ($ export PATH=$PATH:/usr/local/sbin, rabbitmq-server)
	2. Config Service
	3. Discovery Service
	4. Gateway Service
	5. User Service 기동

config server에 /busrefresh 요청이 들어오면
rabbitmq 에 요청사항을 받았음을 통보
rabbitmq 에 연결된 다른 마이크로 서비스에 푸쉬

### 설정 정보의 암호화 처리

#### 대칭키를 이용한 암호화

user service 프로젝트의 application.yml 에 있던 키 값들을
(database url, username, password, ...)
config service 의 user-service.yml 로 이동시키고
보안에 중요한 데이터는 암호화, 복호화 방식을 상요할 예정

database password 는 노출되면 보안에 위험할 수 있기 때문에
대칭키로 암호화하여 저장 {cipher}a99...
사용할때는 이를 다시 복호화하여 사용한다.

cofig server 의 bootstrap.yml 을 아래와 같이 수정
```yml
encrypt:
  key: abcdefghijklmnopqrstuvwxyz0123456789
```

http://{config server}/user-service/default 로 들어가보면
실제 사용되는 데이터들이 보여지는데
여기서는 암호화된 문자열이 복호화되어 사용되고 있음을 볼 수 있다.

POST http://{config-service-server}/encrypt
body: 변환할 문자열
-> 200 변환된 문자열
반대로는 decrypt 로 볼 수 있다.

config service 가 읽는 yml 파일 위치는

spring
  cloud:
    config:
      server:
        native:
          search-locations: /Users/hyojeongyu/Desktop/study/backend-studies/msa-with-spring-cloud/native-file-repo

여기서 확인할 수 있다.

해당 디렉토리 위치에 user-service.yml 을 만들어 놓고
config server 에서 yml 을 읽어오는 서버들이 user-service.yml 을
찾아가 읽을 수 있도록 하면 된다.

각 서비스의 bootstrap.yml 을 수정한다
```yml
spring:
  cloud:
    config:
      uri: http://127.0.0.1:8888 # config server
      name: user-service # yml file name
```

1234 를 config server 의 키로 encrypt 해보면
0c7649b1698b8df8337716a26eafba4a90499ea5df3eff8d8492abfe0fff978a
가 나온다.
user-service.yml 에서 db password 를 '{cipher}0c7649...' 로 수정한다.

config server 를 가동시키고
http://localhost:8888/user-service/default
로 들어가서 확인해보면

우리가 저장한 user-service.yml 에는 암호화된 문자열이 들어가있지만
spring.datasource.password 값이 1234 로 복호화된 평문이 들어가 있는 걸 볼 수 있다.

유저 서비스와 eureka 서버 config server 를 띄우고
user service 의 /h2-console 로 들어가서 DB 패스워드로 1234 값을 정상적으로 사용하고 있는지 확인해보면 된다!

### REST TEMPLATE

config service 에 order_service 의 url 을 등록하여
user_service 에서 rest template 을 이용한 데이터 송수신에 사용할 예정.

user_service.yml
```yml
order_service:
  url: http://localhost:8000/order_service/$s/orders
```

유레카에 등록된 서비스 명으로도 서버를 찾아갈 수 있다.

user_service.yml
```yml
order_service:
  url: http://ORDER_SERVICE/order_service/%s/orders
```

### Feign Client

user-service 에 spring-cloud-starter-openfeign 라이브러리 추가

- 로깅
```java
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
```
```
11:32:11 GMT}{Keep-Alive: timeout=60}{Connection: keep-alive}
2021-11-28 20:32:11.706 DEBUG 12255 --- [o-auto-1-exec-3] m.h.u.client.OrderServiceClient          : [OrderServiceClient#getOrders] <--- HTTP/1.1 404 (331ms)
2021-11-28 20:32:11.706 DEBUG 12255 --- [o-auto-1-exec-3] m.h.u.client.OrderServiceClient          : [OrderServiceClient#getOrders] connection: keep-alive
2021-11-28 20:32:11.706 DEBUG 12255 --- [o-auto-1-exec-3] m.h.u.client.OrderServiceClient          : [OrderServiceClient#getOrders] content-type: application/json
2021-11-28 20:32:11.706 DEBUG 12255 --- [o-auto-1-exec-3] m.h.u.client.OrderServiceClient          : [OrderServiceClient#getOrders] date: Sun, 28 Nov 2021 11:32:11 GMT
2021-11-28 20:32:11.706 DEBUG 12255 --- [o-auto-1-exec-3] m.h.u.client.OrderServiceClient          : [OrderServiceClient#getOrders] keep-alive: timeout=60
2021-11-28 20:32:11.706 DEBUG 12255 --- [o-auto-1-exec-3] m.h.u.client.OrderServiceClient          : [OrderServiceClient#getOrders] transfer-encoding: chunked
2021-11-28 20:32:11.706 DEBUG 12255 --- [o-auto-1-exec-3] m.h.u.client.OrderServiceClient          : [OrderServiceClient#getOrders] vary: Access-Control-Request-Headers
2021-11-28 20:32:11.706 DEBUG 12255 --- [o-auto-1-exec-3] m.h.u.client.OrderServiceClient          : [OrderServiceClient#getOrders] vary: Access-Control-Request-Method
2021-11-28 20:32:11.706 DEBUG 12255 --- [o-auto-1-exec-3] m.h.u.client.OrderServiceClient          : [OrderServiceClient#getOrders] vary: Origin
2021-11-28 20:32:11.706 DEBUG 12255 --- [o-auto-1-exec-3] m.h.u.client.OrderServiceClient          : [OrderServiceClient#getOrders] 
2021-11-28 20:32:11.706 DEBUG 12255 --- [o-auto-1-exec-3] m.h.u.client.OrderServiceClient          : [OrderServiceClient#getOrders] {"timestamp":"2021-11-28T11:32:11.699+00:00","status":404,"error":"Not Found","message":"No message available","path":"/order-service/83cbfd15-6fd9-4588-a77f-96de61fd44d9/orders_ng"}
2021-11-28 20:32:11.706 DEBUG 12255 --- [o-auto-1-exec-3] m.h.u.client.OrderServiceClient          : [OrderServiceClient#getOrders] <--- END HTTP (182-byte body)
```

- 전역 예외 처리
  - Feign Error Decoder 를 빈으로 주입시킴으로써 예외처리를 전역으로 할 수 있다.

### 데이터 동기화 문제

- Order Service 를 여러 포트에 띄워서 서비스 할 수 있다.
  - User Service 에서 오더 서비스로 보내는 요청이 분산 처리 되는데
  - 문제는 Order Service 는 인스턴스에 종속적인 h2 데이터베이스를 사용하고 있고 데이터가 분산 저장이 되기 때문에 동기화 문제가 발생한다.
    - 첫번째 오더 서비스의 데이터베이스에 있는 데이터와 두번째 오더 서비스의 데이터베이스의 데이터가 동기화되지 않는 문제

- 데이터베이스를 나눠서 사용하지 않고 하나의 데이터베이스를 사용하면 문제가 해결 된다.

- 데이터베이스를 각각 가지고 있되, 동기화를 하기 위해 메세지 큐잉 서버를 이용할 수 있다.
  - Apache Kafka, Rabbit MQ
  - 변경된 데이터가 있으면 알려줘!
  - 이를 구독하고 있다가 변화를 감지하여 자신의 데이터베이스도 업데이트

- 사용할 방법: Kafka Connector + DB 단일화

### 카프카 시나리오
- 다양한 방법이 있겠지만 가장 일반적으로 많이 사용되는 카프카 클라이언트라고 해서 카프카에 메세지를 보내고, 카프카가 메세지를 보관하고 있다가, 다른쪽에 있는 컨슈머에게 그 메세지를 보내주는 시나리오가 있다.
- 카프카에 메세지를 보냄에 있어서 기본적으로 자바 프로그램이라던가 이런 쪽을 통해서 데이터를 받을 수도 있겠지만, 데이터베이스에 있는 자료가 증가나 수정 등의 변경사항이 생겼을 때 데이터베이스로부터 카프카가 변경된 데이터의 메세지를 가지고 와서 그 값을 다른 쪽에 있는 데이터베이스라든가 다른쪽의 스토리지 등 다른 서비스에 전달해주는 기능을 하는 카프카 커넥터를 알아봐야 한다.

- 카프카 클러스터에서 다른 쪽의 어플리케이션으로 메세지를 주고 받는 형태로, 예를 들어 a 라는 서비스에서 b 라는 서비스, b -> c 서비스로 데이터를 주고 받을 때 다이렉트로 주고받는게 아니라 보낼 때 카프카로 보내고, 받을 때 카프카에서 받는. 구체적으로 어떤 서비스가 보냈거나 받는지 명시하진 않는 프로듀서 컨슈머 구조를 사용

### Kafka

- https://cwiki.apache.org/confluence/display/KAFKA/Clients
- zookeeper
  - 카프카를 관리해주는 coordinator
- producer
  - 메세지를 보낸다.
  - 메세지를 보내면 토픽이 만들어진다.
- consumer
  - 메세지를 받는다.
- 서버 구동
  1. zookeeper 구동
    - `$KAFKA_HOME/bin/zookeeper-server-start.sh $KAFKA_HOME/config/zookeeper.prperties`
  2. kafka 구동
    - `$KAFKA_HOME/bin/kafka-server-start.sh $KAFKA_HOME/config/server.properties`
- 토픽 생성
  - `$KAFKA_HOME/bin/kafka-topics.sh --create --topic quickstart-events --bootstrap-server localhost:9092 --partitions 1`
- 토픽 목록 확인
  - `$KAFKA_HOME/bin/kafka-topics.sh --bootstrap-server localhost:9092 --list`
- 토픽 정보 확인
  - `$KAFKA_HOME/bin/kafka-topics.sh --describe --topic quickstart-events --bootstrap-server localhost:9092`
- 메시지 생산
  - `$KAFKA_HOME/bin/kafka-console-producer.sh --broker-list localhost:9092 --topic quickstart-events`
- 메시지 소비
  - `$KAFKA_HOME/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic quickstart-events --from-beginning


### Kafka connect

- Kafka connect 를 통해 data 를 import/export 할 수 있다.
- 코드 없이 configuration 값을 이용해서 데이터 이동
- standalone mode, distribution mode 지원
  - RESTful API 를 통해 지원
  - Stream 또는 Batch 형태로 데이터 전송 가능
  - 커스텀 Connector 를 통한 다양한 Plugin 제공 (File, S3, Hive, Mysql, etc ...)
- Source System (Hive, jdbc, ...) -> Kfka connect source -> Kafka cluster -> Kafka connect sink -> Target system
  - 카프카 클러스터를 기준으로
  - 데이터를 가져오는 곳(DB, file, ...)을 카프카 source system 이라고 하고
  - 데이터를 가져다 사용하는곳(S3, ...)을 sink 라고 한다.

### Kafka 와 Maria DB 연동하기

- 카프카를 사용하는 DB 로 maria DB 를 사용해보자.

- Maria DB 설치
  - `$ brew install mariadb`
  - `$ mysql.server start, mysql.server stop, mysql.server status`
  - `$ mysql -uroot`
  - `mysql> create database mydb;`

- 테이블 생성
```sql
create table users(
  id int auto_increment primary key,
  user_id varchar(20),
  pwd varchar(20),
  name varchar(20),
  created_USERS at datetime default NOW()
);
```

### Order service 에 Maria DB 연동

- maria db, DB: mydb, table: users

- maria db 에 데이터 변경이 생길 때 카프카 커넥터를 이용해서 다른 데이터베이스에 이 변경점을 알려주는 기능을 작업할 예정.

### Kafka Connect 설치

- curl -O http://packages.confluent.io/archive/5.5/confluent-community-5.5.2-2.12.tar.gz

- curl -O http://packages.confluent.io/archive/6.1/confluent-community-6.1.0.tar.gz

- tar xvf confluent-community-6.1.0.tar.gz

- cd $KAFKA_CONNECT_HOME

- Kafka Connect 실행
  - `(pwd: confluent-6.1.0) ./bin/connect-distributed ./etc/kafka/connect-distributed.properties`

- 카프카 커넥트를 실행하기전, 카프카와 주키퍼가 실행 되어야 있어야 한다.

- JDBC Connector 설치
  - https://docs.confluent.io/5.5.1/connect/kafka-connect-jdbc/index.html

- confluentinc-kafka-connect-jdbc-10.0.1.zip 

- 설치 후 해야 할 작업
  1. etc/kafka/connect-distributed.properties 파일 마지막에 아래 plugin 정보 추가
    - plugin.path=[confluentinc-kafka-connect-jdbc-10.0.1 폴더]
  
  2. JdbcSourceConnector에서 MariaDB 사용하기 위해 mariadb 드라이버 복사
    - 서비스에서 쓰고 있는 DB 드라이버 복사할 것 (order service 의 pom.xml 에 maria db dependency 를 추가해 놓았었음)
    - ./share/java/kafka/ 폴더에 mariadb-java-client-2.7.2.jar  파일 복사

(실습)

- 주키퍼 실행

- 카프카 실행

- 토픽 리스트 확인

- 카프카 커넥터 실행

- 토픽 리스트 확인
  - connect-configs, connect-offsets, connect-status 토픽이 생겨있음 확인

- connector 중단 후, property 추가
  - confluent-6.1.0/etc/kafka/connect-distributed.properties
  - plugin.path=/Users/hyojeongyu/Desktop/dev/kafka/confluentinc-kafka-connect-jdbc-10.2.6/lib

- 카프카 커넥트에 mariadb 드라이버 복사
  - /Users/hyojeongyu/.m2/repository/org/mariadb/jdbc/mariadb-java-client/2.7.2
  - mv mariadb-java-client-2.7.2.jar ~/Desktop/dev/kafka/confluent-6.1.0/share/java/kafka


### Kafka Source Connect 사용

- 카프카 커넥트에 Source, Sink 를 연결할 수 있다.

- 카프카 커넥트의 REST API 를 사용할 수 있다
  - GET. POST: http://localhost:8083/connectors
  - GET: http://localhost:8083/connectors/{connector-name}/status


- 참고자료: https://www.inflearn.com/questions/199173

- 그 후 db 에 데이터 추가
  - `insert into my_db.users(user_id, name) values('test2', 'TEST ADMIN');`
  - `SELECT * FROM my_db.users;`

- DB 변경점을 감시하고 있던 카프카 소스 커넥터가 이를 토픽으로 추가해준다. 이를 카프카 컨슈머로 확인해보자.

(실습)

- 주키퍼, 카프카 코어, 커넥트 시작
- POST: http://localhost:8083/connectors
```
{
   "name":"my-source-connect",
   "config":{
      "connector.class":"io.confluent.connect.jdbc.JdbcSourceConnector",
      "connection.url":"jdbc:mysql://localhost:3306/mydb",
      "connection.user":"root",
      "connection.password":"${db-password}",
      "mode":"incrementing",
      "incrementing.column.name":"id",
      "table.whitelist":"users",
      "topic.prefix":"my_topic_",
      "tasks.max":"1"
   }
}
```
- GET http://localhost:8083/connectors
- GET http://localhost:8083/connectors/my-source/status
- DB 에 데이터 추가
- 토픽 생성 확인: ${table.prefix} + ${table.whitelist} ex) my_topic_users
- consumer 로 확인해보기
  - ./bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic my_topic_users --from-beginning


### Kafka Sink Connect 사용

- Kafka Sink Connect 를 추가할 것임
  - 소스 커넥트가 데이터를 전달하면 토픽이 쌓이고, 싱크는 쌓여있는 토픽을 가져다 사용함

- source connect 에 연결된 DB 에 데이터 변경이 감지되면 my_topic_users 라는 토픽으로 데이터가 쌓이고 이 데이터를 DB 에 추가함

- Kafka producer 를 이용해서 Kafka topic 에 데이터 직접 전송해보기
  - 콘솔 프로듀서를 통해서 실제 토픽에서 사용할 메세지 토픽을 전송해보고, 토픽에 데이터가 추가되면, 싱크 커넥트가 업데이트 되어있는 토픽의 데이터를 가지고와서 자신이 연결하려는 데이터베이스도 업데이트 해줄 것
  - 콘솔 프로듀서에 데이터를 직접 전송하게 되면 토픽의 데이터가 maria db 에도 데이터가 추가된다.

(실습)

- 주키퍼, 카프카 서버, 카프카 콘솔 컨슈머, 카프카 커넥트 시작
- 현재 토픽 확인
  - (카프카 코어) ./bin/kafka-topics.sh --bootstrap-server localhost:9092 --list
- 싱크 커넥트 추가
  - POST http://localhost:8083/connectors
```
{
   "name":"my-sink-connect",
   "config":{
      "connector.class":"io.confluent.connect.jdbc.JdbcSinkConnector",
      "connection.url":"jdbc:mysql://localhost:3306/mydb",
      "connection.user":"root",
      "connection.password":"${db-password}",
      "auto.create":"true", // topic 과 같은 이름의 테이블을 생성하겠다.
      "auto.evolve":"true",
      "delete.enabled":"false",
      "tasks.max":"1",
      "topics":"my_topic_users"
   }
}
```
- DB 에 데이터 추가
- my_topic_users 테이블이 자동생성되면서 users 테이블에 추가된 데이터가 my_topic_users 테이블에도 추가되어 있음을 확인
- 콘솔 프로듀서를 통해 데이터 직접 전송해보기
  - `$KAFKA_HOME/bin/kafka-console-producer.sh --broker-list localhost:9092 --topic my_topic_users`
```
{"schema":{"type":"struct","fields":[{"type":"int32","optional":false,"field":"id"},{"type":"string","optional":true,"field":"user_id"},{"type":"string","optional":true,"field":"pwd"},{"type":"string","optional":true,"field":"name"},{"type":"int64","optional":true,"name":"org.apache.kafka.connect.data.Timestamp","version":1,"field":"created_at"}],"optional":false,"name":"users"},"payload":{"id":1,"user_id":"user1","pwd":"test1111","name":"user name","created_at":1640918280000}}
```
- 싱크 커넥트에 연결된 데이터베이스 테이블에 (my_topic_users 테이블) 데이터가 추가되었음을 확인

### Orders 서비스와 Catalogs 서비스에서 Kafka topic 의 적용

1. 유저 -> 카탈로그 서비스: 상품 조회
2. 유저 -> 유저 서비스: 사용자 조회
3. 유저 -> 오더 서비스: 상품 주문
4. 오더 서비스 -> Kafka message queue service -> 카탈로그 서비스: 상품 수량 업데이트
5. 유저 -> 유저 서비스: 주문 확인
6. 유저 서비스 -> 오더 서비스: 주문 조회

- Orders Service 에 요청된 주문의 수량 정보를 Catalogs service 에 반영
- Orders Service 에서 DB 를 업데이트 하여 Kafka topic 으로 메세지 전송 (Producer)
- 이를 카탈로그 서비스에서 재고를 계산하기 위해 Kafka topic 에 전송된 메세지를 취득 (Consumer)
- 각 서비스에서 한 DB 를 사용하면 메세지 큐를 쓸 필요는 없겠지만 각 서비스의 DB를 독립적으로 사용하기 위함임

(실습)
- 주키퍼, 카프카, 유레카, api gateway service, configuration service, order service, catalog service 실행
- Order 생성시, Order service 에서 메세지 큐에 토픽을 생성하고, Catalog 서비스에서 생성된 토픽을 받아서 수량을 업데이트한다


### Multi Orders Microservice 사용에 대한 데이터 동기화 문제

- Orders Service 2개 기동
  - Users 요청 분산 처리
  - Orders 데이터도 분산 저장 -> 동기화 문제

- Orders Service 에 요청된 주문 정보를 DB 가 아니라 Kafka topic 으로 전송
- Kafka topic 에 설정된 Kafka Sink Connect 를 사용해 단일 DB 에 저장 -> 데이터 동기화

(실습)

```
create table orders
(
    id          int auto_increment primary key,
    user_id     varchar(50) not null,
    product_id  varchar(20) not null,
    order_id    varchar(50) not null,
    qty         int      default 0,
    unit_price  int      default 0,
    total_price int      default 0,
    created_at  datetime default now()
);
```

- order service 가 maria db 를 사용하도록 수정

- order service 가 바로 DB 에 데이터를 저장하지 않고 create order 시 카프카에게 전송하며 카프카가 단일 DB 에 데이터를 저장
  - order controller, POST /{userId}/orders 에서 orders 라는 토픽으로 dto 를 json 직렬화하여 던진다.
  - ```
  {"schema":{"type":"struct","fields":[{"type":"int32","optional":false,"field":"id"},{"type":"string","optional":true,"field":"user_id"},{"type":"string","optional":true,"field":"pwd"},{"type":"string","optional":true,"field":"name"},{"type":"int64","optional":true,"name":"org.apache.kafka.connect.data.Timestamp","version":1,"field":"created_at"}],"optional":false,"name":"users"},"payload":{"id":1,"user_id":"user1","pwd":"test1111","name":"user name","created_at":1640918280000}}
  ```
  - sink connect 생성
  ```
{
   "name":"my-order-sink-connect",
   "config":{
      "connector.class":"io.confluent.connect.jdbc.JdbcSinkConnector",
      "connection.url":"jdbc:mysql://localhost:3306/mydb",
      "connection.user":"root",
      "connection.password":"${db-password}",
      "auto.create":"true", // topic 과 같은 이름의 테이블을 생성하겠다.
      "auto.evolve":"true",
      "delete.enabled":"false",
      "tasks.max":"1",
      "topics":"orders"
   }
}
```

(테스트)

- 주키퍼, 카프카, 카프카 커넥트 서버 구동
- eureka, api gateway, catalog service, user service 구동
- order service 서버는 2개 구동
- 주문 생성 API 를 여러번 호출 시 번갈아가면서 order service 가 카프카로 메세지를 전달함
- 카프카 메세지가 날아 올 때마다 orders 테이블에 데이터를 추가함
- 단일 DB 를 쓰고 있기 때문에 예전에 한 유저의 오더를 검색했을 때 검색할 때마다 오더 정보가 달라지던 이슈가 해결됨.


## 장애 처리와 Microservice 분산 추적

### CircuitBreaker 와 Resilience4J 의 사용

- 연쇄 통신시 발생할 수 있는 문제
  - 유저 서비스 -> 오더 서비스 -> 카탈로그 서비스 로 연쇄 통신을 하는 경우 유저 서비스의 문제가 아닌데도 500 에러가. 발생하는 경우가 있다. (오더 서비스나, 카탈로그 서비스에서 에러가 발생하는 경우)
  - 이 경우 에러가 발생하는생했을 때 디폴트 값이나, 우회하는 방식이나, 정상적인 데이터 처럼 보이는 방식이 유저 서비스에 준비가 되어 있어야 한다.
  - 카탈로그 서비스나 오더 서비스가 문제가 있더라도 유저 서비스는 문제가 없기 때문에

- CircuitBreaker
  - 장애(timeout, ...)가 발생하는 서비스에 반복적인 호출이 되지 못하게 차단
  - 특정 서비스가 정상적으로 동작하지 않을 경우 다른 기능으로 대체 수행 -> 장애 회피
  - open: 서킷 브레이커를 여는 것
    - 유저 서비스에서 오더 서비스를 사용함에 있어서 접속이 안된다거나 타임아웃이 여러번 발생했을 때 서킷 브레이커가 오픈 상태가 되고 오픈이 되면 클라이언트의 요청을 최종적인 마이크로 서비스에게 전달하지 않고 서킷 브레이커에서 기본값이나 우회할 수 있는 값을 리턴하는 것을 이야기함
  - closed: 서킷 브레이커를 닫는 것
    - 정상적으로 다른 서비스를 사용할 수 있다.

- 스프링 부트에서 CircuitBreaker 사용법 (이전의 방법, 현재는 maintained)
  - Spring cloud netflix hystrix
  - @EnableCurcuitBreaker
  - feign.hystrix.enabled = true

- 따라서 대체제
  - Hystirx -> Resilience4j
  - Hystrix Dashboard / Turbine -> Micrometer + Monitoring System
  - Ribbon -> ...

- Resilience4j
  - fault tolerance
    - 에러가 발생하더라도 정상적으로 가용할 수 있도록 하는 성질
  - spring-cloud-starter-circuitbreaker-resilience4j library
  - 유저 서비스에서 order 라는 서비스로부터 order 리스트를 받아올 때 오더 서비스가 기동하지 않는 경우 (timeout, ...) 서킷 브레이커를 열고 빈 배열을 응답하도록
  - CircuitBreakerFacty -> Resilience4JCircuitBreakeerFactory 커스터마이징
  - failureRateThreshold: 서킷 브레이커를 열지 결정하는 failure rate threshold % (default 50)
  - waitDurationInOpenState: 서킷 브레이커를 오픈한 상태에서 유지하는 시간
  - slidingWindowType: 써킷 브레이커가 닫힐 때 통화 결과를 기록하는데 사용되는 슬라이딩 창의 유형 (카운트 기반 Or 시간 기반)
  - slidingWindowSize: 써킷 브레이커가 닫힐 때 호출 결과를 기록하는데 사용되는 슬라이딩 창의 크기를 구성

(실습)

- 유레카, rabbitmq 서버 실행
  - mvn spring-boot:run
  - brew services start rabbitmq
- api gateway 서버 실행
- 오더 서비스는 구동 시키지 않고 유저 서비스만 구동 시킨 뒤
- 유저 생성
- 유저 정보 얻기
  - 여기서 getOrders() 부분에서 오더 서비스가 띄워져 있지 않아서
  - 유저 서비스는 500 에러를 반환한다.
- circuit breaker 추가
  - 오더 서비스의 API 를 호출하는 코드를 circuit breaker 의 toRun 파라미터로 넘기고 해당 코드를 실행 중 오류가 발생할 경우 실행되는 fallback 파라미터로 빈 배열을 넘긴다
  - 오더 서비스가 띄워져 있지 않은 경우 유저 정보를 조회하면 orders 는 빈 배열이 나오게 된다.
  - 따라서 유저 서비스는 잘 동작하는 것 처럼 보이고 더 이상 500 에러를 반환하지 않는다.


### 분산 추적의 개요 Zipkin 서버 설치

- 마이크로서비스가 연쇄적으로 여러개의 서비스가 실행될 때, 그 과정에서 해당하는 요청 정보가 어떻게 실행되고 어느 단게를 거쳐서 어떤 마이크로서비스로 이동이 되는지를 추적할 수 있는 방법을 살펴보자.
- 마이크로 서비스의 분산 추적이라고 하고
- tracing 정보를 저장하기 위한 서비스가 필요하고 이를 집킨을 사용할 것

- Zipkin
  - 트위터에서 사용하는 분산 환경의 타이밍 데이터 수집, 추적 시스템을 오픈소스로 만든 것
  - Google Drapper 에서 발전하였으며 분산환경에서의 시스템 병목 현상을 파악
  - 하나의 서비스가 시작되고 끝날 때까지 다양한 마이크로 서비스가 연결될 수 있기 때문에 사용자 요청이 누구를 거쳐 어디를 거쳐 누가 문제가 있는지 시각적으로 표시 + 로그로 표시할 수 있다.
  - Collector, Query Service, Database WebUI 로 구성
  - 마이크로 서비스 내의 데이터를 집킨에게 모두 전달하고, 한 마이크로서비스에서 다른 마이크로서비스를 호출했을 때 그 데이터 역시 집킨에게 전송한다.
  - Span
    - 한 요청에 사용되는 작업의 단위
    - 64 bit unique ID
  - Trace
    - 트리 구조로 이루어진 span set
    - 하나의 요청에 대한 같은 Trace ID 발급
  - 사용자가 요청을 하면 trace id 가 만들어지고, 그 요청이 여러 서비스를 돌아다닐 때 각 서비스에서 span id 가 만들어지고 한 요청에 대한 span set 은 trace 로 구성된다.

- Spring Cloud Sleuth
  - 스프링 부트 애플리케이션을 Zipkin 과 연동
  - 요청 값에 따른 Trace ID, Span ID 부여
  - Trace와 Span Id 를 이용해서 로그에 추가하는 기능을 할 수 있다.
    - servlet filter
    - rest template
    - scheduled actions
    - message channels
    - feign client

- Spring Cloud Sleuth 을 이용해서 Zipkin 을 연동시키는 것임
- 마이크로 서비스는 여러개의 쪼개져 있는 서비스간 데이터 통신을 계속하기 때문에 중간에 문제가 생기면 작동하지 않는 서비스가 있을 수 있다. 서킷 브레이커를 이용해서 우회하는 방식도 있고, 그런 마이크로 서비스가 연결된 상태값을 추적해서 누가 누구를 호출했고 시간이 얼마가 걸리며 정상 상태인지 비정상 상태인지를 시각화해주는 툴이 Sleuth 와 Zipkin 인 것

- zipkin 설치
  - curl -sSL https://zipkin.io/quickstart.sh | bash -s
- zipkin 실행
  - java -jar zipkin.jar
  - localhost:9411

 
## Microservice 모니터링

### Micrometer 개요

- 이전 버전의 스프링 부트와 스프링 클라우드에서는 각종 마이크로 서비스에서 발생하는 상황, 성능, 모니터링을 하기 위해서 히스트릭스나 터빈 서버 같은것을 구성해서 사용했었음
  - 터빈 서버? 마이크로서비스에서 발생하는 각종 로그라던가 결과값들을 히스트릭스 클라이언트 스트림을 통해 전송하게 되면, 전송되어진 내용을 모아서 로그파일처럼 저장하고 있다가 히스트릭스 대쉬보드나 모니터링 보드에 전달하는 역할을 한다.
  - 마이크로서비스에서 생성되는 스트림의 메세지를 수집하고 있는 용도로써 터빈서버를 사용하는게 일반적이었음.
  - 터빈 서버의 설정파일에 어떤 서비스에서 로그를 수집하고 싶은지 설정하면 해당 서비스의 정보를 수집할 수 있었음.

- 수집해뒀던 로그는 히스트릭스 대쉬보드라는 어플리케이션을 통해서 각종 지표로 화면에 보여줄 수 있다.
- 히스트릭스 대쉬보드는 웹 대쉬보드 역할을 한다
- 터빈 서버에서 회원, 상품, 배송 정보와 같은 것들의 지표나 로그정보를 히스트릭스 서버에 불러오기 위해서 해당 데이터를 터빈 서버가 저장하고 있는 것.
- 히스트릭스 대쉬보드에 액션을 취하게 되면, 대쉬보드 서버에서 터빈 서버가 가지고 있던 정보를 다시 읽어들여서 화면에 도식화해서 보여주는 역할을 하게 된다.

- 히스트릭스 대쉬보드
  - 읽어오고자 하는 터빈서버의 정보를 입력하면 지표, 데이터를 보여준다.
  - 현재 작동중인 마이크로 서비스나, 마이크로 서비스의 함수 등을 보여준다.
  - 마이크로 서비스가 가진 메소드들의 성공 횟수, 실패 횟수나 서킷 브레이커가 열려 있는지 닫혀 있는지 등등의 정보를 표시

그러나 최근 스프링 클라우드 버전에서는 기존에 사용했던 개념들 대신에 새롭게 변경된 솔루션과 라이브러리를 소개한다.
히스트릭스 대쉬보드나 터빈 서버는 Micrometer 나 Monitoring system 을 이용해 대신한다.

- Micrometer
  - 자바 기반의 애플리케이션의 모니터링을 위한 각종 자료를 수집하는 목적으로 사용된다.
  - 모니터링? 현재 cpu의 사용량, 메소드의 사용량, 발생하고 사용된 네트워크 트래픽 량, 사용자 요청이 몇번 호출 됐는지, ...
  - 모니터링 도구를 연동해 줌으로써 현재 운영되는 서버라던가 시스템이 갖고 있는 부하나 문제가 생겼던 시점을 파악할 수 있다.
  - 스프링 클라우드를 이용해서 마이크로 서비스를 개발하는데 마이크로 서비스 자체가 하나의 어플리케이션 자체가 아니라 분산되어 있는 여러개의 소프트웨어로 구성되어 있기 때문에 각종 서버의 기능이 잘 동작하는지, 문제가 생기진 않았는지, 병목 현상은 없는지를 보면서 자원을 재할당해주는 것이 필요하다.
  - 스프링 프레임워크 5, 스프링 부트 2 부터 스프링의 Metrics 자료를 Micrometer 를 사용해서 보여준다
  - Prometheus 등의 다양한 모니터링 시스템과 연동 될 수 있기 때문에 시각화를 할 때 상당히 유용하게 작용된다.

- Timer
  - 마이크로 미터를 사용할 때 짧은 지연 시간, 이벤트 사용 빈도 등을 등록하고 체크하기 위해서 사용되는 클래스 Timer
  - 시계열로 이벤트의 시간, 호출 빈도등을 제공
  - @Timed 를 사용하면 자주 사용되는 메소드나 특정 클래스가 호출되는 시간이나 빈도를 체크 가능

(실습)
- 유저 서비스, 오더 서비스, 게이트웨이 서버에 prometheus 추가
- 유저 서비스의 health_check, welcome 메소드에 @Timed 추가 후 호출
- 유저서비스/actuator/metrics 에 해당 지표가 추가되었음을 확인
- 유저서비스/actuator/prometheus 에서 아래 데이터 확인
```
# HELP users_welcome_seconds_max  
# TYPE users_welcome_seconds_max gauge
users_welcome_seconds_max{method="GET",uri="/welcome",} 0.0
# HELP users_welcome_seconds  
# TYPE users_welcome_seconds summary
users_welcome_seconds_active_count{method="GET",uri="/welcome",} 0.0
users_welcome_seconds_duration_sum{method="GET",uri="/welcome",} 0.0
```
```
# HELP users_status_seconds_max  
# TYPE users_status_seconds_max gauge
users_status_seconds_max{method="GET",uri="/health_check",} 0.0
# HELP users_status_seconds  
# TYPE users_status_seconds summary
users_status_seconds_active_count{method="GET",uri="/health_check",} 0.0
users_status_seconds_duration_sum{method="GET",uri="/health_check",} 0.0
```

### Prometheus + Grafana

- MSA 에서 각각의 엔티티는 자동화된 문제에 대해서 감지를 하고, 경고 하고, 필요하면 디버깅, 상태분석 등 적절한 서비스를 생성해줘야 한다. 이러한 서비스는 시스템 분석을 위해 health check 라던가 metrics, end to end's tracing 등의 정보를 수집할 수 있는 구조여야 한다. -> 그 중 대표적으로 많이 사용되는 오픈소스인 프로메테우스를 사용해보자

- Prometheus
  - 모니터링 도구
  - Metrics 를 수집하고 모니터링 및 알람에 사용되는 오픈소스 머플리케이션
  - 2016 년부터 CNCF 에서 관리되는 2번째 공식 프로젝트
    - 첫번째는 쿠버네티스 라는 컨테이너 기반의 오케스트레이션 도구가 공식 프로젝트고
    - 두번째가 프로메테우스
    - 초창기에는 Level DB 를 사용하다가 Time Series Database 로 변경됨
    - 각종 지표가 시간 순서대로 남는다
  - Pull 방식의 구조와 다양한 Metric Exporter 를 제공함
  - 시계열 데이터베이스로 Metrics 를 저장하고 이를 조회하는 쿼리를 같이 제공한다.
  - 프로메테우스에서 스프링 클라우드 어플리케이션에서 /actuator/prometheus 정보를 수집해서 도식화하기 위해 사용한다

- Grafana
  - 데이터 시각화, 모니터링 및 분석을 위한 오픈소스 애플리케이션
  - 시계열 데이터를 시각화하기 위한 대쉬보드 제공

- Prometheus 다운로드
  - https://prometheus.io/download/

- Grafana 다운로드
  - https://grafana.com/grafana/download


(실습)
- ./prometheus --config.file=prometheus.yml
- prometheus.yml 수정
```
 - job_name: 'user-service'
    scrape_interval: 15s
    metrics_path: '/user-service/actuator/prometheus'
    static_configs:
    - targets: ['localhost:8000']
  - job_name: 'order-service'
    scrape_interval: 15s
    metrics_path: '/order-service/actuator/prometheus'
    static_configs:
    - targets: ['localhost:8000']
  - job_name: 'apigateway-service'
    scrape_interval: 15s
    metrics_path: '/actuator/prometheus'
    static_configs:
    - targets: ['localhost:8000']
```
- localhost:9090

- ./bin/grafana-server
- localhost:3000
- admin, admin login