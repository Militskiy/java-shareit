drop table if exists users cascade;
drop table if exists item_requests cascade;
drop table if exists items cascade;
drop table if exists bookings cascade;
drop table if exists comments cascade;

create table if not exists users
(
    user_id   bigint generated by default as identity
        primary key,
    email     varchar(255) not null
        constraint uk_email
            unique,
    user_name varchar(255) not null
);

create table if not exists item_requests
(
    request_id          bigint generated by default as identity
        primary key,
    created             timestamp,
    request_description varchar(255),
    requester_id        bigint
        constraint fk_item_requests_users
            references users
);


create table if not exists items
(
    item_id     bigint generated by default as identity
        primary key,
    available   boolean      not null,
    description varchar(255) not null,
    item_name   varchar(255) not null,
    owner_id    bigint       not null
        constraint fk_items_users
            references users,
    request_id  bigint
        constraint fk_items_requests
            references item_requests
);


create table if not exists bookings
(
    booking_id bigint generated by default as identity
        primary key,
    end_time   timestamp,
    start_time timestamp,
    status     varchar(255),
    booker_id  bigint
        constraint fk_bookings_users
            references users,
    item_id    bigint
        constraint fk_bookings_items
            references items
);

create table if not exists comments
(
    comment_id bigint generated by default as identity
        primary key,
    created    timestamp,
    text       varchar(255) not null,
    author_id  bigint
        constraint fk_comments_users
            references users,
    item_id    bigint       not null
        constraint fk_comments_items
            references items
);