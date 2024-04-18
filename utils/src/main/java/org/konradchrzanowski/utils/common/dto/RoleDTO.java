package org.konradchrzanowski.utils.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.konradchrzanowski.utils.common.enumeration.ERole;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleDTO {
    private long id;
    private ERole name;
}
