package com.example.menumaker.repository;

import com.example.menumaker.model.MenuItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuItemCategoryRepository extends JpaRepository<MenuItemCategory, Long> {
}
