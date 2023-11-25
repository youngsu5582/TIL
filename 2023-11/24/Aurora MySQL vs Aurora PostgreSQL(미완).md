## 아티클 링크

https://techblog.woowahan.com/6550/


## 세줄 요약

## 내용
### 서론 : 왜 MySQL -> PostgreSQl 로 넘어갔는가?
데이터가 누적될수록 ( 특히 통계 정산 등 고성능 대량 필요 ) 작업 수행 속도는 느려진다.
MySQL 에서 동작중인 서비스는 쿼리 개선 작업을 진행했으나 , 드라마틱 효과는 볼 수 없었다!
=> 대량 데이터 처리에 특화 되어있는 PostgreSQL 로 이관 고려

### Aurora ( MySQL vs PostgreSQL )
#### Aurora?
- AWS 에서 제공해주는 관계형 DB Service
- 고성능 & 고가용성 목표 설계 + MySQL / PostgreSQL 두 가지 버전 제공!
- 자동 확장 & 빠른 백업 & 복제 & 장애 복구 기능등 제공
##### Aurora VS Rds
자칫 생각하면 , 그러면 Aurora 랑 Rds 는 같은게 아니야? 라고 생각할 수 있다.
- Aurora 는 AWS 에서 자체 개발한 DB Engiene ( 따라서 더 높은 성능 제공 )
	-> 클라우드 이점을 활용해 대규모 처리 작업 & 자동 장애 복구에 최적화!
=> RDS 의 상위 버전의 느낌 ( 대규모 에 적합 )

#### DB 특성
- MySQL : RDBMS ( 일반적인 Relation DataBase Management System )
- PostgreSQL : ORDBMS ( Object Relation Database Management System )
	-> RDBMS  에 객체 지향 DB 기능을 추가함 ( 기존 type 에서 확장 type 자유롭게 정의해 사용 가능 + 상속 기능 )
#### 방식
- MYSQL : 멀티 스레드
- PostgreSQL : 멀티 프로세스
	-> 더 나은 격리 단위를 제공하나 , 더 많은 자원 소비 역시 가능!

#### 사용환경
- MYSQL : OLTP 에 적절
- PostgreSQL : OLTP , OLAP 에 적절
##### OLTP
- Online Transaction Processing
- 일상적인 트랜잭션 처리에 사용
- 빠른 쿼리 처리 속도 , 데이터 무결성 , 동시성 , 빈번한 갱신 작업에 필요
##### OLAP
- Online Analytical Processing
- 대량 데이터 분석 , 보고서 생성에 사용 ( 데이터 마이닝 , 복잡한 쿼리 등 )
- 대규모 데이터 처리 , 복잡한 쿼리 & 분석 작업에 최적화

#### MVCC ( Multi Version Concurrency Control ) 지원
- MySQL : Undo Segement 지원
	-> update 된 최신 데이터는 기존 레코드에 반영 , 이전 값은 undo 라는 별도 공간에 저장
- PostgreSQL : MGA 방식
	-> 튜플을 update 할 때 , 새로운 튜플을 추가하고 이전 튜플은 유효 범위를 마킹해 처리
=> Undo 는 기존 데이터 블록 내 레코드 실제 업데이트 , MGA 는 새로운 레코드 추가

#### Update 방식
- MySQL : Update
- PostgreSQL : Insert & Delete ( check )
	-> Update 시 , 새 데이터가 Insert 되고 , 이전 데이터는 삭제 표시
		( 행이 업데이트 되면 , 변경 위치값에 대한 인덱스 정보도 업데이트 )
=> Update 시 , Mysql 보다 성능 떨어짐
#### Join 
- MySQL : NL Join , HASH Join ( 8.0 이후 지원 )
- PostgreSQL : NL Join , HASH Join , SORT Join
##### NL Join
- 한 테이블 각 행을 다른 테이블 모든 행과 비교해 조건 맞는 행 찾음
- 당연히 , 작은 데이터 Set 일때만 효과적
##### Hash Join
- 한 테이블 행을 Hash Table 로 변환 후 , 다른 테이블 행과 비교해 조건 맞는 행 찾음
- 대규모 Set 일때 빠른 Join 제공
##### Sort Merge Join
- PostgreSQL 전용
- 두 테이블을 각각 정렬 해 , 순서대로 조건 맞는 행 합병
- 두 테이블이 이미 정렬 되 있거나 , 정렬에 유리한 상황일때 효과적



## 결론


## 참고 링크



##### Writed By Obisidan