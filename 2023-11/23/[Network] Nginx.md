### Nginx 

- 가장 많이 사용되는 웹 서버
- Apache 와 유사한 느낌

#### 기본적인 용어
##### 클라이언트
- 서비스를 이용하기 위해 요청을 보내는 주체
- 단순 웹 브라우저 뿐만이 아닌 , 이메일 클라이언트들도 가능!
- EX ) Chrome,  Safari , Explore 같은 류들
##### 웹 서버
- 클라이언트 요청에 따라 정적 파일을 제공하는 S/W
- HTTP 프로토콜을 사용해 클라이언트와 통신
- EX ) Nginx , Apache ...
##### WAS
- Web Application Server
- 클라이언트 요청에 대해 동적인 처리 담당
- 애플리케이션 로직 실행 가능
- 우리가 알고 있는 일반적인 서버
##### Request / Response
Client -> WAS -> WS -> DB 순으로 요청
DB -> WAS -> WS -> WAS -> Client 순으로 응답
### 웹 서버 왜 사용하는가?
1. WAS 의 부담을 줄여줌!
	-> 비즈니스 로직만으로도 WAS 는 작업량이 많음! ( 정적 파일 전달 역활은 웹 서버에 위임 )
2. 보안 기능 제공
	-> 보안 기능 제공해 , 접근 제어 가능! ( SSL/TLS 프로토콜 사용해 데이터 암호화 + 방화벽 기능 )
3. 높은 성능 제공
	-> 비동기 처리 방식 사용해 높은 성능 제공
	( 이벤트 기반 , 멀티 프로세싱 , 스레드 풀 같은 기술들 사용 )

### Nginx 구조

- Master-Worker Model ( 1:N )
#### Master Process
- 구성 ( 설정 ) 파일을 읽고 , Worker Process 생성 / 관리
- 유효성 검사 
- 서버 시작 & 종료 , 재로드 담당
#### Worker Process
- 실제 요청 처리
- 독립적으로 클라이언트 요청 처리 ( 각자 지정된 listen 소켓 배정 받음 )
- 소켓 통해 커넥션 형성해 요청 처리 ( Keep Alive 시간 동안 유지 )
- 요청 없는 커넥션을 유지하지 않고 , 다른 요청 처리 하거나 새로운 커넥션 형성
- 일반적으로 , 사용 가능 CPU 코어 수만큼 생성 ( Context Switching 최소화 )
##### Event
- 커넥션 형성 , 제거 + 새로운 요청 처리
- OS 커널을 통해 Event 를 Queue 형식으로 Worker Process에 전달
- Worker Process 가 처리할 때까지 비동기 방식으로 대기

#### Thread Pool
- 처리 시간 긴 요청 대비해 스레드 풀 사용
- 처리 요청 시간 오래 걸릴 것으로 예상 되는 경우 , 스레드 풀에 위임
	-> Worker Process 가 다른 이벤트 처리 계속 진행하게 해줌!

=> 이벤트 기반 모델
- 대량 동시 연결 효율적 관리
- 각 워커 프로세스 독립적 작동하므로 , 시스템 안전성과 확장성을 높여줌!

### 장점

- Process 를 적게 만드므로 매우 가벼움 ( Master , Worker 가 끝 )
	-> 동시 커넥션 양 매우 증가!
	-> 동일 커넥션 수일 때 속도 2배 향상
- Nginx 설정 동작으로 변경 가능!
	-> 설정 변경해 적용하면 거기에 맞게 새로 생성
	-> 기존 프로세스는 커넥션 형성하지 않도록 하여 차차 종료

### 기능
- 로드 밸런서
- 웹 서버
- 웹 서버 가속기
	-> 정적 콘텐츠 캐싱 , 압축 , SSL / TLS 처리 통해 로딩 속도 향상
- SSL 터미네이션
	-> 클라이언트 통신은 HTTPS 암호화 , 서버와 통신은 HTTP 통신
- 캐싱
	-> 전달되는 콘텐츠 캐싱해 빠르게 제공
- HSTS ( Http Strict Transport Security )
	-> 무조건 , HTTPS 로만 접속하게 강제 ( 자동으로 HTTPS 연결로 리다이렉션 )
- CORS 처리
	-> 리소스 공유 허용하는 헤더 설정
- TCP / UDP 커넥션 부하 분산
	-> TCP UDP 프로토콜 사용 어플리케이션 대해 로드 밸런싱 기능 제공
- HTTP/2 지원

### In AWS

Client -> DNS Mapping -> CDN -> Load Balancer -> Web Server ( Reverse Proxy ) -> Application Server
	-> DB -> Third Party


### 참고 출처

https://ssdragon.tistory.com/60
https://blog.naver.com/gi_balja/223028077537

##### Writed By Obisidan