package com.epam.esm.config;

import com.epam.esm.mapper.GiftCertificateMapper;
import com.epam.esm.mapper.PurchaseMapper;
import com.epam.esm.mapper.TagMapper;
import com.epam.esm.mapper.UserMapper;
import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan(basePackages = "com.epam.esm")
public class RepositoryConfig {

  @Bean
  public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
    SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
    factoryBean.setDataSource(dataSource);
    return factoryBean.getObject();
  }

  @Bean
  public MapperFactoryBean<TagMapper> tagMapper(SqlSessionFactory sqlSessionFactory) {
    MapperFactoryBean<TagMapper> factoryBean = new MapperFactoryBean<>(TagMapper.class);
    factoryBean.setSqlSessionFactory(sqlSessionFactory);
    return factoryBean;
  }

  @Bean
  public MapperFactoryBean<GiftCertificateMapper> certificateMapper(SqlSessionFactory sqlSessionFactory) {
    MapperFactoryBean<GiftCertificateMapper> factoryBean = new MapperFactoryBean<>(GiftCertificateMapper.class);
    factoryBean.setSqlSessionFactory(sqlSessionFactory);
    return factoryBean;
  }

  @Bean
  public MapperFactoryBean<UserMapper> userMapper(SqlSessionFactory sqlSessionFactory) {
    MapperFactoryBean<UserMapper> factoryBean = new MapperFactoryBean<>(UserMapper.class);
    factoryBean.setSqlSessionFactory(sqlSessionFactory);
    return factoryBean;
  }

  @Bean
  public MapperFactoryBean<PurchaseMapper> purchaseMapper(SqlSessionFactory sqlSessionFactory) {
    MapperFactoryBean<PurchaseMapper> factoryBean = new MapperFactoryBean<>(PurchaseMapper.class);
    factoryBean.setSqlSessionFactory(sqlSessionFactory);
    return factoryBean;
  }
}