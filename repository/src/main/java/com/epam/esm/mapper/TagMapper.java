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

  @Select("SELECT t.tag_id, t.name FROM tag T WHERE t.tag_id = ("
      + "       SELECT tg.tag_id FROM purchase p "
      + "       join gift_certificate g ON p.gift_certificate_id=g.gift_certificate_id "
      + "       join tag_gift_certificate tg ON g.gift_certificate_id=tg.gift_certificate_id "
      + "       WHERE p.user_id = ("
      + "                   SELECT p.user_id FROM purchase p "
      + "                   group by p.user_id "
      + "                   order by sum(p.cost) desc "
      + "                   LIMIT 1) "
      + "       group by tg.tag_id "
      + "       order by count(tg.tag_id) desc "
      + "       LIMIT 1)")
  @Results({
      @Result(property = "id", column = "tag_id"),
      @Result(property = "name", column = "name"),
  })
  Tag selectMostPopularTagOfHighestSpendingUser();
}