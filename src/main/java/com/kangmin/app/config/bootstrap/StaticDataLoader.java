package com.kangmin.app.config.bootstrap;

import com.kangmin.app.config.EnvConfig;
import com.kangmin.app.dao.AccountDao;
import com.kangmin.app.model.Account;
import com.kangmin.app.model.profile.EnvNode;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Import({
    EnvConfig.class,
})
@Component
public class StaticDataLoader implements CommandLineRunner {

    private final EnvNode envNode;
    private final AccountDao accountDao;

    public StaticDataLoader(
        final EnvNode envNode,
        final AccountDao accountDao
    ) {
        this.envNode = envNode;
        this.accountDao = accountDao;
    }

    @Override
    public void run(final String... args) {
        if (envNode == EnvNode.DEV && accountDao.findAll().isEmpty()) {
            // developer, also super admin
            final Account dev = Account.builder()
                .withId("uuid-0000")
                .withEmail("dev@security.com")
                .withUsername("dev")
                .withName("Name-dev")
                .withRole("SUPER_ADMIN")
                .withPassword("dev")
                .build();

            // super admin, h2-console one
            final Account sa = Account.builder()
                .withId("uuid-0001")
                .withEmail("sa@sa.com")
                .withUsername("sa")
                .withName("MountKing Sa")
                .withRole("SUPER_ADMIN")
                .withPassword("password")
                .build();

            // normal
            final Account normal = Account.builder()
                .withId("uuid-0111")
                .withEmail("normal111@security.com")
                .withUsername("normal")
                .withName("Name-normal")
                .withRole("NORMAL")
                .withPassword("normal")
                .build();

            // regular admin
            final Account admin = Account.builder()
                .withId("uuid-0222")
                .withEmail("admin222@security.com")
                .withUsername("admin")
                .withName("Name-admin")
                .withRole("ADMIN")
                .withPassword("admin")
                .build();

            final List<Account> initAccounts = Arrays.asList(dev, sa, normal, admin);
            accountDao.saveAll(initAccounts);
        }
    }
}
