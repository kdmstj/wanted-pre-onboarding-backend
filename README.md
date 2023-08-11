# wanted-pre-onboarding-backend
**`이름` :**  강은서<br/>
**`데모영상` :** 링크 삽입<br/>

## AWS 배포 환경
**`AWS 배포 주소` :**  52.78.179.183<br/>
![image](https://github.com/kdmstj/wanted-pre-onboarding-backend/assets/62414231/a8dc90c2-8f2a-4272-b974-48b6dd3f6294)

## 애플리케이션 실행 방법(엔드포인트 호출 방법 포함)
### 애플리케이션 실행방법

    $ ./gradlew clean build --exclude-task test
    $ docker-compose up -d

### 엔드포인트 호출 방법
| HTTP Method | EndPoint | Description |
|------|---|---|
|POST|/members|회원가입|
|POST|/auth/login|로그인|
|POST|/posts|게시물 등록|
|GET|/posts|게시물 목록 조회|
|GET|/posts/{post-idx}|특정 게시물 조회|
|PATCH|/posts/{post-idx}|게시물 수정|
|DELETE|/posts/{post-idx}|게시물 삭제|



## 데이터베이스 테이블 구조
![image](https://github.com/kdmstj/wanted-pre-onboarding-backend/assets/62414231/e67533b9-af4f-452e-996d-5ef5e3c61ddf)

## 구현 방법
### 회원가입
회원가입 유효성 검사 방법 ( 이메일 , 비밀번호)
비밀번호 암호화 저장

### 로그인
로그인 AuthenticationFilter (Response Header JWT 토큰 반환)
로그인 유효성 검사 방법 ( 이메일 , 비밀번호)

## 게시물 생성
Request Header Authorization JWT 포함, JWT Verfication Filter
CustomUserDetails에서 userIdx 추출

## 게시물 조회
특정 게시물 조회

## 게시물 목록 조회
페이지네이션

## 게시물 수정
Request Header Authorization JWT 포함

## 게시물 삭제
Request Header Authorization JWT 포함

### API 명세(request/response 포함)
