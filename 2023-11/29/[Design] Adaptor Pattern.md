
## Adaptor Pattern

- 구조적 패턴
- 서로 호환성 없는 인터페이스 / 클라스를 연결해 동작할수 있게 도와줌
- Wrapper Pattern 이라고도 불림

110V 전용 가전제품에 220V 어댑터를 끼워 사용하는 것 과 같다.
=> 호환 되지 않은 단자를 어댑터로 호환해 작동하게 하는 것

( 전기 콘센트 - 전압 어댑터 - 전기 플러그 )

### 적용 사례
- 외부 라이브러리 ( Auth , Payment , Media )
	-> 해당 클래스들은 우리가 접근 못할 수도 있음! ( 중간 어댑터로 변환 )
- 레거시 시스템 부분 확장
	-> 인터페이스 , 클래스 가 전역 사용 비중이 굉장히 클 때 , 어댑터 클래스를 부분적으로 만들어 사용 

### 구성 요소
- Adaptee ( Service ) : 어댑터 대상 객체 , 기존 시스템 / 외부 시스템 / 서드파티 라이브러리
- Target ( Client Interface ) : Adapter 가 구현하는 인터페이스
- Adapter : Client - Adaptee 중간에서 호환성 없는 둘을 연결시켜주는 역활 담당
	- Object Adaptor 방식에선 합성 이용해 구성
	- Adaptte 를 따로 클래스 멤버로 설정해 위임 통해 동작 매치
- Client : 기존 시스템 어댑터를 통해 이용하려는 쪽 , Client Interface 를 통해 Service 이용 가능 

### Structure
- 기존 시스템 Class 를 상속 하는 호환 작업
- 합성 해서 호환 작업

### Object Adaptor ⭐️

