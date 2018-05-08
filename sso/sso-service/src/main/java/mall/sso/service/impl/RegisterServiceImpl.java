package mall.sso.service.impl;


import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import mall.commom.utils.E3Result;
import mall.mapper.TbUserMapper;
import mall.pojo.TbUser;
import mall.pojo.TbUserExample;
import mall.pojo.TbUserExample.Criteria;
import mall.sso.service.RegisterService;
/**
 * 用户注册处理
 * <p>Title: RegisterServiceImpl</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
@Service
public class RegisterServiceImpl implements RegisterService {
	@Autowired
	private TbUserMapper userMapper;
	@Override
	public E3Result checkData(String param, int type) {
		//根据不同的type生成不同的查询条件
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		//1.用户名 2.手机 3.邮箱 
		if (type == 1) {
			criteria.andUsernameEqualTo(param);
		}else if(type == 2){
			criteria.andPhoneEqualTo(param);
		}else if(type == 3){
				criteria.andEmailEqualTo(param);
			}else {
				return E3Result.build(400, "数据类型错误");
			}
		//执行查询
		List<TbUser> list = userMapper.selectByExample(example);
		//判断结果中是否有数据
		if(list != null && list.size() > 0){
		//如果有数据返回false
			return E3Result.ok(false);
		}
		//如果没有数据返回true
		return E3Result.ok(true);
	}
	@Override
	public E3Result register(TbUser user) {
		//对数据有效性进行校验
		if(StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPhone()) || StringUtils.isBlank(user.getPassword())){
			return E3Result.build(400, "用户数据不完整,注册失败");
		}
		System.out.println(10086);
		//1.用户名 2.手机 3.邮箱 
		E3Result result = checkData(user.getUsername(),1);
		if(!(boolean) result.getData()){
			return E3Result.build(400, "用户名已经占用");
		}
		result = checkData(user.getPhone(),2);
		if(!(boolean) result.getData()){
			return E3Result.build(400, "手机号已经注册");
		}
		//补全pojo的属性
		user.setCreated(new Date());
		user.setUpdated(new Date());
		//对密码进行md5加密
		String md5password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5password);
		//把用户数据插入到数据库中
		userMapper.insert(user);
		//返回成功
		return E3Result.ok();
	}
	
}

