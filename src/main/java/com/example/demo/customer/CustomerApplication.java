package com.example.demo.customer;

import com.example.demo.boss.dao.DaoBossDeal;
import com.example.demo.common.DaoService;
import com.example.demo.common.dao.DaoCode;
import com.example.demo.customer.dao.DaoCustomerDeal;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.*;

import static org.springframework.web.servlet.function.RouterFunctions.route;

@Controller
public class CustomerApplication {
    @Autowired
    private DaoService daoService;

    @GetMapping("/open_deal")
    public String open_deal(Model model, HttpSession session, @RequestParam(name = "COURSE_ID", required = false) String course_id, @RequestParam(name = "CUSTOMER_DEAL_ID", required = false) String customer_deal_id,
                              @RequestParam(name = "BOSS_DEAL_ID", required = false) String boss_deal_id, @RequestParam(name = "SHOW_BOSS_DEALS", defaultValue = "N") String show_boss_deals) {
        DaoCustomerDeal daoCustomerDeal = daoService.getMapper(DaoCustomerDeal.class);
        DaoCode daoCode = daoService.getMapper(DaoCode.class);
        boolean editable = false;

        model.addAttribute("menu_buttons", "widgets/customer/main");
        model.addAttribute("menu_buttons_fragment", "menu_buttons_main");
        model.addAttribute("screen", "widgets/customer/open_deal");
        model.addAttribute("screen_fragment", "open_deal");

        model.addAttribute("my_courses", daoCustomerDeal.SelectCourses(Map.of("WRITER_ID", session.getAttribute("user_id"))));
        model.addAttribute("popular_courses", daoCustomerDeal.SelectCourses(Map.of("POPULAR_YN", "Y")));
        if (course_id != null) {
            Map<String, Object> selectedCourse = daoCustomerDeal.SelectCourses(Map.of("COURSE_ID", course_id)).getFirst();
            List<Map<String, Object>> customerDeals = daoCustomerDeal.SelectCustomerDeals(Map.of("COURSE_ID", course_id));

            // 제목 조립
            for (int i = 0; i < customerDeals.size(); i++) {
                Map<String, Object> customerDeal = customerDeals.get(i);
                String themes = "";

                if (customerDeal.get("HOPE_THEME_NM1") != null) {
                    themes += customerDeal.get("HOPE_THEME_NM1");
                }
                if (customerDeal.get("HOPE_THEME_NM2") != null) {
                    if (!themes.isEmpty()) {
                        themes += ", ";
                    }
                    themes += customerDeal.get("HOPE_THEME_NM2");
                }
                if (customerDeal.get("HOPE_THEME_NM3") != null) {
                    if (!themes.isEmpty()) {
                        themes += ", ";
                    }
                    themes += customerDeal.get("HOPE_THEME_NM3");
                }

                if (customerDeal.get("HOPE_REGION_NM") != null) {
                    if (themes.isEmpty())
                        customerDeal.put("TITLE", customerDeal.get("HOPE_REGION_NM") );
                    else
                        customerDeal.put("TITLE", customerDeal.get("HOPE_REGION_NM") + "\n(" + themes + ")" );
                }
                else {

                    if (!themes.isEmpty())
                        customerDeal.put("TITLE", "지역미정\n(" + themes + ")" );
                }

                if (customerDeal.get("WRITER_ID").equals(session.getAttribute("user_id"))) {
                    customerDeal.put("EDITABLE", "Y");
                } else {
                    customerDeal.put("EDITABLE", "N");
                }
            }
            model.addAttribute("selected_course", selectedCourse);
            model.addAttribute("customer_deals", customerDeals);
            if (selectedCourse.get("WRITER_ID").equals(session.getAttribute("user_id"))) {
                editable = true;
            }
        } else {
            model.addAttribute("selected_course", null);
        }
        if (customer_deal_id != null) {
            Map<String, Object> selectedCustomerDeal = daoCustomerDeal.SelectCustomerDeals(Map.of("CUSTOMER_DEAL_ID", customer_deal_id)).getFirst();

            model.addAttribute("selected_customer_deal", selectedCustomerDeal);
            model.addAttribute("theme_codes", daoCode.SelectCodes(Map.of("CD_KNM", "테마구분코드")));
            model.addAttribute("region_codes", daoCode.SelectCodes(Map.of("CD_KNM", "지역구분코드")));

            if (show_boss_deals.equals("Y")) {
                model.addAttribute("boss_deals", daoCustomerDeal.SelectBossDeals(Map.of("ORG_CUSTOMER_DEAL_ID", customer_deal_id)));
            }
        } else {
            model.addAttribute("selected_customer_deal", null);
        }

        if (boss_deal_id != null) {
            Map<String,Object> bossDeal = daoCustomerDeal.SelectBossDeals(Map.of("BOSS_DEAL_ID", boss_deal_id)).getFirst();
            ObjectMapper mapper = new ObjectMapper();

            try {
                String desc = "";
                List<String> image_list = new ArrayList<>();

                if (bossDeal.containsKey("INFO")) {
                    Map info = mapper.readValue(bossDeal.get("INFO").toString(), Map.class);
                    if (info.containsKey("IMAGE_LIST")) {
                        image_list = (List<String>) info.get("IMAGE_LIST");
                    }
                    if (info.containsKey("DESC")) {
                        desc = info.get("DESC").toString();
                    }
                }
                bossDeal.put("DESC", desc);
                bossDeal.put("IMAGE_LIST", image_list);
            }
            catch (NoSuchElementException | JsonProcessingException _) {}

            model.addAttribute("selected_boss_deal", bossDeal);
        } else {
            model.addAttribute("selected_boss_deal", null);
        }

        model.addAttribute("editable", editable);
        model.addAttribute("SHOW_BOSS_DEALS", show_boss_deals.equals("Y"));
        return "layout/main";
    }


