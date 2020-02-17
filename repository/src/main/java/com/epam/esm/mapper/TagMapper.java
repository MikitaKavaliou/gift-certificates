package com.epam.esm.mapper;

import com.epam.esm.model.Tag;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.RowBounds;


@Mapper
public interface TagMapper {

  @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "tag_id")
  @Insert("INSERT INTO tag(name) VALUES(#{name})")
  void insert(Tag tag);


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

  @Select("SELECT T.tag_id FROM tag T"
      + " JOIN tag_gift_certificate TG ON T.tag_id=TG.tag_id WHERE TG.tag_id = #{tagId} and TG.gift_certificate_id = "
      + "#{certificateId}")
  Optional<Long> selectTagIdByTagIdAndCertificateId(Long tagId, Long certificateId);

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

  @Delete("DELETE FROM tag WHERE tag_id = #{tagId}")
  int deleteById(Long tagId);

  @Select("SELECT t.tag_id, t.name FROM user u "
      + "join purchase p ON u.user_id=p.user_id "
      + "join gift_certificate g ON p.gift_certificate_id=g.gift_certificate_id "
      + "join tag_gift_certificate tg ON g.gift_certificate_id=tg.gift_certificate_id "
      + "join tag t ON tg.tag_id=t.tag_id "
      + "WHERE u.user_id = ( "
      + "     SELECT u.user_id FROM user u "
      + "                JOIN purchase p ON u.user_id=p.user_id "
      + "                group by p.user_id "
      + "                order by sum(p.cost) desc "
      + "     LIMIT 1) "
      + "group by t.tag_id "
      + "order by count(t.name) desc "
      + "LIMIT 1")
  @Results({
      @Result(property = "id", column = "tag_id"),
      @Result(property = "name", column = "name"),
  })
  Tag selectMostPopularTagOfHighestSpendingUser();
}