-- V1__Create_users_table.sql

CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,  -- UUID will be generated in Java
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL, -- The hashed password will be stored here
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    `role` VARCHAR(50) NOT NULL DEFAULT 'ROLE_USER'
    );
