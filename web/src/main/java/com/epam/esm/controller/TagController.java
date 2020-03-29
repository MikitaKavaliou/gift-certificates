package com.epam.esm.controller;

import com.epam.esm.dto.EntityListDto;
import com.epam.esm.exception.ExceptionType;
import com.epam.esm.exception.ServerException;
import com.epam.esm.model.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.validation.TagValidator;
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
@RequestMapping(value = "/api/tags", produces = MediaType.APPLICATION_JSON_VALUE)
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
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
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
  @GetMapping(value = "/{id}")
  public ResponseEntity<Tag> findTagById(@PathVariable Long id) {
    return ResponseEntity.status(HttpStatus.OK.value()).body(tagService.findById(id));
  }

  /**
   * Find all service returns response.
   *
   * @param parameters the parameters
   * @return the service response
   */
  @Secured({"ROLE_ADMIN", "ROLE_USER"})
  @GetMapping()
  public ResponseEntity<EntityListDto<Tag>> findAllTags(@RequestParam Map<String, String> parameters) {
    return ResponseEntity.status(HttpStatus.OK.value()).body(tagService.findAll(parameters));
  }

  /**
   * Find the most popular tag response entity.
   *
   * @return the response entity
   */
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
  @DeleteMapping(value = "/{id}")
  public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
    int numberOfDeletedRows = tagService.delete(id);
    if (numberOfDeletedRows < 1) {
      throw new ServerException(ExceptionType.RESOURCE_NOT_FOUND);
    }
    return ResponseEntity.status(HttpStatus.OK).build();
  }
}