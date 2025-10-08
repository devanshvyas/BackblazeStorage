package com.devanshvyas.BackblazeStorage.config.multitenancy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class TenantService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void createTenant(String tenantId) {
        String sql = "CREATE SCHEMA IF NOT EXISTS " + tenantId + ";";
        //String userTableSql = "CREATE TABLE IF NOT EXISTS " + tenantId + ".users (id SERIAL PRIMARY KEY, username VARCHAR(50) UNIQUE, email VARCHAR(255) UNIQUE, password VARCHAR(100), role VARCHAR(20), admin_id INTEGER, created_at TIMESTAMP, updated_at TIMESTAMP);";
        jdbcTemplate.execute(sql);
    }
}
