package com.example.demo.customer;

import com.example.demo.boss.dao.DaoBossDeal;
import com.example.demo.common.DaoService;
import com.example.demo.customer.dao.DaoCustomerDeal;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
    public String open_deal(Model model, HttpSession session, @RequestParam(name = "COURSE_ID", required = false) String course_id, @RequestParam(name = "CUSTOMER_DEAL_ID", required = false) String customer_deal_id) {
        DaoCustomerDeal daoCustomerDeal = daoService.getMapper(DaoCustomerDeal.class);
        boolean editable = false;

        model.addAttribute("menu_buttons", "widgets/customer/main");
        model.addAttribute("menu_buttons_fragment", "menu_buttons_main");
        model.addAttribute("screen", "widgets/customer/open_deal");
        model.addAttribute("screen_fragment", "open_deal");

        model.addAttribute("my_courses", daoCustomerDeal.SelectCourses(Map.of("WRITER_ID", session.getAttribute("user_id"))));
        model.addAttribute("popular_courses", daoCustomerDeal.SelectCourses(Map.of("POPULAR_YN", "Y")));
        if (course_id != null) {
            Map<String, Object> selectedCourse = daoCustomerDeal.SelectCourses(Map.of("COURSE_ID", course_id)).getFirst();

            model.addAttribute("selected_course", selectedCourse);
            model.addAttribute("customer_deals", daoCustomerDeal.SelectCustomerDeals(Map.of("COURSE_ID", course_id)));
            if (selectedCourse.get("WRITER_ID").equals(session.getAttribute("user_id"))) {
                editable = true;
            }
        } else {
            model.addAttribute("selected_course", null);
        }
        if (customer_deal_id != null) {
            Map<String, Object> selectedCustomerDeal = daoCustomerDeal.SelectCourses(Map.of("CUSTOMER_DEAL_ID", customer_deal_id)).getFirst();

            model.addAttribute("selected_customer_deal", selectedCustomerDeal);
        } else {
            model.addAttribute("selected_customer_deal", null);
        }

        model.addAttribute("editable", editable);
        return "layout/main";
    }


    @PostMapping(path = "/api/add_course")
    public @ResponseBody Map<String, Object> api_add_course(Model model, HttpSession session) {
        DaoCustomerDeal daoCustomerDeal = daoService.getMapper(DaoCustomerDeal.class);

        daoCustomerDeal.InsertCourse(Map.of("WRITER_ID", session.getAttribute("user_id")));

        return Map.of("status", "ok");
    }

    @PostMapping(path = "/api/remove_course")
    public @ResponseBody Map<String, Object> api_remove_course(Model model, HttpSession session, @RequestParam(name = "COURSE_ID") String course_id) {
        DaoCustomerDeal daoCustomerDeal = daoService.getMapper(DaoCustomerDeal.class);

        daoCustomerDeal.DeleteCourse(Map.of("COURSE_ID", course_id));

        return Map.of("status", "ok");
    }

    @PostMapping(path = "/api/rename_course")
    public @ResponseBody Map<String, Object> api_remove_course(Model model, HttpSession session, @RequestParam(name = "COURSE_ID") String course_id, @RequestBody Map<String, String > payload) {
        DaoCustomerDeal daoCustomerDeal = daoService.getMapper(DaoCustomerDeal.class);

        System.out.println("-=-==================");
        System.out.println(payload);
        System.out.println("-=-==================");
        daoCustomerDeal.UpdateCourse(Map.of("COURSE_ID", course_id, "COURSE_NM", payload.get("COURSE_NM")));

        return Map.of("status", "ok");
    }

    @PostMapping(path = "/api/add_customer_deal")
    public @ResponseBody Map<String, Object> api_add_customer_deal(Model model, HttpSession session, @RequestParam(name = "COURSE_ID") String course_id) {
        DaoCustomerDeal daoCustomerDeal = daoService.getMapper(DaoCustomerDeal.class);

        daoCustomerDeal.InsertCustomerDeal(Map.of("COURSE_ID", course_id));

        return Map.of("status", "ok");
    }


    @PostMapping(path = "/api/remove_customer_deal")
    public @ResponseBody Map<String, Object> api_add_customer_deal(Model model, HttpSession session, @RequestParam(name = "COURSE_ID") String course_id, @RequestParam(name = "CUSTOMER_DEAL_ID") String customer_deal_id) {
        DaoCustomerDeal daoCustomerDeal = daoService.getMapper(DaoCustomerDeal.class);

        daoCustomerDeal.DeleteCustomerDeal(Map.of("COURSE_ID", course_id, "CUSTOMER_DEAL_ID", customer_deal_id));

        return Map.of("status", "ok");
    }

    public String mypage(Model model, HttpSession session)  {
        DaoCustomerDeal daoCustomerDeal = daoService.getMapper(DaoCustomerDeal.class);

        model.addAttribute("menu_buttons", "widgets/customer/main");
        model.addAttribute("menu_buttons_fragment", "menu_buttons_main");
        model.addAttribute("screen", "widgets/customer/main");
        model.addAttribute("screen_fragment", "mypage");

        model.addAttribute("my_customer_deals", daoCustomerDeal.SelectCustomerDeals(Map.of("CUSTOMER_ID", session.getAttribute("user_id"))));

        return "layout/main";
    }

}
