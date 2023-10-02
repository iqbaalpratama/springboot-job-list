package com.springboot.joblist.repository;
import java.util.Optional;

import com.springboot.joblist.entity.Role;
import com.springboot.joblist.entity.utils.EnumRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Integer> {
    boolean existsByName(EnumRole name);
    Optional<Role> findByName(EnumRole name);
}
