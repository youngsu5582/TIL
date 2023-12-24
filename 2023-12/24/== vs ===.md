### == vs ===

== 과 === 의 차이점은 동등성 비교의 정도 차이입니다


```typescript
5 === 5
// true
```

당연하겠죠?

```typescript
'hello world' === "hello world"
true === true

//true
```

역시 true 입니다

```typescript

77 == '77'
// true ( 값이 같으므로 )

77 === '77'
// false ( 값은 같으나 , 타입이 다르므로 )
```


== 은 느슨한 동등 비교를 합니다.
강제 형변환을 수행합니다
-> 즉 , 피연산자들을 공통 타입으로 만듭니다

```typescript
false == 0
false === 0
```

false == 0 은 true 가 나오게 됩니다

#### Falsy

- false
- 0
- ""
- nll
- undefined
- NaN

이 값들은 전부 falsy 값으로 통용이 됩니다

=> 즉 , 이 값들끼리는 == 비교할 시 True 를 리턴합니다

### 결론

결국 , == 보다는 === 을 사용하는게 무조건 적으로 좋습니다!

### 번외

```javascript
const object1 = {
    key:'youngsu',
    value:'student'
}
const object2 = {
    key:'youngsu',
    value:'student'
}

object1 == object2
object1 === object2
```

그러면 해당 결과는 어떻게 나올까요?
-> False 가 나옵니다

객체간 비교를 할 때는 , 참조에 의해서 평가됩니다
즉 , 같은 객체를 참조할때만 true가 나옵니다
```javascript
const object1 = {
    key:'youngsu',
    value:'student'
}
const object2 = object1

object1 == object2
```

그러면 , 같은 값들일 경우에는 어떻게 비교를 해야할까요??

=> 전부 비교하는 방법이 있지만 , 그냥 lodash Library 를 쓰는걸 추천합니다

```javascript
const _ = require('lodash');
const object1 = {
    key:'youngsu',
    value:'student'
}
const object2 = {
    key:'youngsu',
    value:'student'
}

_.isEqual(object1, object2);

```

Lodash 에 대해선 , 나중에 다시 설명하겠습니다.