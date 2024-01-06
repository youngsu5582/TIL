### Jenkins 설치

강사는 EC2 에 직접 설치하는 식으로 했으나 , 
docker-compose 를 통한 자동 Pulling 으로 설치

```docker-compose
services:
  jenkins:
    image: jenkins/jenkins:lts
    container_name: jenkins
    ports:
      - 8080:8080  # Jenkins 웹 인터페이스에 사용됩니다.
      - 50000:50000 # Jenkins 에이전트 연결에 사용됩니다.
    volumes:
      - jenkins_data:/var/jenkins_home # Jenkins 데이터 보존을 위한 볼륨
    environment:
      - JENKINS_OPTS=--httpPort=8080

volumes:
  jenkins_data: {}

```


### AWS EC2 설정

![](https://i.imgur.com/soQNAoR.png)


- 단순 테스트 용이므로 , 
- AMI 프리티어 용으로 생성

![450](https://i.imgur.com/sFWEF7f.png)

해당 기능을 통해 키 페어 생성
=> 생성한 키 ( In Macbook 기준 ) ~/.ssh 로 이동


![450](https://i.imgur.com/bn3VqZ7.png)

Instance 에서 퍼블릭 IP 확인

.ssh 에서 config file 에 해당 Option 추가

```cmd
Host test_jenkins
    User ec2-user
    HostName 52.79.233.177
    IdentityFile ~/.ssh/jenkins_test_key.pem
```


![450](https://i.imgur.com/DBPHbhS.png)

보안 그룹 - 인바운드 규칙에 8080 Port 추가

https://github.com/frontalnh/temp

해당 코드 git clone 후 , 자신에게 맞게 rm -rf .git + repo 명으로 변경 후 git init