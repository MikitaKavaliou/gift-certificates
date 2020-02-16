package com.epam.esm.dao.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.mapper.UserMapper;
import com.epam.esm.model.User;
import java.util.Optional;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDao {

  private final SqlSessionFactory sqlSessionFactory;

  @Autowired
  public UserDaoImpl(SqlSessionFactory sqlSessionFactory) {
    this.sqlSessionFactory = sqlSessionFactory;
  }

  @Override
  public Long create(User user) {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      sqlSession.getMapper(UserMapper.class).insert(user);
      return user.getId();
    }
  }

  @Override
  public Optional<User> findById(Long id) {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      return sqlSession.getMapper(UserMapper.class).selectById(id);
    }
  }

  @Override
  public Optional<User> findByUsername(String username) {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      return sqlSession.getMapper(UserMapper.class).selectByUsername(username);
    }
  }
}