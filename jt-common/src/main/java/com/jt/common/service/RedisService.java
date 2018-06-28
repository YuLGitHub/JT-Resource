package com.jt.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

@Service
public class RedisService {

	@Autowired(required=false)
	private JedisSentinelPool jedisSentinelPool;
	
	public void set(String key, String value) {
		Jedis jedis = jedisSentinelPool.getResource();
		jedis.set(key, value);
		jedisSentinelPool.returnResource(jedis);
	}
	
	public String get(String key) {
		Jedis jedis = jedisSentinelPool.getResource();
		String result = jedis.get(key);
		jedisSentinelPool.returnResource(jedis);
		return result;
	}
}
