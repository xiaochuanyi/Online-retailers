package mall.service;

import mall.commom.utils.E3Result;
import mall.common.pojo.EasyUIDataGridResult;
import mall.pojo.TbItem;
import mall.pojo.TbItemDesc;

public interface ItemService {
	TbItem getItemById(long itemid);
	EasyUIDataGridResult getItemList(int page,int rows);
	E3Result addItem(TbItem item,String desc);
	E3Result deleteItem(long ids);
	TbItemDesc getItemDescById(long itemId);
}
