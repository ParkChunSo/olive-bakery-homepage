# Olive Bakery REST Api Server
## About
모친이 운영하는 베이커리의 홈페이지로써 기본적인 기능과 관리기능(예약, 매출, 상품 등)을 개발했습니다.
베이커리 특성상 당일 아침에 당일 판매할 빵을 모두 만들어야 하기 때문에 예약 시스템이 있다면 대략적인 수량을 파악할 수 있지 않을까 하는 마음에서
개발하였습니다. 또한 수기로 관리하고 있는 매출 정보 또한 편리하게 관리하고 한눈에 볼 수 있는 그래프로 보여줄 수 있게 매출 관리 시스템을 도입하였습니다.

## Function
1. 각 도메인 별 Crud
2. 예약 관리 시스템
3. 매출 관리 시스템

## Usage
1. Spring Boot
2. Maven
3. Spring Security, JWT
4. JPA, QueryDSL
5. MySQL, H2
6. Swagger
7. AWS, RDS

## Unique
1. Spring-Security와 JWT를 활용하여 Role의 따라 차별화된 서비스 제공
2. Swagger를 사용하여 Front-End 개발자와 지속적인 컨텍
3. JPA와 QueryDSL을 사용하여 보다 안정적인 서버
4. 도메인을 직관적으로
5. 브랜치 전략
6. Google-code-style 적용

## TODO
1. 테스트 케이스 만들기
2. 로그백 활용하여 로깅 체계화
3. AOP를 활용하여 로깅
