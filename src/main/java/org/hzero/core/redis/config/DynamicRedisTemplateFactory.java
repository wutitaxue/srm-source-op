//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.hzero.core.redis.config;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.data.redis.JedisClientConfigurationBuilderCustomizer;
import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.Assert;

public class DynamicRedisTemplateFactory<K, V> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicRedisTemplateFactory.class);
    private RedisProperties properties;
    private ObjectProvider<RedisSentinelConfiguration> sentinelConfiguration;
    private ObjectProvider<RedisClusterConfiguration> clusterConfiguration;
    private ObjectProvider<List<JedisClientConfigurationBuilderCustomizer>> jedisBuilderCustomizers;
    private ObjectProvider<List<LettuceClientConfigurationBuilderCustomizer>> lettuceBuilderCustomizers;
    private static final String REDIS_CLIENT_LETTUCE = "lettuce";
    private static final String REDIS_CLIENT_JEDIS = "jedis";

    public DynamicRedisTemplateFactory(RedisProperties properties, ObjectProvider<RedisSentinelConfiguration> sentinelConfiguration, ObjectProvider<RedisClusterConfiguration> clusterConfiguration, ObjectProvider<List<JedisClientConfigurationBuilderCustomizer>> jedisBuilderCustomizers, ObjectProvider<List<LettuceClientConfigurationBuilderCustomizer>> lettuceBuilderCustomizers) {
        this.properties = properties;
        this.sentinelConfiguration = sentinelConfiguration;
        this.clusterConfiguration = clusterConfiguration;
        this.jedisBuilderCustomizers = jedisBuilderCustomizers;
        this.lettuceBuilderCustomizers = lettuceBuilderCustomizers;
    }

    public RedisConnectionFactory createRedisConnectionFactory(int database) {
        RedisConnectionFactory redisConnectionFactory = null;
        String var3 = this.getRedisClientType();
        byte var4 = -1;
        switch(var3.hashCode()) {
            case 68393790:
                if (var3.equals("lettuce")) {
                    var4 = 0;
                }
                break;
            case 101001587:
                if (var3.equals("jedis")) {
                    var4 = 1;
                }
        }

        switch(var4) {
            case 0:
                LettuceConnectionConfigure lettuceConnectionConfigure = new LettuceConnectionConfigure(this.properties, this.sentinelConfiguration, this.clusterConfiguration, this.lettuceBuilderCustomizers, database);
                redisConnectionFactory = lettuceConnectionConfigure.redisConnectionFactory();
                break;
            case 1:
                JedisConnectionConfigure jedisConnectionConfigure = new JedisConnectionConfigure(this.properties, this.sentinelConfiguration, this.clusterConfiguration, this.jedisBuilderCustomizers, database);
//                redisConnectionFactory = jedisConnectionConfigure.redisConnectionFactory();
                JedisConnectionFactory jedisConnectionFactory = jedisConnectionConfigure.redisConnectionFactory();
                jedisConnectionFactory.afterPropertiesSet();
                redisConnectionFactory = jedisConnectionFactory;
                break;
            default:
                LOGGER.warn("Unsupported dynamic redis client.");
        }

        return (RedisConnectionFactory)redisConnectionFactory;
    }

    public RedisTemplate<K, V> createRedisTemplate(int database) {
        RedisConnectionFactory redisConnectionFactory = this.createRedisConnectionFactory(database);
        Assert.notNull(redisConnectionFactory, "redisConnectionFactory is null.");
        return this.createRedisTemplate(redisConnectionFactory);
    }

    private RedisTemplate<K, V> createRedisTemplate(RedisConnectionFactory factory) {
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        RedisTemplate<K, V> redisTemplate = new RedisTemplate();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setStringSerializer(stringRedisSerializer);
        redisTemplate.setDefaultSerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(stringRedisSerializer);
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    private String getRedisClientType() {
        try {
            Class.forName("io.lettuce.core.RedisClient");
            return "lettuce";
        } catch (ClassNotFoundException var3) {
            LOGGER.debug("Not Lettuce redis client");

            try {
                Class.forName("redis.clients.jedis.Jedis");
                return "jedis";
            } catch (ClassNotFoundException var2) {
                LOGGER.debug("Not Jedis redis client");
                throw new RuntimeException("redis client not found.");
            }
        }
    }
}
