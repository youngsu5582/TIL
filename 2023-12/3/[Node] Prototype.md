### 프로토타입?

- 자바스크립트에서 객체 생성의 모체가 되는 객체

=> 사실 2가지 유형이 있다.

#### Example
```javascript
 function Member(){
	this.x = "this";
};
Member.prototype.x = "protoX";
Member.prototype.y = "protoY";
Member.x = "x";

var a = new Member();
console.log(a.x);
console.log(a.y);
```
=> 해당 결과는?
- a.x 는 this 출력
- a.y 는 protoY 출력
( Member.x 는 어디로 ? , prototype.x 는 어디로? )

#### watch Expression 결과
- watch Expression 이 어디 있는지 몰라서 찾아 헤맸는데, 
 Dev Tools -  Source - 하단 Console 에 출력을 하면 된다.
![500](https://i.imgur.com/Eyuj2IU.png)

( 향로님 결과 화면인데 , 예전이랑 또 달라진듯 )

![400](https://i.imgur.com/0Lw2Kz1.png)

- `__proto__` 는 `[[Prototype]]` 로 바뀐듯 하다.

a 의 속성은 x 와 `[[Prototype]]` 2가지를 가지고 있다.
=> a의 Prototype 은 어디에서 나왔는가?
![500](https://i.imgur.com/tNPpw4E.png)
- Member 의 속성
- Member.x 도 "x"를 가지고 있음! ( 처음에 , Member.x = "x" 로 선언했으므로 )
	-> a의 x와 동일한 값이 아님! ( a는 "this" , Member 는 "x" )
- prototype 과 `[[Prototype]]` 두개를 가지고 있다!
![400](https://i.imgur.com/z49YE5v.png)
- prototype 을 확인해보면 끝없이 확장이 된다.
	-> 아까 , a의 `[[Prototype]]`과  동일한 값들을 가지고 있다.
=> a 는 Member 로 직접 객체 생성이 된 것이 아닌 , Member 의 prototype 이란 속성으로 , 객체 생성이 된 것!

Member.prototype 을 프로토타입 속성
a.`[[Prototype]] `을 프로토타입 연결
=> 최초 객체 생성시 원형 객체의 prototype 에 있는 원형 복사본을 통해서 객체 생성

###  By GPT


### 참고 출처

##### Writed By Obisidan