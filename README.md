# 📝 Spring 팀 프로젝트 / PostBlog

### 💡 [Notion개발일지](https://rhetorical-durian-6e6.notion.site/37a80fc9ed704a0e90afbea32cba2d33?v=0e00d1cb800040d0b5773e29f8693a48)
---

#### ⏳ 2022-08-26 ~ 2022-09-01

### 🔎 개발환경
- Java 11

- Spring Boot 2.7.2

- Database : H2, MySQL

- Security : JWT

- AWS S3

#### 기능1
- **게시글 좋아요 기능 및 댓글/대댓글 좋아요**
    - **`200`** AccessToken이 있고, 유효한 Token일 때(== 로그인 상태일 때)만 좋아요 가능하게 하기
    - **`Exception`** AccessToken이 없거나, 유효하지 않은 Token일 때 ‘로그인이 필요합니다.’를 401 응답으로 나타내기
    - 게시글 목록 response에 id, 제목, 작성자, 좋아요 개수, 댓글 개수, 등록일, 수정일 나타내기
       
    → 좋아요 기능 추가 후에 테이블 간의 복잡한 관계매핑 문제로 상위객체 삭제 시 삭제오류가 생기는 것으로 추정(수정해야 할 부분)
    

#### 기능2

- (게시글에 들어갈) **이미지 업로드**
    - AWS IAM, SDK, S3
    - 게시글 작성 중 요청하는 플로우이며, 게시글당 1개의 이미지만 업로드 가능하다는 전제로 진행
    - **`200`** AccessToken이 있고, 유효한 Token일 때(== 로그인 상태일 때)만 요청 가능하게 하기
    + s3 객체 주소를 response로 반환하기 (이미지 url)
    - `Exception` Multipartfile로 이미지 파일을 받고, 파일 변환에 실패할 경우, ‘파일 변환에 실패했습니다’를 200 정상 응답으로 나타내기
    - cascade를 활용해 상위 객체 삭제 시 하위 객체도 모두 삭제   


------
### 💡 API 명세서
![스크린샷 2022-09-15 오후 6 37 51](https://user-images.githubusercontent.com/44489399/190371311-2d3bd1bb-ee35-4052-a0fd-11958f854a03.jpeg)

### 💡ERD 설계
![ERD설계(postBlog)](https://user-images.githubusercontent.com/44489399/190371429-18233770-4a7b-445f-8c11-1ecb85d9dc90.jpeg)

