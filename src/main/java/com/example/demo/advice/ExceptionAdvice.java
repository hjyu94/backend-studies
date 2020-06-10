package com.example.demo.advice;

import com.example.demo.advice.exception.CUserNotFoundException;
import com.example.demo.model.response.CommonResult;
import com.example.demo.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestControllerAdvice
/*
    ControllerAdvice의 annotation은 @ControllerAdvice @RestControllerAdvice 두가지가 있습니다.
    예외 발생 시 json형태로 결과를 반환하려면 @RestControllerAdvice를 클래스에 선언하면 됩니다.

    annotation에 추가로 패키지를 적용하면 위에서 설명한 것처럼 특정 패키지 하위의 Controller에만 로직이 적용되게도 할 수 있습니다.
    ex) @RestControllerAdvice(basePackages = “com.rest.api”)

    실습에서는 아무것도 적용하지 않아 프로젝트의 모든 Controller에 로직이 적용됩니다.
*/

public class ExceptionAdvice {

    private final ResponseService responseService;
    private final MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    /*
        Exception이 발생하면 해당 Handler로 처리하겠다고 명시하는 annotation입니다.
        괄호안에는 어떤 Exception이 발생할때 handler를 적용할 것인지 Exception Class를 인자로 넣습니다.

        예제에서는 Exception.class를 지정하였는데 Exception.class는 최상위 예외처리 객체이므로
        다른 ExceptionHandler에서 걸러지지 않은 예외가 있으면 최종으로 이 handler를 거쳐 처리됩니다.
        그래서 메서드 명도 defaultException이라 명명하였습니다.
    */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    /*
        해당 Exception이 발생하면 Response에 출력되는 HttpStatus Code가 500으로 내려가도록 설정합니다.
        참고로 성공 시엔 HttpStatus code가 200으로 내려갑니다.
        실습에서 HttpStatus Code의 역할은 성공이냐(200) 아니냐 정도의 의미만 있고
        실제 사용하는 성공 실패 여부는 json으로 출력되는 정보를 이용합니다.
    */
    protected CommonResult defaultException(HttpServletRequest request, Exception e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("unKnown.code")), getMessage("unKnown.msg"));
        /*
            Exception 발생시 이미 만들어둔 CommonResult의 실패 결과를 json 형태로 출력하도록 설정합니다.

            위에서 세팅한 HttpStatus Code외에 추가로 api 성공 실패여부를 다시 세팅하는 이유는
            상황에 따라 다양한 메시지를 전달하기 위해서 입니다.
            HttpStatus Code는 이미 고정된 스펙이기 때문에 (예 200 == OK, 404 == Not Found 등등…)
            상세한 예외 메시지 전달에 한계가 있습니다.

            예를 들자면 “회원 정보가 없음” 이라는 에러 메시지는 HttpStatus Code상에 존재하지 않아 표현할 수가 없습니다.
            따라서 커스텀 Exception을 정의하고 해당 Exception 발생하면
            적절한 형태의 오류 메시지를 담은 Json을 결과에 내리도록 처리하는 것입니다.
        */
    }

    @ExceptionHandler(CUserNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult userNotFoundException(HttpServletRequest request, CUserNotFoundException e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("userNotFound.code")), getMessage("userNotFound.msg"));
    }
    /*
        CUserNotFoundException을 적용해봅시다.
        이제 Controller에서 CUserNotFoundException이 발생하면 해당 ExceptionHandler에서 받아 처리하게 됩니다.
        기존의 ExceptionHandler는 새로운 ExceptionHandler가 제대로 작동하는지 테스트하기 위해 일단 주석 처리합니다.
    */

    // code정보에 해당하는 메시지를 조회합니다.
    private String getMessage(String code) {
        return getMessage(code, null);
    }

    // code정보, 추가 argument로 현재 locale에 맞는 메시지를 조회합니다.
    private String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}