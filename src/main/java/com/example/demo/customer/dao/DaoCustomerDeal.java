package com.example.demo.customer.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DaoCustomerDeal {
	List<Map<String,Object>> Select01(Map<String, Object> param);
	void Insert01(Map<String, Object> param);
}
 