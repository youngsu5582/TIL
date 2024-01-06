
데이터 변환 계층의 기준은 Framework 단위에서
기준이 정해져 있지 않습니다

이에 따라 , 컨벤션이 잡혀 있지 않으면
서로 데이터 변환 계층을 다르게 해 프로젝트 전체의
일관성이 떨어지고 , 코드 가독성 & 리팩토링 내성 역시 떨어집니다

데이터 변환 게층의 기준은 어떻게 해야 할까요?


### 문제

- 프로젝트에서 , js-joda ( Dayjs ) 같은 날짜 타입을 쓰는 상황일시?
	( DB SQL 에서 사용하기 위해 Date 로 치환해야 하는 경우 )
	( API 로 외부 데이터 전송 or 요청 위해 String 으로 치환해야 하는 경우 )
- 프로젝트에서 , BigInt 를 쓰는 상황일시?
	외부에 제공하는 지표 데이터로는 String 전달시

이렇게 , 사용하는 Data Type 과 Transfer Type 이 다를 수 있습니다!
( 특히 , Framework 들을 사용하지 않으면 Transform 이 매끄럽지 않습니다 )

### 방법

```typescript
export class LectureRepository extends BaseRepository<Lecture> {
    ...
    async getLectureByDate (createdAt: Date): Promise<Lecture> {
        const lectures = await this.queryTemplate.queryWith(
            'SELECT * FROM lecture l WHERE l.created_at >= $1',
            [createdAt]
        );

        return transform(lectures[0], Lecture);
    }
}
```

이렇게 , Data Access 계층의 파라미터로 전달하는 것입니다
-> 이러면 Repository 는 Date 타입만을 처리하면 됩니다
```typescript
export class LectureService {

    async getLecture (createdAt: LocalDateTime): Promise<Lecture> {
        ...
        const date = convert(createdAt).toDate(); // LocalDateTime -> Date 로 변환
        const lecture = await this.lectureRepository.getLectureByDate(date);
        ....
    }
}
```

Service 계층은 Date 로 변환해야 하는 역활을 해야만 합니다
하지만 , 문제는 lectureRepository 를 호출하는 곳이 있다면 영역 모두에서 Date 로 변환을 해야만 합니다

```typescript
it('getLectureByDate', async () => {
    ...

    const createdAt = LocalDateTime.of(2022,11,26, 12,0,5);
    const createdDate = convert(createdAt).toDate(); 
    const result = await lectureRepository.getLectureByDate(createdDate);

    expect(...);
});
```

테스트 코드를 작성할때에도 마찬가지입니다

Repository 는 데이터를 변환할 필요 없기 때문에 Repository 는 깔끔한 형태를 가집니다
Service 계층에서 데이터 변환을 해주기 때문입니다.

결국 Repository 를 호출하는 영역이 한정되어 있더라도 , 현재 방식은 확장에 어려움이 있습니다
- RDBMS 에서 Message Queue , API 등으로 EndPoint 변경이 필요해
	날짜 데이터를 문자열로 변경해야 할 시

물론 , Global Handler 또는 Pipe 등으로 액션이 수행되기 전 Date -> String 도 가능 합니다
-> 이 경우 Data Access 계층을 호출하는 모든 영역이 변경 대상이 됩니다.
	+ 의도치 않게 추가 기능이 필요한 경우 변경양 역시 많아집니다.

##### 1. 로직과 로직 사이 추가 날짜 연산이 필요할 시?

```
export class LectureService {

    async getLecture (createdAt: LocalDateTime): Promise<Lecture> {
        ...
        const date = convert(createdAt).toDate(); // 첫 번째 로직
        const lecture = await this.lectureRepository.getLectureByDate(date); // 두 번째 로직
        ....
    }
}
```
이 경우 , 첫 번째 로직을 전체적으로 손 봐야 합니다

```typescript
export class LectureService {

    async getLecture (createdAt: LocalDateTime): Promise<Lecture> {
        ...
        const lastEndedAt = createdAt.plusDays(END_DAY); // date 변환용 신규 파라미터
        const date = convert(eventEndedAt).toDate();  // createdAt이 아니라 endedAt 을 쓰도록 변경해야한다.
        const lecture = await this.lectureRepository.getLectureByDate(date); 
        ....
    }
}
```

