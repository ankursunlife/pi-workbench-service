package com.aarete.pi.config;

/*@Configuration
@EnableRedisRepositories*/
public class RedisConfiguration {

	
	/*
	 * @Bean public JedisConnectionFactory connectionFactory() {
	 * RedisStandaloneConfiguration configuration = new
	 * RedisStandaloneConfiguration(); //configuration.setHostName(
	 * "redis-temp-pi.6eobuo.0001.use1.cache.amazonaws.com"); JedisConnectionFactory
	 * connectionFactory = new JedisConnectionFactory(configuration);
	 * connectionFactory.getPoolConfig().setMinIdle(10);
	 * connectionFactory.getPoolConfig().setMaxIdle(30);
	 * connectionFactory.getPoolConfig().setMaxWaitMillis(2000); return
	 * connectionFactory; }
	 * 
	 * @Bean public RedisTemplate<String, Object> template() { RedisTemplate<String,
	 * Object> template = new RedisTemplate<>();
	 * template.setConnectionFactory(connectionFactory());
	 * template.setKeySerializer(new StringRedisSerializer());
	 * template.setHashKeySerializer(new StringRedisSerializer());
	 * template.setHashKeySerializer(new JdkSerializationRedisSerializer());
	 * template.setValueSerializer(new JdkSerializationRedisSerializer());
	 * template.setEnableTransactionSupport(true); template.afterPropertiesSet();
	 * return template; }
	 */
}
