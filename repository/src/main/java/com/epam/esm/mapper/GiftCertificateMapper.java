package com.epam.esm.mapper;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.session.RowBounds;

@Mapper
public interface GiftCertificateMapper {

  @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "gift_certificate_id")
  @Insert("INSERT INTO gift_certificate (name, description, price, duration) VALUES (#{name}, #{description}, " +
      "#{price}, #{duration})")
  void insert(GiftCertificate certificate);

  @Insert({
      "<script>",
      "INSERT INTO tag_gift_certificate (tag_id, gift_certificate_id) VALUES",
      "      <foreach item='item' index='index' collection='tagIdList' separator=','>",
      "        (#{item}, #{certificateId})",
      "      </foreach>",
      "</script>"})
  void insertAssociativeRecords(Set<Long> tagIdList, Long certificateId);

  @Update({
      "<script>",
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

  @Delete({
      "<script>",
      "DELETE TG FROM tag_gift_certificate TG ",
      "JOIN tag T ON T.tag_id=TG.tag_id ",
      "WHERE ",
      "<foreach item='item' index='index' collection='tags'  open='(' separator=' OR ' close=')'>",
      "(T.name = #{item.name} AND gift_certificate_id = #{certificateId})",
      "</foreach>",
      "</script>"})
  void deleteAssociativeRecords(List<Tag> tags, Long certificateId);

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

  @Select({
      "<script>",
      "SELECT DISTINCT G.gift_certificate_id,G.name,G.description,G.price,G.create_date,G.last_update_date,G.duration",
      " <if test=\"tags!=null\">",
      "     , group_concat(T.name order by T.name) as 'tags'",
      " </if>",
      "FROM gift_certificate G",
      " <if test=\"tags!=null\">",
      "     JOIN tag_gift_certificate TG ON G.gift_certificate_id=TG.gift_certificate_id ",
      "     JOIN TAG T ON TG.tag_id=T.tag_id",
      " </if>",
      "<where>",
      "      <if test=\"parameters.searchValue!=null\">",
      "         G.name LIKE CONCAT('%', #{parameters.searchValue},'%')",
      "         OR G.description LIKE CONCAT('%', #{parameters.searchValue},'%')",
      "      </if>",
      "      <if test=\"parameters.minPrice!=null\">",
      "         AND G.price &gt;= #{parameters.minPrice} ",
      "      </if>",
      "      <if test=\"parameters.maxPrice!=null\">",
      "         AND G.price &lt;= #{parameters.maxPrice} ",
      "      </if>",
      "</where>",
      " <if test=\"tags!=null\">",
      "     group by G.gift_certificate_id ",
      "     HAVING ",
      "      <foreach item='item' index='index' collection='tags' open='(' separator=' AND ' close=')'>",
      "          tags REGEXP CONCAT('^',#{item},'$|^',#{item},',a*|a*,',#{item},',a*|a*,',#{item},'$')",
      "      </foreach>",
      " </if>",
      "<choose>",
      " <when test=\"parameters.sortField eq 'name' || parameters.sortField eq 'create_date' || parameters.sortField "
          + "eq 'last_update_date'\">",
      "     ORDER BY G.${parameters.sortField}",
      "      <if test=\"parameters.sortType eq 'asc' || parameters.sortType eq 'desc'\">",
      "          ${parameters.sortType}",
      "      </if>",
      " </when>",
      " <otherwise>",
      "     ORDER BY G.create_date DESC",
      " </otherwise>",
      "</choose>",
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
  List<GiftCertificate> selectByCriteria(List<String> tags, Map<String, String> parameters, RowBounds rowBounds);

  @Select({
      "<script>",
      "SELECT COUNT(*) FROM ",
      "(",
      "SELECT DISTINCT G.gift_certificate_id,G.name,G.description,G.price,G.create_date,G.last_update_date,G.duration",
      " <if test=\"tags!=null\">",
      "     , group_concat(T.name order by T.name) as 'tags'",
      " </if>",
      "FROM gift_certificate G",
      " <if test=\"tags!=null\">",
      "     JOIN tag_gift_certificate TG ON G.gift_certificate_id=TG.gift_certificate_id ",
      "     JOIN TAG T ON TG.tag_id=T.tag_id",
      " </if>",
      "<where>",
      "      <if test=\"parameters.searchValue!=null\">",
      "         G.name LIKE CONCAT('%', #{parameters.searchValue},'%')",
      "         OR G.description LIKE CONCAT('%', #{parameters.searchValue},'%')",
      "      </if>",
      "      <if test=\"parameters.minPrice!=null\">",
      "         AND G.price &gt;= #{parameters.minPrice} ",
      "      </if>",
      "      <if test=\"parameters.maxPrice!=null\">",
      "         AND G.price &lt;= #{parameters.maxPrice} ",
      "      </if>",
      "</where>",
      " <if test=\"tags!=null\">",
      "     group by G.gift_certificate_id ",
      "     HAVING ",
      "      <foreach item='item' index='index' collection='tags' open='(' separator=' AND ' close=')'>",
      "          tags REGEXP CONCAT('^',#{item},'$|^',#{item},',a*|a*,',#{item},',a*|a*,',#{item},'$')",
      "      </foreach>",
      " </if>",
      " <if test=\"parameters.sortField eq 'name' || parameters.sortField eq 'create_date' || parameters.sortField eq 'last_update_date'\">",
      "     ORDER BY G.${parameters.sortField}",
      "      <if test=\"parameters.sortType eq 'asc' || parameters.sortType eq 'desc'\">",
      "          ${parameters.sortType}",
      "      </if>",
      " </if>",
      ") AS subquery",
      "</script>"})
  int getCountOfSuitableRecordsOfFindByCriteria(List<String> tags, Map<String, String> parameters);

  @Select("SELECT G.gift_certificate_id, G.name, G.description, G.price, G.create_date, G.last_update_date, "
      + "G.duration FROM gift_certificate G JOIN purchase p ON g.gift_certificate_id=p.gift_certificate_id "
      + "JOIN user u ON p.user_id=u.user_id WHERE u.user_id=#{userId}")
  @Results({
      @Result(property = "id", column = "gift_certificate_id"),
      @Result(property = "name", column = "name"),
      @Result(property = "description", column = "description"),
      @Result(property = "price", column = "price"),
      @Result(property = "createDate", column = "create_date"),
      @Result(property = "lastUpdateDate", column = "last_update_date"),
      @Result(property = "duration", column = "duration"),
  })
  List<GiftCertificate> selectByUserId(Long userId, RowBounds rowBounds);
}