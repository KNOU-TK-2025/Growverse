package reverse_order.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import reverse_order.dao.DaoPost;

public class Post {
	public static void get_post(ServiceContext ctx, SqlSession sqlSession) {
		DaoPost daoPost = sqlSession.getMapper(DaoPost.class);
		List<Map<String, Object>> post_list = null;
		
		System.out.println("********* aaaaaaaa");
		
		post_list = daoPost.Select01();

		System.out.println("********* post_list : " + post_list.toString());

		ctx.result.put("post_list", post_list);
	}

	public static void new_post(ServiceContext ctx, SqlSession sqlSession) {
		DaoPost daoPost = sqlSession.getMapper(DaoPost.class);
		Map<String, Object> daoParam = new HashMap<String, Object>();
		
		daoParam.put("TITLE", ctx.param.get("TITLE"));
		daoPost.Insert01(daoParam);
	}

	public static void delete_post(ServiceContext ctx, SqlSession sqlSession) {
		DaoPost daoPost = sqlSession.getMapper(DaoPost.class);
		Map<String, Object> daoParam = new HashMap<String, Object>();
		
		daoParam.put("POST_ID", ctx.param.get("POST_ID"));
		daoPost.Delete01(daoParam);
	}
}
