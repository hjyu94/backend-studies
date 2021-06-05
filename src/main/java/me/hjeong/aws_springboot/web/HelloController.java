package me.hjeong.aws_springboot.web;

import me.hjeong.aws_springboot.web.dto.HelloResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// JSON을 반환하는 컨트롤러
// 예전에는 @ResponseBody를 각 메소드마다 선언했던 것을 한번에 사용할 수 있게 해준다.
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/hello/dto")
    public HelloResponseDto hello(@RequestParam("name") String name/* 1 */, @RequestParam("amount") int amount) {
        return new HelloResponseDto(name, amount);
    }

    /*
        1) @RequestParam
        - 외부에서 API로 넘긴 파라미터를 가져오는 어노테이션
     */

}
