package com.kangmin.app.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "tb_account")
public class Account implements Serializable {

    private static final int COMMON_LENGTH = 50;
    // == PK string length is suggested to be less than 50
    private static final int ID_LENGTH = COMMON_LENGTH;
    private static final int USERNAME_LENGTH = COMMON_LENGTH;
    private static final int EMAIL_LENGTH = COMMON_LENGTH;
    private static final int NAME_LENGTH = 64;
    private static final int PASSWORD_LENGTH = 64;
    private static final int ROLE_LENGTH = 32;
    private static final int PROVIDER_ID_LENGTH = 32;
    private static final int PROVIDER_LENGTH = 32;

    @Id
    @Column(length = ID_LENGTH)
    private String id;

    // == unique email ==
    @Column(nullable = false, unique = true, length = EMAIL_LENGTH)
    private String email;

    // == unique username ==
    @Column(nullable = false, unique = true, length = USERNAME_LENGTH)
    private String username;

    @Column(nullable = false, length = NAME_LENGTH)
    private String name;

    @Column(nullable = false, length = ROLE_LENGTH)
    private String role;

    @Column(length = PASSWORD_LENGTH)
    private String password;

    @Column(nullable = false)
    private Date createdDate;

    // == For OAuth2 providers such as Google, GitHub ==

    @Column(length = PROVIDER_ID_LENGTH)
    private String providerId;

    @Column(length = PROVIDER_LENGTH)
    private String provider;

    private Account(
        final String id,
        final String email,
        final String username,
        final String name,
        final String role,
        final String password
    ) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.name = name;
        this.role = role;
        this.password = password;
        this.createdDate = new Date();
    }

    public Account() {

    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String email;
        private String username;
        private String name;
        private String role;
        private String password;

        public Builder withId(final String id) {
            this.id = id;
            return this;
        }

        public Builder withEmail(final String email) {
            this.email = email;
            return this;
        }

        public Builder withUsername(final String username) {
            this.username = username;
            return this;
        }

        public Builder withName(final String name) {
            this.name = name;
            return this;
        }

        public Builder withRole(final String role) {
            this.role = role;
            return this;
        }

        public Builder withPassword(final String password) {
            this.password = password;
            return this;
        }

        public Account build() {
            return new Account(id, email, username, name, role, password);
        }
    }
}
