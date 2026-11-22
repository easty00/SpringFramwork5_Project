# LG CNS InspireCamp 7기 백엔드 수업 - Spring Framework

## 폴더 구성

| 경로 | 내용 |
|---|---|
| `src/main/java/myspring/di/xml` | 수업 예제 - XML 설정으로 DI (Hello, Printer, StringPrinter, ConsolePrinter) |
| `src/main/java/myspring/di/annotation` | 수업 예제 - 어노테이션으로 DI |
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
| 실습2 | DI 전략2 - 어노테이션과 XML 설정을 혼합해서 설정하기 | - | - | 예정 |
| 실습3 | DI 전략3 - Java Configuration 방식으로 설정하기 | - | - | 예정 |

---

## STS 실습 내역

### 2026-11-23 ::: [Spring 실습1] Dependency Injection 전략1 - 모든 Configuration을 XML로 설정하기

- 파일
  - 제공 코드 : `mylab.order.di.xml` (Product, ShoppingCart, OrderService)
  - 직접 작성 : `src/main/resources/mylab-order-di.xml`, `src/test/java/mylab/order/di/xml/OrderSpringTest.java`
- 소요시간 : 약 2시간
- 참고한 예제
  - `src/main/resources/spring-beans.xml` : setter injection(`property`), constructor injection(`constructor-arg`), 목록 주입(`list`)
  - `src/test/java/myspring/di/xml/HelloBeanSpringTest.java` : `@ExtendWith`, `@ContextConfiguration`, `@Autowired`, `assertEquals`

#### 빈 구성

| 빈 id | 클래스 | 주입 방식 | 넣은 값 |
|---|---|---|---|
| product1 | Product | setter injection | id `P001`, name `노트북`, price `150000` |
| product2 | Product | constructor injection | `P002`, `스마트폰`, `800000` |
| shoppingCart | ShoppingCart | setter injection (list) | products = [product1, product2] |
| orderService | OrderService | setter injection (ref) | shoppingCart = shoppingCart 빈 |

- 어떻게 풀었는지
  - **mylab-order-di.xml**
    - 수업 예제 `spring-beans.xml`을 복사해서 맨 위 `<beans ...>` 부분은 그대로 두고 빈만 새로 작성했다.
    - 빈의 `id`(스프링이 객체를 찾을 때 쓰는 이름표)와 Product의 `id` 필드(상품 번호)를 헷갈려서, 처음에 빈 id에 `P001`을 넣었다. 빈 id는 `product1`, 상품 번호는 `<property name="id" value="P001"/>`로 넣어야 한다.
    - `<property name="xxx">`를 쓰면 스프링이 객체를 만든 뒤 `setXxx()`를 대신 호출해 준다. 이게 setter injection이다. 값은 `value`, 다른 빈은 `ref`로 넣는다.
    - `<constructor-arg index="...">`의 index는 생성자 괄호 순서(0부터)다. Product 생성자가 `(String id, String name, double price)`라서 세 개를 순서대로 맞춰야 했다.
    - 값 끝에 띄어쓰기(`"스마트폰 "`)가 들어가면 테스트에서 "스마트폰"과 다르다고 실패할 수 있어서 지웠다.
    - shoppingCart와 orderService 빈의 class와 내용을 반대로 넣었었다. `setProducts()`가 있는 ShoppingCart에 상품 목록을, `setShoppingCart()`가 있는 OrderService에 장바구니 빈을 넣어야 한다.
    - 목록 주입은 글자 목록이면 `<value>`, 빈 목록이면 `<ref bean="..."/>`를 쓴다. 테스트에서 순서를 확인하므로 product1 → product2 순서로 넣었다.
    - 문서에서 복사한 둥근 따옴표(`”`)는 XML에서 에러가 나서 직접 다시 쳤다.
  - **OrderSpringTest**
    - `@ExtendWith(SpringExtension.class)`는 테스트할 때 스프링을 같이 켜 주고, `@ContextConfiguration(locations = "classpath:mylab-order-di.xml")`은 어떤 설정 파일을 읽을지 정한다.
    - `@Autowired`를 붙이면 스프링이 타입이 맞는 빈을 찾아서 넣어 준다. 직접 `new`를 하지 않아도 된다. 예제는 Hello 타입 빈이 두 개라 `@Qualifier`가 필요했지만, 이번엔 ShoppingCart, OrderService 빈이 하나씩이라 `@Autowired`만 썼다.
    - 예제의 `@Disabled`는 테스트를 건너뛰는 표시라서 지웠다. 같은 이름의 테스트 메서드는 두 개 만들 수 없다.
    - 테스트 클래스는 shoppingCart, orderService만 받는다. .getName()의 위치를 찾는데 시간이 소요되었다. 상품은 `shoppingCart.getProducts().get(0).getName()`처럼 점으로 한 단계씩 들어가서 꺼냈다.
    - `assertNotNull()`로 빈이 제대로 주입됐는지, `orderService.getShoppingCart()`로 장바구니가 꽂혔는지 확인했다.
    - `assertEquals(기대값, 실제값)`에서 숫자인 기대값을 따옴표로 감싸면 글자가 되어 숫자와 비교할 때 실패한다. `calculateOrderTotal()`은 double이라 기대값을 `950000.0`으로 작성해야한다.
