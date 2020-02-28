package com.epam.esm.mapper;

import com.epam.esm.model.Purchase;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.RowBounds;

@Mapper
public interface PurchaseMapper {

  @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "purchase_id")
  @Insert("INSERT INTO purchase(user_id, gift_certificate_id, cost ) VALUES(#{userId},#{giftCertificateId}, (SELECT "
      + "price FROM gift_certificate WHERE gift_certificate_id=#{giftCertificateId}))")
  void insert(Purchase purchase);

  @Select("SELECT purchase_id, user_id, gift_certificate_id, cost, purchase_date FROM purchase WHERE purchase_id = #{id}")
  @Results({
      @Result(property = "id", column = "purchase_id"),
      @Result(property = "userId", column = "user_id"),
      @Result(property = "giftCertificateId", column = "gift_certificate_id"),
      @Result(property = "cost", column = "cost"),
      @Result(property = "purchaseDate", column = "purchase_date")
  })
  Optional<Purchase> selectById(Long id);

  @Select("SELECT purchase_id, user_id, gift_certificate_id, cost, purchase_date FROM purchase WHERE user_id = "
      + "#{userId}")
  @Results({
      @Result(property = "id", column = "purchase_id"),
      @Result(property = "userId", column = "user_id"),
      @Result(property = "giftCertificateId", column = "gift_certificate_id"),
      @Result(property = "cost", column = "cost"),
      @Result(property = "purchaseDate", column = "purchase_date")
  })
  List<Purchase> selectByUserId(Long userId, RowBounds rowBounds);
}