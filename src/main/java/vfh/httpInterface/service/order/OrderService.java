package vfh.httpInterface.service.order;


import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import vfh.httpInterface.commons.Page;
import vfh.httpInterface.commons.PageRequest;
import vfh.httpInterface.commons.valid.annotation.MapValid;
import vfh.httpInterface.dao.buildings.BuildingsDeveloperMapper;
import vfh.httpInterface.dao.order.OrderDao;

/**
 * TODO 楼盘业务逻辑
 * @author harry
 * <b> 有问题请联系qq:359705093</b>
 * @create 2016年1月11日
 */
@Service
@Transactional
public class OrderService {

	@Autowired
	private OrderDao orderDao;
	
	public Page<Map<String, Object>> findOrderList(PageRequest pageRequest, Map<String, Object> filter) {
        long total = orderDao.count(filter);
        filter.putAll(pageRequest.getMap());
        List<Map<String, Object>> content = orderDao.find(filter);

        return new Page<Map<String, Object>>(pageRequest, content, total);
    }
	
	public void insert(@MapValid("insert-order") Map<String, Object> entity){
		 orderDao.insert(entity);
	}

}
