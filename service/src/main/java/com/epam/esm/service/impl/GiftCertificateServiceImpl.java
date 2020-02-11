package com.epam.esm.service.impl;

import com.epam.esm.dto.GiftCertificateWithTags;
import com.epam.esm.exception.ExceptionType;
import com.epam.esm.exception.ServerException;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.specification.impl.certificate.CertificateIdSpecification;
import com.epam.esm.repository.specification.impl.certificate.CertificatesCriteriaSpecification;
import com.epam.esm.repository.specification.impl.tag.TagCertificateIdSpecification;
import com.epam.esm.repository.specification.impl.tag.TagIdCertificateIdSpecification;
import com.epam.esm.repository.specification.impl.tag.TagNameSpecification;
import com.epam.esm.service.GiftCertificateService;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
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

  private GiftCertificateRepository certificateRepository;
  private TagRepository tagRepository;

  /**
   * Instantiates a new Gift certificate service.
   *
   * @param certificateRepository the certificate repository
   * @param tagRepository         the tag repository
   */
  @Autowired
  public GiftCertificateServiceImpl(GiftCertificateRepository certificateRepository, TagRepository tagRepository) {
    this.certificateRepository = certificateRepository;
    this.tagRepository = tagRepository;
  }


  @Override
  public GiftCertificateWithTags findById(Long id) {
    GiftCertificate giftCertificate = findCertificate(id);
    List<Tag> tags = tagRepository.query(new TagCertificateIdSpecification(id));
    return new GiftCertificateWithTags(giftCertificate, tags);
  }

  private GiftCertificate findCertificate(Long id) {
    List<GiftCertificate> certificates = certificateRepository.query(new CertificateIdSpecification(id));
    if (certificates.isEmpty()) {
      throw new ServerException(ExceptionType.RESOURCE_NOT_FOUND);
    }
    return certificates.get(0);
  }

  @Override
  public int delete(Long id) {
    return certificateRepository.delete(id);
  }

  @Transactional
  @Override
  public Long create(GiftCertificateWithTags giftCertificateWithTags) {
    GiftCertificate certificate = giftCertificateWithTags.getGiftCertificate();
    List<Tag> tags = giftCertificateWithTags.getTags();
    List<Long> listOfTagId = new ArrayList<>();
    if (tags != null) {
      listOfTagId.addAll(createTagIdSetForCreation(tags));
    }
    return certificateRepository.create(certificate, listOfTagId);
  }

  @Transactional
  @Override
  public Long update(GiftCertificateWithTags giftCertificateWithTags) {
    List<Long> tagIdListForCreation = findTagIdListForAddingToCertificateWhileUpdating(giftCertificateWithTags);
    List<Long> tagIdListForDeletion = findTagIdListForDeletingTagFromCertificateWhileUpdating(giftCertificateWithTags);
    return certificateRepository
        .update(giftCertificateWithTags.getGiftCertificate(), tagIdListForCreation, tagIdListForDeletion);
  }

  private List<Long> findTagIdListForAddingToCertificateWhileUpdating(GiftCertificateWithTags giftCertificateWithTags) {
    List<Tag> tagsForCreation = giftCertificateWithTags.getTags();
    List<Long> tagIdListForCreation = new ArrayList<>();
    if (tagsForCreation != null) {
      Set<Long> setOfTagsIdForUpdate = createTagIdSetForCreation(tagsForCreation);
      removeExistedTagsFromSet(setOfTagsIdForUpdate, giftCertificateWithTags.getId());
      tagIdListForCreation.addAll(setOfTagsIdForUpdate);
    }
    return tagIdListForCreation;
  }


  private List<Long> findTagIdListForDeletingTagFromCertificateWhileUpdating(
      GiftCertificateWithTags giftCertificateWithTags) {
    List<Tag> tagsForDeletion = giftCertificateWithTags.getTagsForDeletion();
    Set<Long> tagIdListForDeletion = new HashSet<>();
    if (tagsForDeletion != null) {
      tagsForDeletion
          .forEach(tag -> tagIdListForDeletion.addAll(tagRepository.query
              (new TagNameSpecification(tag.getName())).stream().map(Tag::getId).collect(Collectors.toSet())));
    }
    return new ArrayList<>(tagIdListForDeletion);
  }

  private Set<Long> createTagIdSetForCreation(List<Tag> tags) {
    Set<Long> tagIdList = new HashSet<>();
    tags.forEach(t -> searchTagsByName(t, tagIdList));
    return tagIdList;
  }

  private void searchTagsByName(Tag tag, Set<Long> tagIdSet) {
    List<Tag> foundTags = tagRepository.query(new TagNameSpecification(tag.getName()));
    if (!foundTags.isEmpty()) {
      tagIdSet.addAll(foundTags.stream().map(Tag::getId).collect(Collectors.toSet()));
    } else {
      tagIdSet.add(tagRepository.create(tag));
    }
  }

  private void removeExistedTagsFromSet(Set<Long> tagIdSet, Long certificateId) {
    Iterator<Long> iterator = tagIdSet.iterator();
    while (iterator.hasNext()) {
      Long tagId = iterator.next();
      List<Tag> tags = tagRepository.query(new TagIdCertificateIdSpecification(certificateId, tagId));
      if (!tags.isEmpty()) {
        iterator.remove();
      }
    }
  }

  @Override
  public List<GiftCertificateWithTags> findCertificatesWithTags(Map<String, String> parameters) {
    List<GiftCertificate> certificates = certificateRepository.query(new CertificatesCriteriaSpecification(parameters));
    List<GiftCertificateWithTags> certificateTags = new ArrayList<>();
    for (GiftCertificate certificate : certificates) {
      List<Tag> tags = tagRepository.query(new TagCertificateIdSpecification(certificate.getId()));
      certificateTags.add(new GiftCertificateWithTags(certificate, tags));
    }
    return certificateTags;
  }
}