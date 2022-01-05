/* Users */

DROP TABLE IF EXISTS users CASCADE;
CREATE TABLE users
(
    id            SERIAL PRIMARY KEY,
    username      VARCHAR(255) NOT NULL,
    status        VARCHAR(255),
    password      VARCHAR(255) NOT NULL,
    token         VARCHAR(255) NULL,
    coins         INT          NOT NULL DEFAULT 20,
    total_battles INT                   DEFAULT 0,
    won_battles   INT                   DEFAULT 0,
    lost_battles  INT                   DEFAULT 0,
    elo           INT                   DEFAULT 100,
    UNIQUE (username)
);

INSERT INTO cards ( name, card_type, monster_type, element_type, damage) VALUES ('Elixiro', 'SPELL', null, 'WATER', 30.56);
