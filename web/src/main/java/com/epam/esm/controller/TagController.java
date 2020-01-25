package com.epam.esm.controller;

import com.epam.esm.builder.ServiceResponseBuilder;
import com.epam.esm.entity.ServiceResponse;
import com.epam.esm.model.Tag;
import com.epam.esm.service.TagService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
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
   * Find by id service response.
   *
   * @param id the id of requested returns resource
   * @return the service response
   */
  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/{id}")
  public @ResponseBody
  ServiceResponse<Tag> findTagById(@PathVariable Long id) {
    Tag tag = tagService.findById(id);
    return new ServiceResponseBuilder<Tag>()
        .status(HttpStatus.OK.toString())
        .data(tag)
        .build();
  }

  /**
   * Find all service returns response.
   *
   * @return the service response
   */
  @ResponseStatus(HttpStatus.OK)
  @GetMapping()
  public @ResponseBody
  ServiceResponse<List<Tag>> findAllTags() {
    List<Tag> tags = tagService.findAll();
    return new ServiceResponseBuilder<List<Tag>>()
        .status(HttpStatus.OK.toString())
        .data(tags)
        .build();
  }

  /**
   * Create tag service returns response.
   *
   * @param tag      the tag
   * @param response the response
   * @param request  the request
   * @return the service response
   */
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping()
  public @ResponseBody
  ServiceResponse<String> createTag(@RequestBody Tag tag, HttpServletResponse response, HttpServletRequest request) {
    long tagId = tagService.create(tag);
    response.setHeader("Location", request.getRequestURL().toString() + "/" + tagId);
    return new ServiceResponseBuilder<String>()
        .status(HttpStatus.CREATED.toString())
        .build();
  }

  /**
   * Delete tag service returns response.
   *
   * @param id the id of deleting entity
   * @return the service response
   */
  @ResponseStatus(HttpStatus.OK)
  @DeleteMapping("/{id}")
  public @ResponseBody
  ServiceResponse<String> deleteTag(@PathVariable Long id) {
    tagService.delete(id);
    return new ServiceResponseBuilder<String>()
        .status(HttpStatus.OK.toString())
        .build();
  }
}