
### 합동식

- 정수 a 와 0이 아닌 정수 b가 있을 때 , a = bq + r ( 0 <= r  < |b| )를 만족하는 정수 q,r 은 유일함 => 합동
	- q : a // b
	- r : a % b
- 정수 a,b 와 양의 정수  n 이 있을 때 , n | ( a - b ) 이면 a 와 b 가 %n 에서 합동
	( n | a -b : n 이 a와 b의 차이를 나눈다. => a-b 가 n의 배수라는 뜻 , 4 | ( 11 - 7 ))
	- a % n ≡ b % n ( 앞으로 , 좌식 우식 모두 %n 을 하는 경우에는 뒤에 *- mod n* 으로 표기 )
		-> a 와 b를 n으로 나눈 나머지가 동일
#### 합동식의 성질
- 반사성 , 대칭성 , 추이성
	- a ≡ a - mod n : 반사성
	- a ≡ b - mod n 이면 , b ≡ a - mod n : 대칭성
	- a ≡ b - mod n 이고 , b ≡ c  - mod n 이면 , a ≡ c % n : 추이성
- 사칙 연산
	- a ≡ b - mod n , c ≡ d - mod n 이면
	- a ± c ≡ b ± d - mod n 
	- a`*`c ≡ b`*`d - mod n 
	- 나눗셈에서는 성립 X
	- ac ≡ bc % n 이고 , gcd (c,n) = 1이면 a ≡ b % n 
		( gcd : 최대 공약수 , 1이면 서로소 )
#### 합동식의 문제들
##### 잉여역수
- a`*`x ≡ 1 - mod n 을 만족하는 x 를 찾는 문제
	( ax % m = 1 이 나오게 하는 x 의 값 )
	-> 실수 체계에서 1 / a 의 역활을 하는 값을 구하는 문제
##### 이산 로그
- a`^`x ≡ b - mod n 을 만족하는 x 를 찾는 문제
	( a 의 거듭제곱 % n 이 b 가 나오게 하는 x 의 값  )
	-> 실수 체계에서 log_a b 역활을 하는 값을 구하는 문제
##### 이산 제곱근
- x^2 ≡ a - mod n 을 만족하는 x 를 찾는 문제
	( x 의 제곱 % n 이 a가 나오게 하는 x의 값 )
	-> 실수 체계에서 √x 의 역활을 하는 값을 구하는 문제

=> 잉여역수에 Focus
- a의 잉여역수 a^-1 가 존재한다면 , 양변 a로 나누는 대신 `a^-1`을 곱할 수 있음 
	( 나눗셈이 성립하지 않으나 , 곱셈은 성립함! => 나눗셈과 같은 효과 기대 가능 )
- 페르마 소정리를 이용하여 n이 소수이고 a 가 n 배수가 아닐 때 잉여역수 빠르게 구할 수 있음

### 페르마 소정리
- p가 소수이고 , a가 p의 배수가 아니면 a^(p-1) ≡ 1 - mod p 가 성립한다.
- p가 소수이면 모든 정수 a에 대해 a^p ≡ a - mod p 가 성립한다.

- a`*`a^(p-2) ≡ 1 - mod p 
- p가 소수이고 a가 p의 배수가 아니면 a^(p-2) 는 a의 잉여역수

### 잉여역수 활용
- 이항계수 % p 의 빠른 계산 가능!

### 참고 출처

https://www.youtube.com/watch?v=e7XnAlaD3TI

##### Writed By Obisidan