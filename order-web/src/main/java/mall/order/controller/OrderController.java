package mall.order.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import mall.cart.service.CartService;
import mall.commom.utils.E3Result;
import mall.order.pojo.OrderInfo;
import mall.order.service.OrderService;
import mall.pojo.TbItem;
import mall.pojo.TbUser;

/**
 * 订单管理controller
 * <p>Title: OrderController</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
@Controller
public class OrderController {
	@Autowired
	private CartService cartService;
	@Autowired
	private OrderService orderService;
	 @RequestMapping("/order/order-cart")
	 public String showOrderCart(HttpServletRequest request){
		 //根据用户id取收货列表
		 //使用静态数据
		 //取支付方式列表
		 //静态数据
		 //取用户id
		 TbUser user = (TbUser) request.getAttribute("user");
		 //根据用户id取购物车列表
		 List<TbItem> cartList = cartService.getCartList(user.getId());
		 //把购物车列表传递给jsp
		 request.setAttribute("cartList", cartList);
		 //返回页面
		 return "order-cart";
	 }
	 @RequestMapping(value="/order/create",method=RequestMethod.POST)
	 public String createOrder(OrderInfo orderInfo,HttpServletRequest request){
		 //	取用户信息
		 TbUser user = (TbUser) request.getAttribute("user");
		 //把用户信息添加到orderInfo中
		 orderInfo.setUserId(user.getId());
		 orderInfo.setBuyerNick(user.getUsername());
		 //调用服务生成订单
		 E3Result result = orderService.createOrder(orderInfo);
		 //如果订单生成成功，删除购物车
		 if(result.getStatus()  == 200){
			 //清空购物车
			 cartService.clearCartItem(user.getId());
		 }
		 //把订单号传递给页面
		 request.setAttribute("orderId", result.getData());
		 request.setAttribute("payment", orderInfo.getPayment());
		 //返回一个逻辑视图
		 return "success";
	 }
}