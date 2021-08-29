package com.kangmin.app.model.security;

public enum WebUserPermission {

    BLOG_READ("BLOG:READ"),
    BLOG_WRITE("BLOG:WRITE"),

    ACCOUNT_READ("ACCOUNT:READ"),
    ACCOUNT_WRITE("ACCOUNT:WRITE"),

    ADMIN_READ("ADMIN:READ"),
    ADMIN_WRITE("ADMIN:WRITE");

    private final String name;

    WebUserPermission(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
