### 문제 링크
https://www.acmicpc.net/problem/11000

### 문제 해석
- Si 시작해서 Ti 에 끝나는 N개의 수업이 있다.
- 최소의 강의실을 사용해서 모든 수업 가능하게 하자!

### 문제 해결
-  Heap 을 활용
	-> 가장 작은 값 보다 작으면 추가 ( 강의실이 더 필요함 )
	-> 가장 작은 값보다 크거나 같으면 제거 후 추가 ( 끝난 강의실은 제거 후 다시 추가 )
	-> 가장 큰 값보다 크면? ( 기존 값들 필요 X )
### 슈도 코드
``` Text
hq = ary[0] 의 오른쪽 값 추가

i 를 1 부터 반복
	만약 i 번쨰 left 보다 hq 의 의 최소값이 작다면
		hq 에 추가
	아니면
		hq 제거후 추가
	만약 i번째 left 보다 hq 의 최대값이 작다면
		최대값 교체
		hq 초기화

최대값 출력

```

### 제출 코드
``` python
from sys import stdin
import heapq

input = stdin.readline

n = int(input())


ary = []
for _ in range(n):
    ary.append(list(map(int,input().split())))
hq = []

max_count=[0]


ary.sort()
ary.append([10**9+1,0])
min_ary = [ary[0][1]]
# 0 이 가장 작음
# 1 이 가장 큼
for index in range(1,n+1):
    element = ary[index][1]
    if ary[index][0]<min_ary[0]:
        heapq.heappush(min_ary,element)
    else:
#        min_ary[0]<=ary[index][0]
        heapq.heappushpop(min_ary,element)
    if ary[index][0]>=min_ary[-1]:
        if max_count[-1]<len(min_ary):
            max_count[-1]=len(min_ary)
        min_ary = [ary[index][1]]

print(max_count[-1])
```

### 사담
- 그리디이나 , 우선순위를 사용해야만 하는 그리디
- 처음에는 , 첫번째의 오른쪽 값만 비교하는 식으로 했지만 그렇게 하면 절대 안풀림
	-> 반레를 보냐 안보냐에 난이도 차이가 존재했다.

##### Writed By Obisidan