package mall.redis;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;


import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

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
	@Test
	public void testJedisPool() throws Exception {
		//创建连接池
		JedisPool jedisPool = new JedisPool("192.168.25.177",6379);
		//从连接池中获得连接
		Jedis jedis = jedisPool.getResource();
		//使用jedis操作redis
		String string = jedis.get("hello");
		System.out.println(string);
		//关闭连接，每次使用完毕都需要关闭连接池回收资源
		jedis.close();
		//关闭连接池
		jedisPool.close();
	}
	@Test
	public void testJedisCluster() throws Exception{
		//创建一个JedisCluster对象
		Set<HostAndPort> nodes = new HashSet<>();
		nodes.add(new HostAndPort("192.168.25.177", 7001));
		nodes.add(new HostAndPort("192.168.25.177", 7002));
		nodes.add(new HostAndPort("192.168.25.177", 7003));
		nodes.add(new HostAndPort("192.168.25.177", 7004));
		nodes.add(new HostAndPort("192.168.25.177", 7005));
		nodes.add(new HostAndPort("192.168.25.177", 7006));
		JedisCluster jedisCluster = new JedisCluster(nodes);
		//使用JedisCluster对象操作redis
		jedisCluster.set("test", "123");
		String string = jedisCluster.get("test");
		System.out.println(string);
		//关闭JedisCluster
		jedisCluster.close();
		
	}
}
