package com.uptc.authmicroservice.repository;

import com.uptc.authmicroservice.entity.AuthUser;
import com.uptc.authmicroservice.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AuthUserRepository extends JpaRepository<AuthUser, Integer> {
    Optional<AuthUser> findByUserName(String userName);

    @Query("SELECT u FROM AuthUser u JOIN u.roles r WHERE r.roleName = :roleName")
    List<AuthUser> findByRoleName(@Param("roleName") RoleEnum roleName);
}
