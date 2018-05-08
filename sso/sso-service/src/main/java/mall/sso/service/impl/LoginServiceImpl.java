package mall.sso.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.servlet.mvc.method.annotation.ServletRequestDataBinderFactory;

import jedis.JedisClient;
import mall.commom.utils.E3Result;
import mall.commom.utils.JsonUtils;
import mall.mapper.TbUserMapper;
import mall.pojo.TbUser;
import mall.pojo.TbUserExample;
import mall.pojo.TbUserExample.Criteria;
import mall.sso.service.LoginService;
import sun.tools.jar.resources.jar;
/**
 * 用户登录处理
 * <p>Title: LoginServiceImpl</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
@Service
public class LoginServiceImpl implements LoginService {
	@Autowired
	private TbUserMapper userMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;
	@Override
	public E3Result userLogin(String username, String passwrod) {
		// 1.判断用户名和密码是否正确
		//根据用户名查询用户信息
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		List<TbUser> list = userMapper.selectByExample(example);
		if(list == null || list.size() == 0){
			//返回登录失败
			return E3Result.build(400, "用户名或者密码错误");
		}
		//取用户信息
		TbUser user = list.get(0);
		//判断密码是否正确
		if(!DigestUtils.md5DigestAsHex(passwrod.getBytes()).equals(user.getPassword())){
		// 2.不正确返回登录失败
			return E3Result.build(400, "用户名或者密码错误");
		}
		// 3.正确生成token
		String token = UUID.randomUUID().toString();
		// 4.把用户信息写入redis，key为token value：用户信息
		//不把密码放入session中
		user.setPassword(null);
		jedisClient.set("SESSION:"+token, JsonUtils.objectToJson(user));
		// 5.设置Session的过期时间
		jedisClient.expire("SESSION:"+token, SESSION_EXPIRE);
		// 6.返回token
		
		return E3Result.ok(token);
	}

}
