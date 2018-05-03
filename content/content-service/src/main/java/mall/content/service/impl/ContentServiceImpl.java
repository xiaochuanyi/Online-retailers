package mall.content.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.druid.support.json.JSONUtils;

import jedis.JedisClient;
import mall.commom.utils.E3Result;
import mall.commom.utils.JsonUtils;
import mall.content.service.ContentService;
import mall.mapper.TbContentMapper;
import mall.pojo.TbContent;
import mall.pojo.TbContentExample;
import mall.pojo.TbContentExample.Criteria;
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
	private JedisClient jedisClient;
	@Autowired
	private TbContentMapper contentMapper;
	@Value("${COUNT_LIST}")
	private String COUNT_LIST;
	@Override
	public E3Result addContent(TbContent content) {
		//将内容数据插入到内容表
		content.setCreated(new Date());
		content.setUpdated(new Date());
		//插入到数据库
		contentMapper.insert(content);
		//缓存同步，删除掉缓存对应的数据
		jedisClient.hdel(COUNT_LIST, content.getCategoryId().toString());
		return E3Result.ok();
	}

	@Override
	public List<TbContent> geTbContentListByid(long cid) {
		//查询缓存
		try {
		//如果缓存有直接响应结果
			String json = jedisClient.hget(COUNT_LIST, cid+"");
			if(StringUtils.isNotBlank(json)){
				List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//如果缓存没有查询数据库
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(cid);
		List<TbContent> list = contentMapper.selectByExampleWithBLOBs(example);
		//把结果添加到缓存
		try {
			jedisClient.hset(COUNT_LIST, cid + "",JsonUtils.objectToJson(list) );
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
