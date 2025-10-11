DROP TABLE IF Exists storage_configs CASCADE;
CREATE TABLE storage_configs (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL UNIQUE,
    configured BOOLEAN NOT NULL DEFAULT FALSE,
    b2_application_key_id VARCHAR(255),
    b2_application_key TEXT,
    b2_bucket_name VARCHAR(255),
    b2_bucket_id VARCHAR(255),
    s3_end_point VARCHAR(255),
    s3_region VARCHAR(255),
    CONSTRAINT fk_user
        FOREIGN KEY(user_id) 
        REFERENCES users(id)
        ON DELETE CASCADE
);
DROP TABLE IF Exists users CASCADE;
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255),
    role VARCHAR(50),  -- UserRole is assumed to be stored as a string
    admin_username VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE
);


--CREATE TABLE IF NOT EXISTS " + tenantId + ".gallery_metadata (id SERIAL PRIMARY KEY, name VARCHAR(255) NOT NULL, content_type VARCHAR(50), b2_file_key VARCHAR(255) UNIQUE, size INTEGER, owner_id INTEGER NOT NULL, uploaded_at TIMESTAMP, CONSTRAINT fk_user FOREIGN KEY(owner_id) REFERENCES public.users(id) on DELETE CASCADE);