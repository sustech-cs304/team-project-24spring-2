CREATE TABLE IF NOT EXISTS "orders"
(
    id BIGINT NOT NULL,
    name VARCHAR(30) NULL DEFAULT NULL,
    description VARCHAR(50) NULL DEFAULT NULL,
    PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS "locations"
(
    id UUID NOT NULL,
    name VARCHAR(30),
    longitude DOUBLE PRECISION,
    latitude DOUBLE PRECISION,
    PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS "events"
(
    id UUID PRIMARY KEY,
    title VARCHAR(255),
    publisher INTEGER,
    publish_time TIMESTAMPTZ,
    start_time TIMESTAMPTZ,
    end_time TIMESTAMPTZ,
    description TEXT,
    location UUID,
    available_capacity INTEGER,
    current_capacity INTEGER
);
CREATE TABLE IF NOT EXISTS "users"
(
    id int PRIMARY KEY,
    username VARCHAR(255),
    email VARCHAR(255)
);