- 합성 된 멤버에게 위임 이용하는 어댑터 패턴
- 자기가 할 일을 클래스 맴버 객체 메소드에게 다시 시킴 - 위임
- 런타임 중 Adaptee 가 결정되므로 유연함
- Adaptee 객체를 필드 변수로 저장해야 하므로 공간 차지의 비용은 듬!
![500](https://i.imgur.com/9FU7b70.png)

```typescript
class SmartHomeClient{
	addConnection(device:IsmartDevice){
	try{
		device.connect();
		console.log('Connect! '+device.device);
	}catch{
		console.error('Error!');
	}
	}
}
const smartHomeClient = new SmartHomeClient();
smartHomeClient.addConnection(new TV());

interface ISmartDevice{
	connect():boolean;
	device():string;
}
class TV implements ISmartDevice{
	private _device:string;
	constructor(){this._device='TV";}
	connect(){
		return true;
	}
	device(){return this._device;}
}
```
- TV 는 ISmartDevice 를 구현하므로 , connect 와 device 를 무조건 가지고 있다!
```typescript
interface IMonitor{
	makeConnection():void;
	getDeviceName():string;
}
class Monitor implements IMonitor{
	private _device:string;
	constructor(){this._device="Monitor";}
	makeConnection(){ console.log('연결중!');}
	getDeviceName(){return this._device;}
}
```
- Monitor 와 ISmartDevice 는 호환이 되지 않는다!
```typescript
class MonitorAdapter implements ISmartDevice(){
	private _target;
	constructor(target:Monitor){this._target = target;}
	connect(){
		this._target.makeConnection();
	}
	get device(){
		return this._target.getDeviceName();
	}
}

smartHomeClient.addConnection(new MonitorAdapter(new Monitor()));
```
- MonitorAdapter 는 connect 와 device 를 가지고 있으므로 가능!

### Class Adapter

- 클래스 상속 이용한 어댑터 패턴
- Adaptee ( Service ) 상속하므로 , 객체 구현없이 바로 코드 재사용 가능
- 자바에서는 다중 상속 불가 문제때문에 전반적 권장 X ( Typescript 역시 불가능! )

```typescript
class MonitorAdapter extends Monitor implements ISmartDevice{
	connect(){
		makeConnection();
		return true;
	}
	device(){return getDeviceName();}
}
```

### 장점
- 비즈니스 로직에서 Interface or Data 변환 로직 분리 가능!
	-> SRP 만족
- 기존 클래스 코드 건들지 않고 클라이언트 인터페이스 통해 어댑터 작동
	-> OCP 만족
- 추가 필요 메소드가 있다면 , 어댑터에 추가 구현 가능!
		+ 버그 발생해도 , Adapter Class 만 조사 & 검사 하면 됨!
	-> 검사가 쉬워짐
### 단점
- 새로운 인터페이스 와 어댑터 클래스 세트 도입해야 하므로 , 코드 복잡성 증가!
- 때로는 서비스 ( Adaptee ) 클래스 변경이 더 간단한 경우도 있음!

### 예시
```typescript
const app = await NestFactory.create(AppModule, new FastifyAdapter());
```
- Fastify 를 NestJS 와 호환되게 하는 Adaptor 역활
###  By GPT

  
	어댑터 패턴(Adapter Pattern)은 소프트웨어 설계 패턴 중 하나로, 인터페이스 호환성 문제를 해결하기 위해 사용됩니다. 기존의 클래스가 제공하는 인터페이스와 다른 인터페이스를 기대하는 클라이언트 사이의 중개자 역할을 하여, 함께 동작하지 않을 것 같은 두 클래스가 서로 협력할 수 있게 해줍니다.
	
	어댑터 패턴은 주로 두 가지 상황에서 사용됩니다:
	
	1. **기존 시스템에 새로운 클래스나 시스템을 통합할 때**: 새로운 컴포넌트가 기존 시스템의 인터페이스와 다를 때 어댑터를 사용하여 새 컴포넌트를 기존 시스템에 맞게 연결할 수 있습니다.
	2. **기존 클래스를 리팩토링 없이 사용할 때**: 기존 코드를 변경하지 않고도 어댑터를 통해 새로운 인터페이스를 제공하여 기존 클래스를 다른 방식으로 사용할 수 있습니다.
	
	어댑터 패턴은 두 가지 형태로 구현될 수 있습니다:
	
	- **객체 어댑터 패턴(Object Adapter Pattern)**: 컴포지션을 사용하여 기존 클래스의 인스턴스를 새 인터페이스에 맞게 '포장'합니다. 어댑터는 기존 인스턴스를 내부적으로 가지고 있으며, 클라이언트가 호출할 수 있는 새 인터페이스를 제공합니다.
	- **클래스 어댑터 패턴(Class Adapter Pattern)**: 다중 상속(Java에서는 지원하지 않음)을 사용하여 기존 클래스를 확장하고 새 인터페이스를 구현합니다. 이 방식은 기존 클래스의 일부 메서드를 오버라이드하여 새 인터페이스를 만족시킬 수 있습니다.
	
	어댑터 패턴의 예시:
	
	클라이언트가 사용하는 메서드 `request()`와 호환되지 않는 기존 클래스 `Adaptee`의 메서드 `specificRequest()`가 있을 때, 어댑터 클래스 `Adapter`는 `request()`를 구현하고 내부적으로 `Adaptee`의 `specificRequest()`를 호출합니다. 클라이언트는 `Adapter` 인스턴스를 통해 `Adaptee`의 기능을 사용할 수 있게 됩니다.
### 참고 출처

https://inpa.tistory.com/entry/GOF-%F0%9F%92%A0-%EC%96%B4%EB%8C%91%ED%84%B0Adaptor-%ED%8C%A8%ED%84%B4-%EC%A0%9C%EB%8C%80%EB%A1%9C-%EB%B0%B0%EC%9B%8C%EB%B3%B4%EC%9E%90

https://www.youtube.com/watch?v=YlIkB4frPcw
##### Writed By Obisidan