package com.steg.smartmonitoring.user.dto;

import com.steg.smartmonitoring.auth.validation.PasswordStrength;
import com.steg.smartmonitoring.user.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO pour créer un nouvel utilisateur
 */
public class CreateUserRequest {

    @NotBlank(message = "Le nom d'utilisateur est requis")
    private String username;

    @Email(message = "L'email doit être valide")
    @NotBlank(message = "L'email est requis")
    private String email;

    @PasswordStrength
    private String password;

    @NotNull(message = "Le rôle est requis")
    private Role role;

    public CreateUserRequest() {
    }

    public CreateUserRequest(String username, String email, String password, Role role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String username;
        private String email;
        private String password;
        private Role role;

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder role(Role role) {
            this.role = role;
            return this;
        }

        public CreateUserRequest build() {
            return new CreateUserRequest(username, email, password, role);
        }
    }
}
