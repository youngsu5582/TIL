### Chain Of Responsibility Pattern
- COR
- Client 요청에 대해 세세한 처리를 객체 하나가 하는게 아닌 ,
여러 개 처리 객체로 나누고 , 사슬 처럼 연결해 집합 안에서 연쇄적 처리

### 문제

온라인 주문 시스템 개발한다고 가정
인증된 사용자만 주문 생성하게 접근을 제한 + 권한 있는 사용자에게 모든 주문 대한 접근 권한 부여
	-> 이런 검사들은 차례차례 수행 해야한다. ( 인증 실패하면 다른 검사 진행할 이유도 없음 )
![400](https://i.imgur.com/JHubSd3.png)
여기서 , 추가적인 순차 검사를 더 구현 해야한다면?
- 검증되지 않은 데이터를 전달하지 않기 위해  정제 하는 단계 추가
- 무차별 대입 공격에 취약해서 , 같은 IP 주소로 반복적 실패한 요청인지 확인하는 단계 추가
- 같은 데이터를 캐싱 하여 , 시스템 속도를 높이는 단계 추가
![400](https://i.imgur.com/TdIUPgA.png)
- 요구사항이 추가될 수록 , 코드는 더욱 복잡해진다
		+ 다른 컴포넌트 보호하기 위해 재사용 하려 할 때 마다 코드를 복제해야함
=> 이해하기 매우 어려워 지며 , 유지 관리 비용이 늘어난다

### 해결책

특정 행동들을 "핸들러" 라는 독립 실행형 객체로 변환
핸들러들을 체인으로 연결
( 다음 핸들러에 대한 참조도 저장하고 있음 )

### 책임 연쇄 패턴 구조

- Handler : 요청을 수신하고 , 처리 객체 집합 정의하는 Interface
- ConcreteHandler : 요청을 처리하는 실제 처리 객체
	- 핸들러에 대한 필드를 내부에 가지고 있음
	- 메소드를 통해 , 다음 핸들러 체인
	- 자신이 처리할 수 없는 요구일 시 , 다음 체인 핸들러에게 넘김
- Client : 요청을 Handler 에 전달한다

### 사용 시기

- 특정 요청 2개 이상을 여러 객체에서 판별 하고 처리해야 할 때
- 특정 순서대로 여러 핸들러를 실행해야 할 때
- 다양한 방식 & 종류에 요청을 처리 예상하지만 , 정확한 유형과 순서를 미리 알 수 없을 떄
- 요청 처리할 수 있는 객체 집합을 동적으로 정의해야 할 때

### 패턴 장점

- 클라이언트는 처리 객체의 내부 구조를 알 필요 없다.
- 각각 체인은 자신 해야하는 일만 알면 되므로 , 새로운 요청에 대한 처리가 쉬워진다.
- 요청의 호출자 - 수신자 분리가 가능하다
- 클라이언트의 코드는 변경할 필요 없이 , 체인에 동적 추가하거나 순서 변경하거나 삭제가 가능하다

### 패턴 단점

- 무한 사이클이 발생할 우려도 있다
- 코드의 흐름이 많아져서 디버깅 및 테스트가 어려울 수 있다

### Example

```java
class Server {
    private Map<String, String> users = new HashMap<>();

    public void register(String email, String password) {
        users.put(email, password);
    }

    public boolean hasEmail(String email) {
        return users.containsKey(email);
    }
    
    public boolean isValidPassword(String email, String password) {
        return users.get(email).equals(password);
    }
}
```

- 로직을 수행하는 Server
- 간단한 Map 자료구조로 , Mocking

```java
class Middleware {
    private int limit = 3;
    private int count = 0;

    Server server;

    public Middleware(Server server) {
        this.server = server;
    }

    public boolean limitLoginAttempt() {
        if (count > limit) {
            System.out.println("로그인 요청 횟수 제한 !!");
            return false;
        }
        count++;
        return true;
    }

    public boolean authorize(String email, String password) {
        if (!server.hasEmail(email)) {
            System.out.println("가입된 이메일 계정이 없습니다 !");
            return false;
        }

        if (!server.isValidPassword(email, password)) {
            System.out.println("패스워드가 다릅니다 !");
            return false;
        }

        System.out.println("로그인 완료 !!");
        return true;
    }

    public boolean authentication(String email) {
        if (email.equals("inpa@tistory.com")) {
            System.out.println("Hello, admin!");
            return true;
        } else {
            System.out.println("Hello, user!");
            return false;
        }
    }
}
```

- 인증을 담당하는 Middleware

```java
class Client {
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.register("john@naver.com", "123123");
        server.register("kail@google.com", "456456");
        server.register("inpa@tistory.com", "789789");

        Middleware middleware = new Middleware(server);

        do {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("\nEmail: ");
            String email = reader.readLine();
            System.out.print("Password: ");
            String password = reader.readLine();

            // 만일 로그인 횟수가 남아있을 경우
            if (middleware.limitLoginAttempt()) {
                // 이메일 패스워드 인증
                if (middleware.authorize(email, password)) {
                    // 인증되었을 경우 일반 유저 계정인지 관리자 계정인지 검증
                    if (middleware.authentication(email)) {
                        // ...
                    } else {
                        // 관리자가 아닌 일반 유저 계정일 경우 로깅 처리
                        middleware.logging();
                    }
                    break;
                } else {
                    continue;
                }
            } else {
                throw new RuntimeException("로그인 시도 횟수 초과로 프로그램을 종료합니다");
            }
        } while (true);
    }
}
```


- 해당 코드는 , 참일 경우 다음 인증 로직을 넘어가 따져가는 if-else 문 형태로 되어 있다
- 이는 , 매우 어렵고 복잡한 로직이 되어 버렸다

![400](https://i.imgur.com/KXMF4gg.png)

- 갈수록 더욱 복잡해진다

```java
class AuthorizeMiddleware extends Middleware {
    private Server server;
    public AuthorizeMiddleware(Server server) {
        this.server = server;
    }
    @Override
    public short check(String email, String password) {
        short flag = 1;
        if (!server.hasEmail(email)) {
            System.out.println("This email is not registered!");
            flag = 0; // continue 플래그
        } else if (!server.isValidPassword(email, password)) {
            System.out.println("Wrong password!");
            flag = 0; // continue 플래그
        } else {
            flag = super.check(email, password);
        }
        return flag;
    }
}
```

middleware
		.setNext(authorizeMiddleware)

차후 , Middleware 를 계속 Next 로 연결
사실상 , 연결 리스트의 느낌

### 결론 

Nest JS 에서 연결되는 Middleware 같은 형태들도 전부 책임 연쇄 패턴이라고 할 수 있다.

next 로 계속 넘어가므로
```typescript
@Injectable()
export class LoggingMiddleware implements NestMiddleware {
  use(req: Request, res: Response, next: NextFunction) {
    console.log(`Request made to: ${req.path}`);
    next();
  }
}
```

굳이 해당 패턴이 필요한 가 싶기도 하다.
연결 리스트로 계속 next 를 지정해줘야 하는 상황이 필요하므로