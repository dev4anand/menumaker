package com.example.menumaker.repository;

import com.example.menumaker.model.ShopSectionMenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopSectionMenuItemRepository extends JpaRepository<ShopSectionMenuItem, Long> {
}
