package com.example.menumaker.repository;

import com.example.menumaker.model.ShopSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopSectionRepository extends JpaRepository<ShopSection, Long> {
}
