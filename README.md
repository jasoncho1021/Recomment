# 2 depth 이상 대댓글 구현 ( 트리구조 RDBMS 에 저장하기 )  
* 대댓글 시뮬레이션 HTTP API 서버와 프론트엔드  

![recomment](https://user-images.githubusercontent.com/12610035/136926043-491cc55e-1dfe-47e3-b336-eb3d233638d8.gif)  

## [v1.0]
* 첫 로딩시 comment_order 순으로 정렬 된 결과 값 수신  

![Screenshot from 2021-10-12 18-04-49](https://user-images.githubusercontent.com/12610035/136926491-b737a6ed-00aa-4fd0-9950-e4f24eadce6a.png)  

## [v2.0]
* 서버에서 nested JSON 생성하여 응답  

![Screenshot from 2021-10-14 19-43-49](https://user-images.githubusercontent.com/12610035/137302730-fbebff53-6519-40ce-a1b1-f49de1390bfd.png)  

* ASCII 코드 (127진법) 로 변환하여 DB 에 저장 (레코드 압축)  

![Screenshot from 2021-10-14 19-46-54](https://user-images.githubusercontent.com/12610035/137303132-7caaadfc-1272-4126-8b2f-ddd2ecc5e3d9.png)  
