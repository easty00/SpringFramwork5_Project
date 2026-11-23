# LG CNS InspireCamp 7기 백엔드 수업 - Spring Framework

## 폴더 구성

| 경로 | 내용 |
|---|---|
| `src/main/java/myspring/di/xml` | 수업 예제 - XML 설정으로 DI (Hello, Printer, StringPrinter, ConsolePrinter) |
| `src/main/java/myspring/di/annotation` | 수업 예제 - 어노테이션으로 DI |
| `src/main/java/myspring/di/*/config` | 수업 예제 - Java Configuration으로 DI |
| `src/main/resources/spring-beans.xml` | 수업 예제 XML 설정 파일 |
| `src/test/java/myspring/di` | 수업 예제 테스트 (JUnit, Spring Test) |
| `src/main/java/mylab` | 실습 과제 |
| `src/main/resources/mylab-*.xml` | 실습 과제 XML 설정 파일 |
| `src/test/java/mylab` | 실습 과제 테스트 |
| `sql` | 수업용 SQL 스크립트 |

## 실습 진행 현황

| 실습 | 주제 | 패키지 | 제출 기한 | 상태 |
|---|---|---|---|---|
| 실습1 | DI 전략1 - 모든 Configuration을 XML로 설정하기 | `mylab.order.di.xml` | 11/24(화) | 완료 |
| 실습2 | DI 전략2 - 어노테이션과 XML 설정을 혼합해서 설정하기 | `mylab.user.di.annot` | - | 완료 |
| 실습3 | DI 전략3 - Java Configuration 방식으로 설정하기 | `mylab.notification.di.annot` | - | 완료 |

---

## STS 실습 내역

### 2026-11-23 ::: [Spring 실습1] Dependency Injection 전략1 - 모든 Configuration을 XML로 설정하기

- 파일
  - 제공 코드 : `mylab.order.di.xml` (Product, ShoppingCart, OrderService)
- 소요시간 : 약 2시간
- 참고한 예제
  - `src/main/resources/spring-beans.xml` : 수업 때 만든 Hello 빈 설정
  - `src/test/java/myspring/di/xml/HelloBeanSpringTest.java` : 수업 때 만든 스프링 테스트

#### 핵심 하나: XML에 "이 클래스 객체를 스프링이 대신 만들어 줘"라고 적는 것이다.

- `<bean id="..." class="...">` → 이 클래스 객체를 스프링이 만든다. `id`는 그 객체에 붙이는 이름표다.
- `<property name="xxx" value="...">` → 스프링이 객체를 만든 다음 `setXxx(값)`을 대신 불러 준다. (setter injection)
- `<constructor-arg index="0" value="...">` → 객체를 만들 때 생성자 괄호에 값을 넣어 준다. index는 괄호 안 순서다(0부터). (constructor injection)
- `value` → 글자나 숫자를 넣을 때 / `ref` → 다른 빈(객체)을 넣을 때

#### 빈 구성

| 빈 이름표(id) | 클래스 | 넣는 방법 | 넣은 값 |
|---|---|---|---|
| product1 | Product | setter | id `P001`, name `노트북`, price `150000` |
| product2 | Product | 생성자 | `P002`, `스마트폰`, `800000` |
| shoppingCart | ShoppingCart | setter (목록) | product1, product2 |
| orderService | OrderService | setter (ref) | shoppingCart 빈 |

#### 원리

- 지금까지는 `new Product(...)`처럼 객체를 내가 직접 만들었는데, 이번엔 XML에 적어 두면 스프링이 만들고 서로 연결까지 해 준다. 이게 DI(의존성 주입)다.
- 장바구니에는 상품이 여러 개라 `<list>` 안에 상품 빈 두 개를 넣었다. 글자 목록이면 `<value>`, 빈 목록이면 `<ref bean="..."/>`를 쓴다.
- 주문 서비스에는 장바구니 빈을 `ref`로 꽂았다. 그래서 주문 서비스가 장바구니 안의 상품 가격을 다 더할 수 있다.

#### 테스트 원리

- `@ExtendWith(SpringExtension.class)` → 테스트를 돌릴 때 스프링도 같이 켜 준다.
- `@ContextConfiguration(locations = "classpath:mylab-order-di.xml")` → 어떤 XML을 읽을지 알려 준다. `classpath:`는 resources 폴더에서 찾으라는 뜻이다.
- `@Autowired` → 스프링이 만든 빈 중에서 타입이 맞는 걸 찾아서 넣어 준다. `new`를 안 해도 된다.
- `assertNotNull(x)` → x가 비어 있지 않으면 통과 / `assertEquals(기대값, 실제값)` → 둘이 같으면 통과

