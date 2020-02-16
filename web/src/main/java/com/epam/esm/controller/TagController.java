package com.epam.esm.controller;

import com.epam.esm.exception.ExceptionType;
import com.epam.esm.exception.ServerException;
import com.epam.esm.model.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.validation.TagValidator;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Tag controller. The class used for processing Tag-related requests.
 */
@RestController
@RequestMapping(value = "/tags", consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
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
    return ResponseEntity.status(HttpStatus.CREATED.value()).body(tagService.create(tag));
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
    return ResponseEntity.status(HttpStatus.OK.value()).body(tagService.findById(id));
  }

  /**
   * Find all service returns response.
   *
   * @return the service response
   */
  @Secured({"ROLE_ADMIN", "ROLE_USER"})
  @GetMapping()
  public ResponseEntity<List<Tag>> findAllTags(@RequestParam Map<String, String> parameters) {
    return ResponseEntity.status(HttpStatus.OK.value()).body(tagService.findAll(parameters));
  }

  @Secured({"ROLE_ADMIN", "ROLE_USER"})
  @GetMapping(params = "mostPopular")
  public ResponseEntity<Tag> findTheMostPopularTag() {
    return ResponseEntity.status(HttpStatus.OK.value()).body(tagService.findTheMostPopularTagOfHighestSpendingUser());
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