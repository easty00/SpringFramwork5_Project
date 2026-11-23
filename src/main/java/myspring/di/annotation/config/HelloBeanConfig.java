package myspring.di.annotation.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

// xml 과 같은 설정역할을 하게 해주는 어노테이션. 메모리 상 우선적으로 적재되게 함
@Configuration
@ComponentScan(basePackages = {"myspring.di.annotation"})
@PropertySource(value = "classpath:values.properties")
public class HelloBeanConfig {
	
}
