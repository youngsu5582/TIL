## View

- View 는 DB 개체 중 하나
- View 는 생성해 놓으면 , 사실상 테이블이라고 생각하고 사용해도 될 정도로 테이블과 동일한 개체 취급 ( 일종의 함수 )

- 테이블 처럼 데이터를 가지고 있지는 않음
	-> 바로가기 와 비슷한 느낌
	-> View 의 실체는 SELECT 문으로 만들어져 있기 때문에 , View에 접근하는 순간 select 가 실행되고 결과가 화면 출력

- 단순 뷰 : 하나의 테이블과 연관된 뷰
- 복합 뷰 : 2개 이상의 테이블과 연관된 뷰
#### Create View
```sql
CREATE VIEW v_member
AS
	select mem_id,mem_name,addr from user;
----

select * from v_member;
```

#### View 의 작동

1. 사용자 -> 뷰 : 조회 or 변경
2. 뷰 -> 테이블 : 쿼리 실행
3. 테이블 -> 뷰 : 쿼리 결과값
4. 뷰 -> 사용자 : 결과

### View 를 사용하는 이유

#### 보안에 도움이 된다.

- view 를 통해 , 주고 싶은 값만 줄 수 있다.
##### Example

- 인터넷 마켓 회원의 이름과 주소 확인하는 작업 진행
- 아르바이트 생이 user table 에 접근하면 , 사용자의 중요한 개인 정보 까지 모두 노출!
	( 그렇다고 , 접근하지 못하게 하면 일을 진행할 수 없음 )
=> 아이디 , 이름 , 주소만 보이는 뷰를 생성해서 , 아르바이트생에게 권한을 줘서 해결 가능!
#### 복잡한 SQL 을 단순하게 만들 수 있다.
```sql
SELECT B.mem_id , M.mem_name , B.prod_name , M.addr,
			CONCAT(M.phone1,M.phone2) '연락처'
		FROM buy B
			INNER JOIN member M
				ON B.mem_id = M.mem_id;
```
=> 몹시 길고 복잡함
=> 쿼리를 자주 사용할 떄마다 , 매번 복잡한 쿼리를 입력해야 함
```sql
CREATE VIEW v_memberbuy
AS
	SELECT B.mem_id , M.mem_name , B.prod_name , M.addr,
				CONCAT(M.phone1,M.phone2) '연락처'
			FROM buy B
				INNER JOIN member M
					ON B.mem_id = M.mem_id;
```

### TIP
- 한글을 정말 부득이하게 사용해야 한다면 , Table Name 보단 Class Name 에 넣자
- View 를 describe 해도 , Column 중 Primary Key 가 있는지는 알지 못한다.
- show create view <view명> : view 를 만들때 Query 문을 보여줌

```sql
alter view v_height167
as 
	select * from member where height >=167
		with check option;
```
##### WITH CHECK OPTION
-  데이터를 수정하거나 삽입할 때 , 기존의 조건을 만족하는 데이터만 허용
	->height >=가 167 이상인 경우에만 insert , update 가능!
##### WITH READ ONLY
- 기존 테이블의 어떤 컬럼에 대해서도 뷰를 통한 내용 수정을 불가능하게 만든다

### 장점

- 논리적 데이터 독립성 제공
- 동일 데이터에 대해 여러 사용자들의 상이한 요구나 응요을 지원
- 사용자의 데이터 관리를 간단하게 해줌
- 접근 제어 통한 자동 보안 제공
### Inline View
- 다른 SQL 문에 중첩된 SELECT 문
- SELECT , FROM , WHERE 절 내 사용
- 주 쿼리 내에서 참조할 수 있는 가상 테이블 ( 서브 쿼리 )을 생성하는 방법
- 객체로 저장되지 않으며 , 실행 시간에 평가 
```sql
SELECT column1, column2
FROM (SELECT * FROM your_table WHERE condition) AS inline_view;
```
### Materialized View

- 어떤 결과를 뽑아내는 쿼리가 너무 빈번히 사용될 경우 , 쿼리 실행 시간 수행 속도 향상 위해 미리 만들어 두는 View
- 특정 시점에 저장을 한다 ( 물리적인 복사본 디스크 저장 )
```sql
CREATE MATERIALIZED VIEW mv_example
AS
SELECT column1, column2, COUNT(*)
FROM your_table
GROUP BY column1, column2;
```

### In Prisma

```prisma
view UserInfo {
	id Int?
	email String? 
	name String? 
	bio String? 
  @@ignore 
}
```
```typescript
const userinfo = await prisma.userInfo.findMany({ where: { name: 'Alice', }, })
```
- 일반적인 Table 처럼 사용 가능!
###  By GPT

	뷰(View)는 데이터베이스에서 가상의 테이블로 정의되는 쿼리 결과를 나타내는 개체입니다. 뷰는 하나 이상의 기본 테이블이나 다른 뷰를 기반으로 만들어지며, 실제 데이터를 저장하지 않고 쿼리를 통해 동적으로 결과를 생성합니다.
	
	간단한 정의:
	
	**뷰(View)는 데이터베이스 객체로, 하나 이상의 테이블이나 뷰를 기반으로 하는 쿼리의 결과를 나타내는 가상의 테이블입니다. 뷰는 데이터 모델링, 보안 및 접근 제어, 복잡한 쿼리의 단순화, 코드 재사용성 등 다양한 목적으로 사용됩니다.**
	
	일반적으로, 뷰는 다음과 같은 특징을 가지고 있습니다:
	
	1. **가상성(Virtuality):** 뷰는 데이터를 직접 저장하지 않고 쿼리 실행 시에 결과를 동적으로 생성합니다.
	
	2. **정의(Definition):** 뷰는 하나 이상의 테이블이나 다른 뷰를 기반으로 SQL 쿼리를 사용하여 정의됩니다.
	
	3. **가독성 및 추상화:** 뷰를 사용하면 복잡한 쿼리나 데이터 모델을 단순화하고 가독성을 높일 수 있습니다. 또한 뷰를 통해 데이터 모델을 추상화할 수 있습니다.
	
	4. **보안 및 접근 제어:** 뷰를 사용하여 특정 사용자 그룹이나 역할에 대한 데이터에 대한 접근을 제어하고 보안을 강화할 수 있습니다.
	
	예시적인 답변:
	
	**"뷰(View)는 데이터베이스에서 가상의 테이블로, 하나 이상의 테이블이나 다른 뷰를 기반으로 하는 SQL 쿼리 결과를 나타내는 객체입니다. 뷰는 데이터 모델링에서 가독성을 향상시키거나 복잡한 쿼리를 간소화하는 데 사용되며, 보안 및 접근 제어에도 활용됩니다."**
### 참고 출처

https://haenny.tistory.com/415

https://m.blog.naver.com/dnjswls23/222280335211

https://www.prisma.io/docs/concepts/components/prisma-schema/views

##### Writed By Obisidan