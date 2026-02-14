package com.dominickcs.job_scheduler_system.config;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import io.github.cdimascio.dotenv.Dotenv;
import java.util.HashMap;
import java.util.Map;

public class DotEnvConfig implements ApplicationContextInitializer<ConfigurableApplicationContext> {

  @Override
  public void initialize(ConfigurableApplicationContext applicationContext) {
    Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

    ConfigurableEnvironment environment = applicationContext.getEnvironment();
    Map<String, Object> dotenvMap = new HashMap<>();

    dotenv.entries().forEach(entry -> {
      dotenvMap.put(entry.getKey(), entry.getValue());
    });

    environment.getPropertySources()
        .addFirst(new MapPropertySource("dotenvProperties", dotenvMap));
  }
}
