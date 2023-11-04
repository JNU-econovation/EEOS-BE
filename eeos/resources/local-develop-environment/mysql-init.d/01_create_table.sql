use eeos;
create table program
(
    program_id      bigint       not null auto_increment,
    created_date    datetime     not null,
    is_deleted      boolean      not null,
    updated_date    datetime     not null,
    program_content varchar(255) not null,
    program_date    TIMESTAMP    not null,
    program_title   varchar(255) not null,
    primary key (program_id)
) engine = InnoDB;

create table member
(
    member_id         bigint       not null auto_increment,
    created_date      datetime     not null,
    is_deleted        boolean      not null,
    updated_date      datetime     not null,
    member_name       varchar(255) not null,
    member_generation BIGINT       not null,
    primary key (member_id)
) engine = InnoDB;

create table attend
(
    attend_id         bigint      not null auto_increment,
    created_date      datetime    not null,
    is_deleted        boolean     not null,
    updated_date      datetime    not null,
    attend_program_id BIGINT      not null,
    attend_member_id  BIGINT      not null,
    attend_status     VARCHAR(40) not null,
    primary key (attend_id)
) engine = InnoDB;

ALTER TABLE member
    ADD INDEX idx_generation_name (member_generation, member_name);

ALTER TABLE attend
    ADD INDEX idx_program (attend_program_id);

ALTER TABLE program
    ADD INDEX idx_program_date (program_date);