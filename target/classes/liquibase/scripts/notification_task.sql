-- liquibase formatted sql

-- changeset Evgeny:1
CREATE TABLE notification_task
(
    id                     SERIAL primary key,
    chat_id                BIGINT,
    message                TEXT,
    notification_send_time TIMESTAMP
);