#### 헷갈렸던 것

- 빈 이름표(`product1`)랑 상품 번호(`P001`)를 헷갈려서 처음에 빈 id에 P001을 넣었다.
- Product 생성자는 `(id, name, price)` 세 칸인데 두 칸만 넣었었다. index 순서대로 세 개를 맞춰야 한다.
- shoppingCart 빈과 orderService 빈의 class와 내용을 반대로 넣었었다. 상품 목록은 장바구니에, 장바구니는 주문 서비스에 넣는다.
- 문서에서 복사한 둥근 따옴표(`”`)는 XML에서 에러가 나서 직접 다시 쳤다.
- 값 끝에 띄어쓰기(`"스마트폰 "`)가 들어가면 테스트에서 다르다고 실패할 수 있다.
- 예제의 `@Disabled`는 "이 테스트는 건너뛰어라"라는 표시라서 지웠다.
- 테스트 클래스는 shoppingCart, orderService만 받는다. .getName()의 위치를 찾는데 시간이 소요되었다. 상품은 `shoppingCart.getProducts().get(0).getName()`처럼 점으로 한 단계씩 들어가서 꺼냈다.
- `assertEquals(기대값, 실제값)`에서 숫자인 기대값을 따옴표로 감싸면 글자가 되어 숫자와 비교할 때 실패한다. `calculateOrderTotal()`은 double이라 기대값을 `950000.0`으로 작성해야한다.

---

### 2026-11-24 ::: [Spring 실습2] Dependency Injection 전략2 - 어노테이션과 XML 설정을 혼합해서 설정하기

- 파일
  - 제공 코드 : `mylab.user.di.annot` (UserService, SecurityService, UserRepository) → 어노테이션만 추가
  - 직접 작성 : `src/main/resources/mylab-user-di.xml`, `src/test/java/mylab/user/di/annot/UserServiceTest.java`
- 소요시간 : 약 1시간
- 참고한 예제
  - `src/main/java/myspring/di/annotation/HelloBean.java` : 수업 때 어노테이션 붙인 클래스
  - `src/main/resources/spring-beans-annot.xml` : 수업 때 만든 컴포넌트 스캔 설정
  - `src/test/java/mylab/order/di/xml/OrderSpringTest.java` : 실습1에서 쓴 테스트

#### 핵심 하나: 어노테이션은 바로 아래 한 줄에만 효과가 있다. 그래서 "어디 위에 붙이냐"가 중요하다.

- `@Component` → 클래스 위 (이 클래스를 스프링이 만들어라) = XML의 `<bean>`
- `@Autowired` → 다른 객체를 받을 필드 위 (빈을 찾아서 넣어라) = XML의 `<property ref="...">`
- `@Value` → 값을 받을 필드 위 (이 값을 넣어라) = XML의 `<property value="...">`
- 메서드 위에는 이번 실습에서 아무것도 안 붙인다.

#### mylab-user-di.xml 원리

- `<context:component-scan base-package="...">` → 그 패키지(와 그 아래 패키지)를 뒤져서 `@Component`가 붙은 클래스를 전부 빈으로 등록한다. 실습1에서 `<bean>`을 네 번 쓰던 걸 이 한 줄이 대신한다.
- 그래서 이번 XML에는 `<bean>` 태그가 하나도 없다. 이게 "XML 설정을 최소화"하는 전략2다.
- 머리 부분의 `xmlns:context`랑 `spring-context.xsd`는 `<context:...>` 태그를 쓰려면 꼭 있어야 하는 선언이다. 예제를 복사하면 이미 들어 있다.

#### 테스트 원리

- UserService 하나만 받으면 된다. UserRepository, SecurityService는 UserService 안에 들어 있어서 `getUserRepository()`처럼 꺼내서 확인한다.
- `userService.getUserRepository().getDbType()` → 점 하나가 한 단계씩 안으로 들어가는 것이다. 마지막에 "MySQL"이 나오면 `@Value`가 잘 들어간 것이다.
- `registerUser(아이디, 이름, 비밀번호)` → 비밀번호가 있으면 저장하고 true, 비밀번호가 비어 있으면 false다. 그래서 하나는 `assertTrue`(true면 통과), 하나는 `assertFalse`(false면 통과)로 확인한다.

#### 헷갈렸던 것

