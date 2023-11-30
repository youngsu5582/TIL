### Builder Pattern
- 생성 패턴
- 복잡한 객체 생성 과정 과 표현 방법을 분리해 다양한 구성 인스턴스 만드는 패턴
- 매개변수를 하나하나 받아들이고 , 통합 빌드해서 객체를 생성하는 방식

### 탄생 배경?
#### Example
House 객체를 만드는 방법?
- 간단한 집은 네 개의 벽 , 바닥 , 문 , 창문 , 지붕으로 이루어져 있다.
- 뒤뜰 , 기타 물품 ( 난방 시스템 or 배관 or 전기 배선 들은? ) 이 있는 현대집은?
#### 해결책 1. House Class 를 확장
- 매개변수 모든 조합 포함하는 자식 Class들을 만들자!
	-> 수많은 자식 클래스를 초래
=> 새로운 매개 변수 추가할 때마다 계층 구조는 더욱 복잡해짐
#### 해결책 2. House Class 에 모든 매개변수 포함한 거대 생성자 생성
- 자식 클래스들의 필요성을 제거하나 , 다른 문제점들이 발생!
```typescript
class House{
	constructor(windows,doors,rooms,hasGrage,hasSwimPool,hasStatus,hasGarden...){
	}
}

const oldHouse = new House(4,2,4,true,null,null,null...)
const newHouse = new House(4,2,4,true,true,true,true,...)
```
=> 어마어마하게 비효율 적이고 못생긴 코드!
- 소수에서만 사용되는 매개변수 존재 할 수 있음!

=> Builder Pattern 을 사용해 , 생성하는 부분을 분리하자!!

