create table spot
(
    id         bigint auto_increment primary key,
    latitude   double      not null,
    longitude  double      not null,
    weight     double      not null,
    created_at datetime(6) null
);
