package reverse_order.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServiceContext {
	public HttpServletRequest request = null;
	public HttpServletResponse response = null;
	public Map<String, Object> param = null;
	Map<String, Object> result = new HashMap<String, Object>();
	

	public ServiceContext(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;

		this.result.put("status", "ok");
		this.result.put("msg", "");
	}
}
