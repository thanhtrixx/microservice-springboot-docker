package tri.le.datavoucher.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import java.time.Duration;

@Configuration
public class RedisConfiguration {

  private static Logger logger = LoggerFactory.getLogger(RedisConfiguration.class);

  @Value("${redis-host:localhost}")
  private String redisHost;

  @Value("${redis-port:6379}")
  private int redisPort;

  @Value("${time-to-live-in-seconds:150}")
  private int timeToLiveInSeconds;


  @Bean
  public JedisConnectionFactory redisConnectionFactory() {
    logger.info("Connect to Redis {}:{}", redisHost, redisPort);

    RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
    return new JedisConnectionFactory(config);
  }

  @Bean
  public RedisCacheManager redisCacheManager(JedisConnectionFactory jedisConnectionFactory) {
    logger.info("Set TTL to Redis {}s", timeToLiveInSeconds);

    RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
      .entryTtl(Duration.ofSeconds(timeToLiveInSeconds));

    return RedisCacheManager.RedisCacheManagerBuilder
      .fromConnectionFactory(jedisConnectionFactory)
      .cacheDefaults(redisCacheConfiguration).build();

  }
}
