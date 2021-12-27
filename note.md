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
  - `$ mysql server start, mysql.server stop, mysql.server status`
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

