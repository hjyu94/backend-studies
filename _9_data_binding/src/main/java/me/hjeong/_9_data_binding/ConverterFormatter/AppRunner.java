package me.hjeong._9_data_binding.ConverterFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

@Component
public class AppRunner implements ApplicationRunner {

    @Autowired
    ConversionService conversionService;
    // ConverterRegistry, FormatterRegistry 를 사용해서
    // 실질적인 변환 작업을 해주는 주체

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("conversionService class type: ");
        System.out.println(conversionService.getClass());
        System.out.println("\nconverter list: ");
        System.out.println(conversionService);
    }
}
