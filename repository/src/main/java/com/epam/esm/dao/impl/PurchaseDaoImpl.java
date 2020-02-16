package com.epam.esm.dao.impl;

import com.epam.esm.dao.PurchaseDao;
import com.epam.esm.mapper.PurchaseMapper;
import com.epam.esm.model.Purchase;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PurchaseDaoImpl implements PurchaseDao {

  private final SqlSessionFactory sqlSessionFactory;

  @Autowired
  public PurchaseDaoImpl(SqlSessionFactory sqlSessionFactory) {
    this.sqlSessionFactory = sqlSessionFactory;
  }


  @Override
  public Long create(Purchase purchase) {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      sqlSession.getMapper(PurchaseMapper.class).insert(purchase);
      return purchase.getId();
    }
  }

  @Override
  public List<Purchase> findPurchasesByUserId(Long userId) {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      return sqlSession.getMapper(PurchaseMapper.class).selectByUserId(userId);
    }
  }

  @Override
  public Optional<Purchase> findBydId(Long id) {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      return sqlSession.getMapper(PurchaseMapper.class).selectById(id);
    }
  }
}