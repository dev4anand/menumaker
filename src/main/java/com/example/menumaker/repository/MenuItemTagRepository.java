package com.example.menumaker.repository;

import com.example.menumaker.model.MenuItemTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuItemTagRepository extends JpaRepository<MenuItemTag, Long> {
}
