package com.example.demo.customer;

import com.example.demo.common.DaoService;
import com.example.demo.customer.dao.DaoCustomerDeal;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
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
    private DaoService daoService;

    @GetMapping("/open_deal")
    public String open_deal(Model model, HttpSession session) {
        model.addAttribute("menu_buttons", "widgets/customer/main");
        model.addAttribute("menu_buttons_fragment", "menu_buttons_main");
        model.addAttribute("screen", "widgets/customer/open_deal");
        model.addAttribute("screen_fragment", "open_deal");
        return "layout/main";
    }

    @PostMapping(path = "/open_deal", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public @ResponseBody RedirectView post_open_deal(Model model, HttpSession session, @RequestParam Map<String, String > formData) {
        DaoCustomerDeal daoCustomerDeal = daoService.getMapper(DaoCustomerDeal.class);
        Map<String, Object> param = new HashMap<>(formData);

        param.put("STATUS_CD", "01");
        param.put("CUSTOMER_ID", session.getAttribute("user_id"));
        daoCustomerDeal.Insert01(param);

        return new RedirectView("/mypage");
    }

    public String mypage(Model model, HttpSession session)  {
        DaoCustomerDeal daoCustomerDeal = daoService.getMapper(DaoCustomerDeal.class);

        model.addAttribute("menu_buttons", "widgets/customer/main");
        model.addAttribute("menu_buttons_fragment", "menu_buttons_main");
        model.addAttribute("screen", "widgets/customer/main");
        model.addAttribute("screen_fragment", "mypage");

        Map<String, Object> param = new HashMap<>();
        param.put("CUSTOMER_ID", session.getAttribute("user_id"));
        model.addAttribute("my_customer_deals", daoCustomerDeal.Select01(param));

        return "layout/main";
    }

}
