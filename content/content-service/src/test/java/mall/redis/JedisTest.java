package mall.redis;

import org.junit.Test;

import redis.clients.jedis.Jedis;

public class JedisTest {
	@Test
	public void testJedis() throws Exception{
		//创建连接对象
		Jedis jedis = new Jedis("192.168.25.177",6379);
		//使用jedis操作redis
		jedis.set("hello", "world");
		String string = jedis.get("hello");
		System.out.println(string);
		//关闭连接
		jedis.close();
	}
}
