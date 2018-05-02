package mall.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import mall.common.pojo.EasyUITreeNode;
import mall.mapper.TbItemCatMapper;
import mall.pojo.TbItemCat;
import mall.pojo.TbItemCatExample;
import mall.pojo.TbItemCatExample.Criteria;
import mall.service.ItemCatService;
/*
 * 商品分类管理
 */
@Service
public class ItemCatServiceImpl implements ItemCatService{
	@Autowired
	public TbItemCatMapper itemCatMapper;
	public List<EasyUITreeNode> getItemCatlist(long parentId) {
		//根据parentid查询子节点列表
		TbItemCatExample example = new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		//设置查询条件
		criteria.andParentIdEqualTo(parentId);
		//把列表转换为EasyUITreeNode列表
		List<TbItemCat> list =  itemCatMapper.selectByExample(example);
		List<EasyUITreeNode> resultList = new ArrayList<>();
		for (TbItemCat tbItemCat : list) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(tbItemCat.getId());
			node.setText(tbItemCat.getName());
			node.setState(tbItemCat.getIsParent()?"closec":"open");
			resultList.add(node);
		}
		//返回结果
		return resultList;
	}

}
