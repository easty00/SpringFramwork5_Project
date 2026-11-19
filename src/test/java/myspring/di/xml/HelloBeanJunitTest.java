package myspring.di.xml;

// Assertions 클래스의 모든 static 메서드를 임포트한다
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.GenericXmlApplicationContext;

public class HelloBeanJunitTest {
	
	BeanFactory factory;
	
	@BeforeEach
	void container() {
		//1. Bean Container 객체생성
		factory = new GenericXmlApplicationContext("classpath:spring-beans.xml");
		
	}
	
	@Test
	void hello() {

		//2. Hello Bean을 Container 에게 요청하기. Bean 의 아이디를 기준으로 Bean 요청
		Hello helloById = (Hello)factory.getBean("hello");
		//2-1 Hello Bean을 Container 에게 요청하기. Bean 의 아이디와 타입을 기준으로 Bean 요청. 이쪽을 더 많이 씀.
		Hello helloByType = factory.getBean("hello",Hello.class);
		
		// 2개의 Hello Bean 의 주소 비교
		System.out.println(helloById == helloByType);
		// assertSame() 메서드 사용하여 주소 비교
		// static 메서드를 사용할 때 : static import를 사용하게 됨. 사용하면 클래스 이름을 생략할 수  있음.
		assertSame(helloById, helloByType);
		
		//setName() Injection을 검증하기 - 값 비교
		assertEquals("Hello 스프링", helloByType.sayHello());
		
		//setPrinter() Injection 을 검증하기
		helloByType.print();
		
		// 투스트링? 가져와야 한다고 함
		// 3. StringPrinter Bean 요청하기
		// 스트링 프린터의 상위 클래스인 프린터 클래스를 요청해도 됨(다형성)
		Printer printer = factory.getBean("strPrinter", Printer.class);
		
		assertEquals("Hello 스프링", printer.toString());
		
	}
	
}
