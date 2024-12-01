package com.mumuca.diet.repository;

import com.mumuca.diet.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolesRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByAuthority(String authority);
}
