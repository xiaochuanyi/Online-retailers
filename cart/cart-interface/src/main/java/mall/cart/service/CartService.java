package mall.cart.service;

import java.util.List;

import mall.commom.utils.E3Result;
import mall.pojo.TbItem;

public interface CartService {
	E3Result addCart(Long userId,Long itemId,int num);
	E3Result mergeCart(Long userId,List<TbItem> itemList);
	List<TbItem> getCartList(Long userId);
	E3Result updateCarNum(Long userId,Long itemId,int num);
	E3Result deleteCarItem(Long userId,Long itemId);
}
