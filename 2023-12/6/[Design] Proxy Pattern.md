ㄷ 객체는 원격 서버에 존재## Proxy Pattern

- 원본 객체 대리해서 대신 처리하게 함으로써 , 로직 흐름 제어 행동 패턴
##### Proxy
- 사전적인 의미 : 대리인
	-> 누군가에게 어떤 일을 대신 시키는 것

- 왜 사용하는가?
대상 클래스가 민감한 정보를 가지고 있거나 , 인스턴스화 하기에는 무겁거나 추가 기능 하고 싶을때 ,
원본 객체를 수정할 수 없는 상황일 때 극복하기 위해 사용

#### 장점
- 보안 : 프록시는 Client 가 작업 수행할 권한이 있는지 확인 후 , 결과가 긍정적인 경우에만 요청 대상 전달
- 캐싱 : 프록시가 내부 캐시를 유지해 데이터가 캐시에 아직 존재하지 않는 경우에만 대상 작업 실행
- 데이터 유효성 검사 : 프록시가 입력 대상으로 전달하기 전 유효성 검사
- 지연 초기화 : 대상 생성 비용이 비싸다면 , 프록시는 그것을 필요로 할 때 까지 연기할 수 있다.
- 로깅 : 프록시는 메소드 호출과 상대 매개 변수를 인터셉트하고 이를 기록한다.
- 원격 객체 : 프록시는 원격 위치에 있는 객체를 가져와서 로컬처럼 보이게 할 수 있다.

### 프록시 패턴 구조

