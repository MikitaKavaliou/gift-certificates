package com.epam.esm.config;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;


@Configuration
@ComponentScan(basePackages = "com.epam.esm")
public class RepositoryConfig {

  @Bean
  public JdbcTemplate jdbcTemplate(@Lazy DataSource dataSource) {
    return new JdbcTemplate(dataSource);
  }
}