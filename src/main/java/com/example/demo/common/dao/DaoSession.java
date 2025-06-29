package com.example.demo.common.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DaoSession {
	Map<String,Object> Select01(Map<String, Object> param);

}
 