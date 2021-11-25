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
