package mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import mall.commom.utils.E3Result;
import mall.common.pojo.EasyUIDataGridResult;
import mall.pojo.TbItem;
import mall.service.ItemService;

@Controller
public class ItemController {
	@Autowired
	private ItemService itemservice;
	@RequestMapping("/item/{itemId}")
	@ResponseBody
	public TbItem getItemById(@PathVariable Long itemId){
		TbItem tbItem = itemservice.getItemById(itemId);
		return tbItem;
	}
	@RequestMapping("/item/list")
	@ResponseBody
	public EasyUIDataGridResult getItemList(Integer page,Integer rows){
		EasyUIDataGridResult result = itemservice.getItemList(page, rows);
		return result;
	}
	/*
	 * 商品添加
	 */
	@RequestMapping(value="/item/save",method=RequestMethod.POST)
	@ResponseBody
	public E3Result addItem(TbItem tbItem,String desc){
		E3Result result = itemservice.addItem(tbItem, desc);
		return result;
	}
	/*
	 * 商品删除
	 */
	@RequestMapping(value="/rest/item/delete",method=RequestMethod.POST)
	@ResponseBody
	public E3Result delteItem(long ids){
		E3Result result = itemservice.deleteItem(ids);
		return result;
	}
	
}