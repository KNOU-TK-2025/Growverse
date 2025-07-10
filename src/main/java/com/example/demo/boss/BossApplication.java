package com.example.demo.boss;

import com.example.demo.boss.dao.DaoBossDeal;
import com.example.demo.common.DaoService;
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

@Controller
public class BossApplication {
    @Autowired
    private DaoService daoService;

    public String mypage(Model model, HttpSession session) {
        DaoBossDeal daoBossDeal = daoService.getMapper(DaoBossDeal.class);
        String bossID = session.getAttribute("user_id").toString();

        model.addAttribute("menu_buttons", "widgets/boss/main");
        model.addAttribute("menu_buttons_fragment", "menu_buttons_main");
        model.addAttribute("screen", "widgets/boss/main");
        model.addAttribute("screen_fragment", "mypage");

        Map<String, Object> param = new HashMap<>();
        param.put("BOSS_ID", bossID);

        model.addAttribute("available_customer_deals", daoBossDeal.Select01(param));
        return "layout/main";
    }

    @GetMapping("/join_deal")
    public String join_deal(Model model, HttpSession session, @RequestParam(name = "CUSTOMER_DEAL_ID") String customerDealID) {
        String bossID = session.getAttribute("user_id").toString();
        DaoBossDeal daoBossDeal = daoService.getMapper(DaoBossDeal.class);

        Map<String, Object> param = new HashMap<>();
        param.put("BOSS_ID", bossID);
        param.put("CUSTOMER_DEAL_ID", customerDealID);

        model.addAttribute("menu_buttons", "widgets/boss/main");
        model.addAttribute("menu_buttons_fragment", "menu_buttons_main");
        model.addAttribute("screen", "widgets/boss/join_deal");
        model.addAttribute("screen_fragment", "join_deal");

        model.addAttribute("customer_deal", daoBossDeal.Select01(param).getFirst());
        return "layout/main";
    }

    @PostMapping(path = "/join_deal", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public @ResponseBody RedirectView post_join_deal(Model model, HttpSession session, @RequestParam(name = "CUSTOMER_DEAL_ID") String customerDealID, @RequestParam Map<String, String > formData) {
        DaoBossDeal daoBossDeal = daoService.getMapper(DaoBossDeal.class);
        Map<String, Object> param = new HashMap<>(formData);
        param.put("STATUS_CD", "01");
        param.put("BOSS_ID", session.getAttribute("user_id"));
        param.put("ORG_CUSTOMER_DEAL_ID", customerDealID);

        daoBossDeal.Insert01(param);
        return new RedirectView("/mypage");
    }
}
