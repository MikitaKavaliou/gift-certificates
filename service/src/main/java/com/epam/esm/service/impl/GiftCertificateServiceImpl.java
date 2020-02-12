package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.GiftCertificateWithTags;
import com.epam.esm.exception.ExceptionType;
import com.epam.esm.exception.ServerException;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.GiftCertificateService;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The type Gift certificate service.
 */
@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

  private GiftCertificateDao giftCertificateDao;
  private TagDao tagDao;

  @Autowired
  public GiftCertificateServiceImpl(GiftCertificateDao giftCertificateDao, TagDao tagDao) {
    this.giftCertificateDao = giftCertificateDao;
    this.tagDao = tagDao;
  }

  @Override
  public GiftCertificateWithTags findById(Long id) {
    return findCertificateById(id, ExceptionType.RESOURCE_NOT_FOUND);
  }

  private GiftCertificateWithTags findCertificateById(Long id, ExceptionType exceptionType) {
    GiftCertificate giftCertificate = giftCertificateDao.findById(id)
        .orElseThrow(() -> new ServerException(exceptionType));
    List<Tag> tags = tagDao.findByCertificateId(id);
    return new GiftCertificateWithTags(giftCertificate, tags);
  }

  @Override
  public int delete(Long id) {
    return giftCertificateDao.delete(id);
  }

  @Transactional
  @Override
  public GiftCertificateWithTags create(GiftCertificateWithTags giftCertificateWithTags) {
    return findCertificateById(createGiftCertificate(giftCertificateWithTags), ExceptionType.ERROR_CREATING_ENTITY);
  }

  private Long createGiftCertificate(GiftCertificateWithTags giftCertificateWithTags) {
    List<Long> listOfTagId = new ArrayList<>();
    if (giftCertificateWithTags.getTags() != null) {
      listOfTagId.addAll(createTagIdSetForAddingToCertificate(giftCertificateWithTags.getTags()));
    }
    return giftCertificateDao.create(giftCertificateWithTags.getGiftCertificate(), listOfTagId);
  }

  @Transactional
  @Override
  public GiftCertificateWithTags update(GiftCertificateWithTags giftCertificateWithTags) {
    List<Long> tagIdListForCreation = findTagIdListForAddingToCertificate(giftCertificateWithTags);
    List<Long> tagIdListForDeletion = findTagIdListForDeletingTagsFromCertificate(giftCertificateWithTags);
    giftCertificateDao.update(giftCertificateWithTags.getGiftCertificate(), tagIdListForCreation, tagIdListForDeletion);
    return findCertificateById(giftCertificateWithTags.getId(), ExceptionType.RESOURCE_NOT_FOUND);
  }

  private List<Long> findTagIdListForAddingToCertificate(GiftCertificateWithTags giftCertificateWithTags) {
    List<Long> tagIdListForCreation = new ArrayList<>();
    if (giftCertificateWithTags.getTags() != null) {
      tagIdListForCreation.addAll(removeExistedCertificateTagsFromSet(
          createTagIdSetForAddingToCertificate(giftCertificateWithTags.getTags()), giftCertificateWithTags.getId()));
    }
    return tagIdListForCreation;
  }

  private List<Long> findTagIdListForDeletingTagsFromCertificate(GiftCertificateWithTags giftCertificateWithTags) {
    List<Long> tagIdListForDeletion = new ArrayList<>();
    if (giftCertificateWithTags.getTagsForDeletion() != null) {
      giftCertificateWithTags.getTagsForDeletion()
          .forEach(tag -> tagDao.findByName(tag.getName()).ifPresent(t -> tagIdListForDeletion.add(t.getId())));
    }
    return tagIdListForDeletion;
  }

  private Set<Long> createTagIdSetForAddingToCertificate(List<Tag> tags) {
    Set<Long> tagIdSet = new HashSet<>();
    tags.forEach(t -> tagDao.findByName(t.getName())
        .ifPresentOrElse(tag -> tagIdSet.add(tag.getId()), () -> tagIdSet.add(tagDao.create(t))));
    return tagIdSet;
  }

  private Set<Long> removeExistedCertificateTagsFromSet(Set<Long> tagIdSet, Long certificateId) {
    return tagIdSet.stream().
        filter(id -> tagDao.findTagIdByTagIdAndCertificateId(id, certificateId).isEmpty()).collect(Collectors.toSet());
  }

  @Override
  public List<GiftCertificateWithTags> findCertificatesWithTags(Map<String, String> parameters) {
    List<GiftCertificate> certificates = giftCertificateDao.findCertificatesByCriteria(parameters);
    List<GiftCertificateWithTags> certificatesWithTags = new ArrayList<>();
    certificates
        .forEach(c -> certificatesWithTags.add(new GiftCertificateWithTags(c, tagDao.findByCertificateId(c.getId()))));
    return certificatesWithTags;
  }
}