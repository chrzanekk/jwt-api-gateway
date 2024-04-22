package org.konradchrzanowski.user.service;

import org.konradchrzanowski.utils.common.dto.RoleDTO;
import org.konradchrzanowski.utils.common.enumeration.ERole;

import java.util.Set;

public interface RoleService {

    Set<RoleDTO> findAll();

    RoleDTO findByName(ERole name);

    RoleDTO saveRole(RoleDTO roleDTO);
}
