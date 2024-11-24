package com.example.menumaker.repository;

import com.example.menumaker.model.ShopCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopCategoryRepository extends JpaRepository<ShopCategory, Long> {
}
