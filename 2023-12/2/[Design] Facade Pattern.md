## Facade Pattern

- 구조 패턴
- 라이브러리 , 프레임워크에 대한 or 다른 클래스들의 복잡한 집합에 대해서 단순 인터페이스를 제공해주는 패턴

=> 클래스를 재정리 하는 행위!

![500](https://i.imgur.com/FOYfmmr.png)
- 전화로 주문할 시 , 뒤에 있는 수많은 기능들을 전부 알 필요가 없다.
	-> 교환원이 대신 기능 수행!
### 구조
![500](https://i.imgur.com/EZ06T8P.png)
##### Facade
-  퍼사드 패턴을 사용해 , 하위 시스템 기능의 특정 부분에 편리하게 접근 가능
	( 퍼사드가 클라이언트 요청을 어디 보내는지 , 어떻게 작동해야 하는지 알고 있음 )
##### Additional Facade
- 추가적인 퍼사드 클래스 생성해 , 퍼사드에 관련없는 기능들로 오염시켜 복잡한 구조 만드는 것 역시 방지 가능
( 추가 퍼사드들은 클라이언트 와 다른 퍼사드들 모두 사용 가능 )
##### Subsystem class
- 수십 개의 다양한 객체로 구성
( 의미 있는 작업을 하려면 , 세브적인 구현 정보를 깊이 있게 살펴야 함 )
- 하위 시스템 클래스들은 퍼사드 존재를 인식하지 못해야 한다.
- 시스템 내 작동하며 , 매게체 없이 직접 서로와 작업
##### Client
- 하위 시스템 객체 직접 호출 대신 퍼사드 사용

### 정리
- 클래스 구조가 정형화 되어 있지 않은 패턴
	-> 클래스 위치 , 어떤 형식 위임 등 그런게 없다
=> 퍼사트 클래스로 적절히 기능 집약만 하면 그게 디자인 패턴

- C , Java 에서 함수 분리를 통해 main 함수 코드를 심플하게 구성하는 것도 퍼사드 패턴!

### 패턴 사용 시기

- 시스템이 너무 복잡할 때
- 간단한 인터페이스 통해 복잡한 시스템 접근하고 싶을 떄
- 시스템 사용하고 있는 외부와 결합도가 너무 높을 떄 의존성을 낮추기 위해

### 패턴 장점

- 하위 시스템 복잡성에서 코드 분리 해 , 외부에서 시스템 사용하기 쉬워짐
- 하위 시스템 간 의존 관계가 많을 시 , 감소 시키고 한 곳으로 모음
- 복잡한 코드를 감춤으로써 , 클라이언트가 시스템 코드를 모르더라도 Facade Class 만 이해하고 사용 가능

### 패턴 단점

- 퍼사드가 모든 클래스에 결합된 God 객체가 될 수도 있음
- 퍼사드 클래스 자체가 서브시스템에 대한 의존성을 가지므로 , 의존성을 완벽히 피하기는 불가능
- 결국 , 추가 코드가 늘어나므로 유지 보수 측면 단위에서는 공수가 더 많이 듬
	=> 추상화 하고자 하는 시스템이 퍼사드 패턴을 통해 얻게 되는 이점과
		추가적인 유지보수 비용 비교해보며 결정해야 함!
### 간단한 DBMS 로직 최적화

DB 를 조회해 데이터 가공하는 로직
1. DBMS 조회 전
2. 과거 조회가 됐는지 캐시에서 먼저 조사
3. 캐시에 데이터가 있다면 , 캐시에서 데이터 가공하고 출력
4. 캐시에 데이터가 없다면 , DBMS 통해 조회
5. 조회된 데이터 가공하고 출력 + 캐시에 저장

```java
// DBMS에 저장된 데이터를 나타내는 클래스
class Row  {
    private String name;
    private String birthday;
    private String email;

    public Row(String name, String birthday, String email) {
        this.name = name;
        this.birthday = birthday;
        this.email = email;
    }

	// 다른 Getter 는 생략
    public String getName() {
        return name;
    }
}

// 데이터베이스 역할을 하는 클래스
class DBMS {
    private HashMap<String, Row> db = new HashMap<>();

    public void put(String name, Row row) {
        db.put(name, row);
    }

    // 데이터베이스에 쿼리를 날려 결과를 받아오는 메소드
    public Row query(String name) {
        try {
            Thread.sleep(500); // DB 조회 시간을 비유하여 0.5초대기로 구현
        } catch(InterruptedException e) {}

        return db.get(name.toLowerCase());
    }
}

// DBMS에서 조회된 데이터를 임시로 담아두는 클래스 (속도 향상)
class Cache {
    private HashMap<String, Row> cache = new HashMap<>();

    public void put(Row row) {
        cache.put(row.getName(), row);
    }

    public Row get(String name) {
        return cache.get(name);
    }
}

// Row 클래스를 보기좋게 출력하는 클래스
class Message {
    private Row row;

    public Message(Row row) {
        this.row = row;
    }
	// 다른 Make 는 생략
    public String makeName() {
        return "Name : \"" + row.getName() + "\"";
    }
}
```

#### 클린하지 않은 코드
```java
class Client {
    public static void main(String[] args) {
        // 1. 데이터베이스 생성 & 등록
        DBMS dbms = new DBMS();
        dbms.put("홍길동", new Row("홍길동", "1890-02-14", "honggildong@naver.com"));
        dbms.put("임꺽정", new Row("임꺽정", "1820-11-02", "imgguckjong@naver.com"));
        dbms.put("주몽", new Row("주몽", "710-08-27", "jumong@naver.com"));

        // 2. 캐시 생성
        Cache cache = new Cache();

        // 3. 트랜잭션에 앞서 먼저 캐시에 데이터가 있는지 조회
        String name = "홍길동";
        Row row = cache.get(name);

        // 4. 만약 캐시에 없다면
        if (row == null){
            row = dbms.query(name); // DB에 해당 데이터를 조회해서 row에 저장하고
            if(row != null) {
                cache.put(row); // 캐시에 저장
            }
        }

        // 5. dbms.query(name)에서 조회된 값이 있으면
        if(row != null) {
            Message message = new Message(row);

            System.out.println(message.makeName());
            System.out.println(message.makeBirthday());
            System.out.println(message.makeEmail());
        }
        // 6. 조회된 값이 없으면
        else {
            System.out.println(name + " 가 데이터베이스에 존재하지 않습니다.");
        }
    }
}
```

- 메인 로직에 그대로 작성
- 나중에 수정과 확장 할 시 , 복잡함 과 더불어 실수 유발할 가능성이 올라간다!

#### 퍼사드 패턴 적용
```java
class Facade {
    private DBMS dbms = new DBMS();
    private Cache cache = new Cache();

    public void insert() {
        dbms.put("홍길동", new Row("홍길동", "1890-02-14", "honggildong@naver.com"));
        dbms.put("임꺽정", new Row("임꺽정", "1820-11-02", "imgguckjong@naver.com"));
        dbms.put("주몽", new Row("주몽", "710-08-27", "jumong@naver.com"));
    }

    public void run(String name) {
        Row row = cache.get(name);

        // 1. 만약 캐시에 없다면
        if (row == null){
            row = dbms.query(name); // DB에 해당 데이터를 조회해서 row에 저장하고
            if(row != null) {
                cache.put(row); // 캐시에 저장
            }
        }

        // 2. dbms.query(name)에서 조회된 값이 있으면
        if(row != null) {
            Message message = new Message(row);

            System.out.println(message.makeName());
            System.out.println(message.makeBirthday());
            System.out.println(message.makeEmail());
        }
        // 3. 조회된 값이 없으면
        else {
            System.out.println(name + " 가 데이터베이스에 존재하지 않습니다.");
        }
    }
}
class Client {
    public static void main(String[] args) {

        // 1. 퍼사드 객체 생성
        Facade facade = new Facade();

        // 2. db 값 insert
        facade.insert();

        // 3. 퍼사드로 데이터베이스 & 캐싱 & 메세징 로직을 한번에 조회
        String name = "홍길동";
        facade.run(name);
    }
}
```
- insert 와 run 으로 분리해 , 기능별 분리
- Client 는 큼지막한 핵심 로직에 대해서만 알면 된다. ( 캐싱 처리 , 디비 처리를 알 필요가 없음 )
### Tip!

- 하위 시스템 캡슐화를 하는 것이 아님!
	-> 서브 시스템을 사용할 간단한 인터페이스를 제공하는 것
	( 사용자는 결국 서브 시스템 내부 클래스들을 사용! - 추상화에 가까운 느낌 ) 

- 생각해 보면 , 퍼사드 객체는 여러개 있을 필요가 없다! 싱글톤으로 구성해주는 것이 좋다.

### 다른 패턴과의 비교
#### 어댑터

- 어댑터는 기존 인터페이스를 사용할 수 있게 만들려고 노력
- 퍼사드는 기존 객체들을 위한 새 인터페이스 정의
#### 플라이웨이트
- 플라이웨이트는 작은 객체를 많이 만드는 방법
- 퍼사드는 전체 하위 시스템을 나타내는 단일 객체를 만드는 방법

### 결론

- 컨트롤러가 , 단일 서비스에서 기능을 수행한다면 , Facade 를 사용하지 않아도 괜찮을 거 같다.
- 하지만 , 여러 서비스들 과 여러 기능들이 결합되어 있다면 Facade 는 좋은 선택이 될 거 같다!
	-> Client 의 관점에서 , 세부 로직을 몰라도 구현하는 법을 알 수 있기 때문이다.
- Facade - Facde 간 순환 Facde Pattern 은  볼 일이 없을 것 같다.

###  By GPT
	Facade Pattern(퍼사드 패턴)은 객체 지향 설계에서 사용되는 패턴 중 하나로, 
	복잡한 시스템 또는 서브시스템의 인터페이스에 대한 단일 간단한 인터페이스를 제공하는 구조적인 패턴입니다. 
	이 패턴을 사용하면 클라이언트가 복잡한 서브시스템을 직접 다루는 대신 퍼사드(Facade)라 불리는 단일 진입점을 통해 간단하게 상호작용할 수 있습니다. 
	주요 목적은 클라이언트와 서브시스템 사이의 의존성을 낮추고 클라이언트 코드를 단순화하는 것입니다.

### 참고 출처

https://refactoring.guru/ko/design-patterns/facade

https://inpa.tistory.com/entry/GOF-%F0%9F%92%A0-%ED%8D%BC%EC%82%AC%EB%93%9CFacade-%ED%8C%A8%ED%84%B4-%EC%A0%9C%EB%8C%80%EB%A1%9C-%EB%B0%B0%EC%9B%8C%EB%B3%B4%EC%9E%90#%ED%8C%A8%ED%84%B4_%EC%9E%A5%EC%A0%90

##### Writed By Obisidan