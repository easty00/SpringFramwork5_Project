package myspring.di.xml.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;

import myspring.di.xml.ConsolePrinter;
import myspring.di.xml.Hello;
import myspring.di.xml.Printer;
import myspring.di.xml.StringPrinter;

@Configuration
@PropertySource(value = "classpath: values.properties")
public class HelloConfig {
	
	// 이전엔 달러로 감쌌었는데 이번엔 좀 다름
	@Autowired
	Environment env; //환경변수 객체를 생성하여 인젝션해줌

	// 메서드 선언
	@Bean
	public Printer stringPrinter() {
		return new StringPrinter();
	}
	
	@Bean
	public Printer consolPrinter() {
		return new ConsolePrinter();
	}
	
	@Bean
	@Scope
	public Hello hello() {
		Hello hello = new Hello();
		//hello.setName("스프링") << 이지만 하드코딩임. 변동이 많은 값을 env 로 따로 뺀것.
		hello.setName(env.getProperty("app.xmlsetter.name")); //스프링 으로 해도되지만 env  사용
		hello.setPrinter(stringPrinter());	 //printer타입. 위의 bean과 동일. 
		return hello;
	}
	
}
