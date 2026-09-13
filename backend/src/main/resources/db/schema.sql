-- SQLite 建表脚本（spring.sql.init，每次启动执行，幂等）
-- 说明：SQLite 中 INTEGER PRIMARY KEY 即自增主键（rowid 别名，插入 NULL 时自动分配），
--       布尔用 integer(0/1) 存储，时间戳存文本。不用 AUTOINCREMENT 关键字，
--       避免每次启动执行 insert or ignore 时虚增 id。

create table if not exists major (
    id      integer primary key,
    code    varchar(32) not null unique,
    name    varchar(64) not null unique,
    enabled boolean     not null default 1
);

create table if not exists account (
    id            integer primary key,
    username      varchar(32)  not null unique,
    password_hash varchar(100) not null,
    real_name     varchar(32)  not null,
    role          varchar(16)  not null,
    status        varchar(16)  not null,
    created_at    timestamp    not null,
    last_login_at timestamp,
    login_count   integer      not null default 0
);
create index if not exists idx_account_status on account (status);

create table if not exists student_profile (
    account_id    bigint primary key references account (id) on delete cascade,
    major_id      bigint references major (id),
    class_name    varchar(32),
    enroll_year   integer,
    graduate_year integer,
    employer      varchar(64),
    city          varchar(32),
    phone         varchar(32),
    email         varchar(64),
    updated_at    timestamp not null
);
create index if not exists idx_profile_major on student_profile (major_id);
create index if not exists idx_profile_enroll on student_profile (enroll_year);

create table if not exists audit_log (
    id              integer primary key,
    operator        varchar(32) not null,
    action          varchar(32) not null,
    target_username varchar(32),
    created_at      timestamp   not null
);
