	### 질문

DB Deadlock 이 뭔가요?
### 내가 한답

데드록은 두 개 이상 트랜잭션이 서로 실행을 기다리는 상황을 말합니다.
데드록이 발생하는 이유는 Locking 기법 때문입니다.
DB에서 Locking 기법을 사용하는 이유는 일관성과 무관성을 유지하기 위해서 입니다.
같은 자원에 동시 접근하면 값이 의도치 않게 변경이 될 수 있습니다.
그렇기 때문에 트랜잭션간 lock을 걸어서 순서 관리를 할 수 있게 합니다.

### 이상적인 답

## DeadLock

Deadlock 은 두 개 이상 트랜잭션이 서로 실행을 영원히 기다리게 되는 상황
해당 상황에선 어떤 트랜잭션도 실행할 수 없으며 , 외부의 개입을 필요로 합니다.

예를 들어 , 
1. 트랜잭션 A 가 레코드 1을 잠그고(사용하고) , 데이터 레코드2에 대한 잠금을 시도합니다.
2. 트랜잭션 B가 데이터 레코드 2를 잠그고 , 데이터 레코드 1에 대한 잠금 시도
3. A는 B가 끝나기를 기다려야 데이터 레코드 2에 접근 할 수 있고 , 
	트랜잭션 B는 트랜잭션 A가 끝나기를 기다려야 레코드 1에 접근할 수 있다.
4. 둘다 서로의 끝을 기다리기에 , 데드락 상태에 빠짐

### Lock 을 사용하는 이유?

DB 는 데이터를 영속적으로 저장하고 있는 시스템
같은 자원에 대해 동시 접근 경우가 생길 수 밖에 없고 , 오염이 될 수 있음!
	-> 데이터의 일관성과 무결성을 유지해야 할 필요가 있음

트랜잭션 간 서로 간섭 없이 독립적 수행하기 위해 Lock기법

### Lock 의 종류
#### Row-level Lock
##### Shared Lock
- Read Lock , 공유 락
- 데이터를 읽을 때 사용하는 Lock
- select 문 같이 Client 가 읽기 작업하는 데이터 영역
- Read Lock 끼리는 동시 접근 가능 ( 사용자가 동시에 사용할 뿐 , 데이터 변경은 없으므로! )
- Excusive Lock 의 접근을 막음
##### Exclusive Lock
- Write Lock , 베타 락
- 데이터를 변경할 때 사용하는 Lock
- insert, update, delete 문 같이 Client 가 쓰기 작업하는 데이터 영역
- 트랜잭션 완료 까지 ( Lock 이 풀릴 때 까지 ) 어떤 접근도 허용 X
	( 다른  트랜잭션 수행 되고 있는 데이터에 대해 접근해 Lock 걸 수 없음 )
- 하나 데이터 항목에서는 X-Lock 만 가능

###  By GPT
	데드락이 발생하는 이유는 여러 트랜잭션이 동시에 데이터베이스의 자원에 접근하려고 할 때 Locking 기법 때문입니다. Locking은 데이터베이스의 일관성과 무결성을 유지하는 데 필수적인 기능입니다. 이는 여러 트랜잭션이 동시에 동일한 데이터에 접근할 경우, 데이터가 의도치 않게 변경되는 것을 방지합니다.
	
	따라서, 데이터베이스 시스템은 트랜잭션 간의 충돌을 방지하고 순서를 관리하기 위해 Locking 기법을 사용합니다. 이 과정에서, 각 트랜잭션이 필요한 자원에 대한 락을 획득하고, 다른 트랜잭션이 그 자원을 사용하려 할 때는 대기해야 합니다. 그러나, 두 개 이상의 트랜잭션이 서로가 보유한 자원에 대한 락을 기다리는 상황이 발생하면, 어느 쪽도 진행할 수 없는 데드락 상태에 빠지게 됩니다.
	
	이러한 데드락 상태는 데이터베이스의 성능을 저하시키고, 트랜잭션 처리에 장애를 일으킬 수 있습니다. 그렇기 때문에 현대의 데이터베이스 관리 시스템들은 데드락을 감지하고 해결하는 여러 메커니즘을 포함하고 있으며, 데이터베이스 설계와 애플리케이션 개발 과정에서 데드락을 예방하는 전략이 중요합니다.

### 참고 출처

https://www.youtube.com/watch?v=w6sFR3ZM64c

https://velog.io/@sweet_sumin/DB-%EB%8D%B0%EB%93%9C%EB%9D%BD

https://akasai.space/db/about_lock/
##### Writed By Obisidan