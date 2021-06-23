package com.dongkuksystems.redissonboottest.configs;

import java.io.File;
import java.io.IOException;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

@Configuration
public class RedisConfig {
	@Bean(destroyMethod = "shutdown")
	public RedissonClient redissonClient() throws IOException {
		String configFileName = "redisson.yaml";
		File resourceURL = ResourceUtils.getFile("classpath:" + configFileName);
		Config config = Config.fromYAML(resourceURL);
		RedissonClient redisson = Redisson.create(config);
		return redisson;
	}
}
