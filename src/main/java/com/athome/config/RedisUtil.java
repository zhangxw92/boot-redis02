package com.athome.config;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtil {

    private static Jedis jedis = null;
    private static JedisPool jedisPool = null;

    static {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(20);
        jedisPoolConfig.setMaxIdle(20);
        jedisPool = new JedisPool(jedisPoolConfig, "39.102.61.252");
    }

    public static Jedis getJedis() {
        if (jedis == null) {
            return jedisPool.getResource();
        }
        return jedis;
    }
}
