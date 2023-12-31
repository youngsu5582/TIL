
DP 를 활용한 최단 경로 탐색 알고리즘 입니다.

- 사례로는 GPS 경우에 가장 많이 사용됩니다.

특정 하나의 정점에서 다른 모든 정점으로 가는 최단 경로를 알려줍니다.
( 이 때 , 음의 간선을 포함할 수는 없습니다 )

#### Why DP?

결국 최단 거리는 여러 개의 최단 거리로 이루어져 있기 때문입니다.
-> 하나의 최단 거리를 구할 때 그 이전까지 구한 최단 거리 정보를 그대로 사용합니다

![450](https://i.imgur.com/pgxCv7y.png)

1부터 가는 기본적인 경로 :

1 -> 2 : 3
1 -> 3 : 6
1 -> 4 : 7

2를 통해 가는 경로 :

1 -> 2 -> 3 : 4

##### 구체적인 작동 과정

1. 출발 노드 설정
2. 출발 노드 기준 각 노드 최소 비용 저장
3. 방문하지 않은 노드 중 가장 비용 적은 노드 선택
4. 해당 노드를 거쳐 특정 노드를 가는 경우를 고려해 다시 최소 비용을 갱신
5. 3 ~ 4번 과정 반복

![450](https://i.imgur.com/fCJvB2J.png)

해당 경로들에 대해 2차원 배열을 만들면 , 

|   0 |   2 | 5 |   1 | INF | INF |
|:----|:----|:--|:----|:----|:----|
|   2 |   0 | 3 |   2 | INF | INF |
|   5 |   3 | 0 |   3 |   1 |   5 |
|   1 |   2 | 3 |   0 |   1 | INF |
| INF | INF | 5 |   1 |   0 |   2 |
| INF | INF | 5 | INF |   2 |   0 |  

이렇게 나옵니다

##### 1. 출발 노드 설정

우선 , 1번 노드로 선택을 해보겠습니다

##### 2. 각 노드의 최소 비용 저장

| 0 | 2 | 5 | 1 | INF | INF |
|:--|:-:|:-:|:-:|:---:|:---:|

##### 3. 방문하지 않은 노드 중 가장 비용 적은 노드 선택 ( 4 )

0 , INF 를 제외하고 최소 값인 4번 노드를 선택합니다.

##### 4. 해당 노드를 거쳐 특정 노드를 가는 경우를 고려해 다시 최소 비용을 갱신

1 -> 4 : 1 , 4-> 5 : 1 
로 1->5 는 비용이 2가 됩니다

1 -> 4 : 1 , 4 -> 3 : 3
로 1->3 는 비용이 4가 됩니다

| 0 | 2 | 4 | 1 | 2 | INF |
|:--|:-:|:-:|:-:|:---:|:---:|

##### 3. 방문하지 않은 노드 중 가장 비용 적은 노드 선택 ( 2 )

그 다음 최소값인 2번 노드를 선택합니다.
##### 4. 해당 노드를 거쳐 특정 노드를 가는 경우를 고려해 다시 최소 비용을 갱신

2번 노드를 통해서도 갱신이 되는 값이 없습니다.

| 0 | 2 | 4 | 1 | 2 | INF |
|:--|:-:|:-:|:-:|:---:|:---:|

##### 3. 방문하지 않은 노드 중 가장 비용 적은 노드 선택 ( 5 )

그 다음 최소값인 5번 노드를 선택합니다.
##### 4. 해당 노드를 거쳐 특정 노드를 가는 경우를 고려해 다시 최소 비용을 갱신

1 -> 4 -> 5 -> 3 을 통해서 1 + 1 + 1 = 3 이 됩니다.
1 -> 4 -> 5 -> 6 을 통해 1 + 1 + 2 = 4 가 됩니다.


| 0 | 2 | 3 | 1 | 2 | 4 |
|:--|:-:|:-:|:-:|:---:|:---:|

##### 3. 방문하지 않은 노드 중 가장 비용 적은 노드 선택 ( 3 )

그 다음 최소값인 3번 노드를 선택합니다.
##### 4. 해당 노드를 거쳐 특정 노드를 가는 경우를 고려해 다시 최소 비용을 갱신

최소 비용 갱신은 일어나지 않습니다.

| 0 | 2 | 3 | 1 | 2 | 4 |
|:--|:-:|:-:|:-:|:---:|:---:|

##### 3. 방문하지 않은 노드 중 가장 비용 적은 노드 선택 ( 6 )

그 다음 최소값인 6번 노드를 선택합니다.
##### 4. 해당 노드를 거쳐 특정 노드를 가는 경우를 고려해 다시 최소 비용을 갱신

최소 비용 갱신은 일어나지 않습니다.

| 0 | 2 | 3 | 1 | 2 | 4 |
|:--|:-:|:-:|:-:|:---:|:---:|

이렇게 끝이 납니다.

### Heap

배열을 선언해 , 단순 탐색하면 시간 복잡도가 O(N^2) 가 됩니다.
추가로 , 공간 복잡도 역시 O(N^2) 가 됩니다.

그렇기에 , 가장 작은 값을 가지는 힙 구조를 활용해 시간 복잡도를 O(N*logN) 을 만들 수 있습니다.


### 단순 구현 코드

```python
import sys
input = sys.stdin.readline

V,E = map(int,input().split())
k = int(input())

ary = [[] for _ in range(V+1)]

for _ in range(E):
    u,v,w = map(int,input().split())
    ary[u].append((v,w))

visited = [False] * (V+1)
visited[k] = True
visited[0]= True
min_path = [float('inf') for _ in range (V+1)]

def find_min():
    min_index = 0
    for i in range(1,V+1):
        if visited[i] is False and min_path[i]<min_path[min_index]:
            min_index = i
    return min_index
for vertex,cost in ary[k]:
    min_path[vertex] = min(min_path[vertex],cost)

while True:
    index = find_min()
    if index == 0:
        break
    for vertex,cost in ary[index]:
        min_path[vertex] = min(min_path[vertex],min_path[index] + cost)
    visited[index] = True
```

