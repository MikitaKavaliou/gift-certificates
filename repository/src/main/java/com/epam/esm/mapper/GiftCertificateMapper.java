package com.epam.esm.mapper;

import com.epam.esm.model.GiftCertificate;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface GiftCertificateMapper {

  @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "gift_certificate_id")
  @Insert("INSERT INTO gift_certificate (name, description, price, duration) VALUES (#{name}, #{description}, " +
      "#{price}, #{duration})")
  void insert(GiftCertificate certificate);

  @Insert("INSERT INTO tag_gift_certificate (tag_id, gift_certificate_id) VALUES (#{tagId}, #{certificateId})")
  void insertAssociativeRecord(Long tagId, Long certificateId);

  @Update({"<script>",
      "UPDATE gift_certificate",
      " <set>",
      "    <if test='name != null'> name=#{name},</if>",
      "    <if test='description != null'>description=#{description},</if>",
      "    <if test='price != null'>price=#{price},</if>",
      "    <if test='duration != null'>duration=#{duration}</if>",
      "  </set>",
      "where gift_certificate_id=#{id}",
      "</script>"})
  void update(GiftCertificate certificate);

  @Update("UPDATE gift_certificate SET price=#{price} WHERE gift_certificate_id=#{id}")
  void updatePrice(Long id, BigDecimal price);

  @Delete("DELETE FROM tag_gift_certificate WHERE tag_id = #{tagId} AND gift_certificate_id = #{certificateId}")
  void deleteAssociativeRecord(Long tagId, Long certificateId);

  @Delete("DELETE FROM gift_certificate WHERE gift_certificate_id = #{certificateId}")
  int delete(Long certificateId);

  @Select("SELECT gift_certificate_id, name, description, price, create_date, last_update_date, duration FROM " +
      "gift_certificate WHERE gift_certificate_id = #{id}")
  @Results({
      @Result(property = "id", column = "gift_certificate_id"),
      @Result(property = "name", column = "name"),
      @Result(property = "description", column = "description"),
      @Result(property = "price", column = "price"),
      @Result(property = "createDate", column = "create_date"),
      @Result(property = "lastUpdateDate", column = "last_update_date"),
      @Result(property = "duration", column = "duration"),
  })
  Optional<GiftCertificate> selectById(Long id);

  @Select({"<script>",
      "SELECT DISTINCT G.gift_certificate_id, g.name, description, price, create_date, last_update_date, duration",
      " <if test=\"tags!=null\">",
      "     , group_concat(T.name order by T.name) as 'tags'",
      " </if>",
      "FROM gift_certificate G",
      " <if test=\"tags!=null\">",
      "     JOIN tag_gift_certificate TG ON G.gift_certificate_id=TG.gift_certificate_id ",
      "     JOIN TAG T ON TG.tag_id=T.tag_id",
      " </if>",
      " <if test=\"values!=null\">",
      "     WHERE ",
      "      <foreach item='item' index='index' collection='values' open='(' separator=' OR ' close=')'>",
      "          G.name LIKE CONCAT('%',#{item},'%') OR G.description LIKE CONCAT('%',#{item},'%')",
      "      </foreach>",
      " </if>",
      " <if test=\"tags!=null\">",
      "     group by G.gift_certificate_id ",
      "     HAVING ",
      "      <foreach item='item' index='index' collection='tags' open='(' separator=' AND ' close=')'>",
      "          tags REGEXP CONCAT('^',#{item},'$|^',#{item},',a*|a*,',#{item},',a*|a*,',#{item},'$')",
      "      </foreach>",
      " </if>",
      " <if test=\"sortField eq 'name' || sortField eq 'create_date' || sortField eq 'last_update_date'\">",
      "     ORDER BY ${sortField}",
      "      <if test=\"sortType eq 'asc' || sortType eq 'desc'\">",
      "          ${sortType}",
      "      </if>",
      " </if>",
      "</script>"})
  @Results({
      @Result(property = "id", column = "gift_certificate_id"),
      @Result(property = "name", column = "name"),
      @Result(property = "description", column = "description"),
      @Result(property = "price", column = "price"),
      @Result(property = "createDate", column = "create_date"),
      @Result(property = "lastUpdateDate", column = "last_update_date"),
      @Result(property = "duration", column = "duration"),
  })
  List<GiftCertificate> findByCriteria(List<String> tags, List<String> values, String sortField, String sortType);
}