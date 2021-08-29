package com.kangmin.app.model.security;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.kangmin.app.model.security.WebUserPermission.ACCOUNT_READ;
import static com.kangmin.app.model.security.WebUserPermission.ACCOUNT_WRITE;
import static com.kangmin.app.model.security.WebUserPermission.ADMIN_READ;
import static com.kangmin.app.model.security.WebUserPermission.ADMIN_WRITE;
import static com.kangmin.app.model.security.WebUserPermission.BLOG_READ;
import static com.kangmin.app.model.security.WebUserPermission.BLOG_WRITE;

public enum WebUserRole {

    UNKNOWN(Sets.newHashSet()),

    NORMAL(Sets.newHashSet(
        BLOG_READ
    )),

    ADMIN(Sets.newHashSet(
        BLOG_READ, BLOG_WRITE,
        ACCOUNT_READ
    )),

    SUPER_ADMIN(Sets.newHashSet(
        BLOG_READ, BLOG_WRITE,
        ACCOUNT_READ, ACCOUNT_WRITE,
        ADMIN_READ, ADMIN_WRITE
    ));

    private final Set<WebUserPermission> permissions;

    WebUserRole(final Set<WebUserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<WebUserPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        final Set<SimpleGrantedAuthority> permissions = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }
}
