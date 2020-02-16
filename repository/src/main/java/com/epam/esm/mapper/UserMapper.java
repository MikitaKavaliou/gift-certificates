package com.epam.esm.mapper;

import com.epam.esm.model.User;
import java.util.Optional;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

public interface UserMapper {

  @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "user_id")
  @Insert("INSERT INTO user(username, password, role) VALUES(#{username},#{password},#{role})")
  void insert(User user);

  @Select("SELECT user_id, username, password, role FROM user WHERE user_id = #{id}")
  @Results({
      @Result(property = "id", column = "user_id"),
      @Result(property = "username", column = "username"),
      @Result(property = "password", column = "password"),
      @Result(property = "role", column = "role"),
  })
  Optional<User> selectById(Long id);

  @Select("SELECT user_id, username, password, role FROM user WHERE username = #{username}")
  @Results({
      @Result(property = "id", column = "user_id"),
      @Result(property = "username", column = "username"),
      @Result(property = "password", column = "password"),
      @Result(property = "role", column = "role"),
  })
  Optional<User> selectByUsername(String username);
}