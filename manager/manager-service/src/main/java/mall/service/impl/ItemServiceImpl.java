package mall.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import mall.commom.utils.E3Result;
import mall.commom.utils.IDUtils;
import mall.common.pojo.EasyUIDataGridResult;
import mall.mapper.TbItemDescMapper;
import mall.mapper.TbItemMapper;
import mall.pojo.TbItem;
import mall.pojo.TbItemDesc;
import mall.pojo.TbItemExample;
import mall.service.ItemService;
@Service
public class ItemServiceImpl implements ItemService {
	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper itemDescMapper;
	public TbItem getItemById(long itemId) {
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		return item;
	}
	@Override
	public EasyUIDataGridResult getItemList(int page, int rows) {
		//设置分页信息
		PageHelper.startPage(page, rows);
		//执行查询
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(list);
		//取得分页结果
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		long total = pageInfo.getTotal();
		result.setTotal(total);
		return result;
	}
	@Override
	public E3Result addItem(TbItem item, String desc) {
		//生成商品id
		long itemId = IDUtils.genItemId();
		//补全item的属性
		item.setId(itemId);
		// 1.正常 2.下架 3.删除
		item.setStatus((byte) 1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		//向商品表插入数据
		itemMapper.insert(item);
		//创建一个商品描述表的pojo对象
		TbItemDesc tbItemDesc = new TbItemDesc();
		//补全属性
		tbItemDesc.setItemId(itemId);
		tbItemDesc.setItemDesc(desc);
		tbItemDesc.setCreated(new Date());
		tbItemDesc.setUpdated(new Date());
		//向商品描述表插入数据
		itemDescMapper.insert(tbItemDesc);
		//返回成功
		return E3Result.ok();
	}
	@Override
	public E3Result deleteItem(long ids) {
		itemMapper.deleteByPrimaryKey(ids);
		return E3Result.ok();
	}
	
}