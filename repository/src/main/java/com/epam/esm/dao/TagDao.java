package com.epam.esm.dao;

import com.epam.esm.model.Tag;
import java.util.List;
import java.util.Optional;

public interface TagDao extends GenericDao<Tag, Long> {

  Long create(Tag tag);

  List<Tag> findAll();

  List<Tag> findByCertificateId(Long certificateId);

  Optional<Long> findTagIdByTagIdAndCertificateId(Long tagId, Long certificateId);

  Optional<Tag> findByName(String name);
}