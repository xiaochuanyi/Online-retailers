package mall.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import mall.cart.service.CartService;
import mall.commom.utils.CookieUtils;
import mall.commom.utils.E3Result;
import mall.commom.utils.JsonUtils;
import mall.pojo.TbItem;
import mall.pojo.TbUser;
import mall.service.ItemService;

/**
 * 购物车处理controller
 * <p>Title: CartController</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
@Controller
public class CartController {
	@Autowired
	private ItemService itemService;
	@Value("${COOKIE_CART_EXPIRE}")
	private Integer COOKIE_CART_EXPIRE;
	@Autowired
	private CartService cartService;
	@RequestMapping("/cart/add/{itemId}")
	public String addCart(@PathVariable Long itemId,@RequestParam(defaultValue="1")Integer num,
			HttpServletRequest request,HttpServletResponse response){
		//判断用户是否登录
		TbUser user = (TbUser) request.getAttribute("user");
		//如果登录，则把购物车写入redis
		if(user != null){
			//保存到服务端
			cartService.addCart(user.getId(), itemId, num);
			//返回逻辑视图
			return "cartSuccess"; 
		}
		//如果未登录则使用cookie
		//从cookie取购物车列表
		List<TbItem> cartList = getCartListFromCookie(request);
		//判断商品在商品列表中是否存在
		boolean flag = false;
		for (TbItem tbItem : cartList) {
			//如果存在数量相加
			if(tbItem.getId() == itemId.longValue()){
				tbItem.setNum(tbItem.getNum() + num);
				flag = true;
				//跳出循环
				break;
			}
		}
		//如果不存在，根据商品id查询商品信息.得到一个tbitem对象
		if(!flag){
			TbItem tbItem = itemService.getItemById(itemId);
			//设置商品数量
			tbItem.setNum(num);
			//取一张图片
			String image = tbItem.getImage();
			if(StringUtils.isNotBlank(image)){
				tbItem.setImage(image.split(",")[0]);
			}
			//把商品添加到商品列表
			cartList.add(tbItem);
		}
		//写入cookie
		CookieUtils.setCookie(request, response,"cart", JsonUtils.objectToJson(cartList),COOKIE_CART_EXPIRE,true);
		//返回添加成功的页面
		return "cartSuccess";
	}
	/**
	 * 从cookie中取购物车列表的消息
	 * <p>Title: getCartListFromCookie</p>
	 * <p>Description: </p>
	 * @param request
	 * @return
	 */
	private List<TbItem> getCartListFromCookie(HttpServletRequest request){
		String json = CookieUtils.getCookieValue(request, "cart",true);
		//判断json是否为空
		if(StringUtils.isBlank(json)){
			return new ArrayList<>();
		}
		//把json转换成商品列表
		List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
		return list;
	}
	/**
	 * 购物车列表展示
	 * <p>Title: showCartList</p>
	 * <p>Description: </p>
	 * @param request
	 * @return
	 */
	@RequestMapping("/cart/cart")
	public String showCartList(HttpServletRequest request,HttpServletResponse response){
		//从cookie取购物车列表
		List<TbItem> cartList = getCartListFromCookie(request);
		//判断用户是否为登录状态
		TbUser user = (TbUser) request.getAttribute("user");
		//如果是登录状态
		if(user != null){
			//从cookie中取购物车列表
			//如果不为空，把cookie中购物车和服务端的购物车商品合并
			cartService.mergeCart(user.getId(), cartList);
			//从cookie中删除购物车
			CookieUtils.deleteCookie(request, response,"cart");
			//从服务端取购物车列表
			cartList = cartService.getCartList(user.getId());
		}
		//把列表传给页面
		request.setAttribute("cartList", cartList);
		//返回逻辑视图
		return "cart";
	}
	/**
	 * 更新购物车数量
	 */
	@RequestMapping("/cart/update/num/{itemId}/{num}")
	@ResponseBody
	public E3Result updateCartNum(@PathVariable Long itemId,@PathVariable Integer num,
			HttpServletRequest request,HttpServletResponse response){
		//判读用户是否为登录状态
		TbUser user = (TbUser) request.getAttribute("user");
		if(user != null){
			cartService.updateCarNum(user.getId(), itemId,num);
			return E3Result.ok();
		}
		//从cookie取购物车列表
		List<TbItem> cartList = getCartListFromCookie(request);
		//遍历商品列表找到对应商品
		for (TbItem tbItem : cartList) {
			if(tbItem.getId().longValue() == itemId){
				//更新数量
				tbItem.setNum(num);
				break;
			}
		}
		//把购物车列表写会cookie
		CookieUtils.setCookie(request, response,"cart", JsonUtils.objectToJson(cartList),COOKIE_CART_EXPIRE,true);
		//返回成功
		return E3Result.ok();
	}
	/**
	 * 删除购物车
	 */
	@RequestMapping("/cart/delete/{itemId}")
	public String deleteCartItem(@PathVariable Long itemId,HttpServletRequest request,HttpServletResponse response){
		//判读用户是否为登录状态
		TbUser user = (TbUser) request.getAttribute("user");
		if(user != null){
			cartService.deleteCarItem(user.getId(), itemId);
			return "redirect:/cart/cart.html";
		}
		//从cookie取购物车列表
		List<TbItem> cartList = getCartListFromCookie(request);
		//遍历列表，找到商品
		for (TbItem tbItem : cartList) {
			if(tbItem.getId().longValue() == itemId)
				//删除商品
				cartList.remove(tbItem);
				break;
		}
		//把购物车列表写入cookie
		CookieUtils.setCookie(request, response,"cart", JsonUtils.objectToJson(cartList),COOKIE_CART_EXPIRE,true);
		//返回逻辑视图
		return "redirect:/cart/cart.html";
	}
}