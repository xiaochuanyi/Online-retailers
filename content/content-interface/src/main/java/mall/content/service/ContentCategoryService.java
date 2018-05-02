package mall.content.service;

import java.util.List;

import mall.commom.utils.E3Result;
import mall.common.pojo.EasyUITreeNode;

public interface ContentCategoryService {
	List<EasyUITreeNode> getContentCatList(Long parentId);
	E3Result addContentCategory(long parentId,String name);
}
