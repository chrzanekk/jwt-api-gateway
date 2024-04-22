package org.konradchrzanowski.user.mapper;

import org.konradchrzanowski.user.domain.Role;
import org.konradchrzanowski.utils.common.dto.RoleDTO;
import org.konradchrzanowski.utils.mapper.EntityMapper;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface RoleMapper extends EntityMapper<RoleDTO, Role> {

    default Role fromId(Long id) {
        if (id == null) {
            return null;
        }
        Role role = new Role();
        role.setId(id);
        return role;
    }
}
