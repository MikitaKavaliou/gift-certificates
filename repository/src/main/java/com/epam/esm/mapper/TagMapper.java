package com.epam.esm.mapper;

import com.epam.esm.model.Tag;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.RowBounds;


@Mapper
public interface TagMapper {

  @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "tag_id")
  @Insert("INSERT INTO tag(name) VALUES(#{name})")
  void insert(Tag tag);

  @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "tag_id")
  @Insert({
      "<script>",
      "INSERT INTO tag (name) VALUES ",
      "      <foreach item='item' index='index' collection='tags' separator=','>",
      "        (#{item.name})",
      "      </foreach>",
      "</script>"})
  void insertSet(@Param("tags") Set<Tag> tags);

  @Select("SELECT tag_id, name FROM tag WHERE tag_id = #{id}")
  @Results({
      @Result(property = "id", column = "tag_id"),
      @Result(property = "name", column = "name"),
  })
  Optional<Tag> selectById(Long id);

  @Select("SELECT tag_id, name FROM tag")
  @Results({
      @Result(property = "id", column = "tag_id"),
      @Result(property = "name", column = "name"),
  })
  List<Tag> selectAll(RowBounds rowBounds);

  @Select({
      "<script>",
      "SELECT T.tag_id FROM tag T ",
      "JOIN tag_gift_certificate TG ON T.tag_id=TG.tag_id ",
      "WHERE ",
      "      <foreach item='item' index='index' collection='tagIdSet' open='(' separator=' OR ' close=')'>",
      "         TG.tag_id = #{item} and TG.gift_certificate_id = #{certificateId}",
      "      </foreach>",
      "</script>"})
  List<Long> selectTagIdListByTagIdSetAndCertificateId(@Param("tagIdSet") Set<Long> tagIdSet, Long certificateId);

  @Select("SELECT T.tag_id, T.name FROM tag T JOIN tag_gift_certificate TG ON T.tag_id=TG.tag_id WHERE TG"
      + ".gift_certificate_id = #{certificateId}")
  @Results({
      @Result(property = "id", column = "tag_id"),
      @Result(property = "name", column = "name"),
  })
  List<Tag> selectByCertificateId(Long certificateId);

  @Select("SELECT tag_id, name FROM tag WHERE name = #{name}")
  @Results({
      @Result(property = "id", column = "tag_id"),
      @Result(property = "name", column = "name"),
  })
  Optional<Tag> selectByName(String name);

  @Select({
      "<script>",
      "SELECT DISTINCT tag_id, name FROM tag WHERE ",
      "      <foreach item='item' index='index' collection='names' separator=' OR ' >",
      "          name = #{item} ",
      "      </foreach>",
      "</script>"})
  @Results({
      @Result(property = "id", column = "tag_id"),
      @Result(property = "name", column = "name"),
  })
  List<Tag> selectByNames(@Param("names") List<String> names);

  @Delete("DELETE FROM tag WHERE tag_id = #{tagId}")
  int deleteById(Long tagId);

  @Select("SELECT T.tag_id, T.name FROM user U "
      + "JOIN purchase P ON U.user_id=P.user_id "
      + "JOIN gift_certificate G ON P.gift_certificate_id=G.gift_certificate_id "
      + "JOIN tag_gift_certificate TG ON G.gift_certificate_id=TG.gift_certificate_id "
      + "JOIN tag T ON TG.tag_id=T.tag_id "
      + "WHERE u.user_id = ( "
      + "     SELECT U.user_id FROM user U "
      + "                JOIN purchase P ON U.user_id=P.user_id "
      + "                GROUP BY P.user_id "
      + "                ORDER BY sum(P.cost) DESC "
      + "     LIMIT 1) "
      + "GROUP BY T.tag_id "
      + "ORDER BY count(T.name) DESC "
      + "LIMIT 1")
  @Results({
      @Result(property = "id", column = "tag_id"),
      @Result(property = "name", column = "name"),
  })
  Tag selectMostPopularTagOfHighestSpendingUser();
}