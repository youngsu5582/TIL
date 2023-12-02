### 질문

3-way handshake 뭔가요?
### 내가 한답

3-way handshake 는 https 통신을 하기 위한 방법입니다.
우선 , 클라이언트가 서버에 연결을 하겠다는 통신을 보내면 , 
서버는 해당 클라이언트의 요청을 받아들이고 , ack+1을 하여 다시 클라이언트에게 보내줍니다.

### 이상적인 답

TCP / IP 프로토콜을 이용해 통신 하는 프로그램이
데이터 전송 전 " 정확한 전송 보장을 위해 사전 세션을 수립하는 과정" 이다

#### TCP
- Transmission Control Protocol
- 흐름 제어 및 혼잡 제어
- 높은 신뢰성 보장
- UDP 보다는 속도가 느리다

### 3-way Handshake

1. Client 가 Server 에게 SYN Packet 전송
	( Sequence Number 를 임의 랜덤 숫자로 지정 , SYN Flag Bit ON )
2. Server 가 SYN(x) 를 받고 , Client 에게 받았다는 신호인 ACK 와 SYN Packet 전송
	( ACK Number 필드를 Sequence Number + 1 , SYN & ACK Flag Bit ON )
3. Client 가 SYN , ACK 를 받고 , ACK(y+1) 를 서버로 보냄

### 4-way Handshake

1. Client 가 Server 에게 FIN Packet 전송
	( 실질적으로 , ACK 도 포함 )
2. 서버는 FIN 을 받고 , 확인했다는 ACK Packet 전송 + 자신 통신 끝날때까지 대기
3. 데이터를 모두 보냈다면 , 서버는 FIN Packet 을 전송
4. 클라이언트는 FIN 을 받고 , 확인했다는 ACK Packet 을 전송
	( 아직 서버에서 못 받은 데이터가 있을 수 있으므로 , 잠시 대기 - TIME_WAIT )
5. 서버는 ACK 를 받은 후 , Socket 을 닫는다.
6. TIME_WAIT 가 끝나면 , Client 도 닫는다.

#### 질문 : Server 가 FIN Flag 를 보내기 전 전송 Packet 이 , ACK Flag 보다 늦게 도착할 시?

- 이런 경우 , Packet 유실이 발생할 수 있으므로 , Client 는 ACK Flag 를 전송한 후 잠시 대기한다.
#### 질문 : Sequece Number 가 난수인 이유?
- 그 전 Connection 에서 사용한 값인지 혼동의 여지가 있기 때문

###  By GPT


### 참고 출처

##### Writed By Obisidan