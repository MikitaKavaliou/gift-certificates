package com.epam.esm.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileMapper {

  @Insert("INSERT INTO processing_file(processing_file_path) VALUES(#{path})")
  void insert(String path);

  @Delete("DELETE FROM processing_file WHERE processing_file_path = #{path}")
  void deleteByPath(String path);
}