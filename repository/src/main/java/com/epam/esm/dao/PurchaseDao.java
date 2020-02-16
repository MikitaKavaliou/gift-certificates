package com.epam.esm.dao;

import com.epam.esm.model.Purchase;
import java.util.List;
import java.util.Optional;

public interface PurchaseDao {

  Long create(Purchase purchase);

  List<Purchase> findPurchasesByUserId(Long userId);

  Optional<Purchase> findBydId(Long id);
}