package com.devanshvyas.BackblazeStorage.service.tenant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class TenantService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void createTenant(String tenantId) {
        String sql = "CREATE SCHEMA IF NOT EXISTS " + tenantId + ";";
        String galleryTableSql = "CREATE TABLE IF NOT EXISTS " + tenantId + ".gallery_metadata (id SERIAL PRIMARY KEY, name VARCHAR(255) NOT NULL, content_type VARCHAR(50), b2_file_key VARCHAR(255) UNIQUE, size INTEGER, owner_id INTEGER NOT NULL, uploaded_at TIMESTAMP, download_count INTEGER, CONSTRAINT fk_user FOREIGN KEY(owner_id) REFERENCES public.users(id) on DELETE CASCADE);";
        String driveTableSql = "CREATE TABLE IF NOT EXISTS " + tenantId + ".drive_metadata (id SERIAL PRIMARY KEY, name VARCHAR(255) NOT NULL, content_type VARCHAR(50), b2_file_key VARCHAR(255) UNIQUE, size INTEGER, owner_id INTEGER NOT NULL, uploaded_at TIMESTAMP, download_count INTEGER, CONSTRAINT fk_user FOREIGN KEY(owner_id) REFERENCES public.users(id) on DELETE CASCADE);";
        jdbcTemplate.execute(sql + galleryTableSql + driveTableSql);
    }
}
