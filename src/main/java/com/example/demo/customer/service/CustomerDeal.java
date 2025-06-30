package com.example.demo.customer.service;

import com.example.demo.customer.dao.DaoCustomerDeal;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CustomerDeal {
    @Autowired
    private SqlSessionTemplate sqlSession;
    private DaoCustomerDeal daoCustomerDeal = null;

    public void setup() {
        if ( this.daoCustomerDeal == null) {
            this.daoCustomerDeal = sqlSession.getMapper(DaoCustomerDeal.class);
        }
    }

    public List<Map<String, Object>> get_my_customer_deal(Map<String, Object> param) {
        this.setup();
        return daoCustomerDeal.Select01(param);
    }

    public void put_customer_deal(Map<String, Object> param) {
        this.setup();
        daoCustomerDeal.Insert01(param);
    }
}
