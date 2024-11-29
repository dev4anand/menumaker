package com.example.menumaker.repository;

import com.example.menumaker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
//    User findTopByOrderByIdAsc();
Optional<User> findOneByUsername(String username);
    List<User> findAllByRole_Id(Long roleId);
}
