package reverse_order.dao;

import java.util.List;
import java.util.Map;

public interface DaoPost {
	List<Map<String, Object>> Select01();

	int Insert01(Map<String, Object> param);
	int Delete01(Map<String, Object> param);
}
 