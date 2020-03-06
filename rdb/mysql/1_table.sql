DROP SCHEMA IF EXISTS market;
CREATE SCHEMA market;
USE market;

CREATE TABLE `user_credentials` (
    `id` VARCHAR(255) NOT NULL,
    `mail` VARCHAR(255) NOT NULL,
    `pass` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY (`mail`)
);

CREATE TABLE `linked_user_credentials` (
    `id` VARCHAR(255) NOT NULL,
    `service_id` VARCHAR(255) NOT NULL,
    `service_name` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY (`service_id`, `service_name`)
);
