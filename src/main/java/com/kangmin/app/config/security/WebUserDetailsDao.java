package com.kangmin.app.config.security;

import java.util.Optional;

public interface WebUserDetailsDao {

    Optional<WebUserDetails> getUserDetailsByUsername(String username);

}
