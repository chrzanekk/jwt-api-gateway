package org.konradchrzanowski.user.entities;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserVO {
    private String id;
    private String email;
    private String password;
    private String role;
}
