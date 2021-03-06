package mall.redis;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import jedis.JedisClient;

public class JedisClientTest {
	@Test
	public void test(){
		//初始化spring容器
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
		//从容器中获得jedis对象
		JedisClient jedisClient = applicationContext.getBean(JedisClient.class);
		jedisClient.set("test", "test1");
		String string = jedisClient.get("test");
		System.out.println(string);
	}
}
