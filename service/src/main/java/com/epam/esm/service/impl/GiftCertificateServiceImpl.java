package com.epam.esm.service.impl;

import com.epam.esm.dto.EntityListDto;
import com.epam.esm.dto.GiftCertificateUpdateDto;
import com.epam.esm.dto.GiftCertificateWithTagsDto;
import com.epam.esm.exception.ExceptionType;
import com.epam.esm.exception.ServerException;
import com.epam.esm.mapper.GiftCertificateMapper;
import com.epam.esm.mapper.TagMapper;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.util.PaginationUtil;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The type Gift certificate service.
 */
@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

  private final GiftCertificateMapper giftCertificateMapper;
  private final TagMapper tagMapper;

  /**
   * Instantiates a new Gift certificate service.
   *
   * @param giftCertificateMapper the gift certificate mapper
   * @param tagMapper             the tag mapper
   */
  @Autowired
  public GiftCertificateServiceImpl(GiftCertificateMapper giftCertificateMapper, TagMapper tagMapper) {
    this.giftCertificateMapper = giftCertificateMapper;
    this.tagMapper = tagMapper;
  }

  @Override
  public GiftCertificateWithTagsDto findById(Long id) {
    return doFindById(id, ExceptionType.RESOURCE_NOT_FOUND);
  }

  private GiftCertificateWithTagsDto doFindById(Long id, ExceptionType exceptionType) {
    GiftCertificate giftCertificate = giftCertificateMapper.selectById(id)
        .orElseThrow(() -> new ServerException(exceptionType));
    List<Tag> tags = tagMapper.selectByCertificateId(id);
    return new GiftCertificateWithTagsDto(giftCertificate, tags);
  }

  @Override
  public int delete(Long id) {
    return giftCertificateMapper.delete(id);
  }

  @Transactional
  @Override
  public GiftCertificateWithTagsDto create(GiftCertificateWithTagsDto giftCertificateWithTagsDto) {
    Long certificateId = insertCertificate(giftCertificateWithTagsDto);
    addTagsToCertificate(giftCertificateWithTagsDto, certificateId);
    return doFindById(certificateId, ExceptionType.ERROR_CREATING_ENTITY);
  }

  private Long insertCertificate(GiftCertificateWithTagsDto giftCertificateWithTagsDto) {
    GiftCertificate giftCertificate = giftCertificateWithTagsDto.getGiftCertificate();
    giftCertificateMapper.insert(giftCertificate);
    return giftCertificate.getId();
  }

  private void addTagsToCertificate(GiftCertificateWithTagsDto giftCertificateWithTagsDto, Long giftCertificateId) {
    List<Tag> tags = giftCertificateWithTagsDto.getTags();
    if (tags != null && !tags.isEmpty()) {
      giftCertificateMapper.insertAssociativeRecords(createTagIdSetForReceivedTags(tags), giftCertificateId);
    }
  }

  @Transactional
  @Override
  public GiftCertificateWithTagsDto update(Long id, GiftCertificateUpdateDto giftCertificateUpdateDto) {
    try {
      updateCertificateFields(id, giftCertificateUpdateDto.getGiftCertificate());
      updateCertificateTags(id, giftCertificateUpdateDto.getTagsForAdding(),
          giftCertificateUpdateDto.getTagsForDeletion());
      return doFindById(id, ExceptionType.RESOURCE_NOT_FOUND);
    } catch (DataIntegrityViolationException e) {
      throw new ServerException(ExceptionType.RESOURCE_NOT_FOUND);
    }
  }

  private void updateCertificateFields(Long id, GiftCertificate giftCertificate) {
    giftCertificate.setId(id);
    if (hasFieldsForUpdate(giftCertificate)) {
      giftCertificateMapper.update(giftCertificate);
    }
  }

  private void updateCertificateTags(Long id, List<Tag> tagsForAdding, List<Tag> tagsForDeletion) {
    Set<Long> tagIdSet;
    if (tagsForDeletion != null && !tagsForDeletion.isEmpty()) {
      giftCertificateMapper.deleteAssociativeRecords(tagsForDeletion, id);
    }
    if (tagsForAdding != null && !tagsForAdding.isEmpty() &&
        !(tagIdSet = removeAlreadyAddedTagIds(createTagIdSetForReceivedTags(tagsForAdding), id)).isEmpty()) {
      giftCertificateMapper.insertAssociativeRecords(tagIdSet, id);
    }
  }

  private boolean hasFieldsForUpdate(GiftCertificate giftCertificate) {
    return giftCertificate.getName() != null || giftCertificate.getDescription() != null ||
        giftCertificate.getPrice() != null || giftCertificate.getDuration() != null;
  }

  private Set<Long> createTagIdSetForReceivedTags(List<Tag> tags) {
    Set<Tag> existingTags =
        new HashSet<>(tagMapper.selectByNames(tags.stream().map(Tag::getName).collect(Collectors.toList())));
    Set<Tag> newTags =
        createNewTags(tags.stream().filter(t -> !containsTagWithTagName(existingTags, t)).collect(Collectors.toSet()));
    return Stream.concat(existingTags.stream().map(Tag::getId), newTags.stream().map(Tag::getId))
        .collect(Collectors.toSet());
  }

  private Set<Tag> createNewTags(Set<Tag> tags) {
    if (!tags.isEmpty()) {
      tagMapper.insertSet(tags);
    }
    return tags;
  }

  private boolean containsTagWithTagName(Set<Tag> tags, Tag tag) {
    return tags.stream().map(Tag::getName).collect(Collectors.toSet()).contains(tag.getName());
  }

  private Set<Long> removeAlreadyAddedTagIds(Set<Long> tagIdSet, Long certificateId) {
    tagIdSet.removeAll(tagMapper.selectTagIdListByTagIdSetAndCertificateId(tagIdSet, certificateId));
    return tagIdSet;
  }

  @Override
  public EntityListDto<GiftCertificateWithTagsDto> findByCriteria(Map<String, String> parameters, List<String> tags) {
    RowBounds rowBounds = PaginationUtil.createRowBounds(parameters);
    validatePriceValues(parameters);
    List<GiftCertificate> certificates =
        giftCertificateMapper.findByCriteria(tags, parameters, rowBounds);
    return new EntityListDto<>(findTagsForGiftCertificates(certificates),
        PaginationUtil.calculatePagesCount(giftCertificateMapper.getCountOfSuitableRecordsOfFindByCriteria(tags,
            parameters), rowBounds.getLimit()));
  }

  private void validatePriceValues(Map<String, String> parameters) {
    parameters.put("minPrice", getCorrectPriceValue(parameters.get("minPrice")));
    parameters.put("maxPrice", getCorrectPriceValue(parameters.get("maxPrice")));
  }

  private String getCorrectPriceValue(String priceValue) {
    try {
      Double.parseDouble(priceValue);
      return priceValue;
    } catch (NumberFormatException | NullPointerException e) {
      return null;
    }
  }

  private List<GiftCertificateWithTagsDto> findTagsForGiftCertificates(List<GiftCertificate> giftCertificates) {
    return giftCertificates.stream()
        .map(g -> new GiftCertificateWithTagsDto(g, tagMapper.selectByCertificateId(g.getId())))
        .collect(Collectors.toList());
  }

  @Override
  public EntityListDto<GiftCertificateWithTagsDto> findByUserId(Long userId, Map<String, String> parameters) {
    List<GiftCertificate> certificates = giftCertificateMapper
        .selectByUserId(userId, PaginationUtil.createRowBounds(parameters));
    return new EntityListDto<>(findTagsForGiftCertificates(certificates));
  }

  @Override
  public GiftCertificateWithTagsDto updatePrice(Long id, BigDecimal price) {
    giftCertificateMapper.updatePrice(id, price);
    return doFindById(id, ExceptionType.RESOURCE_NOT_FOUND);
  }
}