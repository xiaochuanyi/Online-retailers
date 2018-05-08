package mall.sso.service;

import mall.commom.utils.E3Result;
import mall.pojo.TbUser;

public interface RegisterService {
	E3Result checkData(String param,int type);
	E3Result register(TbUser user);
}