    @PostMapping(path = "/api/customer/add_course")
    public @ResponseBody Map<String, Object> api_add_course(Model model, HttpSession session) {
        DaoCustomerDeal daoCustomerDeal = daoService.getMapper(DaoCustomerDeal.class);

        daoCustomerDeal.InsertCourse(Map.of("WRITER_ID", session.getAttribute("user_id")));

        return Map.of("status", "ok");
    }

    @PostMapping(path = "/api/customer/remove_course")
    public @ResponseBody Map<String, Object> api_remove_course(Model model, HttpSession session, @RequestParam(name = "COURSE_ID") String course_id) {
        DaoCustomerDeal daoCustomerDeal = daoService.getMapper(DaoCustomerDeal.class);

        daoCustomerDeal.DeleteCourse(Map.of("COURSE_ID", course_id));

        return Map.of("status", "ok");
    }

    @PostMapping(path = "/api/customer/rename_course")
    public @ResponseBody Map<String, Object> api_remove_course(Model model, HttpSession session, @RequestParam(name = "COURSE_ID") String course_id, @RequestBody Map<String, String > payload) {
        DaoCustomerDeal daoCustomerDeal = daoService.getMapper(DaoCustomerDeal.class);

        daoCustomerDeal.UpdateCourse(Map.of("COURSE_ID", course_id, "COURSE_NM", payload.get("COURSE_NM")));

        return Map.of("status", "ok");
    }

    @PostMapping(path = "/api/customer/add_customer_deal")
    public @ResponseBody Map<String, Object> api_add_customer_deal(Model model, HttpSession session, @RequestParam(name = "COURSE_ID") String course_id) {
        DaoCustomerDeal daoCustomerDeal = daoService.getMapper(DaoCustomerDeal.class);

        daoCustomerDeal.InsertCustomerDeal(Map.of("COURSE_ID", course_id));

        return Map.of("status", "ok");
    }


    @PostMapping(path = "/api/customer/remove_customer_deal")
    public @ResponseBody Map<String, Object> api_add_customer_deal(Model model, HttpSession session, @RequestParam(name = "COURSE_ID") String course_id, @RequestParam(name = "CUSTOMER_DEAL_ID") String customer_deal_id) {
        DaoCustomerDeal daoCustomerDeal = daoService.getMapper(DaoCustomerDeal.class);

        daoCustomerDeal.DeleteCustomerDeal(Map.of("COURSE_ID", course_id, "CUSTOMER_DEAL_ID", customer_deal_id));

        return Map.of("status", "ok");
    }

