-- liquibase formatted sql

-- changeset Evgeny:1
CREATE TABLE notification_task
(
    id                     SERIAL primary key not null,
    chat_id                BIGINT             not null,
    message                TEXT               not null,
    notification_send_time TIMESTAMP          not null
);

