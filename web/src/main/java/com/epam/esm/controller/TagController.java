package com.epam.esm.controller;

import com.epam.esm.exception.ExceptionType;
import com.epam.esm.exception.ServerException;
import com.epam.esm.model.Tag;
import com.epam.esm.response.TagList;
import com.epam.esm.service.TagService;
import com.epam.esm.validation.TagValidator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Tag controller. The class used for processing Tag-related requests.
 */
@RestController
@RequestMapping("/tags")
public class TagController {

  private TagService tagService;

  /**
   * Instantiates a new Tag controller.
   *
   * @param tagService the tag service
   */
  @Autowired
  public TagController(TagService tagService) {
    this.tagService = tagService;
  }

  /**
   * Create tag service returns response.
   *
   * @param tag the tag
   * @return the service response
   */
  @PostMapping()
  public ResponseEntity<Tag> createTag(@RequestBody Tag tag) {
    if (!TagValidator.isValidTag(tag)) {
      throw new ServerException(ExceptionType.INCORRECT_INPUT_DATA);
    }
    long tagId = tagService.create(tag);
    Tag createdTag = tagService.findById(tagId);
    return new ResponseEntity<>(createdTag, HttpStatus.CREATED);
  }

  /**
   * Find by id service response.
   *
   * @param id the id of requested returns resource
   * @return the service response
   */
  @GetMapping("/{id}")
  public ResponseEntity<Tag> findTagById(@PathVariable Long id) {
    Tag tag = tagService.findById(id);
    return new ResponseEntity<>(tag, HttpStatus.OK);
  }

  /**
   * Find all service returns response.
   *
   * @return the service response
   */
  @GetMapping()
  public ResponseEntity<TagList> findAllTags() {
    List<Tag> tags = tagService.findAll();
    return new ResponseEntity<>(new TagList(tags), HttpStatus.OK);
  }

  /**
   * Delete tag service returns response.
   *
   * @param id the id of deleting entity
   * @return the service response
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
    int numberOfDeletedRows = tagService.delete(id);
    return numberOfDeletedRows > 0 ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }
}