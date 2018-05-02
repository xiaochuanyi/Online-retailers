package mall.content.service;

import java.util.List;

import mall.commom.utils.E3Result;
import mall.pojo.TbContent;

public interface ContentService {
	E3Result addContent(TbContent tbContent);
	List<TbContent> geTbContentListByid(long cid);
}
