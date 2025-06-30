package com.example.demo.boss.service;

import com.example.demo.boss.dao.DaoBossDeal;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BossDeal {
    @Autowired
    private SqlSessionTemplate sqlSession;
    private DaoBossDeal daoBossDeal = null;

    public void setup() {
        if ( this.daoBossDeal == null) {
            this.daoBossDeal = sqlSession.getMapper(DaoBossDeal.class);
        }
    }

    public List<Map<String, Object>> get_available_customer_deal(String bossID) {
        this.setup();
        Map<String, Object> param = new HashMap<>();
        param.put("BOSS_ID", bossID);
        return daoBossDeal.Select01(param);
    }

    public Map<String, Object> get_customer_deal(String bossID, String customerDealID) {
        this.setup();
        Map<String, Object> param = new HashMap<>();
        param.put("BOSS_ID", bossID);
        param.put("CUSTOMER_DEAL_ID", customerDealID);
        return daoBossDeal.Select01(param).getFirst();
    }

    public void put_boss_deal(Map<String, Object> param) {
        this.setup();
        daoBossDeal.Insert01(param);
    }
}
