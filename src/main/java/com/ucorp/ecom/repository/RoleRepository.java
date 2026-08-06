package com.ucorp.ecom.repository;

import com.ucorp.ecom.model.AppRole;
import com.ucorp.ecom.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleName(AppRole appRole);
}
