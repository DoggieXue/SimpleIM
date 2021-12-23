package org.cloudxue.imClient.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.stereotype.Component;

/**
 * @ClassName SystemConfig
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2021/12/23 下午9:50
 * @Version 1.0
 **/
@Data
@Component
@Configuration
@PropertySource("classpath:application.properties")
public class SystemConfig {
    @Value("${chat.server.ip}")
    private String host;
    @Value("${chat.server.port}")
    private int port;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer () {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
