package com.example.demo.common;

import com.example.demo.boss.BossApplication;
import com.example.demo.customer.CustomerApplication;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;
import org.springframework.web.servlet.view.RedirectView;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import static org.springframework.web.servlet.function.RequestPredicates.path;
import static org.springframework.web.servlet.function.RouterFunctions.route;

@SpringBootApplication(scanBasePackages="com.example.demo")
@Controller
public class CommonApplication {
    @Autowired
    private DaoService daoService;

    @Autowired
    private BossApplication bossApplication;

    @Autowired
    private CustomerApplication customerApplication;

    public static void main(String[] args) throws UnsupportedEncodingException {
        System.setOut(new PrintStream(System.out, true, "UTF-8"));
        System.setErr(new PrintStream(System.err, true, "UTF-8"));
        System.out.println("😊😊😊😊 [한글 인코딩 테스트.] 😂😂😂😂");
        System.out.println("😊😊😊😊 [if you can't see text, type chcp 65001 or something.] 😂😂😂😂");
        SpringApplication.run(CommonApplication.class, args);
    }

    public String get_button_fragment(HttpSession session) {
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

    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        model.addAttribute("menu_buttons", this.get_button_fragment(session));
        model.addAttribute("menu_buttons_fragment", "menu_buttons_main");
        model.addAttribute("screen", "widgets/common/guest_main");
        model.addAttribute("screen_fragment", "default");
        return "layout/main";
    }

    @GetMapping("/alarm")
    public String alarm(Model model, HttpSession session) {
        model.addAttribute("menu_buttons", this.get_button_fragment(session));
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
            model.addAttribute("menu_buttons", this.get_button_fragment(session));
            model.addAttribute("menu_buttons_fragment", "menu_buttons_main");
            model.addAttribute("screen", "widgets/common/guest_main");
            model.addAttribute("screen_fragment", "default");
        }
        return "layout/main";
    }

    @GetMapping("/login")
    public String login(HttpSession session) {
        session.setAttribute("user_id", 1);
        session.setAttribute("user_nm", "관리자");
        session.setAttribute("user_mode", "customer");
        return "layout/login";
    }

    @GetMapping("/change_user_mode")
    public RedirectView login(HttpSession session, @RequestParam(name = "user_mode") String user_mode) {
        session.setAttribute("user_mode", user_mode);
        return new RedirectView("/home");
    }

    @Bean
    RouterFunction<ServerResponse> spaRouter() {
        ClassPathResource index = new ClassPathResource("static/index.html");
        return route().resource(path("/"), index).build();
    }
}