물론 , convert(createdAt.plusDays(END_DAY)).toDate(); 로 변경도 가능은 합니다!
( 해당 값이 의도하는 바가 무엇인지 명확하게 하기 위해 변수명 선언 & 할당 해야 하는 경우도 많습니다 )

=> 특정 계층을 위한 타입 변환의 책임이 프로젝트 전체에 타입 변환 책임이 전파되는 문제가 발생합니다

### 해결

데이터 타입 변환의 책임이 프로젝트 전체에 전파 되는 것을 막아야 합니다

#### Data Access 계층에서 변환

가장 쉬운 방법은 Data access 계층에서 변환되게 하는 것입니다.

```typescript
async getLectureByLocalDate (createdAt: LocalDateTime): Promise<LectureItem> {
    const createdDate = convert(createdAt).toDate();
    const lectures = await this.queryTemplate.queryWith(
        'SELECT * FROM lecture l WHERE l.created_at >= $1',
        [createdDate]
    );

    return transform(lectures[0], LecturesItem);
}
```

이 경우 , 프로젝트 전체에서 데이터 변환의 책임은 없어집니다

```typescript
it('getLectureByDate', async () => {
    ...

    const createdAt = LocalDateTime.of(2022,11,26, 12,0,5);
    const result = await lectureRepository.getLectureByDate(createdAt);

    expect(...);
});
```
테스트 코드 역시 , 지정 날짜를 그대로 사용하면 됩니다

#### Data Transfer Object ( DTO )

DTO 를 단순 , 호출횟수 최적화 관점에서만 생각할 수 있습니다
또 다른 장점으로는 직렬화 로직을 캡슐화 하는 장점이 있습니다

```typescript
export class LectureSearchDto {
  private _createdAt: LocalDateTime;

  constructor(createdAt: LocalDateTime) {
    this._createdAt = createdAt;
  }

  get createdAt(): Date {
    return convert(this._createdAt).toDate();
  }
}
```

객체가 내부적으로 다루는 값은 프로젝트에서 사용하기로 하는 LocalDateTime 입니다.
이를 호출해서 사용할 시에는 계층 목적에 맞게 데이터를 변환해 제공합니다.

```typescript
async getLectureByDto (param: LectureSearchDto): Promise<LectureItem> {
    const createdAt = param.createdAt;
    const lectures = await this.nodePgTemplate.queryWith(
        'SELECT * FROM lecture l WHERE l.created_at >= $1',
        [createdAt]
    );

    return transform(lectures[0], LecturesItem);
}
```

이렇게 필요할 때만 캡슐화한 날짜를 사용합니다

이를 호출하는 Service , Test 계층 역시도 DTO 에 대해서만 고려를 하면 됩니다
타입 변환에 대한 책임은 모두 DTO가 책임지면 됩니다

또한 , View 계층에서도 이를 쉽게 적용 가능합니다.
```typescript
export class LectureResponse {
    private readonly _createdAt:LocalDateTime;
    ...

    get createdAt(): string {
      return toString(this._createdAt);
    }
}
```

API 의 응답으로 문자열 날짜값을 전달해야 하는 경우에도 DTO 가 책임지면 비즈니스 로직을 훼손시키지 않습니다

### 마무리

프로젝트를 진행하고 , 프로젝트가 커지면 필연적으로
외부 의존성 ( Data Access , View , 외부 API 등 ) 으로 인해 데이터 변환이 발생합니다
이를 , 비즈니스 계층에서 책임지면 프로젝트 전체가 변화 및 확장에 대응하기 어렵습니다
![450](https://i.imgur.com/5CFpBgv.png)

가능하면 , 이렇게 DTO 를 사용해 데이터 변환 계층에 대한 책임을 비즈니스 로직과 분리하는게 좋습니다.

#### 번외 : DTO

- Data Transfer Object
- 원격 인터페이스에서 메소드 호출 수를 최소화 하는데 사용을 합니다
- 데이터 전송할 때 , 직렬화 메커니즘 역시 캡슐화를 통해 내부에 포함할 수 있습니다
