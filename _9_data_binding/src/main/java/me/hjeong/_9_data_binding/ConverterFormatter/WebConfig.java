package me.hjeong._9_data_binding.ConverterFormatter;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new EventConverter.StringToEventConverter());
        registry.addFormatter(new EventFormatter());
    }
    // FormatterRegistry 에 컨버터, 포매터를 등록해 놓으면
    // ConversionService 가 등록된 컨버터, 포매터를 가져다 사용한다.
}
