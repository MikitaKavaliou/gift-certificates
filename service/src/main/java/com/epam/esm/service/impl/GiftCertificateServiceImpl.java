package com.epam.esm.service.impl;

import com.epam.esm.dto.GiftCertificateWithTagsDto;
import com.epam.esm.exception.ExceptionType;
import com.epam.esm.exception.ServerException;
import com.epam.esm.mapper.GiftCertificateMapper;
import com.epam.esm.mapper.TagMapper;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.util.PaginationTool;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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

  private final GiftCertificateMapper giftCertificateMapper;
  private final TagMapper tagMapper;

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
    return doFindById(createGiftCertificate(giftCertificateWithTagsDto), ExceptionType.ERROR_CREATING_ENTITY);
  }

  private Long createGiftCertificate(GiftCertificateWithTagsDto giftCertificateWithTagsDto) {
    List<Long> listOfTagId = new ArrayList<>();
    if (giftCertificateWithTagsDto.getTags() != null) {
      listOfTagId.addAll(createTagIdSetForAddingToCertificate(giftCertificateWithTagsDto.getTags()));
    }
    GiftCertificate giftCertificate = giftCertificateWithTagsDto.getGiftCertificate();
    giftCertificateMapper.insert(giftCertificate);
    giftCertificateMapper.insertAssociativeRecords(listOfTagId, giftCertificate.getId());
    return giftCertificate.getId();
  }

  @Transactional
  @Override
  public GiftCertificateWithTagsDto update(GiftCertificateWithTagsDto giftCertificateWithTagsDto, String tagAction) {
    updateCertificateFields(giftCertificateWithTagsDto.getGiftCertificate());
    updateCertificateTags(giftCertificateWithTagsDto, tagAction);
    return doFindById(giftCertificateWithTagsDto.getId(), ExceptionType.RESOURCE_NOT_FOUND);
  }

  private void updateCertificateFields(GiftCertificate giftCertificate) {
    if (hasFieldsForCertificateUpdate(giftCertificate)) {
      giftCertificateMapper.update(giftCertificate);
    }
  }

  private void updateCertificateTags(GiftCertificateWithTagsDto certificateWithTags, String tagAction) {
    if (certificateWithTags.getTags() != null && !certificateWithTags.getTags().isEmpty()
        && (tagAction.equalsIgnoreCase("add") || (tagAction.equalsIgnoreCase("delete")))) {
      List<Long> foundTagIdList;
      if (tagAction.equalsIgnoreCase("add")
          && !(foundTagIdList = findTagIdListForAddingToCertificate(certificateWithTags)).isEmpty()) {
        giftCertificateMapper.insertAssociativeRecords(foundTagIdList, certificateWithTags.getId());
      } else if (tagAction.equalsIgnoreCase("delete")
          && !(foundTagIdList = findTagIdListForDeletingFromCertificate(certificateWithTags)).isEmpty()) {
        giftCertificateMapper.deleteAssociativeRecords(foundTagIdList, certificateWithTags.getId());
      }
    }
  }

  private List<Long> findTagIdListForAddingToCertificate(GiftCertificateWithTagsDto giftCertificateWithTagsDto) {
    return new ArrayList<>
        (removeExistedCertificateTagsFromSet(createTagIdSetForAddingToCertificate(giftCertificateWithTagsDto.getTags()),
            giftCertificateWithTagsDto.getId()));
  }

  private List<Long> findTagIdListForDeletingFromCertificate(GiftCertificateWithTagsDto giftCertificateWithTagsDto) {
    return giftCertificateWithTagsDto.getTags().stream().filter(t -> tagMapper.selectByName(t.getName()).isPresent())
        .map(Tag::getId).collect(Collectors.toList());
  }

  private boolean hasFieldsForCertificateUpdate(GiftCertificate giftCertificate) {
    return giftCertificate.getName() != null || giftCertificate.getDescription() != null ||
        giftCertificate.getPrice() != null || giftCertificate.getDuration() != null;
  }

  private Set<Long> createTagIdSetForAddingToCertificate(List<Tag> tags) {
    return tags.stream()
        .peek(tag -> tagMapper.selectByName(tag.getName()).ifPresentOrElse(foundTag -> tag.setId(foundTag.getId()),
            () -> tag.setId(insertTag(tag)))).map(Tag::getId).collect(Collectors.toSet());
  }

  private Long insertTag(Tag tag) {
    tagMapper.insert(tag);
    return tag.getId();
  }

  private Set<Long> removeExistedCertificateTagsFromSet(Set<Long> tagIdSet, Long certificateId) {
    return tagIdSet.stream().
        filter(id -> tagMapper.selectTagIdByTagIdAndCertificateId(id, certificateId).isEmpty())
        .collect(Collectors.toSet());
  }

  @Override
  public List<GiftCertificateWithTagsDto> findByCriteria(Map<String, String> parameters) {
    List<GiftCertificate> certificates = giftCertificateMapper.findByCriteria(getListOfParameters(parameters, "tag"),
        getListOfParameters(parameters, "searchValue"),
        getParameter(parameters, "sortField"),
        getParameter(parameters, "sortType"), PaginationTool.createRowBounds(parameters));
    return findTagsForGiftCertificates(certificates);
  }

  private List<String> getListOfParameters(Map<String, String> parameters, String parameterName) {
    return parameters.containsKey(parameterName) ? Arrays.asList(parameters.get(parameterName).split(" ")) : null;
  }

  private String getParameter(Map<String, String> parameters, String parameterName) {
    return parameters.containsKey(parameterName) ? parameters.get(parameterName).toLowerCase() : null;
  }

  private List<GiftCertificateWithTagsDto> findTagsForGiftCertificates(List<GiftCertificate> giftCertificates) {
    return giftCertificates.stream()
        .map(g -> new GiftCertificateWithTagsDto(g, tagMapper.selectByCertificateId(g.getId())))
        .collect(Collectors.toList());
  }

  @Override
  public List<GiftCertificateWithTagsDto> findByUserId(Long userId, Map<String, String> parameters) {
    List<GiftCertificate> certificates = giftCertificateMapper.selectByUserId(userId,
        PaginationTool.createRowBounds(parameters));
    return findTagsForGiftCertificates(certificates);
  }

  @Override
  public GiftCertificateWithTagsDto updatePrice(Long id, BigDecimal price) {
    giftCertificateMapper.updatePrice(id, price);
    return doFindById(id, ExceptionType.RESOURCE_NOT_FOUND);
  }
}