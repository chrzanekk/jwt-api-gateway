package org.konradchrzanowski.user.repository;

import org.konradchrzanowski.user.domain.Role;
import org.konradchrzanowski.utils.common.enumeration.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
