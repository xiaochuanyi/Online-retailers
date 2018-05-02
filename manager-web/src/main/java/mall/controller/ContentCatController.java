package mall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import mall.commom.utils.E3Result;
import mall.common.pojo.EasyUITreeNode;
import mall.content.service.ContentCategoryService;

/**
 * 内容管理分类Controller
 * <p>Title: ContentCatController</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
@Controller
public class ContentCatController {
	@Autowired
	private ContentCategoryService contentCategoryService;
	@RequestMapping("content/category/list")
	@ResponseBody
	public List<EasyUITreeNode> getContentCatList(@RequestParam(name="id",defaultValue="0")Long parentId){
		List<EasyUITreeNode> list = contentCategoryService.getContentCatList(parentId);
		return list;
	}
	@RequestMapping(value="/content/category/create",method=RequestMethod.POST)
	@ResponseBody
	public E3Result createcontentCategory(Long parentId,String name){
		//调用服务添加节点
		E3Result e3Result = contentCategoryService.addContentCategory(parentId, name);
		return e3Result;
	}
	
}
