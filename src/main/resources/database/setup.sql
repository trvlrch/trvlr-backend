-- TRVLR DB
CREATE DATABASE IF NOT EXISTS trvlr CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE trvlr;

CREATE TABLE traveler
(
    `id` int(10) unsigned NOT NULL,
    `firstname` varchar(128) NOT NULL,
    `lastname` varchar(128) NOT NULL,
    `email` varchar(128) NOT NULL,
    `auth_token` varchar(256) NOT NULL,

    PRIMARY KEY (`id`)
);

CREATE TABLE station
(
    `id` int(10) unsigned NOT NULL,
    `name` varchar(128) NOT NULL,

    PRIMARY KEY (`id`)
);

-- should be multiple tables with inheritance for public and private chat
CREATE TABLE chat_room
(
    `id` int(10) unsigned NOT NULL,
    `from` int(10) unsigned NULL,
    `to` int(10) unsigned NULL,
    `created_on` datetime NOT NULL,

    PRIMARY KEY (`id`),
    FOREIGN KEY (`from`) REFERENCES station(`id`),
    FOREIGN KEY (`to`) REFERENCES station(`id`)
);

CREATE TABLE message
(
    `id` int(10) unsigned NOT NULL,
    `author` int(10) unsigned NOT NULL,
    `text` longtext NOT NULL,
    `timestamp` datetime NOT NULL,
    `chat_room_id` int(10) unsigned NOT NULL,

    PRIMARY KEY (`id`),
    FOREIGN KEY (`author`) REFERENCES traveler(`id`),
    FOREIGN KEY (`chat_room_id`) REFERENCES chat_room(`id`)
);

CREATE TABLE chat_room_traveler
(
    `id` int(10) unsigned NOT NULL,
    `chat_room_id` int(10) unsigned NOT NULL,
    `traveler_id` int(10) unsigned NOT NULL,

    PRIMARY KEY (`id`),
    FOREIGN KEY (`chat_room_id`) REFERENCES chat_room(`id`),
    FOREIGN KEY (`traveler_id`) REFERENCES traveler(`id`)
);
