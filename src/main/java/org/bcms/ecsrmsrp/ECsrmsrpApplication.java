package org.bcms.ecsrmsrp;

import java.time.Duration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@SpringBootApplication
@EnableCaching
public class ECsrmsrpApplication {

	public static void main(String[] args) {
		SpringApplication.run(ECsrmsrpApplication.class, args);
	}
	
	@Bean
	RedisCacheConfiguration cacheConfiguration() {
	    return RedisCacheConfiguration
	        .defaultCacheConfig()
	        .entryTtl(Duration.ofMinutes(60))
	        .serializeValuesWith(RedisSerializationContext
	            .SerializationPair
	            .fromSerializer(new GenericJackson2JsonRedisSerializer()));
	}

}
