create table auth_info
(
    auth_info_id        BIGINT       not null auto_increment,
    created_date        DATETIME(6)  not null,
    is_deleted          BOOLEAN      not null,
    updated_date        DATETIME(6)  not null,
    auth_info_member_id BIGINT       not null,
    auth_info_token     varchar(255) not null,
    primary key (auth_info_id),
    INDEX idx_auth_info_member_id (auth_info_member_id)
) engine = InnoDB;

create table oauth_info
(
    oauth_info_id        BIGINT       not null auto_increment,
    created_date         DATETIME(6)  not null,
    is_deleted           BOOLEAN      not null,
    updated_date         DATETIME(6)  not null,
    oauth_info_member_id BIGINT       not null,
    oauth_info_oauth_id  VARCHAR(255) not null,
    primary key (oauth_info_id)
) engine = InnoDB;