# Growverse
 - [Notion 홈페이지](https://www.notion.so/ishtar3612/1b719e2014c6808384a4ff5e786c40fd)
---
# 화면
## 공통
- 메인 페이지 ( layout/main.html )  
  부모화면으로 메뉴 버튼을 제공하고, 다른 화면을 액자식으로 보여줌.

- 비로그인 메인 페이지 ( widgets/common/guest_main.html )  
  공통적으로 보여주는 메인 페이지로 공지사항, 추천코스, 베스트 리뷰 등을 보여줌.

- 로그인 페이지( layout/login.html )  
  로그인하는 화면

- 회원가입 페이지( layout/register.html )  
  회원가입하는 화면

- 아이디 찾기 페이지( layout/find_id.html )  
  잊어버린 ID를 찾아주는 화면

- 비밀번호 찾기 페이지( layout/find_password.html )  
  잊어버린 ID를 찾아주는 화면

## 사장님
- 사장님 메인 페이지( widgets/boss/main.html )  
  사장님이 보낸 제안서를 조회/취소할 수 있고, 제안서를 보낼 수 있는 요청들을 확인하고 작성할 수 있음.
- 제안서 작성 페이지 ( widgets/boss/join_deal.html )
  요청에 대해서 제안서를 작성하는 화면 
- 매장정보 페이지 ( widgets/boss/myplace.html )
  내 매장에 대한 정보를 관리하고, 제안서 작성 시 기본템플릿도 관리함.

## 손님
- 손님 메인 페이지( widgets/customer/main.html )  
  내가 보낸 제안서 요청의 현황을 조회하고, 요청을 체결/취소할 수 있음.
- 제안서 요청 페이지 ( widgets/customer/open_deal.html )
  제안서 요청을 작성하는 화면 
---
# 원장
- TB_CUSTOMER_DEALS : 제안서 요청에 대한 정보를 담음
- TB_BOSS_DEALS : 제안서에 대한 정보를 담음
---