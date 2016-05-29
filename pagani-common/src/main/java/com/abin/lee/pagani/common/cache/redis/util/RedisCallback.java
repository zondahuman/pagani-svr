package com.abin.lee.pagani.common.cache.redis.util;

import redis.clients.jedis.Jedis;

public interface RedisCallback<T> {

    T doInRequest(Jedis jedis);

}