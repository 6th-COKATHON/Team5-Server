package cotato.hack.common.property.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import cotato.hack.common.property.property.S3Properties;
import cotato.hack.common.property.property.SwaggerProperties;

// 전역적으로 사용되는 상수
@Configuration
@EnableConfigurationProperties(value = {
	S3Properties.class,
	SwaggerProperties.class,
})
public class PropertyConfig {
}
