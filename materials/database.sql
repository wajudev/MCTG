create table users
(
    id            serial
        primary key,
    username      varchar(255)       not null
        unique,
    password      varchar(255)       not null,
    token         varchar(255),
    coins         integer default 20 not null,
    total_battles integer default 0,
    won_battles   integer default 0,
    lost_battles  integer default 0,
    elo           integer default 100,
    admin         boolean            not null,
    bio           varchar(255),
    image         varchar(255),
    drawn_battles integer,
    wl_ratio      double precision
);

alter table users
    owner to postgres;

create table sessions
(
    token         varchar not null,
    last_loggedin timestamp
);

alter table sessions
    owner to waju;

create table cards
(
    id           varchar               not null
        primary key,
    name         varchar(255),
    card_type    varchar(255),
    monster_type varchar(255),
    element_type varchar(255),
    damage       numeric(6, 2),
    deck         boolean default false,
    user_id      integer
        constraint fk_users
            references users,
    is_locked    boolean default false not null,
    package_id   integer
);

alter table cards
    owner to waju;

create table trades
(
    id         varchar(255) not null
        constraint trades_pk
            primary key,
    is_monster boolean default false,
    min_damage numeric,
    user_id    integer
        constraint trades_users__fk
            references users,
    card_id    varchar(255)
        constraint trades_cards__fk
            references cards
);

alter table trades
    owner to waju;