- `@Value`를 메서드 위에 붙이고, dbType 필드에는 `@Autowired`를 붙였었다. 다른 객체를 넣을 땐 `@Autowired`, "MySQL" 같은 글자 값을 넣을 땐 `@Value`다.
- SecurityService는 받을 것도 넣을 값도 없어서 클래스 위 `@Component` 하나면 됐다.
- UserService에 setter가 없어서 괜찮은지 헷갈렸는데, 필드 위에 `@Autowired`를 붙이면 스프링이 필드에 바로 넣어 준다.
- `registerUser(userId, name, password)`처럼 이름표를 그대로 넣었다. 메서드를 부를 때는 `"userId"`처럼 실제 값을 넣어야 한다.

---

### 2026-11-24 ::: [Spring 실습3] Dependency Injection 전략3 - Java Configuration 방식으로 설정하기

- 파일
  - 제공 코드 : `mylab.notification.di.annot` (NotificationService, EmailNotificationService, SmsNotificationService, NotificationManager)
  - 직접 작성 : `src/main/java/mylab/notification/di/annot/NotificationConfig.java`, `src/test/java/mylab/notification/di/annot/NotificationConfigTest.java`
- 소요시간 : 약 2시간
- 참고한 예제
  - `src/main/java/myspring/di/xml/config/HelloConfig.java` : 수업 때 만든 자바 설정 클래스
  - `src/test/java/myspring/di/xml/config/HelloConfigTest.java` : 수업 때 만든 자바 설정 테스트
  - JavaSE_Project `src/mylab/bank/control/BankDemo.java` 33번 줄 : `(SavingsAccount)` 붙여서 상자 타입 바꾸기

#### 핵심 하나: XML 대신 자바 클래스 하나에 빈을 전부 적는다.

- `@Configuration` → 클래스 위 (이 클래스가 XML 설정 파일 대신이다)
- `@Bean` → 메서드 위 (이 메서드가 return하는 객체를 빈으로 등록해라). 메서드 이름이 빈 이름표가 된다.
- 실습1은 XML, 실습2는 어노테이션 + XML, 실습3은 자바 클래스로 설정한다. 결국 "스프링이 객체를 만들고 연결해 준다"는 건 셋 다 같다.

#### 빈 구성

| 빈 이름표(메서드 이름) | 클래스 | 생성자에 넣은 값 |
|---|---|---|
| emailNotificationService | EmailNotificationService | `"smtp.gmail.com"`, `587` |
| smsNotificationService | SmsNotificationService | `"SKT"` |
| notificationManager | NotificationManager | 이메일 빈, SMS 빈 |

#### NotificationConfig 원리

- `@Bean` 메서드 안에서 `new`로 객체를 만들어서 return하면, 그 객체가 빈이 된다.
- 이메일, SMS 서비스는 생성자로 값을 받는다. 587은 숫자라 따옴표 없이 쓴다.
- NotificationManager는 생성자로 이메일 서비스, SMS 서비스를 받는다. 괄호 안에 위에서 만든 `@Bean` 메서드를 불러서 넣는다.
- 메서드를 또 부르면 새로 만들 것 같지만, `@Configuration` 클래스 안에서는 스프링이 이미 만들어 둔 걸 돌려준다.

#### 테스트 원리

- 설정이 XML이 아니라 클래스라서 `@ContextConfiguration(classes = NotificationConfig.class)`로 쓴다. (`class`가 아니라 `classes`)
- `getEmailService()`는 NotificationService 상자로 돌려준다. 이 상자에서는 `sendNotification()`밖에 안 보여서 `getSmtpServer()`를 바로 못 쓴다.
- 그래서 앞에 `(EmailNotificationService)`를 붙여서 이메일 서비스 상자로 바꿔 담은 다음에 쓴다. 자바 실습3에서 `(SavingsAccount)` 붙인 것과 같다. SMS도 똑같이 `(SmsNotificationService)`를 붙인다.
- 마지막에 `sendNotificationByEmail()`, `sendNotificationBySms()`를 불러서 콘솔에 메시지가 찍히는지 본다.

#### 헷갈렸던 것

- NotificationManager를 만들 때 `new`를 빠뜨렸고, `return notificationManager();`처럼 괄호를 붙여서 자기 자신을 다시 불렀다. 만든 상자를 돌려줄 때는 괄호 없이 `return notificationManager;`로 쓴다.
- 테스트 파일을 `src/main/java`에 만들어서 import에 빨간 줄이 생겼다. JUnit이랑 스프링 테스트 도구는 `src/test/java`에서만 쓸 수 있게 pom.xml에 정해져 있어서, `src/test/java`에 같은 이름 패키지를 만들고 옮겼다.
- `assertNotNull` 같은 import는 Ctrl+Shift+O로 잘 안 들어와서 이전 테스트 파일에서 복사했다.
- 형변환할 때 왼쪽에 담을 상자(`EmailNotificationService emailService =`)를 안 만들어서 다음 줄에서 `emailService`를 못 썼다.
