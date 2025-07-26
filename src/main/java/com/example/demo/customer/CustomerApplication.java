package com.example.demo.customer;

import com.example.demo.boss.dao.DaoBossDeal;
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
import java.util.List;
import java.util.Map;

import static org.springframework.web.servlet.function.RouterFunctions.route;

@Controller
public class CustomerApplication {
    @Autowired
    private DaoService daoService;

    @GetMapping("/open_deal")
    public String open_deal(Model model, HttpSession session) {
        DaoCustomerDeal daoCustomerDeal = daoService.getMapper(DaoCustomerDeal.class);

        model.addAttribute("menu_buttons", "widgets/customer/main");
        model.addAttribute("menu_buttons_fragment", "menu_buttons_main");
        model.addAttribute("screen", "widgets/customer/open_deal");
        model.addAttribute("screen_fragment", "open_deal");
        model.addAttribute("popular_courses", daoCustomerDeal.SelectPopularCourses(Map.of()));

        List<Map<String, Object>> newCourse = daoCustomerDeal.SelectNewCourse(Map.of("WRITER_ID", session.getAttribute("user_id")));
        if (!newCourse.isEmpty()) {
            model.addAttribute(
    "deals_on_new_course",
                daoCustomerDeal.Select01(Map.of("COURSE_ID", newCourse.getFirst().get("COURSE_ID")))
            );
        }
        else {
            model.addAttribute("deals_on_new_course", List.of());
        }

        return "layout/main";
    }

    @PostMapping("/open_deal/add_deal")
    public @ResponseBody Map<String, Object> open_deal_add_deal(HttpSession session) {
        DaoCustomerDeal daoCustomerDeal = daoService.getMapper(DaoCustomerDeal.class);

        List<Map<String, Object>> newCourse = daoCustomerDeal.SelectNewCourse(Map.of("WRITER_ID", session.getAttribute("user_id")));
        if (newCourse.isEmpty()) {
            daoCustomerDeal.InsertNewCourse(Map.of("WRITER_ID", session.getAttribute("user_id")));
            newCourse = daoCustomerDeal.SelectNewCourse(Map.of("WRITER_ID", session.getAttribute("user_id")));
        }
        int newCourseId = (int) newCourse.getFirst().get("COURSE_ID");
        daoCustomerDeal.InsertDealOnNewCourse(Map.of("CUSTOMER_ID", session.getAttribute("user_id"), "COURSE_ID", newCourseId));
        return Map.of("status", "ok");
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
