
Course Registration Practice App (수강신청 연습 앱)

대학교 수강신청 과정을 모사한 안드로이드 수강신청 연습 애플리케이션입니다.
실제 수강신청 상황에서 발생하는 시간 제한, 경쟁 상황, 성공/실패 처리를 중심으로 구현하였습니다.

프론트엔드는 Android(Java, XML),
백엔드는 JSP 기반 서버와 MySQL 데이터베이스를 사용하여 구성하였습니다.

⸻

프로젝트 개요
	•	프로젝트 유형: 개인 프로젝트 / 실습 프로젝트
	•	목적
	•	안드로이드 앱과 서버(JSP) 연동 경험
	•	실제 수강신청 로직(신청, 중복 체크, 정원 제한 등) 구현
	•	클라이언트–서버–DB 전체 흐름 이해

⸻

사용 기술

Android (Frontend)
	•	Java
	•	Android XML
	•	Activity / Intent
	•	RecyclerView
	•	Dialog / Toast

Backend
	•	JSP
	•	JDBC
	•	Apache Tomcat

Database
	•	MySQL

기타
	•	REST 형태의 데이터 통신
	•	JSON 데이터 처리

⸻

주요 기능

수강신청 기능
	•	과목 목록 조회
	•	수강신청 버튼 클릭 시 서버로 요청 전송
	•	정원 초과 시 신청 실패 처리
	•	이미 신청한 과목 중복 신청 방지

내 수강내역 조회
	•	사용자가 신청한 과목 목록 조회
	•	신청 이력 DB 기반 관리

서버 연동
	•	JSP를 통한 DB 조회 및 저장
	•	JDBC를 이용한 SQL 처리
	•	JSON 응답을 안드로이드 앱에서 파싱

⸻

화면 구성 (예시)
	•	메인 화면: 과목 목록 표시
	•	수강신청 화면: 신청 버튼 및 상태 표시
	•	내 수강내역 화면: 신청 완료 과목 리스트



⸻

프로젝트 구조

Android
 ├─ java/
 │   ├─ activity
 │   ├─ adapter
 │   └─ model
 └─ res/
     ├─ layout
     └─ values

Backend (JSP)
 ├─ *.jsp
 ├─ WEB-INF/
 │   └─ web.xml
 └─ JDBC 연결 로직


⸻

실행 방법

Android
	1.	Android Studio에서 프로젝트 열기
	2.	에뮬레이터 또는 실제 기기 실행
	3.	서버 주소를 JSP 서버 주소로 설정

Backend
	1.	Apache Tomcat 실행
	2.	JSP 프로젝트 배포
	3.	MySQL DB 연결 설정 (DB 계정, URL)

⸻

배운 점
	•	안드로이드와 서버 간 데이터 흐름 이해
	•	JSP + JDBC 기반 서버 로직 구현 경험
	•	실제 서비스와 유사한 비즈니스 로직 설계 경험
	•	프론트엔드와 백엔드 역할 분리의 중요성 인식

⸻

향후 개선 사항
	•	로그인/세션 기반 사용자 구분
	•	동시 신청 경쟁 상황(락 처리) 강화
	•	UI/UX 개선
	•	Spring 기반 백엔드로 리팩토링

⸻

저장소
	•	Android App
https://github.com/woomin2021/course-registration-android
	•	Backend (JSP)
https://github.com/woomin2021/course-registration-backend-jsp
<img width="3024" height="1964" alt="스크린샷 2025-11-14 오후 9 38 02(2)" src="https://github.com/user-attachments/assets/7890d987-eaf0-4bb6-ac3f-3acfdbfc906d" />



⸻

