package com.example.demo.customer.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DaoCustomerDeal {
	List<Map<String,Object>> SelectCustomerDeals(Map<String, Object> param);
	void InsertCustomerDeal(Map<String, Object> param);
	void DeleteCustomerDeal(Map<String, Object> param);

	List<Map<String,Object>> SelectCourses(Map<String, Object> param);
	void InsertCourse(Map<String, Object> param);
	void UpdateCourse(Map<String, Object> param);
	void DeleteCourse(Map<String, Object> param);
}
 