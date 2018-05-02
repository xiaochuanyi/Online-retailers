package mall.content.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mall.common.pojo.EasyUITreeNode;
import mall.content.service.ContentCategoryService;
import mall.mapper.TbContentCategoryMapper;
import mall.pojo.TbContentCategory;
import mall.pojo.TbContentCategoryExample;
import mall.pojo.TbContentCategoryExample.Criteria;
/**
 * 内容分类管理service
 * <p>Title: ContentCategoryServiceImpl</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {
	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;
	@Override
	public List<EasyUITreeNode> getContentCatList(Long parentId) {
		// 根据parentid查询子节点列表
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		//设置查询条件
		criteria.andParentIdEqualTo(parentId);
		//执行查询
		List<TbContentCategory> catlist = contentCategoryMapper.selectByExample(example);
		//转换成easyuitreenode的列表
		List<EasyUITreeNode> nodelist = new ArrayList<>();
		for (TbContentCategory tbContentCategory  : catlist) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(tbContentCategory.getId());
			node.setText(tbContentCategory.getName());
			node.setState(tbContentCategory.getIsParent()?"closed":"open");
			nodelist.add(node);
		}
		return nodelist ;
	}

}
