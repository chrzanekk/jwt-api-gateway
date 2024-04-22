package org.konradchrzanowski.user.mapper;

import org.konradchrzanowski.user.domain.User;
import org.konradchrzanowski.utils.common.dto.UserDTO;
import org.konradchrzanowski.utils.mapper.EntityMapper;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface UserMapper extends EntityMapper<UserDTO, User> {

    default User fromId(Long id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }
}
