package com.paladin.framework.core.configuration.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author TontoZhou
 * @since 2018年3月15日
 */
@Configuration
@EnableConfigurationProperties(RedisProperties.class)
public class RedisConfiguration extends CachingConfigurerSupport {

	private Logger logger = LoggerFactory.getLogger(RedisConfiguration.class);

	@Bean
	public JedisConnectionFactory getJedisConnectionFactory(RedisProperties redisProperties) {
		logger.info("创建JedisConnectionFactory");

		RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(redisProperties.getHost(), redisProperties.getPort());
		configuration.setDatabase(0);
		configuration.setPassword(RedisPassword.of(redisProperties.getPassword()));

		JedisConnectionFactory factory = new JedisConnectionFactory(configuration);

		return factory;
	}

	@Bean
	public CacheManager cacheManager(JedisConnectionFactory jedisConnectionFactory) {
		logger.info("创建CacheManager");
		RedisCacheManager redisCacheManager = RedisCacheManager.create(jedisConnectionFactory);
		return redisCacheManager;
	}

	@Bean("jdkRedisTemplate")
	@ConditionalOnMissingBean
	public RedisTemplate<String, Object> getJdkRedisTemplate(JedisConnectionFactory jedisConnectionFactory) {
		logger.debug("getJdkRedisTemplate()");

		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(jedisConnectionFactory);
		return redisTemplate;
	}

	@Bean("jsonRedisTemplate")
	@ConditionalOnMissingBean
	public RedisTemplate<String, Object> getJsonRedisTemplate(JedisConnectionFactory jedisConnectionFactory) {
		logger.debug("getJsonRedisTemplate()");

		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

		redisTemplate.setKeySerializer(redisTemplate.getStringSerializer());
		redisTemplate.setHashKeySerializer(redisTemplate.getStringSerializer());

		// 使用Jackson2JsonRedisSerialize 替换默认序列化
		Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);

		ObjectMapper objectMapper = new ObjectMapper();
		jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

		redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
		redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
		redisTemplate.setConnectionFactory(jedisConnectionFactory);

		return redisTemplate;
	}

	@Bean("stringRedisTemplate")
	@ConditionalOnMissingBean
	public RedisTemplate<String, String> getStringRedisTemplate(JedisConnectionFactory jedisConnectionFactory) {
		logger.debug("getStringRedisTemplate()");
		return new StringRedisTemplate(jedisConnectionFactory);
	}

}