    @PostMapping(path = "/api/customer/save_customer_deal")
    public @ResponseBody Map<String, Object> api_save_customer_deal(Model model, HttpSession session, @RequestParam(name = "COURSE_ID") String course_id, @RequestParam(name = "CUSTOMER_DEAL_ID") String customer_deal_id, @RequestBody Map<String, String > payload) {
        DaoCustomerDeal daoCustomerDeal = daoService.getMapper(DaoCustomerDeal.class);
        Map<String, Object> param = new HashMap<>(Map.of("COURSE_ID", course_id,"CUSTOMER_DEAL_ID", customer_deal_id));

        param.put("OPEN_YN", payload.getOrDefault("OPEN_YN", null));
        param.put("BOSS_DEAL_ID", payload.getOrDefault("BOSS_DEAL_ID", null));
        param.put("HOPE_BOSS_ID", payload.getOrDefault("HOPE_BOSS_ID", null));
        param.put("HOPE_DT", payload.getOrDefault("HOPE_DT", null));
        param.put("HOPE_REGION_CD", payload.getOrDefault("HOPE_REGION_CD", null));
        param.put("HOPE_PEOPLE_CNT", payload.getOrDefault("HOPE_PEOPLE_CNT", null));
        param.put("HOPE_OTHER_CN", payload.getOrDefault("HOPE_OTHER_CN", null));
        param.put("HOPE_THEME_CD1", payload.getOrDefault("HOPE_THEME_CD1", null));
        param.put("HOPE_THEME_CD2", payload.getOrDefault("HOPE_THEME_CD2", null));
        param.put("HOPE_THEME_CD3", payload.getOrDefault("HOPE_THEME_CD3", null));


        daoCustomerDeal.UpdateCustomerDeal(param);

        return Map.of("status", "ok");
    }

    @PostMapping(path = "/api/customer/open_customer_deal")
    public @ResponseBody Map<String, Object> api_open_customer_deal(Model model, HttpSession session, @RequestParam(name = "COURSE_ID") String course_id, @RequestParam(name = "CUSTOMER_DEAL_ID") String customer_deal_id) {
        DaoCustomerDeal daoCustomerDeal = daoService.getMapper(DaoCustomerDeal.class);
        Map<String, Object> param = new HashMap<>(Map.of("COURSE_ID", course_id,"CUSTOMER_DEAL_ID", customer_deal_id));
        param.put("OPEN_YN", "Y");
        daoCustomerDeal.UpdateCustomerDeal(param);

        return Map.of("status", "ok");
    }

    @PostMapping(path = "/api/customer/cancel_customer_deal")
    public @ResponseBody Map<String, Object> api_cancel_customer_deal(Model model, HttpSession session, @RequestParam(name = "COURSE_ID") String course_id, @RequestParam(name = "CUSTOMER_DEAL_ID") String customer_deal_id) {
        DaoCustomerDeal daoCustomerDeal = daoService.getMapper(DaoCustomerDeal.class);
        Map<String, Object> param = new HashMap<>(Map.of("COURSE_ID", course_id,"CUSTOMER_DEAL_ID", customer_deal_id));
        param.put("OPEN_YN", "N");
        daoCustomerDeal.UpdateCustomerDeal(param);

        return Map.of("status", "ok");
    }

    @PostMapping(path = "/api/customer/done_customer_deal")
    public @ResponseBody Map<String, Object> api_done_customer_deal(Model model, HttpSession session, @RequestParam(name = "COURSE_ID") String course_id, @RequestParam(name = "CUSTOMER_DEAL_ID") String customer_deal_id, @RequestBody Map<String, String > payload) {
        DaoCustomerDeal daoCustomerDeal = daoService.getMapper(DaoCustomerDeal.class);
        Map<String, Object> param = new HashMap<>(Map.of("COURSE_ID", course_id,"CUSTOMER_DEAL_ID", customer_deal_id, "OPEN_YN", "N"));
        param.put("BOSS_DEAL_ID", payload.get("BOSS_DEAL_ID"));
        daoCustomerDeal.UpdateCustomerDeal(param);

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

