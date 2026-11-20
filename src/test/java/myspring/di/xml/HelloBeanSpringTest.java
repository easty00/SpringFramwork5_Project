package myspring.di.xml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.annotation.Resource;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "classpath:spring-beans.xml")
public class HelloBeanSpringTest {
	@Autowired
	Hello hello;
	// @Autowired : Bean 의 Class Type 로 의존하는 객체를 찾는 방식
	@Autowired
	@Qualifier("helloCons")
	Hello hello2;
	
	// @Resource : Bean의 name(id) 값으로 의존 객체를 찾는 방식 
	@Resource(name = "helloCons")
	Hello hello3;
	
	@Test @Disabled
	void helloBean() {
		assertEquals("Hello 스프링", hello.sayHello());
		
		assertEquals("Hello 생성자", hello2.sayHello());
		
		assertEquals("Hello 생성자", hello3.sayHello());
		
		// 콘솔프린터가 호출됨
		hello2.print();
	}	
	
	// 반복문 돌리기
	// getNames 를  Hello 에서 불러오기
	// 리스트용 테스트
	@Test 
	void list() {
		for(String value:hello2.getNames()) {
			System.out.println(value);
		}
		
	}
}
