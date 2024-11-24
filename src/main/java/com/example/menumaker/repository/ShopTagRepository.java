package com.example.menumaker.repository;

import com.example.menumaker.model.ShopTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopTagRepository extends JpaRepository<ShopTag, Long> {
}
