package com.example.menumaker.repository;

import com.example.menumaker.model.ShopUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopUserRepository extends JpaRepository<ShopUser, Long> {
}
