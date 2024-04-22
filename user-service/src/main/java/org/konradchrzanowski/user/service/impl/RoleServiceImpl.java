package org.konradchrzanowski.user.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.konradchrzanowski.user.domain.Role;
import org.konradchrzanowski.user.exception.RoleException;
import org.konradchrzanowski.user.mapper.RoleMapper;
import org.konradchrzanowski.user.repository.RoleRepository;
import org.konradchrzanowski.user.service.RoleService;
import org.konradchrzanowski.utils.common.dto.RoleDTO;
import org.konradchrzanowski.utils.common.enumeration.ERole;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
@AllArgsConstructor
@Log4j2
@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public Set<RoleDTO> findAll() {
        log.info("Fetching all roles.");
        List<Role> roleList = roleRepository.findAll();
        return roleMapper.toDto(Set.copyOf(roleList));
    }

    @Override
    public RoleDTO findByName(ERole name) {
        log.info("Fetching role {}", name);
        Optional<Role> role = roleRepository.findByName(name);
        return role.map(roleMapper::toDto)
                .orElseThrow(() -> new RoleException("ErrorRole not found: " + ERole.ROLE_ADMIN.getRoleName()));
    }

    @Override
    public RoleDTO saveRole(RoleDTO roleDTO) {
        log.info("Adding new role {} to database", roleDTO.getName());
        return roleMapper.toDto(roleRepository.save(roleMapper.toEntity(roleDTO)));
    }
}
