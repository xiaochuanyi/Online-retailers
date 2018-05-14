package mall.order.service;

import mall.commom.utils.E3Result;
import mall.order.pojo.OrderInfo;

public interface OrderService {
	E3Result createOrder(OrderInfo orderInfo);
}
