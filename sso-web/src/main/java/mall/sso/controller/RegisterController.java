package mall.sso.controller;

import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import mall.commom.utils.E3Result;
import mall.pojo.TbUser;
import mall.sso.service.RegisterService;

/**
 * 注册功能controller
 * <p>Title: RegisterController</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
@Controller
public class RegisterController {
	@Autowired
	private RegisterService registerService;
	@RequestMapping("/page/register")
	public String showRegister(){
		return "register";
	}
	@RequestMapping("/user/check/{param}/{type}")
	@ResponseBody
	public E3Result checkData(@PathVariable String param,@PathVariable Integer type){
		E3Result result = registerService.checkData(param, type);
		return result;
	}
	@RequestMapping(value="/user/register",method=RequestMethod.POST)
	@ResponseBody
	public E3Result register(TbUser user){
		E3Result result = registerService.register(user);
		return result;
	}
	
}
