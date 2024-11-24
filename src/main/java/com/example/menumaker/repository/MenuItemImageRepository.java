package com.example.menumaker.repository;

import com.example.menumaker.model.MenuItemImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuItemImageRepository extends JpaRepository<MenuItemImage, Long> {
}
