package org.konradchrzanowski.utils.common.payload.request;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;


public class LoginRequest {

    private String username;

    @NotBlank
    private String password;

    private Boolean rememberMe;

    public LoginRequest(String username, String password, Boolean rememberMe) {
        this.username = username;
        this.password = password;
        this.rememberMe = rememberMe;
    }

    private LoginRequest(Builder builder) {
        setUsername(builder.username);
        setPassword(builder.password);
        setRememberMe(builder.rememberMe);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(LoginRequest copy) {
        Builder builder = new Builder();
        builder.username = copy.getUsername();
        builder.password = copy.getPassword();
        builder.rememberMe = copy.getRememberMe();
        return builder;
    }

    public String getUsername() {
        return username;
    }

    public LoginRequest setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public LoginRequest setPassword(String password) {
        this.password = password;
        return this;
    }

    public Boolean getRememberMe() {
        return rememberMe;
    }

    public LoginRequest setRememberMe(Boolean rememberMe) {
        this.rememberMe = rememberMe;
        return this;
    }


    public static final class Builder {
        private String username;
        private @NotBlank String password;
        private Boolean rememberMe;

        private Builder() {
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(@NotBlank String password) {
            this.password = password;
            return this;
        }

        public Builder rememberMe(Boolean rememberMe) {
            this.rememberMe = rememberMe;
            return this;
        }

        public LoginRequest build() {
            return new LoginRequest(this);
        }
    }
}
