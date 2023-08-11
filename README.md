# wanted-pre-onboarding-backend
**`이름` :**  강은서<br/>
**`데모영상` :** 링크 삽입<br/>

<br/>

## AWS 배포 환경
**`AWS 배포 주소` :**  52.78.179.183<br/>
![image](https://github.com/kdmstj/wanted-pre-onboarding-backend/assets/62414231/a8dc90c2-8f2a-4272-b974-48b6dd3f6294)

<br/>

## 실행 방법
### 애플리케이션 실행 방법
````bash
    $ ./gradlew clean build --exclude-task test
    $ docker-compose up -d
````
<br/>

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

<br/>

## 데이터베이스 테이블 구조
![image](https://github.com/kdmstj/wanted-pre-onboarding-backend/assets/62414231/e67533b9-af4f-452e-996d-5ef5e3c61ddf)

<br/>

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

### 게시물 조회
특정 게시물 조회

### 게시물 목록 조회
페이지네이션

### 게시물 수정
Request Header Authorization JWT 포함

### 게시물 삭제
Request Header Authorization JWT 포함

<br/>

## API 명세(request/response 포함)
### 회원가입

    **POST** /members

- Request

````json
    {
      "email" : "kdmstj@gmail.com",
      "password" : "kdmstjpassword"
    }
````  
- Response
````
  201 Created
````
````
  400 Bad Request
  - 이메일 조건(@포함)을 충족하지 않은 경우
  - 비밀번호 조건(8자 이상)을 충족하지 않은 경우
````

### 로그인

    POST /auth/login

- Request 

````json
    {
      "email" : "kdmstj@gmail.com",
      "password" : "kdmstjpassword"
    }
````  
- Response
````
  200 Ok
````
````
  400 Bad Request
  - 이메일 조건(@포함)을 충족하지 않은 경우
  - 비밀번호 조건(8자 이상)을 충족하지 않은 경우
````

### 게시물 생성

    POST /posts

- Request Headers
```
Authorization : Bearer AccessToken
```
- Request Body
````json
    {
    "content" : "새로운 게시글 생성"
    }
````  
- Response
````
    201 Created
````

### 게시물 목록 조회

    GET /posts?pageNum=1

- Request Headers
```
Authorization : Bearer AccessToken
```
- Request Body
````json
    {
    "content" : "새로운 게시글 생성"
    }
````  
- Response
````json
    200 OK
    [
        {
            "postIdx": 3,
            "content": "새로운 게시글 생성"
        },
        {
            "postIdx": 4,
            "content": "새로운 게시글 생성2"
        }
    ]
````

### 특정 게시물 조회

    GET /posts/{post-idx}

- Response
````json
    200 OK
    {
    "postIdx": 3,
    "content": "새로운 게시글 생성"
    }
````
````json
    //등록되지 않은 게시물 조회 요청
    404 NOT_FOUND
    "status": 404,
    "error": "NOT_FOUND",
    "code": "POST_NOT_FOUND",
    "message": "해당 게시물을 찾을 수 없습니다."
````

### 게시물 수정

    PATCH /posts/{post-idx}

- Request Headers
```
Authorization : Bearer AccessToken
```
- Request Body
````json
    {
    "content" : "새로운 게시글 생성"
    }
````  
- Response
````json
    200 Ok
    {
    "postIdx": 5,
    "content": "수정된 게시글1"
    }
````
````json
    //게시물 소유자가 아닌 경우
    401 Unauthorized
    {
    "status": 401,
    "error": "UNAUTHORIZED",
    "code": "INVALID_AUTH_TOKEN",
    "message": "권한 정보가 없는 토큰입니다."
    }
````
### 게시물 삭제

    DELETE /posts/{post-idx}

- Request Headers
````
Authorization : Bearer AccessToken
```` 
- Response
````
    204 NoContent
````
````json
    401 Unauthorized (게시물 소유자가 아닌 경우)
    {
    "status": 401,
    "error": "UNAUTHORIZED",
    "code": "INVALID_AUTH_TOKEN",
    "message": "권한 정보가 없는 토큰입니다."
    }
````
