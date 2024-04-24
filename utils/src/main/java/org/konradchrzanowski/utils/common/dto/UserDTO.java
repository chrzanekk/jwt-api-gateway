package org.konradchrzanowski.utils.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

public class UserDTO {
    private final Long id;
    private final String email;
    private final String username;
    private final String password;
    private final Boolean locked;
    private final Boolean enabled;
    private final Set<RoleDTO> roles;

    public UserDTO(Long id, String email, String username, String password, Boolean locked, Boolean enabled, Set<RoleDTO> roles) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.locked = locked;
        this.enabled = enabled;
        this.roles = roles;
    }


    private UserDTO(Builder builder) {
        id = builder.id;
        email = builder.email;
        username = builder.username;
        password = builder.password;
        locked = builder.locked;
        enabled = builder.enabled;
        roles = builder.roles;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(UserDTO copy) {
        Builder builder = new Builder();
        builder.id = copy.getId();
        builder.email = copy.getEmail();
        builder.username = copy.getUsername();
        builder.password = copy.getPassword();
        builder.locked = copy.getLocked();
        builder.enabled = copy.getEnabled();
        builder.roles = copy.getRoles();
        return builder;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Boolean getLocked() {
        return locked;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public Set<RoleDTO> getRoles() {
        return roles;
    }

    public static final class Builder {
        private Long id;
        private String email;
        private String username;
        private String password;
        private Boolean locked;
        private Boolean enabled;
        private Set<RoleDTO> roles;

        private Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder locked(Boolean locked) {
            this.locked = locked;
            return this;
        }

        public Builder enabled(Boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder roles(Set<RoleDTO> roles) {
            this.roles = roles;
            return this;
        }

        public UserDTO build() {
            return new UserDTO(this);
        }
    }
}
