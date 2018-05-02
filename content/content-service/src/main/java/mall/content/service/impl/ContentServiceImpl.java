package mall.content.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mall.commom.utils.E3Result;
import mall.content.service.ContentService;
import mall.mapper.TbContentMapper;
import mall.pojo.TbContent;
/**
 * 内容
 * <p>Title: ContentServiceImpl</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;
	
	@Override
	public E3Result addContent(TbContent content) {
		//将内容数据插入到内容表
		content.setCreated(new Date());
		content.setUpdated(new Date());
		//插入到数据库
		contentMapper.insert(content);
		return E3Result.ok();
	}
}
