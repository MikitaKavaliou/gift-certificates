package com.epam.esm.service.impl;

import com.epam.esm.entity.CertificateWithTags;
import com.epam.esm.exception.ExceptionType;
import com.epam.esm.exception.ServerException;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.specification.impl.certificate.CertificateIdSpecification;
import com.epam.esm.repository.specification.impl.certificate.CertificatesCriteriaSpecification;
import com.epam.esm.repository.specification.impl.tag.TagCertificateIdSpecification;
import com.epam.esm.repository.specification.impl.tag.TagNameSpecification;
import com.epam.esm.service.GiftCertificateService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
  public CertificateWithTags findById(Long id) {
    GiftCertificate giftCertificate = findCertificate(id);
    List<Tag> tags = tagRepository.query(new TagCertificateIdSpecification(id));
    return new CertificateWithTags(giftCertificate, tags);
  }

  private GiftCertificate findCertificate(Long id) {
    List<GiftCertificate> certificates = certificateRepository.query(new CertificateIdSpecification(id));
    if (certificates.isEmpty()) {
      throw new ServerException(ExceptionType.RESOURCE_NOT_FOUND);
    }
    return certificates.get(0);
  }

  @Override
  public void delete(Long id) {
    certificateRepository.delete(id);
  }

  @Transactional
  @Override
  public Long create(CertificateWithTags certificateWithTags) {
    GiftCertificate certificate = certificateWithTags.getGiftCertificate();
    List<Tag> tags = certificateWithTags.getTags();
    long certificateId;
    if (tags != null) {
      List<Long> listOfTagsIdForCreation = getTagIdList(tags);
      certificateId = certificateRepository.create(certificate, listOfTagsIdForCreation);
    } else {
      certificateId = certificateRepository.create(certificate);
    }
    return certificateId;
  }

  @Transactional
  @Override
  public Long update(CertificateWithTags certificateWithTags) {
    GiftCertificate certificate = certificateWithTags.getGiftCertificate();
    List<Tag> tags = certificateWithTags.getTags();
    long certificateId;
    if (tags != null) {
      List<Long> listOfTagsIdForUpdate = getTagIdList(tags);
      certificateId = certificateRepository.update(certificate, listOfTagsIdForUpdate);
    } else {
      certificateId = certificateRepository.update(certificate);
    }
    return certificateId;
  }

  @Override
  public List<CertificateWithTags> findCertificatesWithTags(Map<String, String> parameters) {
    List<GiftCertificate> certificates = certificateRepository.query(new CertificatesCriteriaSpecification(parameters));
    List<CertificateWithTags> certificateTags = new ArrayList<>();
    for (GiftCertificate certificate : certificates) {
      List<Tag> tags = tagRepository.query(new TagCertificateIdSpecification(certificate.getId()));
      certificateTags.add(new CertificateWithTags(certificate, tags));
    }
    return certificateTags;
  }

  private List<Long> getTagIdList(List<Tag> tags) {
    List<Long> tagIdList = new ArrayList<>();
    for (Tag tag : tags) {
      searchTagsByName(tag, tagIdList);
    }
    return tagIdList;
  }

  private void searchTagsByName(Tag tag, List<Long> tagIdList) {
    List<Tag> foundTags = tagRepository.query(new TagNameSpecification(tag.getName()));
    if (!foundTags.isEmpty()) {
      addFountTagIdInList(foundTags, tagIdList);
    } else {
      long tagId = tagRepository.create(tag);
      tagIdList.add(tagId);
    }
  }

  private void addFountTagIdInList(List<Tag> foundTags, List<Long> tagIdList) {
    for (Tag foundTag : foundTags) {
      tagIdList.add(foundTag.getId());
    }
  }
}