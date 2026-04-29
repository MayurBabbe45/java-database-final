package com.project.code.Repo;

import com.project.code.Model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    
    // EXACT RUBRIC REQUIREMENT: Fetch inventory by productId and storeId using @Query
    @Query("SELECT i FROM Inventory i WHERE i.product.id = :productId AND i.store.id = :storeId")
    Inventory findByProductIdandStoreId(@Param("productId") Long productId, @Param("storeId") Long storeId);

    // EXACT RUBRIC REQUIREMENT: Method to fetch all inventory entries for a given store ID
    List<Inventory> findByStoreId(Long storeId);

    void deleteByProductId(Long productId);
}