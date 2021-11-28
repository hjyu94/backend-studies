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