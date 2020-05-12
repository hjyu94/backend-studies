package me.hjeong._4_environment;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:/app.properties")
public class PropertyConfig {
}
