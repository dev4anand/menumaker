package com.example.menumaker.repository;

import com.example.menumaker.model.MasterUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MasterUserRoleRepository extends JpaRepository<MasterUserRole, Long> {
}