# wanted-pre-onboarding-backend
**`이름` :**  강은서<br/>
**`데모영상` :** [데모영상 보러가기](https://youtu.be/g7dNa-cl4V8) <br/>

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
- __회원가입 유효성 검사 방법(이메일 양식, 비밀번호 8자 이상)<br/>__
Bean Validator를 이용해 LocalValidatorFactoryBean이 객체의 제약 조건을 검증하도록 지시하는 어노테이션인 @Valid를 사용하였다. 회원가입을 요청하는 DTO에 이메일 양식 제약 조건 어노테이션인 @Email, 비밀번호 길이가 8자 이상 작성해야 한다는 제약 조건 어노테이션 @Size(min = 8)을 붙이고 컨트롤러의 메소드에 @Valid를 붙여주면 유효성 검증이 진행된다. 검증에 오류가 있다면 MethodArgumentNotValidException 예외가 발생하게 되고, GlobalExceptionHandler에서 MethodArgumentNotValidException Handler를 Override하여 공통 JSON Response인 ErrorResponse로 변환하여 응답하였다.<br/>

- __비밀번호 암호화 저장<br/>__
Spring Security PasswordEncoder를 이용하여 Service 계층에서 평문의 비밀번호를 암호화하여 DB에 저장하였다. @Configuration 어노테이션을 활용하여 Spring Security와 관련된 클래스들을 Bean으로 등록하고자 함을 명시하고, PasswordEncoderFactories 클래스의 createDelegatingPasswordEncoder() 메소드 통해 PasswordEncoder를 리턴하는 passwordEncoder 메소드를 @Bean 어노테이션을 활용하여 Bean으로 동록하였다. 해당 메소드를 통해 암호화된 비밀번호는 {encodingId} + encode된 password 형태를 갖는다. encodingId는 암호화 알고리즘을 나타내는데 이 프로젝트에서는 Bcrypt을 사용하였다. 

### 로그인
- __회원 인증<br/>__
UsernamePasswordAuthenticationFilter클래스를 상속받은 JWTAuthenticationFilter 클래스를 빈으로 등록된 SecurityFilterChain에 등록하여 회원 인증 하였다. 사용자 요청을 Object Mapper 클래스의 readValue() 메서드로 LoginDto로 변환하고, UsernamePasswordAuthenticationToken에 principal은 이메일, credentials는 비밀번호를 담아 생성하고 AuthenticationManager의 authenticate 메서드가 실질적인 인증 처리를 하도록 하였다. 인증 처리는 다음과 같은 순서로 하게 된다.
1. ID 검증 : UserDetailsService를 상속받은 MemberDetailsService 클래스에서 loadUserByUsername메서드를 통해  Username(나와 같은 경우 email)로 DB에서 사용자를 조회한다. 사용자가 없을 경우 Exception이 발생되고, 성공하면 Member객체의 정보(memberIdx, email, password)가 담긴 MemberDetails 객체를 반환한다. 
2. Password 검증: 반환 받은 MemberDetails 객체에 저장된 password와 UsernamePasswordAuthenticationToken에 저장된 password(로그인시 입력한 password)를 matches 메서드를 이용하여 비교하고 일치하지 않으면 UNAUTHORIZED 401 에러를 반환한다. 
3. 추후에 JWT 토큰을 사용하여 사용자 식별을 하기 위해서 JWT Tokenizer 클래스에서 Access Token을 생성하는 메소드를 작성할 때 "memberIdx"와 "username"이 키 값으로 담긴 claim을 함께 build한다. 생성된 JWT 토큰은 로그인 성공 시 response Header 값으로 키는 Authorization 값은 "Bearer " + accessToken 으로 하여 응답하도록 하였다. 


### 게시물 생성,수정,삭제 시 회원 검증
- __회원 검증<br/>__
1. 메소드 실행 전 권한 검사
Controller 계층에서 @PreAuthorize("isAuthenticated()") 어노테이션을 통해 메서드를 실행시키기 전에 권한 검사를 하도록 하여 사용자가 Request시 Header에 키는 Authorization 값은 JWT 토큰을 포함시켜 요청을 보내고 현재 사용자가 익명이 아니라면 (로그인 상태라면) true값을 내 메서드를 실행시키도록 하였다.
2. JWT 토큰으로 사용자 식별
토큰을 포함시켜 요청을 보내게 되면 검증을 처리하는 클래스인 JWTVerificationFilter 클래스를 생성하여 빈으로 등록된 SecurityFilterChain에 등록하였다. 키는 "username" 값은 email이 담긴 claim을 함께 build한 JWT 토큰에서 그 값을 추출하여 MemberDetailsService의 loadUserByUsername 메소드로 사용자 식별을 하고, 이 정보를 전역적으로 사용할 수 있도록 SecurityContext에 전달하였다.
3. SecurityContext에 저장된 Authentication 객체에서 memberIdx를 추출하여 해당 게시물의 소유자인지 검증하도록 하였다.등록된 게시물이 없는 경우 404, 게시물의 소유자가 아닌 경우 401 에러가 발생한다.

### 게시물 목록 조회 페이지네이션
- __게시물 목록 페이지네이션 조회<br/>__
Pageable 인터페이스의 구현체인 PageRequest로 객체를 생성해 페이지네이션을 구현하였다. 사용자가 RequestParam으로 조회하고 싶은 페이지 값(1 이상)을 요청하게 되면, 실제 페이지는 0부터 시작되므로 Service 계층에서 요청이 들어온 페이지 값에 1을 뺀 값과 페이지네이션 단위가 담긴 PageRequest를 생성하고 PagingAndSortingRepository 인터페이스의 findAll 메소드로 조회하도록 하였다.

<br/>

## API 명세(request/response 포함)
### 회원가입

    POST /members

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
````json
400 Bad Request
- 이메일 조건(@포함)을 충족하지 않은 경우
- 비밀번호 조건(8자 이상)을 충족하지 않은 경우
{
    "status": 400,
    "error": "BAD_REQUEST",
    "code": "INVALID_INPUT",
    "message": "[email] : 이메일 형식이 맞지 않습니다. [password] : 비밀번호는 8자 이상이어야 합니다. "
}
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
````json
400 Bad Request
- 이메일 조건(@포함)을 충족하지 않은 경우
- 비밀번호 조건(8자 이상)을 충족하지 않은 경우
{
    "status": 400,
    "error": "BAD_REQUEST",
    "code": "INVALID_INPUT",
    "message": "[email] : 이메일 형식이 맞지 않습니다. [password] : 비밀번호는 8자 이상이어야 합니다. "
}
````
````json
401 UNAUTHORIZED 등록된 회원이 아닌 경우
{
    "status": 401,
    "error": "UNAUTHORIZED",
    "code": "INVALID_USER",
    "message": "등록된 회원 정보가 없습니다."
}
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
404 Not Found (등록된 게시물이 없는 경우)
{
    "status": 404,
    "error": "NOT_FOUND",
    "code": "POST_NOT_FOUND",
    "message": "해당 게시물을 찾을 수 없습니다."
}
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
404 Not Found (등록된 게시물이 없는 경우)
{
    "status": 404,
    "error": "NOT_FOUND",
    "code": "POST_NOT_FOUND",
    "message": "해당 게시물을 찾을 수 없습니다."
}
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
404 Not Found (등록된 게시물이 없는 경우)
{
    "status": 404,
    "error": "NOT_FOUND",
    "code": "POST_NOT_FOUND",
    "message": "해당 게시물을 찾을 수 없습니다."
}
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
