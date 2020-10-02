# KDLibrary

도서관 관리 & 이용 시스템

## 개요
- 소규모 온라인 도서관 시스템을 구상
- 회원과 관리자가 한 프로그램으로 도서관 시스템 이용과 전체적인 관리를 가능하도록 제작한 프로그램

### 개발일정
- 2020/06/16 ~ 2020/06/30

### 개발환경
- 운영체제 : Windows 10 64bit
- 언어 : Java - jdk1.8.0_251, JavaFx
- 개발툴 : Eclipse IDE version 4.14.0
- DB : MySQL 5.7 
- DBMS : Workbench 6.3.5 

### 주요기능
1. 도서관 시스템에 회원, 도서를 등록하고 관리할 수 있다.
2. 회원가입을 통해 회원유저는 등록된 도서를 대여,반납할 수 있다.
3. 공지사항과 일정을 양방향으로 등록하고, 확인할 수 있다.
4. 필요한 자료를 요청하고, 요청한 도서를 등록할 수 있다.
5. 도서관시스템에서 도서를 대여한 모든 기록을 통계로 확인할 수 있다.

### 프로그램 소개

#### 메인화면 
- 회원가입, 로그인, 비밀번호찾기, 관리자 로그인  
![image](https://user-images.githubusercontent.com/63944004/94945696-07032c80-0516-11eb-9d17-e878be35d015.png)

1. 회원가입  
- 중복된 아이디가 있는지 확인
- 비밀번호와 비밀번호 확인    입력값이 일치하는지 확인
- 모든 항목이 입력되었는지 확인
- 보안문구가 일치하는지 확인
- 위 항목을 검사하고, 올바르게 작성하였다면 회원으로 등록  
![image](https://user-images.githubusercontent.com/63944004/94945615-e935c780-0515-11eb-8703-2ebf4749b437.png)

2. 비밀번호 찾기  
- DB에 등록된 아이디와 휴대전화   정보로 회원 정보를 찾아 해당   계정의 비밀번호를 바꾸는 기능
- 새 비밀번호와 새 비밀번호 확인    입력값이 일치하는지 확인 후    일치하면 변경 완료  
![image](https://user-images.githubusercontent.com/63944004/94945722-12565800-0516-11eb-99ed-1f7a2a0229bd.png)

<br>  

#### 회원 메인화면
- 회원정보수정, 자료검색, 대여중인 책, 자료신청, 공지사항, 일정  
![image](https://user-images.githubusercontent.com/63944004/94945740-184c3900-0516-11eb-842f-38099ea07052.png)  
<br>  

1. 회원정보 수정
- 비밀번호를 확인 후 회원정보 수정 창으로 진입  
- 비밀번호와 휴대전화를 변경할 수 있음 
![image](https://user-images.githubusercontent.com/63944004/94945756-1e421a00-0516-11eb-8581-b3a58c628175.png)  
<br>  

2. 자료검색
- 도서목록을 확인
- 분류별로 정렬
- 도서명으로 도서 검색
- 도서 선택시 자세한 정보 확인가능. ->대여  
- 이미 해당 도서가 대여되었는지, 현재 대여중인 도서가 있는지 확인 후 해당사항이 없다면 도서를 대여할 수 있음
![image](https://user-images.githubusercontent.com/63944004/94945774-2437fb00-0516-11eb-8665-9ca8a73f6552.png)  
![image](https://user-images.githubusercontent.com/63944004/94945787-29954580-0516-11eb-9397-d6982aef4d5a.png)    
<br>  

3. 대여중인 책
- 대여중인 도서 정보를 확인
- 대여중인 도서를 반납할 수 있음  
![image](https://user-images.githubusercontent.com/63944004/94945796-2ef29000-0516-11eb-9732-1f410e140e10.png)    
<br>  

4. 자료 신청
- 관리자에게 자료요청(메세지 전송)
- 제목과 내용을 입력 후 자료를 신청할 수 있음 
![image](https://user-images.githubusercontent.com/63944004/94945812-344fda80-0516-11eb-873d-a11164fad9ea.png)    
<br>  

5. 공지사항
- 도서관의 공지 사항을 확인
- 리스트의 공지 사항을 클릭 시 해당 공지사항 내용을 확인 가능  
![image](https://user-images.githubusercontent.com/63944004/94945840-3f0a6f80-0516-11eb-8f41-6b6c5502e6e9.png)    
<br>  

6. 일정
- 도서관의 일정을 달력으로 확인
- 일정이 있는날은 색으로 표시
- 표시된 달력을 클릭하면 해당 날짜의 일정 확인 가능  
![image](https://user-images.githubusercontent.com/63944004/94945827-3a45bb80-0516-11eb-9fce-badd160e0e36.png)    
<br>  



<br>  
<br>  

#### 관리자 메인화면
- 관리, 공지사항, 일정, 대여기록  
![image](https://user-images.githubusercontent.com/63944004/94945867-492c6e00-0516-11eb-878c-49fca6f5972f.png)  
<br>  



1. 관리  
1-1 회원관리
- 도서관에 등록된 회원 정보를 조회, 수정, 삭제할 수 있음
- 이름으로 검색 가능  
![image](https://user-images.githubusercontent.com/63944004/94945878-4df12200-0516-11eb-80a7-e34fe8f67ada.png)    
<br> 

1-2 도서관리
- 도서관에 등록된 도서 정보를 조회, 추가, 수정, 삭제할 수 있음
- 도서 제목으로 검색 가능
- 장르별 도서의 개수를 그래프로 확인  
![image](https://user-images.githubusercontent.com/63944004/94945888-534e6c80-0516-11eb-9d35-798c21d802d0.png)  
<br> 

1-3 자료신청  
- 도서관 이용자들이 신청한 목록 관리 탭
- 신청내용을 확인하고 도서를 등록할 수 있음  
![image](https://user-images.githubusercontent.com/63944004/94945902-59444d80-0516-11eb-8b20-455ba8b100ff.png)  
<br> 


2. 공지사항 
- 도서관의 공지사항을 등록, 수정, 삭제할 수 있는 기능
- 이곳에서 등록한 공지 사항이 회원들의 공지 사항 메뉴에 나타남
![image](https://user-images.githubusercontent.com/63944004/94945916-5e090180-0516-11eb-9d08-181b7dbf8771.png)    
<br> 


3. 일정 
- 도서관의 일정을 등록, 수정, 삭제할 수 있는 기능
- 이곳에서 등록한 일정이 회원들의 일정 메뉴에 나타남
![image](https://user-images.githubusercontent.com/63944004/94945936-65300f80-0516-11eb-8982-a167a2625864.png)    
<br> 


4. 대여기록  
- 도서관 시스템의 모든 대여기록을 확인할 수 있는 기능
- 특정 기간을 설정해 그에 맞는 대여기록 현황을 확인할 수 있음
![image](https://user-images.githubusercontent.com/63944004/94945954-6b25f080-0516-11eb-9dfa-b7877c02933f.png)    
<br> 

