package mall.service;

import java.util.List;

import mall.common.pojo.EasyUIDataGridResult;
import mall.common.pojo.EasyUITreeNode;

public interface ItemCatService {
	List<EasyUITreeNode> getItemCatlist(long parentId);
}
