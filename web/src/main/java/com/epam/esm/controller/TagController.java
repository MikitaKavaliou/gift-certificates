package com.epam.esm.controller;

import com.epam.esm.exception.ExceptionType;
import com.epam.esm.exception.ServerException;
import com.epam.esm.model.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.validation.TagValidator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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

  private final TagService tagService;

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
  @Secured("ROLE_ADMIN")
  @PostMapping()
  public ResponseEntity<Tag> createTag(@RequestBody Tag tag) {
    if (!TagValidator.isValidTag(tag)) {
      throw new ServerException(ExceptionType.INCORRECT_INPUT_DATA);
    }
    return new ResponseEntity<>(tagService.create(tag), HttpStatus.CREATED);
  }

  /**
   * Find by id service response.
   *
   * @param id the id of requested returns resource
   * @return the service response
   */
  @Secured({"ROLE_ADMIN", "ROLE_USER"})
  @GetMapping("/{id}")
  public ResponseEntity<Tag> findTagById(@PathVariable Long id) {
    return new ResponseEntity<>(tagService.findById(id), HttpStatus.OK);
  }

  /**
   * Find all service returns response.
   *
   * @return the service response
   */
  @Secured({"ROLE_ADMIN", "ROLE_USER"})
  @GetMapping()
  public ResponseEntity<List<Tag>> findAllTags() {
    return new ResponseEntity<>(tagService.findAll(), HttpStatus.OK);
  }

  @Secured({"ROLE_ADMIN", "ROLE_USER"})
  @GetMapping("/most_popular")
  public ResponseEntity<Tag> findTheMostPopularTag() {
    return new ResponseEntity<>(tagService.findTheMostPopularTagOfHighestSpendingUser(), HttpStatus.OK);
  }

  /**
   * Delete tag service returns response.
   *
   * @param id the id of deleting entity
   * @return the service response
   */
  @Secured("ROLE_ADMIN")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
    int numberOfDeletedRows = tagService.delete(id);
    return numberOfDeletedRows > 0 ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }
}