#### Builder
![500](https://i.imgur.com/3MPCStM.png)
- 복잡한 객체를 단계별로 생성 ( step-by-step )
	-> 최종적으로 build 메소드를 통해 , 인스턴스를 생성해 리턴!
- 제품이 생성되는 동안 다른 객체가 제품 접근하는 것을 허용하지 않음! - 불변성 보장 
	( Setter 함수는 협업 과정 중 다른 곳에서 객체 조작을 할 여지가 있다!)
=> 객체 특정 설정 제작하는데 필요한 단계만 호출

```typescript
const myHouse : House = new HouseBuilder()
								.buildWalls(4)
								.buildDoors(2)
								.buildWindwos(6)
								.buildGarage(True)
								.getResult()
```

#### Naming Pattern
##### 멤버이름()
```typescript
new HouseBuilder().walls(4).doors(2).getResult();
```
##### set멤버이름()
```typescript
new HousdeBuilder().setWalls(2).setDoors(3);
```
##### with멤버이름()
```typescript
new HousdeBuilder().withWalls(2).withDoors(3);
```

=> 첫번째가 제일 무난함 ( 언어적 네이밍에도 무관 )

### 장점
#### 1. 객체 생성 과정을 일관된 프로세스로 표현
- 생성자 방식은 객체 생성하는 경우 매개변수 많아질수록 가독성이 급격하게 떨어짐
- 클래스 변수는 4개 이상만 되도 인자 순서마다 값이 어던 멤버에 해당되는지 파악하기 힘듬!
( Named Parameter 를 사용하더라도 이는 매우 비효율적! - 몰라도 되는 것 까지 전부 알아야 하므로 )

```typescript
// 생성자 방식
Student student1 = new Student(2016120091, "홍길동", "freshman", "010-5555-5555");

// 빌더 방식
Student student2 = new StudentBuilder()
            .id(2016120091)
            .name("임꺽정")
            .grade("Senior")
            .phoneNumber("010-5555-5555")
            .build();
```
- 어떤 데이터에 어떤 값이 설정되는지 직관적 파악 가능!
- 연속되는 동일 타입 매개 변수 많이 설정할 시 발생하는 설정 오류 실수를 방지 가능!

=> 뿐만 아니라 공통되는 매개변수 및 기능도 설정 가능하게 해준다!
```kotlin
mail
  .enableSmsFallbackFeature("010-1234-5678")
  .enableForceSendFeatureForCompliancePurpose()
  .enableScheduleSendFeature(Instant.now().plus(Duration.ofHours(2)))
  .enableRetryFeature()
  .send(
    recipient = "jaeeun.na@tosspayments.com",
    title = "안녕하세요",
    body = "메일 본문입니다"
  )
```
#### 2. 디폴트 매개변수 생략 간접적 지원
- Python , JS&TS 는 Default Parameter 지원
```java
class Student{
    private int id;
    private String name;
    private String grade = "freshman"; // 디폴트 매개변수 역할
    private String phoneNumber;
    public Student(int id, String name, String grade, String phoneNumber) {
        ...
    }
    // 디폴트 매개변수를 제외한 인자들을 받는 생성자 오버로딩
    public Student(int id, String name, String phoneNumber) {
        ...
    }
}
```
- 이렇게 , 생성자 오버로딩 통해 본래 문제점으로 회귀가 된다.
``` java
class StudentBuilder {
    private int id;
    private String name;
    private String grade = "freshman"; // 디폴트 매개변수 역할
    private String phoneNumber;
    ...
}
```
- 필드 설정 메소드 호출하지 않는 방식으로 간접적 구현 가능!
#### 3. 필수 멤버와 선택적 멤버 분리 가능
- 객체 인스턴스는 목적 따라 초기화 필수인 멤버 변수 있고 , 선택적 멤버 변수 있을 수 있음!
```java
class StudentBuilder{
    public StudentBuilder(int id) {
        this.id = id;
    }
}
```
- 초기화 필수인 멤버는 빌더 생성자로 받게 해서 필수 멤버 설정해주어야 빌더 객체 생성하도록 유도
#### 4. 객체 생성 단계 지연
- 단계별로 구성하거나 , 구성 단계 지연하거나 재귀적 생성 처리 가능
- 빌더 재사용 함으로써 객체 생성 주도적 지연 가능
```java
List<StudentBuilder> builders = new ArrayList<>();
builders.add(
    new StudentBuilder(2016120091)
    .name("홍길동")
);
for(StudentBuilder b : builders) {
    Student student = b.build();
    System.out.println(student);
}
```
- 빌더 리스트를 순회하며 , 최종 객체 생성 주도 가능!
#### 5. 초기화 검증을 멤버별 분리
생성자로 부터 멤버값 받는 형태면 , 각 생성자 매개변수 대한 검증 로직을 생성자 메소드 마다 복잡하게 구현해야 한다.
	-> 생성자 크기가 비대해지는 결과를 낳게 됌!
```java
class StudentBuilder { 
	...
    public StudentBuilder grade(String grade) {
        if (!grade.equals("freshman") && !grade.equals("sophomore") && !grade.equals("junior") && !grade.equals("senior")) {
            throw new IllegalArgumentException(grade);
        }
        this.grade = grade;
        return this;
    }
    public StudentBuilder phoneNumber(String phoneNumber) {
        if (!phoneNumber.startsWith("010")) {
            throw new IllegalArgumentException(phoneNumber);
        }
        this.phoneNumber = phoneNumber;
        return this;
    }
}
```
- 멤버 변수 초기화와 검증을 , 각각 멤버별로 분리해서 작성 가능
- 유지 보수 용이
#### 6. 멤버에 대한 변경 가능성 최소화 추구

일반적으로 , 멤버 값 할당할 때 사용하는 것이 Setter Method!
=> 클래스 멤버 초기화를 Setter 를 통해 구성하는 것은 매우 좋지 않은 방법이다!!

- 프로그램 개발하는데 있어 가장 중요시되는 점이 불변 객체이다!
-> 불변 객체는 오로지 읽기(get) 메소드만 제공하며 쓰기(set)은 제공하지 않는다! - Java 의 final Keyword
##### Why?
- 가변 객체를 통해 작업 하는 도중 예외 발생하면 해당 객체가 불안정항 상태에 빠질 수 있어 또 다른 에러 유발 위험성이 있다!
- 불변 객체로 구성하면 다른 사람이 개발한 함수 위험없이 이용을 보장할 수 있어 현업 + 유지보수에도 유용

=> 가변적이여야 하는 타당한 이유가 없으면 반드시 불변으로 만들어야 함!
( 불변 만드는 것이 불가능하면 가능한 변경 가능성 최소화 )

∴ 빌더 패턴은 생성자 없이 객체에 대해 '변경 가능성 최소화' 추구해 불변성을 가지게 해준다.
### 단점
#### 1. 코드 복잡성 증가
- 빌더 패턴 적용하려면 N 개 Domain : N 개 Builder 를 만들어야 한다.
	-> 클래스 수가 기하급수적 늘어나 , 관리가 힘들고 구조가 복잡해질 수 있다!
- 선택적 매개변수 많이 받는 객체 생성 위해 먼저 빌더 클래스 부터 정의해야함!
#### 2. 생성자 보다 성능이 떨어진다
- 매번 메소드 호출해 빌더 거쳐 인스턴스화 하기 때문에 , 당연한 일
- 생성 비용 자체는 크지 않지만 , 성능이 극 중요시 되는 상황일 시 고려해봐야함.
#### 3. 지나친 빌더 남용 금지
- 클래스 필드 개수 4개 보다 적고 , 필드 변경 가능성이 없는 경우라면 비효율적
	( 오히려 빌더 패턴 코드가 더욱 장황할 수도 있기 때문 )
	-> 생성자나 , 정적 팩토리 메소드를 이용하는 것을 추천
=> 필드 갯수 와 필드 변경 가능성을 중점으로 보고 패턴 적용 유무를 가리자
( API 는 시간 지날수록 많은 매개변수 갖는 경향이 있으므로 빌더 패턴으로 시작하는 편이 나을 수도 있다 )

###  By GPT
  
	빌더 패턴(Builder Pattern)은 복잡한 객체의 생성 과정을 단계별로 분리하여, 최종 객체를 구성하는 디자인 패턴입니다. 이 패턴은 객체 생성과 객체 표현을 분리하여, 동일한 생성 과정을 거쳐도 다른 표현 결과를 얻을 수 있도록 합니다. 빌더 패턴은 주로 객체의 생성이 복잡하거나 여러 단계를 거쳐야 할 때 유용하게 사용됩니다.
	
	### 빌더 패턴의 주요 구성 요소
	
	1. **Builder(빌더 인터페이스)**: 객체의 각 부분을 생성하는 단계별 메서드를 정의합니다.
	2. **ConcreteBuilder(구체적인 빌더 클래스)**: Builder 인터페이스를 구현하며, 실제 객체의 부분들을 생성하고 조립하는 메서드를 제공합니다.
	3. **Product(생성될 객체)**: 최종적으로 생성될 복잡한 객체입니다.
	4. **Director(디렉터)**: Builder 인터페이스를 사용하여 객체의 여러 부분을 조립하는 순서를 정의합니다. (선택적으로 사용)
	
	### 빌더 패턴의 특징
	
	- **가독성 향상**: 객체 생성 과정이 명확하고 읽기 쉽습니다.
	- **유연성**: 동일한 생성 과정에서 다양한 표현의 객체를 생성할 수 있습니다.
	- **안정성**: 필요한 속성만 설정하고, 나머지는 기본값을 사용할 수 있어 안정적인 객체 생성이 가능합니다.
### 참고 출처

##### Writed By Obisidan