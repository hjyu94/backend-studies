package com.example.demo.advice.exception;

/*
    예외 발생 시 이미 구현되어있는 Exception Class를 사용할 수 있지만 매번 정의된 Exception을 사용하는 것은
    여러 가지 예외 상황을 구분하는데 적합하지 않을 수 있습니다.
    그래서 이번에는 Custom Exception을 정의하여 사용해 보겠습니다.
*/

/*
    Class명의 prefix C는 Custom을 의미합니다.
    Exception 이름은 알아보기 쉽고 의미가 명확하게 전달될 수 있는 한 자유롭게 지으면 됩니다.
*/

/*
    CUserNotFoundException은 RuntimeException을 상속받아 작성합니다.
    총 3개의 메서드가 제공되는데. 메서드 중 CUserNotFoundException()을 사용하도록 하겠습니다.
    혹시 Controller에서 메시지를 받아 예외 처리 시 사용이 필요하면 CUserNotFoundException(String msg)을 사용하면 됩니다.
*/
public class CUserNotFoundException extends RuntimeException {

    public CUserNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }

    public CUserNotFoundException(String msg) {
        super(msg);
    }

    public CUserNotFoundException() {
        super();
    }
}