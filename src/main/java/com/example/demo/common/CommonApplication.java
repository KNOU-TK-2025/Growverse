package com.example.demo.common;

import com.example.demo.boss.BossApplication;
import com.example.demo.common.dao.DaoUser;
import com.example.demo.customer.CustomerApplication;
import com.example.demo.customer.dao.DaoCustomerDeal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;
import org.springframework.web.servlet.view.RedirectView;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.web.servlet.function.RequestPredicates.path;
import static org.springframework.web.servlet.function.RouterFunctions.route;

@SpringBootApplication(scanBasePackages="com.example.demo")
@Controller
public class CommonApplication {
    @Autowired
    private BossApplication bossApplication;

    @Autowired
    private CustomerApplication customerApplication;

    @Autowired
    private AuthManager authManager;

    @Autowired
    private DaoService daoService;

    public static void main(String[] args) throws UnsupportedEncodingException {
        System.setOut(new PrintStream(System.out, true, "UTF-8"));
        System.setErr(new PrintStream(System.err, true, "UTF-8"));
        System.out.println("😊😊😊😊 [한글 인코딩 테스트.] 😂😂😂😂");
        System.out.println("😊😊😊😊 [if you can't see text, type chcp 65001 or something.] 😂😂😂😂");
        SpringApplication.run(CommonApplication.class, args);
    }
    //사용자 구분에 따라 다른 위젯을 띄우는 메서드
    public String get_button_fragment(Model model, HttpSession session) {
        System.out.println(session);

        if ("customer".equals(session.getAttribute("user_mode"))) {
            return "widgets/customer/main";
        }
        else if ("boss".equals(session.getAttribute("user_mode"))) {
            return "widgets/boss/main";
        }
        else {
            return "widgets/common/guest_main";
        }
    }

    // home이라는 주소로 들어올 때 타는 메서드
    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        DaoCustomerDeal daoCustomerDeal = daoService.getMapper(DaoCustomerDeal.class);

        model.addAttribute("menu_buttons", this.get_button_fragment(model, session));
        model.addAttribute("menu_buttons_fragment", "menu_buttons_main");
        model.addAttribute("screen", "widgets/common/guest_main");
        model.addAttribute("screen_fragment", "default");

        model.addAttribute("popular_courses", daoCustomerDeal.SelectCourses(Map.of("POPULAR_YN", "Y")));
        return "layout/main";
    }

    @GetMapping("/alarm")
    public String alarm(Model model, HttpSession session) {
        model.addAttribute("menu_buttons", this.get_button_fragment(model, session));
        model.addAttribute("menu_buttons_fragment", "menu_buttons_main");
        model.addAttribute("screen", "widgets/common/guest_main");
        model.addAttribute("screen_fragment", "alarm");
        return "layout/main";
    }

    @GetMapping("/mypage")
    public String mypage(Model model, HttpSession session) {
        if ("customer".equals(session.getAttribute("user_mode"))) {
            return customerApplication.mypage(model, session);
        }
        else if ("boss".equals(session.getAttribute("user_mode"))) {
            return bossApplication.mypage(model, session);
        }
        else {
            model.addAttribute("menu_buttons", this.get_button_fragment(model, session));
            model.addAttribute("menu_buttons_fragment", "menu_buttons_main");
            model.addAttribute("screen", "widgets/common/guest_main");
            model.addAttribute("screen_fragment", "default");
        }
        return "layout/main";
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request) {

        UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken("TEST", "TEST");

        // 🔐 여기서 직접 인증 호출
        Authentication authentication = authManager.authenticate(authRequest);

        // ✅ 인증 성공 시 SecurityContext에 등록
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
        session.setAttribute("user_id", 1);
        session.setAttribute("user_nm", "관리자");
        session.setAttribute("user_mode", "customer");

        return "layout/login";
    }

    @GetMapping("/change_user_mode")
    public RedirectView login(HttpSession session, @RequestParam(name = "user_mode") String user_mode) {
        session.setAttribute("user_mode", user_mode);
        return new RedirectView("/");
    }

    @GetMapping("/find_id")
    public String find_id(HttpSession session) {
        return "layout/find_id";
    }

    @PostMapping("/find_id")
    @ResponseBody
    public Map<String,Object> find_id_post(@RequestBody Map<String,Object> requestData) {
        DaoUser daoUser = daoService.getMapper(DaoUser.class);
        Map<String,Object> responseData = new HashMap<>();

        List<Map<String, Object>> emails = daoUser.Select01(
            Map.of("EMAIL", requestData.get("EMAIL").toString())
        );

        // 조회해서 결과값이 있는 경우, 이메일을 메시지로 출력함.
        if (!emails.isEmpty())  {
            System.out.println(emails);
            responseData.put("msg", "ID[" + emails.getFirst().get("ID").toString() + "]로 가입하셨습니다." );
        }
        else { // 조회해서 결과 값이 없으면 오류 호출함.
            responseData.put("msg", "해당 이메일로 가입된 계정이 없습니다." );
        }

        return responseData;
    }
}
