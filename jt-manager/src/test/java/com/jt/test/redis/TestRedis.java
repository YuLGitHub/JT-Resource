package com.jt.test.redis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class TestRedis {

	//@Test
	public void test01() {
		@SuppressWarnings("resource")
		Jedis jedis = new Jedis("192.168.240.133",6379);
		jedis.set("names", "jedis");
		System.out.println("获取redis中的数据" + jedis.get("names"));
	}
	
	// 分片的测试
	//@Test
	public void test02() {
		
		/*
		 * 1.poolConfig定义连接池的大小
		 * 2.shards 表示List<Shardinfo> 表示redis信息的集合
		 * 
		 *  补充：
		 *  Jedis引入会有线程安全问题，所以通过线程池调度方式动态获取Jedis
		 */
		// &&定义连接池大小
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(1000);
		poolConfig.setTestOnBorrow(true); // 每次连接都要测试链接是否正常，若不正常，会重新获取
		poolConfig.setMaxIdle(30);
		poolConfig.setMinIdle(10); 
		
		// &&&定义分片的list集合
		List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
		shards.add(new JedisShardInfo("192.168.240.133", 6379));
		shards.add(new JedisShardInfo("192.168.240.133", 6380));
		shards.add(new JedisShardInfo("192.168.240.133", 6381));
		// &定义分片的连接池
		ShardedJedisPool jedisPool = new ShardedJedisPool(poolConfig, shards);
		
		// &&&&获取redis的链接
		ShardedJedis jedis = jedisPool.getResource();
		for (int i = 1; i <= 20; i++) {
			jedis.set("key" + i, "" + i);
		}
		System.out.println("Redis插入操作成功");
	}
	
	// 哨兵测试
	@Test
	public void test03() {

		// &&String 类型表示哨兵的IP：端口号
		Set<String> sentinels = new HashSet<String>();
		
		// &&&为Set集合赋值，保存哨兵的信息
		sentinels.add(new HostAndPort("192.168.240.133", 26379).toString());
		sentinels.add(new HostAndPort("192.168.240.133", 26380).toString());
		sentinels.add(new HostAndPort("192.168.240.133", 26381).toString());
		
		// &创建哨兵的链接池
		/*
		 * 参数介绍：
		 * 1.masterName为链接哨兵的主机的名称，一般为mymaster
		 * 2.sentinels 哨兵的集合Set<>
		 * 
		 */
		JedisSentinelPool sentinelPool = new JedisSentinelPool("mymaster", sentinels);
		
		Jedis jedis = sentinelPool.getResource();
		jedis.set("name", "tom");
		System.out.println("获取数据" + jedis.get("name"));
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
