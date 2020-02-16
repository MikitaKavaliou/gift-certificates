package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.mapper.TagMapper;
import com.epam.esm.model.Tag;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class TagDaoImpl implements TagDao {

  private final SqlSessionFactory sqlSessionFactory;

  @Autowired
  public TagDaoImpl(SqlSessionFactory sqlSessionFactory) {
    this.sqlSessionFactory = sqlSessionFactory;
  }

  @Override
  public Long create(Tag tag) {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      sqlSession.getMapper(TagMapper.class).insert(tag);
      return tag.getId();
    }
  }

  @Override
  public List<Tag> findAll() {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      return sqlSession.getMapper(TagMapper.class).selectAll();
    }
  }

  @Override
  public List<Tag> findByCertificateId(Long certificateId) {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      return sqlSession.getMapper(TagMapper.class).selectByCertificateId(certificateId);
    }
  }

  @Override
  public Optional<Long> findTagIdByTagIdAndCertificateId(Long tagId, Long certificateId) {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      return sqlSession.getMapper(TagMapper.class).selectTagIdByTagIdAndCertificateId(tagId, certificateId);
    }
  }

  @Override
  public Optional<Tag> findByName(String name) {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      return sqlSession.getMapper(TagMapper.class).selectByName(name);
    }
  }

  @Override
  public int delete(Long id) {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      return sqlSession.getMapper(TagMapper.class).deleteById(id);
    }
  }

  @Override
  public Optional<Tag> findById(Long id) {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      return sqlSession.getMapper(TagMapper.class).selectById(id);
    }
  }

  @Override
  public Tag findTheMostPopularTagOfHighestSpendingUser() {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      return sqlSession.getMapper(TagMapper.class).selectMostPopularTagOfHighestSpendingUser();
    }
  }
}