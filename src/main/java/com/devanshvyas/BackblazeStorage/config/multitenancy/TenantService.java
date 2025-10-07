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
        String userTableSql = "CREATE TABLE IF NOT EXISTS " + tenantId + ".users (id SERIAL PRIMARY KEY, username VARCHAR(50) UNIQUE, email VARCHAR(100) UNIQUE, password VARCHAR(100), role VARCHAR(20), admin_id INTEGER, created_at date, updated_at date);";
        String storageConfigTableSql = "CREATE TABLE IF NOT EXISTS " + tenantId + ".storage_configs (id SERIAL PRIMARY KEY, b2_application_key VARCHAR(100), b2_application_key_id VARCHAR(100), b2_bucket_id VARCHAR(100), b2_bucket_name VARCHAR(100), configured bool);";
        jdbcTemplate.execute(sql + userTableSql + storageConfigTableSql);
    }
}
