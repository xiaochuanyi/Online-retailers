package mall.order.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jedis.JedisClient;
import mall.commom.utils.E3Result;
import mall.mapper.TbOrderItemMapper;
import mall.mapper.TbOrderMapper;
import mall.mapper.TbOrderShippingMapper;
import mall.order.pojo.OrderInfo;
import mall.order.service.OrderService;
import mall.pojo.TbItem;
import mall.pojo.TbOrderItem;
import mall.pojo.TbOrderShipping;
/**
 * 订单处理服务
 * <p>Title: OrderServiceImpl</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	private TbOrderMapper orderMapper;
	@Autowired
	private TbOrderItemMapper orderItemMapper;
	@Autowired
	private TbOrderShippingMapper orderShippingMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${ORDER_ID_GEN_KEY}")
	private String ORDER_ID_GEN_KEY;
	@Value("${ORDER_ID_START}")
	private String ORDER_ID_START;
	@Value("${ORDER_DETAIL_GEN_KEY}")
	private String ORDER_DETAIL_GEN_KEY;
	@Override
	public E3Result createOrder(OrderInfo orderInfo) {
		//生成订单号,使用redis的incr生成
		if(!jedisClient.exists(ORDER_ID_GEN_KEY)){
			jedisClient.set(ORDER_ID_GEN_KEY,ORDER_ID_START);
		}
		String orderId = jedisClient.incr(ORDER_ID_GEN_KEY).toString();
		//补全TbOrder的属性
		orderInfo.setOrderId(orderId);
		//1.未付款 2.已付款 3.未发货 4.已发货 5.交易成功 6.交易关闭
		orderInfo.setStatus(1);
		orderInfo.setCloseTime(new Date());
		orderInfo.setUpdateTime(new Date());
		//向订单表插入数据
		orderMapper.insert(orderInfo);
		//向订单明细表插入数据
		List<TbOrderItem> orderItems = orderInfo.getOrderItems();
		for (TbOrderItem tbOrderItem : orderItems) {
			String odId = jedisClient.incr(ORDER_DETAIL_GEN_KEY).toString();
			//补全pojo的属性
			tbOrderItem.setId(odId);
			tbOrderItem.setOrderId(orderId);
			//向明细表插入数据
			orderItemMapper.insert(tbOrderItem);
		}
		//向订单物流表插入数据
		TbOrderShipping orderShipping = orderInfo.getOrderShipping();
		orderShipping.setOrderId(orderId);
		orderShipping.setCreated(new Date());
		orderShipping.setUpdated(new Date());
		orderShippingMapper.insert(orderShipping);
		//返回成功，包含订单号
		return E3Result.ok(orderId);
	}

}
