package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.mapper.GiftCertificateMapper;
import com.epam.esm.model.GiftCertificate;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class GiftCertificateDaoImpl implements GiftCertificateDao {

  private final SqlSessionFactory sqlSessionFactory;

  @Autowired
  public GiftCertificateDaoImpl(SqlSessionFactory sqlSessionFactory) {
    this.sqlSessionFactory = sqlSessionFactory;
  }

  @Override
  public Long create(GiftCertificate giftCertificate, List<Long> tagIdsForAddingToCertificate) {
    insertGiftCertificate(giftCertificate);
    insertAssociativeTableRecords(tagIdsForAddingToCertificate, giftCertificate.getId());
    return giftCertificate.getId();
  }

  @Override
  public Optional<GiftCertificate> findById(Long id) {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      return sqlSession.getMapper(GiftCertificateMapper.class).selectById(id);
    }
  }

  @Override
  public List<GiftCertificate> findByCriteria(Map<String, String> parameters) {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      return sqlSession.getMapper(GiftCertificateMapper.class)
          .findByCriteria(getListOfParameters(parameters, "tag"),
              getListOfParameters(parameters, "searchValue"),
              getParameter(parameters, "sortField"),
              getParameter(parameters, "sortType"));
    }
  }

  private List<String> getListOfParameters(Map<String, String> parameters, String parameterName) {
    return parameters.containsKey(parameterName) ? Arrays.asList(parameters.get(parameterName).split(" ")) : null;
  }

  private String getParameter(Map<String, String> parameters, String parameterName) {
    return parameters.containsKey(parameterName) ? parameters.get(parameterName).toLowerCase() : null;
  }

  @Override
  public List<GiftCertificate> findByUserId(Long userId) {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      return sqlSession.getMapper(GiftCertificateMapper.class).selectByUserId(userId);
    }
  }

  @Override
  public void updatePrice(Long id, BigDecimal price) {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      sqlSession.getMapper(GiftCertificateMapper.class).updatePrice(id, price);
    }
  }

  @Override
  public void update(GiftCertificate giftCertificate, List<Long> tagIdsForAddingToCertificate,
      List<Long> tagIdsForDeletingFromCertificate) {
    updateCertificateFields(giftCertificate);
    insertAssociativeTableRecords(tagIdsForAddingToCertificate, giftCertificate.getId());
    deleteAssociativeTableRecords(tagIdsForDeletingFromCertificate, giftCertificate.getId());
  }

  private void insertGiftCertificate(GiftCertificate giftCertificate) {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      sqlSession.getMapper(GiftCertificateMapper.class).insert(giftCertificate);
    }
  }

  private void insertAssociativeTableRecords(List<Long> tagIdsForAddingToCertificate, Long giftCertificateId) {
    try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
      for (Long tagId : tagIdsForAddingToCertificate) {
        sqlSession.getMapper(GiftCertificateMapper.class).insertAssociativeRecord(tagId, giftCertificateId);
      }
      sqlSession.commit();
    }
  }

  private void deleteAssociativeTableRecords(List<Long> tagIdsForDeletingFromCertificate, Long giftCertificateId) {
    try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
      for (Long tagId : tagIdsForDeletingFromCertificate) {
        sqlSession.getMapper(GiftCertificateMapper.class).deleteAssociativeRecord(tagId, giftCertificateId);
      }
      sqlSession.commit();
    }
  }

  private void updateCertificateFields(GiftCertificate giftCertificate) {
    if (hasFieldsForCertificateUpdate(giftCertificate)) {
      try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
        sqlSession.getMapper(GiftCertificateMapper.class).update(giftCertificate);
      }
    }
  }

  private boolean hasFieldsForCertificateUpdate(GiftCertificate giftCertificate) {
    return giftCertificate.getName() != null || giftCertificate.getDescription() != null ||
        giftCertificate.getPrice() != null || giftCertificate.getDuration() != null;
  }

  @Override
  public int delete(Long id) {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      return sqlSession.getMapper(GiftCertificateMapper.class).delete(id);
    }
  }
}