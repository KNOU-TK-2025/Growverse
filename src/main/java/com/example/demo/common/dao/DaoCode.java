package com.example.demo.common.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DaoCode {
	List<Map<String,Object>> SelectCodes(Map<String,Object> param);
}
 