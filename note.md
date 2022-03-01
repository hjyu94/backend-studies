## NestJS CLI Install

```shell
npm i -g @nestjs/cli
```

## Create new NestJS Project

```shell
nest new ./
```

- eslintrc.js

  - 개발자들이 특정한 규칙을 가지고 코드를 깔끔하게 짤 수 있도록 도와주는 라이브러리
  - 타입스크립트를 쓰는 가이드 라인 제시
  - 문법에 오류가 나면 알려주는 역할 등등

- .prettierrc

  - 주로 코드 형식을 맞추는데 사용
  - 작음 따옴표를 사용할 지, 큰 따옴표를 사용할 지
  - Indent 값을 4로 줄지 등등
  - 에러 찾는 것이 아닌 코드 포맷터의 역할을 한다.

- nest-cli.json

  - nest 프로젝트를 위해 특정한 설정을 할 수 있는 json 파일

- tsconfig.json

  - 어떻게 타입 스크립트를 컴파일 할 지 설정

- tsconfig.build.json

  - tsconfig.json 의 연장선 상 파일
  - build 할 때 필요한 설정들
  - excludes 에서느 빌드 할 때 필요 없는 파일을 명시

- package.json

  - build: 운영환경을 위한 빌드
  - format: 린트 에러가 났을 지 수정
  - start: 앱 시작

- src 폴더

  - 대부분의 비즈니스 로직이 들어가는 곳
  - main.ts: 앱을 생성하고 실행
  - app.module.ts: 앱 모듈을 정의(?)

- 실행
  ```shell
  npm run start:dev
  ```

## Create module

```shell
nest g module boards
```

- nest: using nestcli
- g: generate
- module: schematic that I want to create
- boards: name of the schematic


## Create controller

```shell
nest g controller boards --no-spec
```

- nest: using nestcli
- g: generate
- controller: controller schematic
- boards: name of the schematic
- --no-spec: 테스트를 위한 소스 코드 생성 X


## Create providers, service

- Provider 란?
  - 프로바이더는 Nest 의 기본 개념입니다.
  - 대부분 기본 Nest 클래스는 서비스, 레포지토리, 팩토리, 헬퍼 등 프로바이더로 취급될 수 있다.
  - 프로바이더의 주요 아이디어는 종속적으로 주입할 수 있다는 것인데
  - 객체는 서로 다양한 관계를 만들 수 있으며 객체의 인스턴스를 연결하는 기능은 대부분 Nest 런타임 시스템에 위임될 수 있다.

- 서비스 생성
  ```shell
  nest g service boards --no-spec
  ```

## Create Board Model

- Interface
  - 변수와 타입만을 체크한다.
- Class

  - 변수의 타입도 체크하고, 인스턴스 또한 생성할 수가 있다.

- ID 처리?
  - ID 는 unique한 값을 사용해야 한다.
  - `npm install uuid --save`
  - `import { v1 as uuid } from 'uuid';`

## DTO

- 클래스는 인터페이스와 다르게 런타임에서 작동하기 때문에 파이프 같은 기능을 이용할 때 더 유용하다
- 따라서 DTO 는 클래스로 작성한다.

## Pipe

- data transformation
- data validation
- level
  - Global-level Pipes
  - Handler-level Pipes
  - Parameter-level Pipes
- Built-in Pipes
  - ValidationPipe
  - ParseIntPipe
  - ParseBoolPipe
  - ParseArrayPipe
  - ParseUUIDPipe
  - DefaultValuePipe

## Validation

- Pipe 를 사용한 validation
- `npm install class-validator class-transformer --save`

## Create custom pipe

- It should implement PipeTransform interface.
- transform()
  - value: 처리가 된 인자의 값
  - metadata: 인자에 대한 메타 데이터를 포함한 객체
  - return 값은 Route handler 로 전달되고, 예외가 발생하면 바로 클라이언트에게 응답

