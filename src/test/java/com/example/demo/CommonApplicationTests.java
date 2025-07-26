package com.example.demo;

import com.example.demo.common.dao.DaoUser;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = com.example.demo.common.ServiceController.class)
@MapperScan("com.example.demo.mapper")
class CommonApplicationTests {
	@Autowired
	private SqlSessionTemplate sqlSession;

	@Test
	void contextLoads() {
		DaoUser daoUser = sqlSession.getMapper(DaoUser.class);
		Map<String, Object> input = new HashMap<>();
		input.put("EMAIL", "admin@knou.ac.kr");

		List<Map<String, Object>> userList = daoUser.Select01(input);
		System.out.println(">>>> Select01 호출 결과: " + userList);
	}
}