package com.example.demo.boss;

import com.example.demo.boss.service.BossDeal;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.Map;

@Controller
public class BossApplication {
    @Autowired
    private BossDeal serviceBossDeal;

    public String get_button_fragment(HttpSession session) {
        if ("customer".equals(session.getAttribute("user_mode"))) {
            return "customer";
        }
        else if ("boss".equals(session.getAttribute("user_mode"))) {
            return "boss";
        }
        else {
            return "default";
        }
    }

    public String mypage(Model model, HttpSession session) {
        model.addAttribute("main_buttons", this.get_button_fragment(session));
        model.addAttribute("main_container", "fragments/main_container");
        model.addAttribute("main_container_fragment", "boss_mypage");

        model.addAttribute("available_customer_deals", serviceBossDeal.get_available_customer_deal());
        return "layout/main";
    }
}
