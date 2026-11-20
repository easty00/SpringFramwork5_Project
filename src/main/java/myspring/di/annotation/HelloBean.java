package myspring.di.annotation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/*
 * 전략2 ( 어노테이션  + XML 같이 사용하는 방식)
 * Setter Injection
 * - 변수 별로 각각 주입받는 방식
 */


// @Component == <bean></bean?
@Component 
public class HelloBean {
	
	//@Value == <property name="name" value="어노테이션"/>
	@Value("${app.setter.name}")
	String name;
	
	// @Autowired == <property name="printer" ref="stringPrinter"/>
	// @Qualifier
	@Autowired
	@Qualifier("stringPrinter")
	PrinterBean printer;
	
	@Value("#{'${app.namelist}'.split(',')}") 	// Java, SpringFW, SpringBoot 이런식으로 글자자름
	List<String> names;

	public HelloBean() {
		System.out.println(this.getClass().getName() + " 기본생성자 호출됨!");
	}

	public List<String> getNames() {
		return this.names;
	}

	public void setNames(List<String> list) {
		this.names = list;
	}

	public String sayHello() {
		return "Hello " + name;
	}

	public void print() {
		this.printer.print(sayHello());
	}

}