- 다른 객체에 대한 접근 제어하는 개체
![500](https://i.imgur.com/DcPX8VD.png)

- Subject : Proxy 와 RealSubject 를 하나로 묶는 인터페이스
	- 대상 객체와 프록시 역활을 동이랗게 해주는 추상 메소드 operation 정의
	- 인터페이스가 있기 때문에 , 클라이언트는 Proxy 역활과 RealSubject 역활 차이 의식 필요 X
- RealSubject : 원본 대상 객체
- Porxy : 대상 객체 ( RealSubject ) 를 중계할 대리자 역활
	- 프록시는 대상 객체를 합성 ( composition )
	- 프록시는 대상 객체와 같은 이름 메소드 호출해 , 별도 로직 수행 가능! ( 인터페이스 구현 메소드 )
	- 프록시는 흐름제어만 할 뿐 , 결과값 조작하거나 변경시키면 안 된다.
- Client : Subject Interface 를 이용해 프록시 객체 생성해 이용
	- Client 는 Proxy 를 중간에 두고 , 프록시를 통해서 RealSubject 와 데이터를 주고 받는다.

### 프록시 패턴 종류
- 어떠한 로직을 짜느냐 따라 , 활용도는 천차만별
#### 기본형 프록시
```typescript
interface ISubject {
    void action();
}

class RealSubject implements ISubject {
    public void action() {
        System.out.println("원본 객체 액션 !!");
    }
}
```
```typescript
class Proxy implements ISubject {
    private RealSubject subject; // 대상 객체를 composition

    Proxy(RealSubject subject) {
        this.subject = subject;
    }

    public void action() {
        subject.action(); // 위임
        /* do something */
        System.out.println("프록시 객체 액션 !!");
    }
}

class Client {
    public static void main(String[] args) {
        ISubject sub = new Proxy(new RealSubject());
        sub.action();
    }
}
```

#### 가상 프록시 ( Virtual Proxy )
- Late Initialization ( 지연 초기화 ) 방식
- 가끔 필요하지만 , 항상 메모리 적재 되어 있는 무거운 서비스 객체가 있는 경우
- 구현은 , 실제 객체 새성에 많은 자원 소모 되지만 , 사용 빈도는 낮을 때 쓰는 방식
- 서비스 시작될 때 , 객체 생성 대신에 객체 초기화가 실제 필요 시점에 초기화 되게 지연
``` typescript
class Proxy implements ISubject {
    private RealSubject subject; // 대상 객체를 composition

    Proxy() {
    }

    public void action() {
    	// 프록시 객체는 실제 요청(action(메소드 호출)이 들어 왔을 때 실제 객체를 생성한다.
        if(subject == null){
            subject = new RealSubject();
        }
        subject.action(); // 위임
        /* do something */
        System.out.println("프록시 객체 액션 !!");
    }
}
```
#### 보호 프록시 ( Protection Proxy )
- 프록시가 대상 객체 대한 자원으로부터 Access 제어 ( 접근 권한 )
- 특정 Client 만 서비스 객체를 사용하게 해야 하는 경우
- 프록시 객체를 통해 , 클라이어트 자격 증명이 기준과 일치하는 경우메나 서비스 객체에 요청 전달
```java
class Proxy implements ISubject {
    private RealSubject subject; // 대상 객체를 composition
    boolean access; // 접근 권한

    Proxy(RealSubject subject, boolean access) {
        this.subject = subject;
        this.access = access;
    }

    public void action() {
        if(access) {
            subject.action(); // 위임
            /* do something */
            System.out.println("프록시 객체 액션 !!");
        }
    }
}

class Client {
    public static void main(String[] args) {
        ISubject sub = new Proxy(new RealSubject(), false);
        sub.action();
    }
}
```
#### 로깅 프록시
- 대상 객체에 대한 로깅을 추가하려는 경우
- 프록시는 서비스 메서드 실행하고 전달하기 전에 로깅을 하는 기능 추가해 재정의
```typescript
class Proxy implements ISubject {
    private RealSubject subject; // 대상 객체를 composition

    Proxy(RealSubject subject {
        this.subject = subject;
    }

    public void action() {
        System.out.println("로깅..................");
        
        subject.action(); // 위임
        /* do something */
        System.out.println("프록시 객체 액션 !!");

        System.out.println("로깅..................");
    }
}

class Client {
    public static void main(String[] args) {
        ISubject sub = new Proxy(new RealSubject());
        sub.action();
    }
}
```
#### 원격 프록시 ( Remote Proxy )
- 프록시 클래스는 로컬에 있고 , 대상 객체는 원격 서버에 존재하는 경우
- 프록시 객체는 네트워크 통해 클라이언트 요청 전달해 네트워크 관련된 불필요한 작업들 처리하고 결과값만 반환
- 클라이언트 입장에선 프록시 통해 객체 이용 => 원격 or 로컬이든 신경 쓸 필요 없고 , 프록시는 진짜 객체와 통신 대리
![500](https://i.imgur.com/Jky1vVj.png)

- Proxy 를 Stub 으로 부름
- Proxy 로부터 전달된 명령 이해 + 적합한 메소드 호출 역활 하는 보조객체 Skeleton

#### Caching Proxy
- 데이터가 큰 경우 , 캐싱하고 재사용 유도
- 클라이언트 요청의 결과 캐싱하고 , 캐시의 수명 주기 관리
##### HTTP Proxy
- 웹 서버와 브라우저 사이에서 웹 페이지의 캐싱 실행하는 SW
- 웹 페이지 표시할 때 직접 웹 서버에 그 페이지를 가져오는게 아닌 , Proxy 가 캐시해 페이지를 대신 취득
- 최신 정보가 필요하거나 , 유효기간 지났을 때 웹 서버에 웹 페이지 호출
![500](https://i.imgur.com/o0SoaDL.png)

- 웹 브라우저가 Client , HTTP Proxy 가 Proxy , Web Server 가 Real Subject
### 프록시 패턴 특징
#### 패턴 사용 시기
- 접근 제어하거나 기능 추가하고 싶은데 , 기존 특정 객체 수정할 수 없는 상황일 때
- 초기화 지연 , 접근 제어 , 로깅 , 캐싱 등 기존 객체 동작에 수정 없이 가미하고 싶을 때

#### 패턴 장점
- 개방 폐쇠 원칙 (OCP) 준수
	-> 기존 대상 객체 코드를 변경하지 않고 새로운 기능 추가
- 단일 책임 원칙 (SRP) 준수
	-> 대상 객체는 자신 기능에만 집중 하고 , 부가 기능 제공 역활은 프록시 객체에 위임해 다중 책임 회피
- 원래 하려던 기능을 수행하며 , 부가적 작업 ( 로깅 , 인증 , 네트워크 통신 등 ) 을 수행하는데 유용
- 클라이언트는 객체 신경쓰지 않고 , 서비스 객체 제어하거나 생명 주기 관리 가능
- 사용자 입장에서는 , 프록시 객체 건 실제 객체 이건 사용법이 유사하므로 사용성에 문제 되지 않는다
#### 패턴 단점
- 많은 프록시 클래스 도입해야 하므로 코드 복잡도 증가!
	-> 각각 클래스에 해당하는 프록시 클래스를 만들어야하므로
=> 물론 해당 부분들을 프레임워크들은 멋지게 해결
- 프록시 클래스 자체 들어가는 자원이 많다면 , 서비스로부터 응답이 늦어짐

### Example In Proxy

#### 가상 프록시 구현

```java
class HighResolutionImage {
    String img;

    HighResolutionImage(String path) {
        loadImage(path);
    }

    private void loadImage(String path) {
        // 이미지를 디스크에서 불러와 메모리에 적재 (작업 자체가 무겁고 많은 자원을 필요로함)
        try {
            Thread.sleep(1000);
            img = path;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("%s에 있는 이미지 로딩 완료\n", path);
    }

    @Override
    public void showImage() {
        // 이미지를 화면에 렌더링
        System.out.printf("%s 이미지 출력\n", img);
    }
}
```
- loadImage 는 , 고해상도 이미지 경로를 인자로 받아 메모리 저장
- showImage 는 , 화면 렌더링
##### 문제의 코드
```java
class ImageViewer {
    public static void main(String[] args) {
        HighResolutionImage highResolutionImage1 = new HighResolutionImage("./img/고해상도이미지_1");
        HighResolutionImage highResolutionImage2 = new HighResolutionImage("./img/고해상도이미지_2");
        HighResolutionImage highResolutionImage3 = new HighResolutionImage("./img/고해상도이미지_3");

        highResolutionImage2.showImage();
    }
}
```
- 해당 코드의 문제점은 , 생성될 떄 바로 같이 loadImage 로 Image 로딩
	=> 이미지를 준비하는 과정에서 , 세개의 Image 가 전부 Loading
#### 프록시 패턴 적용 코드
- 사용자가 선택한 이미지만 로드해서 렌더링 하게 대상 객체 제어
- 가상 프록시를 이용해 늦은 초기화 ( Late Initialisation ) 로 실제 객체 사용 시점 제어
```java
interface IImage {
    void showImage(); // 이미지를 렌더링하기 위해 구현체가 구현해야 하는 추상메소드
}
```
- 대상 객체와 프록시 객체를 묶는 인터페이스 => 다형성
```java
class ImageProxy implements IImage {
    private IImage proxyImage;
    private String path;

    ImageProxy(String path) {
        this.path = path;
    }

    @Override
    public void showImage() {
        // 고해상도 이미지 로딩하기
        proxyImage = new HighResolutionImage(path);
        proxyImage.showImage();
    }
}
```
- 생성을 , showImage 하기 전 실행
	=> 실제 메소드 호출 시점에 메모리 적재가 이루어짐 ( 불필요한 자원 낭비 발생 X )

사담 : 사실 , 이게 진짜 좋은 기법인지는 잘 모르겠다.
물론 , 당장 메모리를 절약한다는 면은 있지만 결국 쓰게 될거면 
생성자에서 모두 초기화 하는게 속도면에서는 이득일 수 있을 거 같기 떄문이다.

#### 보호 프록시 패턴 구현하기
- 인사팀에서 보안을 위해 데이터 접근을 직책별 세분화
- 상위 계급의 정보는 열람 불가능하게!
- 기존 프로그램 로직 수정하기에는 너무 방대하고 , 난관에 부딪힘
```java
enum RESPONSIBILITY {
    STAFF, // 사원
    MANAGER, // 과장
    DIRECTOR // 상무
}
```
- 회사의 직책 표현하는 Enum
```java
// 구성원 클래스
class Employee {
    private String name; // 이름
    private RESPONSIBILITY position; // 직위

    public Employee(String name, RESPONSIBILITY position) {
        this.name = name;
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public RESPONSIBILITY getGrade() {
        return position;
    }

    public String getInfo(Employee viewer) {
        return "Display " + getGrade().name() + " '" + getName() + "' personnel information.";
    }
}
```
- 직원 정보 Class 화
- 구성원 이름 , 직책 정보 활용
```java
class PrintEmployeeInfo {
    Employee viewer; // 조회하려는 자
    
    PrintEmployeeInfo(Employee viewer) {
        this.viewer = viewer;
    }
	
    // Employee 객체 리스트를 받아 직원들의 정보를 순회하여 조회
    void printAllInfo(List<Employee> employees) {
        employees.stream()
                .map(employee -> employee.getInfo(viewer))
                .forEach(System.out::println);
    }
}
```
- 회사 전 구성 모두 출력 메소드
- 생성자 인자로 , 누가 조회하는지 대상자 받고 , 구성원 리스트 인자 출력
##### 클린하지 않은 문제 코드 ❌
- 직책 클래스 자체에는 문제 X
- printAllInfo 메소드 로직 수정 필요
- 프로그램 수정하기엔 , 비용이 많이 듬!
	=>프록시 객체 통해 기존 프로그램 일부 기능만 제어 하자!!
##### 프록시 패턴 적용한 코드
- 보호 프록시는 프록시 객체가 실제 객체에 대한 접근 제어 ( 직책에 따른 정보 열람 접근 제어 )
	=>프록시 구성에 앞서 , 대상 객체 와 프록시 객체 모두 묶어주는 인터페이스 선언
```java
interface IEmployee {
    String getName(); // 구성원의 이름
    RESPONSIBILITY getGrade(); // 구성원의 직책
    String getInfo(IEmployee viewer); // 구성원의 인사정보
}
```
```java
class Employee REPONSIBILITY position;
	private String name;
	private RESPONSIBILITY position;
	public Employee(String name,REPONSIBLITY position){
		this.name = name;
		this.position = position;
	}
	@Override
	public String getName(){
	}
	@Override
	public RESPONSIBILITY getGrade(){
	}
	@Override
	public String getInfo(IEmployee viewer){
	}
}
```


```java
class PrintEmployeeInfo {
	IEmployee viewer;
	PrintEmployeeInfo (IEmployeer viewer){
		this.viewer = viewer;
	}
	void printAllInfo(List<IEmployee> employees){
		employees.stream()
					.map(employee -> employee.getInfo(viewer))
					.forEach(System.out::println)
	}
}
```
```java
// 보호 프록시 : 인사정보가 보호된 구성원 (인사 정보 열람 권한 없으면 예외 발생)
class ProtectedEmployee implements IEmployee {
    private IEmployee employee;

    public ProtectedEmployee(IEmployee employee) {
        this.employee = employee;
    }

    @Override
    public String getInfo(IEmployee viewer) {

        RESPONSIBILITY position = this.employee.getGrade(); // 조회 당하는 자의 직책을 얻기
		
        // 매개변수로 받은 조회자의 직책에 따라 출력을 제어
        switch (viewer.getGrade()) {
            case DIRECTOR:
                // 부사장은 과장, 사원들을 볼 수 있다.
                return this.employee.getInfo(viewer);
            case MANAGER:
                // 과장은 같은 직무와 그 아래 사원들을 볼 수 있다. 사장 정보는 볼 수 없다.
                if (position != RESPONSIBILITY.DIRECTOR) {
                    return this.employee.getInfo(viewer);
                }
            case STAFF:
                // 사원은 같은 직무인 사람들만 볼 수 있다. 과장, 사장 정보는 볼 수 없다.
                if (position != RESPONSIBILITY.DIRECTOR && position != RESPONSIBILITY.MANAGER) {
                    return this.employee.getInfo(viewer);
                }
            default: return "다른 사람의 인사정보를 조회 할수 없습니다";
        }
    }

    @Override
    public String getName() {
        return employee.getName();
    }

    @Override
    public RESPONSIBILITY getGrade() {
        return employee.getGrade();
    }
}
```
- Proxy 객체로 감싸서 , Instance화
![500](https://i.imgur.com/nrKq7tR.png)


```java
class Client {
    public static void main(String[] args) {
        // 직원별 개인 객체 생성
        Employee CTO = new Employee("Dragon Jung", RESPONSIBILITY.DIRECTOR);
        Employee devManager = new Employee("Cats Chang", RESPONSIBILITY.MANAGER);
        Employee financeManager = new Employee("Dell Choi", RESPONSIBILITY.MANAGER);
        Employee devStaff = new Employee("Dark Kim", RESPONSIBILITY.STAFF);
        Employee financeStaff = new Employee("Pal Yoo", RESPONSIBILITY.STAFF);

        // 직원들을 리스트로 가공
        List<Employee> employees = Arrays.asList(CTO, devManager, financeManager, devStaff, financeStaff);

        /*-----------------------------------------------------------------------------------------*/

        // 기존에 등록된 리스트를 수정할 수 없으니, 동적으로 기존의 Employee 객체를 프록시 객체 ProtectedEmployee로 Wrap하는 작업을 실행한다.
        List<IEmployee> protectedEmployees = new ArrayList<>();
        for (Employee e : employees) {
            protectedEmployees.add(new ProtectedEmployee((IEmployee) e));
        }

        /*-----------------------------------------------------------------------------------------*/

        // 나 : 일개 사원 직책
        Employee me = new Employee("inpa", RESPONSIBILITY.STAFF);

        System.out.println("\n================================================================");
        System.out.println("시나리오1. 일개 사원인 내가 회사 인원 인사 정보 조회");
        System.out.println("================================================================");
        PrintEmployeeInfo view = new PrintEmployeeInfo(me); 
        view.printAllInfo(protectedEmployees); 

        System.out.println("\n================================================================");
        System.out.println("시나리오2. 과장이 회사 인원 인사 정보 조회");
        System.out.println("================================================================");
        PrintEmployeeInfo view2 = new PrintEmployeeInfo(devManager);
        view2.printAllInfo(protectedEmployees); 

        System.out.println("\n================================================================");
        System.out.println("시나리오3. 상무가 회사 인원 인사 정보 조회");
        System.out.println("================================================================");
        PrintEmployeeInfo view3 = new PrintEmployeeInfo(CTO);
        view3.printAllInfo(protectedEmployees);
    }
}
```
=> 간편하며 , 빠르게 코드를 수정 안해도 추가 설계 가능!
( 사담 : 대신 , 칼럼이나 부가 기능을 위한 자료 구조는 미리 설계 되어 있어야 좋은거 같다. )

### 실무 in Proxy
#### Spring AOP
- AOP 와 JPA 는 결국 Proxy 기술로 구현!
- Bean 역시 Proxy 기술! ( Dynamic proxy 이용해 기술 구현 )
```java
@Aspect
@Comonent
public class PerfAspect {
	@Around("bean(gameService)")
	public void timestamp(ProceedingJoinPoint point) throws Throwable {
    	System.out.println("프록시 실행 1");
        
        point.proceed(); // 대상 객체의 원본 메서드를 실행
        
        System.out.println("프록시 실행 2");
    }
}
```
#### JS Proxy / Reflect

- 자바스크립트 진영들 역시 독립적인 Proxy 객체 존재
	-> 대상 객체를 감싸 ( wrapping ) 여러 기본 동작을 가로채 특별 동작 가미하는 대리자 역활
	
```javascript
let obj = {
    name: '홍길동',
    print: function () {
        console.log(`My Name is ${this.name}`);
    },
};

// print 함수를 프록시로 감싸기
obj.print = new Proxy(obj.print, {
    apply(target, thisArg, args) {
        console.log('메서드 실행할 때 중간에 가로채어 로직 시행');

        console.log('target: ', target); // 대상 함수
        console.log('thisArg: ', thisArg); // this의 값
        console.log('args: ', args); // 매개변수 목록 (배열)

        console.log('이름 바꿔 버리기 ~');
        thisArg.name = '임꺽정';

        Reflect.apply(target, thisArg, args); // 대상 원본 함수 실행
    },
});

obj.print();
```
=> Decorator 를 적용 시키는 거와 매우 유사한 기술
- 사담 : 이런 방식의 코드 변경이 이러지면 안되는 거아닌가?
- Proxy 가 객체의 역활을 대신 수행해준다고는 하나 , 변수 변경까지는 충분히 꺼릭칙함!!

해당 부분에 대한 얘기는 Proxy Pattern 의 주제를 벗어나는 것 같기에 , [JS] Reflect 부분에서 더욱 자세히 탐구해볼 예정이다.
