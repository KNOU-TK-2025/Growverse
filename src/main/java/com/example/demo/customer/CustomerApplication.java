package com.example.demo.customer;

import com.example.demo.common.service.Session;
import com.example.demo.customer.service.CustomerDeal;
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

import static org.springframework.web.servlet.function.RouterFunctions.route;

@Controller
public class CustomerApplication {
    @Autowired
    private CustomerDeal serviceCustomerDeal;

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

    @GetMapping("/open_deals")
    public String open_deals(Model model, HttpSession session) {
        model.addAttribute("main_buttons", this.get_button_fragment(session));
        model.addAttribute("main_container", "fragments/open_deals");
        model.addAttribute("main_container_fragment", "open_deals");
        return "layout/main";
    }

    @PostMapping(path = "/open_deals", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public @ResponseBody RedirectView post_open_deals(Model model, HttpSession session, @RequestParam Map<String, String > formData) {
        Map<String, Object> param = new HashMap<>(formData);
        param.put("STATUS_CD", "01");
        param.put("CUSTOMER_ID", session.getAttribute("user_id"));
        serviceCustomerDeal.put_customer_deal(param);
        return new RedirectView("/mypage");
    }

    public String mypage(Model model, HttpSession session) {
        model.addAttribute("main_buttons", this.get_button_fragment(session));
        model.addAttribute("main_container", "fragments/main_container");
        model.addAttribute("main_container_fragment", "customer_mypage");
        return "layout/main";
    }

}
