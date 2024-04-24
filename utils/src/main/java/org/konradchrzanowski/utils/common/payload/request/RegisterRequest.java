package org.konradchrzanowski.utils.common.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.Set;

public class RegisterRequest {

    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    private Set<String> role;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;


    public RegisterRequest(String username, String email, Set<String> role, String password) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.password = password;
    }

    private RegisterRequest(Builder builder) {
        setUsername(builder.username);
        setEmail(builder.email);
        setRole(builder.role);
        setPassword(builder.password);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(RegisterRequest copy) {
        Builder builder = new Builder();
        builder.username = copy.getUsername();
        builder.email = copy.getEmail();
        builder.role = copy.getRole();
        builder.password = copy.getPassword();
        return builder;
    }

    public String getUsername() {
        return username;
    }

    public RegisterRequest setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public RegisterRequest setEmail(String email) {
        this.email = email;
        return this;
    }

    public Set<String> getRole() {
        return role;
    }

    public RegisterRequest setRole(Set<String> role) {
        this.role = role;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public RegisterRequest setPassword(String password) {
        this.password = password;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegisterRequest that = (RegisterRequest) o;
        return Objects.equals(username, that.username) && Objects.equals(email, that.email) && Objects.equals(role, that.role) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, email, role, password);
    }

    @Override
    public String toString() {
        return "RegisterRequest{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", password='" + password + '\'' +
                '}';
    }


    public static final class Builder {
        private @NotBlank
        @Size(min = 3, max = 20) String username;
        private @NotBlank
        @Size(max = 50)
        @Email String email;
        private Set<String> role;
        private @NotBlank
        @Size(min = 6, max = 40) String password;

        private Builder() {
        }

        public Builder username(@NotBlank @Size(min = 3, max = 20) String username) {
            this.username = username;
            return this;
        }

        public Builder email(@NotBlank @Size(max = 50) @Email String email) {
            this.email = email;
            return this;
        }

        public Builder role(Set<String> role) {
            this.role = role;
            return this;
        }

        public Builder password(@NotBlank @Size(min = 6, max = 40) String password) {
            this.password = password;
            return this;
        }

        public RegisterRequest build() {
            return new RegisterRequest(this);
        }
    }
}
