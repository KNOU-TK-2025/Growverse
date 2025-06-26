package reverse_order.service;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.google.gson.Gson;

@WebServlet("")
public class ServiceHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public SqlSessionFactory sqlSessionFactory = null;
	Gson gson = new Gson();

	public ServiceHandler() {
		super();

		String resource = "webapp/WEB-INF/mybatis-config.xml";
		InputStream inputStream;
		try {
			inputStream = Resources.getResourceAsStream(resource);
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		handleService(request, response, null);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		handleService(request, response, this.gson
				.fromJson(request.getReader().lines().collect(Collectors.joining(System.lineSeparator())), Map.class));
	}

	@SuppressWarnings("rawtypes")
	private void handleService(HttpServletRequest request, HttpServletResponse response, Map<String, Object> param)
			throws IOException {
		ServiceContext ctx = new ServiceContext(request, response);
		ctx.param = param;

		try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
			System.out.print("=========================== ");
			System.out.print("call service[" + request.getParameter("service") + "]/");
			System.out.print("[" + request.getParameter("method") + "] with " + param);
			System.out.println(" ===========================");

			Class<?> service = Class.forName("reverse_order.service." + request.getParameter("service"));
			Class[] parameterType = new Class[2];
			parameterType[0] = ServiceContext.class;
			parameterType[1] = SqlSession.class;

			Method method = service.getDeclaredMethod(request.getParameter("method"), parameterType);

			method.invoke(null, ctx, sqlSession);

			sqlSession.commit();
			ctx.request = request;
			ctx.response = response;

		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());

			ctx.result.put("status", "error");
			ctx.result.put("msg", "등록되지 않은 서비스를 호출하였습니다.\n" + e.getMessage());
		} catch (NoSuchMethodException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());

			ctx.result.put("status", "error");
			ctx.result.put("msg", "등록되지 않은 메서드를 호출하였습니다.\n" + e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
			ctx.result.put("status", "error");
			ctx.result.put("msg", e.getMessage());
		} finally {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(gson.toJson(ctx.result));
		}
	}
}
