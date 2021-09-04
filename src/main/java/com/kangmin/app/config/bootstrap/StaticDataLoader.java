package com.kangmin.app.config.bootstrap;

import com.kangmin.app.config.EnvConfig;
import com.kangmin.app.config.security.PasswordConfig;
import com.kangmin.app.dao.AccountDao;
import com.kangmin.app.model.Account;
import com.kangmin.app.model.profile.EnvNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Import({
    EnvConfig.class,
    PasswordConfig.class,
})
@Component
public class StaticDataLoader implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(StaticDataLoader.class);

    private final EnvNode envNode;
    private final AccountDao accountDao;
    private final PasswordEncoder passwordEncoder;

    public StaticDataLoader(
        final EnvNode envNode,
        final AccountDao accountDao,
        final PasswordEncoder passwordEncoder
    ) {
        this.envNode = envNode;
        this.accountDao = accountDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(final String... args) {
        if (envNode == EnvNode.DEV && accountDao.findAll().isEmpty()) {
            final List<Account> initAccounts = createInitAccounts();
            accountDao.saveAll(initAccounts);
        } else if (envNode == EnvNode.PROD) {
            LOG.info(">>> Application in PROD mode is up >>>");
        }
    }

    private List<Account> createInitAccounts() {
        // developer, also super admin
        final Account dev = Account.builder()
            .withId("uuid-0000")
            .withEmail("dev@security.com")
            .withUsername("dev")
            .withName("Name-dev")
            .withRole("SUPER_ADMIN")
            .withPassword(passwordEncoder.encode("dev"))
            .build();

        // super admin, h2-console one
        final Account sa = Account.builder()
            .withId("uuid-0001")
            .withEmail("sa@sa.com")
            .withUsername("sa")
            .withName("MountKing Sa")
            .withRole("SUPER_ADMIN")
            .withPassword(passwordEncoder.encode("password"))
            .build();

        // normal
        final Account normal = Account.builder()
            .withId("uuid-0111")
            .withEmail("normal111@security.com")
            .withUsername("normal")
            .withName("Name-normal")
            .withRole("NORMAL")
            .withPassword(passwordEncoder.encode("normal"))
            .build();

        // regular admin
        final Account admin = Account.builder()
            .withId("uuid-0222")
            .withEmail("admin222@security.com")
            .withUsername("admin")
            .withName("Name-admin")
            .withRole("ADMIN")
            .withPassword(passwordEncoder.encode("admin"))
            .build();

        return Arrays.asList(dev, sa, normal, admin);
    }
}
