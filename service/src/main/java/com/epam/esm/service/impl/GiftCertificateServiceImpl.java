package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.GiftCertificateWithTagsDto;
import com.epam.esm.exception.ExceptionType;
import com.epam.esm.exception.ServerException;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.GiftCertificateService;
import java.math.BigDecimal;
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

  private final GiftCertificateDao giftCertificateDao;
  private final TagDao tagDao;

  @Autowired
  public GiftCertificateServiceImpl(GiftCertificateDao giftCertificateDao, TagDao tagDao) {
    this.giftCertificateDao = giftCertificateDao;
    this.tagDao = tagDao;
  }

  @Override
  public GiftCertificateWithTagsDto findById(Long id) {
    return doFindById(id, ExceptionType.RESOURCE_NOT_FOUND);
  }

  private GiftCertificateWithTagsDto doFindById(Long id, ExceptionType exceptionType) {
    GiftCertificate giftCertificate = giftCertificateDao.findById(id)
        .orElseThrow(() -> new ServerException(exceptionType));
    List<Tag> tags = tagDao.findByCertificateId(id);
    return new GiftCertificateWithTagsDto(giftCertificate, tags);
  }

  @Override
  public int delete(Long id) {
    return giftCertificateDao.delete(id);
  }

  @Transactional
  @Override
  public GiftCertificateWithTagsDto create(GiftCertificateWithTagsDto giftCertificateWithTagsDto) {
    return doFindById(createGiftCertificate(giftCertificateWithTagsDto), ExceptionType.ERROR_CREATING_ENTITY);
  }

  private Long createGiftCertificate(GiftCertificateWithTagsDto giftCertificateWithTagsDto) {
    List<Long> listOfTagId = new ArrayList<>();
    if (giftCertificateWithTagsDto.getTags() != null) {
      listOfTagId.addAll(createTagIdSetForAddingToCertificate(giftCertificateWithTagsDto.getTags()));
    }
    return giftCertificateDao.create(giftCertificateWithTagsDto.getGiftCertificate(), listOfTagId);
  }

  @Transactional
  @Override
  public GiftCertificateWithTagsDto update(GiftCertificateWithTagsDto giftCertificateWithTagsDto) {
    List<Long> tagIdListForCreation = findTagIdListForAddingToCertificate(giftCertificateWithTagsDto);
    List<Long> tagIdListForDeletion = findTagIdListForDeletingTagsFromCertificate(giftCertificateWithTagsDto);
    giftCertificateDao
        .update(giftCertificateWithTagsDto.getGiftCertificate(), tagIdListForCreation, tagIdListForDeletion);
    return doFindById(giftCertificateWithTagsDto.getId(), ExceptionType.RESOURCE_NOT_FOUND);
  }

  private List<Long> findTagIdListForAddingToCertificate(GiftCertificateWithTagsDto giftCertificateWithTagsDto) {
    List<Long> tagIdListForCreation = new ArrayList<>();
    if (giftCertificateWithTagsDto.getTags() != null) {
      tagIdListForCreation.addAll(removeExistedCertificateTagsFromSet(
          createTagIdSetForAddingToCertificate(giftCertificateWithTagsDto.getTags()),
          giftCertificateWithTagsDto.getId()));
    }
    return tagIdListForCreation;
  }

  private List<Long> findTagIdListForDeletingTagsFromCertificate(
      GiftCertificateWithTagsDto giftCertificateWithTagsDto) {
    List<Long> tagIdListForDeletion = new ArrayList<>();
    if (giftCertificateWithTagsDto.getTagsForDeletion() != null) {
      giftCertificateWithTagsDto.getTagsForDeletion()
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
  public List<GiftCertificateWithTagsDto> findByCriteria(Map<String, String> parameters) {
    List<GiftCertificate> certificates = giftCertificateDao.findByCriteria(parameters);
    return findTagsForGiftCertificates(certificates);
  }

  private List<GiftCertificateWithTagsDto> findTagsForGiftCertificates(List<GiftCertificate> giftCertificates) {
    return giftCertificates.stream().map(g -> new GiftCertificateWithTagsDto(g, tagDao.findByCertificateId(g.getId())))
        .collect(Collectors.toList());
  }

  @Override
  public List<GiftCertificateWithTagsDto> findByUserId(Long userId) {
    List<GiftCertificate> certificates = giftCertificateDao.findByUserId(userId);
    return findTagsForGiftCertificates(certificates);
  }

  @Override
  public GiftCertificateWithTagsDto updatePrice(Long id, BigDecimal price) {
    giftCertificateDao.updatePrice(id, price);
    return doFindById(id, ExceptionType.RESOURCE_NOT_FOUND);
  }
}