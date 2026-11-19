package myspring.di.xml;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.GenericXmlApplicationContext;

public class HelloBeanJunitTest {
	
	@Test
	void hello() {
		//1. Bean Container 객체생성
		BeanFactory factory = new GenericXmlApplicationContext();
	}
	
}
