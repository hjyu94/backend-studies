package me.hjeong._4_spring_boot_web_mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {
    @GetMapping("/sample")
    public String sample() {
        return "sample";
    }
}
