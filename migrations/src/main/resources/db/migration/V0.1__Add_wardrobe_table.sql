CREATE TABLE IF NOT EXISTS wardrobe (
    player_uuid uuid NOT NULL,
    cosmetic_key varchar(255) NOT NULL,
    is_equipped bool NOT NULL,
    PRIMARY KEY (player_uuid, cosmetic_key)
);

CREATE TABLE IF NOT EXISTS egghunt (
    player_uuid uuid NOT NULL,
    event varchar(63) NOT NULL,
    egg_id real NOT NULL,
    PRIMARY KEY (player_uuid, event, egg_id)
)