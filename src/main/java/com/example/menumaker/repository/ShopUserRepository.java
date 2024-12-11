package com.example.menumaker.repository;

import com.example.menumaker.model.ShopUser;
import com.example.menumaker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShopUserRepository extends JpaRepository<ShopUser, Long> {
    Optional<ShopUser> findOneById(Long id);
    Optional<ShopUser> findOneByUser(User user);
    Optional<ShopUser> findOneByGid(UUID gid);
}
