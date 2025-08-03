package com.example.demo.boss;

import com.example.demo.boss.dao.DaoBoss;
import com.example.demo.boss.dao.DaoBossDeal;
import com.example.demo.common.DaoService;
import com.example.demo.common.dao.DaoCode;
import com.example.demo.customer.dao.DaoCustomerDeal;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

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

        model.addAttribute("available_customer_deals", daoBossDeal.SelectDeals(Map.of("BOSS_ID", bossID)));
        return "layout/main";
    }

    @GetMapping("/join_deal")
    public String join_deal(Model model, HttpSession session, @RequestParam(name = "CUSTOMER_DEAL_ID") String customerDealID) {
        String bossID = session.getAttribute("user_id").toString();
        DaoBoss daoBoss = daoService.getMapper(DaoBoss.class);
        DaoBossDeal daoBossDeal = daoService.getMapper(DaoBossDeal.class);
        DaoCode daoCode = daoService.getMapper(DaoCode.class);
        ObjectMapper mapper = new ObjectMapper();

        model.addAttribute("menu_buttons", "widgets/boss/main");
        model.addAttribute("menu_buttons_fragment", "menu_buttons_main");
        model.addAttribute("screen", "widgets/boss/join_deal");
        model.addAttribute("screen_fragment", "join_deal");

        model.addAttribute("theme_codes", daoCode.SelectCodes(Map.of("CD_KNM", "테마구분코드")));
        model.addAttribute("region_codes", daoCode.SelectCodes(Map.of("CD_KNM", "지역구분코드")));


        Map<String, Object> deal = daoBossDeal.SelectDeals(Map.of("BOSS_ID", bossID,"CUSTOMER_DEAL_ID", customerDealID)).getFirst();
        String theme_name_list = "";
        if (deal.containsKey("HOPE_THEME_NM1")) {
            theme_name_list = deal.get("HOPE_THEME_NM1").toString();
        }
        if (deal.containsKey("HOPE_THEME_NM2")) {
            if (!theme_name_list.isEmpty()) {
                theme_name_list += ", ";
            }
            theme_name_list += deal.get("HOPE_THEME_NM2");
        }
        if (deal.containsKey("HOPE_THEME_NM3")) {
            if (!theme_name_list.isEmpty()) {
                theme_name_list += ", ";
            }
            theme_name_list += deal.get("HOPE_THEME_NM3");
        }
        deal.put("HOPE_THEME_NM_LIST", theme_name_list);

        model.addAttribute("selected_customer_deal", deal);

        try {
            Map<String, Object> boss = daoBoss.SelectBoss(Map.of("BOSS_ID", bossID)).getFirst();
            if (boss.containsKey("INFO")) {
                Map info = mapper.readValue(boss.get("INFO").toString(), Map.class);
                if (info.containsKey("IMAGE_LIST")) {
                    model.addAttribute("boss_image_list",  (List<String>) info.get("IMAGE_LIST"));
                }
                if (info.containsKey("DESC")) {
                    model.addAttribute("boss_desc",  info.get("DESC").toString());
                }
            }
            model.addAttribute("boss_region_cd", boss.get("REGION_CD"));
            model.addAttribute("boss_theme_cd", boss.get("THEME_CD"));
        }
        catch (NoSuchElementException | JsonProcessingException _) {}

        return "layout/main";
    }

    @GetMapping("/myplace")
    public String myplace(Model model, HttpSession session) {
        DaoBoss daoBoss = daoService.getMapper(DaoBoss.class);
        DaoCode daoCode = daoService.getMapper(DaoCode.class);
        String bossID = session.getAttribute("user_id").toString();
        ObjectMapper mapper = new ObjectMapper();

        model.addAttribute("menu_buttons", "widgets/boss/main");
        model.addAttribute("menu_buttons_fragment", "menu_buttons_main");
        model.addAttribute("screen", "widgets/boss/myplace");
        model.addAttribute("screen_fragment", "myplace");

        try {
            Map<String, Object> boss = daoBoss.SelectBoss(Map.of("BOSS_ID", bossID)).getFirst();
            if (boss.containsKey("INFO")) {
                Map info = mapper.readValue(boss.get("INFO").toString(), Map.class);
                if (info.containsKey("IMAGE_LIST")) {
                    model.addAttribute("boss_image_list",  (List<String>) info.get("IMAGE_LIST"));
                }
                if (info.containsKey("DESC")) {
                    model.addAttribute("boss_desc",  info.get("DESC").toString());
                }
            }
            model.addAttribute("boss_region_cd", boss.get("REGION_CD"));
            model.addAttribute("boss_theme_cd", boss.get("THEME_CD"));
        }
        catch (NoSuchElementException | JsonProcessingException _) {}

        model.addAttribute("theme_codes", daoCode.SelectCodes(Map.of("CD_KNM", "테마구분코드")));
        model.addAttribute("region_codes", daoCode.SelectCodes(Map.of("CD_KNM", "지역구분코드")));

        return "layout/main";
    }


    @PostMapping(path = "/api/boss/update_desc")
    public @ResponseBody Map<String, Object> api_boss_update_desc(Model model, HttpSession session, @RequestBody Map<String, String > payload) {
        DaoBoss daoBoss = daoService.getMapper(DaoBoss.class);
        String bossID = session.getAttribute("user_id").toString();
        ObjectMapper mapper = new ObjectMapper();

        try {
            String desc = payload.get("BOSS_DESC");
            List<String> image_list = new ArrayList<>();

            try {
                Map<String, Object> boss = daoBoss.SelectBoss(Map.of("BOSS_ID", bossID)).getFirst();
                if (boss.containsKey("INFO")) {
                    Map info = mapper.readValue(boss.get("INFO").toString(), Map.class);
                    if (info.containsKey("IMAGE_LIST")) {
                        image_list = (List<String>) info.get("IMAGE_LIST");
                    }
                }
            }
            catch (NoSuchElementException _) {}

            Map<String, Object> param = new HashMap<>(Map.of("BOSS_ID", bossID));
            param.put("INFO", mapper.writeValueAsString(Map.of("IMAGE_LIST", image_list, "DESC", desc)));
            param.put("REGION_CD", payload.get("BOSS_REGION_CD"));
            param.put("THEME_CD", payload.get("BOSS_THEME_CD"));

            daoBoss.UpdateBoss(param);

        } catch (IOException e) {
        }

        return Map.of("status", "ok");
    }

    @PostMapping(path = "/api/boss/add_boss_deal")
    public @ResponseBody Map<String, Object> api_add_boss_deal(Model model, HttpSession session, @RequestParam(name = "CUSTOMER_DEAL_ID") String customer_deal_id, @RequestBody Map<String, String > payload) {
        DaoBossDeal daoBossDeal = daoService.getMapper(DaoBossDeal.class);

        daoBossDeal.InsertBossDeal(Map.of("ORG_CUSTOMER_DEAL_ID", customer_deal_id, "BOSS_ID", session.getAttribute("user_id"), "DEAL_CN", payload.get("BOSS_DEAL_CN")));

        return Map.of("status", "ok");
    }

    @PostMapping(path = "/api/boss/remove_boss_deal")
    public @ResponseBody Map<String, Object> api_remove_boss_deal(Model model, HttpSession session, @RequestParam(name = "BOSS_DEAL_ID") String boss_deal_id) {
        DaoBossDeal daoBossDeal = daoService.getMapper(DaoBossDeal.class);

        daoBossDeal.DeleteBossDeal(Map.of( "BOSS_DEAL_ID", boss_deal_id));

        return Map.of("status", "ok");
    }

    @PostMapping(path = "/api/upload_file", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public @ResponseBody RedirectView api_upload_file(HttpSession session, @RequestParam("imageFile") MultipartFile file) {
        DaoBoss daoBoss = daoService.getMapper(DaoBoss.class);
        String bossID = session.getAttribute("user_id").toString();
        ObjectMapper mapper = new ObjectMapper();

        try {
            String fileName = bossID + "_" + UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path dstPath = Paths.get("C:\\Growverse\\src\\main\\resources\\static\\upload").resolve(fileName);
            Files.copy(file.getInputStream(), dstPath, StandardCopyOption.REPLACE_EXISTING);

            String desc = "";
            List<String> image_list = new ArrayList<>();

            try {
                Map<String, Object> boss = daoBoss.SelectBoss(Map.of("BOSS_ID", bossID)).getFirst();
                if (boss.containsKey("INFO")) {
                    Map info = mapper.readValue(boss.get("INFO").toString(), Map.class);
                    if (info.containsKey("IMAGE_LIST")) {
                        image_list = (List<String>) info.get("IMAGE_LIST");
                    }
                    if (info.containsKey("DESC")) {
                        desc = info.get("DESC").toString();
                    }
                }
            }
            catch (NoSuchElementException _) {}

            image_list.add(fileName);

            Map<String, Object> param = new HashMap<>(Map.of("BOSS_ID", bossID));
            param.put("INFO", mapper.writeValueAsString(Map.of("IMAGE_LIST", image_list, "DESC", desc)));
            param.put("REGION_CD", null);
            param.put("THEME_CD", null);

            daoBoss.UpdateBoss(param);

        } catch (IOException e) {
        }
        return new RedirectView("/myplace");
    }
}
