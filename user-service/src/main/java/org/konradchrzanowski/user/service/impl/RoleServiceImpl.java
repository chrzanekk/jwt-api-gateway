package org.konradchrzanowski.user.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.konradchrzanowski.user.domain.Role;
import org.konradchrzanowski.user.exception.RoleException;
import org.konradchrzanowski.user.mapper.RoleMapper;
import org.konradchrzanowski.user.repository.RoleRepository;
import org.konradchrzanowski.user.service.RoleService;
import org.konradchrzanowski.utils.common.dto.RoleDTO;
import org.konradchrzanowski.utils.common.enumeration.ERole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private final Logger log = LoggerFactory.getLogger(RoleServiceImpl.class);

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleServiceImpl(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    @Override
    public Set<RoleDTO> findAll() {
        log.debug("Fetching all roles.");
        List<Role> roleList = roleRepository.findAll();
        return roleMapper.toDto(Set.copyOf(roleList));
    }

    @Override
    public RoleDTO findByName(ERole name) {
        log.debug("Fetching role {}", name);
        Optional<Role> role = roleRepository.findByName(name);
        return role.map(roleMapper::toDto)
                .orElseThrow(() -> new RoleException("ErrorRole not found: " + ERole.ROLE_ADMIN.getRoleName()));
    }

    @Override
    public RoleDTO saveRole(RoleDTO roleDTO) {
        log.debug("Adding new role {} to database", roleDTO.getName());
        return roleMapper.toDto(roleRepository.save(roleMapper.toEntity(roleDTO)));
    }
}
