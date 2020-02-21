package com.epam.esm.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan(basePackages = "com.epam.esm")
@MapperScan(basePackages = "com.epam.esm.mapper")
public class RepositoryConfig {

}