# bSearch

검색 API 구현

## DB 구조
![image](https://user-images.githubusercontent.com/51351974/226681733-bcc7aabd-a693-4b7b-a9ce-043d7655f20e.png)

1. datasource : 연동 가능한 API 정보 테이블
2. datasource_req_param : 공통적으로 사용하는 API의 Request 파라미터와 이에 해당하는 API에서 사용하는 파라미터명 정의
3. datasource_res_param : 공통적으로 사용하는 API의 Response 파라미터와 이에 해당하는 API에서 사용하는 파라미터명 정의
4. datasource_header : API의 헤더 정보
5. keyword : 검색 API 호출 시 입력했던 검색어 히스토리

## 기능

1. 데이터소스 : 연동 API에 대한 정보 관리
2. 검색 : 키워드를 특정 데이터소스에 전송해 검색 수행
3. 키워드 : 검색에 사용된 키워드 
