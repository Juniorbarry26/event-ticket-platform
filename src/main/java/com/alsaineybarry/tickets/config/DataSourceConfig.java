package com.alsaineybarry.tickets.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class DataSourceConfig {

    @Value("${DATABASE_URL:}")
    private String databaseUrl;

    @Value("${spring.datasource.url:jdbc:postgresql://localhost:5432/event-ticket-platform}")
    private String dbUrl;

    @Value("${spring.datasource.username:postgres}")
    private String dbUsername;

    @Value("${spring.datasource.password:Avond778@}")
    private String dbPassword;

    @Value("${spring.datasource.driver-class-name:org.postgresql.Driver}")
    private String dbDriver;

    @Bean
    @Primary
    public DataSource dataSource() {
        if (databaseUrl != null && !databaseUrl.isEmpty()) {
            return parseRenderDatabaseUrl(databaseUrl);
        }

        return DataSourceBuilder.create()
                .url(dbUrl)
                .username(dbUsername)
                .password(dbPassword)
                .driverClassName(dbDriver)
                .build();
    }

    private DataSource parseRenderDatabaseUrl(String databaseUrl) {
        try {
            URI uri = new URI(databaseUrl);
            String username = uri.getUserInfo().split(":")[0];
            String password = uri.getUserInfo().split(":")[1];
            String host = uri.getHost();
            int port = uri.getPort();
            String database = uri.getPath().substring(1); // Remove leading '/'
            
            // Add SSL mode for Render PostgreSQL
            String jdbcUrl = String.format("jdbc:postgresql://%s:%d/%s?sslmode=require", host, port, database);
            
            return DataSourceBuilder.create()
                    .url(jdbcUrl)
                    .username(username)
                    .password(password)
                    .driverClassName("org.postgresql.Driver")
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Invalid DATABASE_URL format", e);
        }
    }
}
