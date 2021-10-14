# 2 depth 이상 대댓글 구현 ( 트리구조 RDBMS 에 저장하기 )  
* 대댓글 시뮬레이션 HTTP API 서버와 프론트엔드  

![recomment](https://user-images.githubusercontent.com/12610035/136926043-491cc55e-1dfe-47e3-b336-eb3d233638d8.gif)  

## [v1.0]  
* 연속된 두 개의 문자열을 하나의 레벨로 지정  (ex: '00' 첫 댓글, '0000' 첫 댓글의 첫 대댓글, '01' 두번째 댓글)  
* 첫 로딩시 comment_order 순으로 정렬 된 결과 값 수신  

![Screenshot from 2021-10-12 18-04-49](https://user-images.githubusercontent.com/12610035/136926491-b737a6ed-00aa-4fd0-9950-e4f24eadce6a.png)  

## [v2.0]  
* 124 개의 문자로 형제들간 순서 표현 ('%', '_', '/' 제외), '/' 구분자로 계층 구분  
* ASCII 코드 (127진법) 로 변환하여 DB 에 저장 (레코드 압축)  

![Screenshot from 2021-10-14 19-46-54](https://user-images.githubusercontent.com/12610035/137303132-7caaadfc-1272-4126-8b2f-ddd2ecc5e3d9.png)  

* 서버에서 nested JSON 생성하여 응답(재귀 아닌 stack 이용한 반복문으로 구현)  

![Screenshot from 2021-10-14 19-43-49](https://user-images.githubusercontent.com/12610035/137302730-fbebff53-6519-40ce-a1b1-f49de1390bfd.png)  
