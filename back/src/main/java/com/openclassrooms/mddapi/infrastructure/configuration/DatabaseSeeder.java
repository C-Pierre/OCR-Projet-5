package com.openclassrooms.mddapi.infrastructure.configuration;

import java.sql.Connection;
import javax.sql.DataSource;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

@Component
@Profile("!test")
public class DatabaseSeeder implements CommandLineRunner {

    private final DataSource dataSource;

    public DatabaseSeeder(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Seeding database...");

        try (Connection conn = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("sql/init.sql"));
        }

        System.out.println("Database seeded");
    }
}
