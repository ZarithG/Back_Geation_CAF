package com.uptc.authmicroservice.repository;

import com.uptc.authmicroservice.entity.Role;
import com.uptc.authmicroservice.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByRoleName(RoleEnum roleName);
}
