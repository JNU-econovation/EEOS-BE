use eeos;
create table program
(
    program_id      bigint       not null auto_increment,
    created_date    datetime     not null,
    is_deleted      boolean      not null,
    updated_date    datetime     not null,
    program_content varchar(255) not null,
    program_date    timestamp    not null,
    program_title   varchar(255) not null,
    primary key (program_id)
) engine = InnoDB;

create table member
(
    member_id    bigint       not null auto_increment,
    created_date datetime     not null,
    is_deleted   boolean      not null,
    updated_date datetime     not null,
    member_name  varchar(255) not null,
    member_generation int not null,
    primary key (member_id)
) engine = InnoDB;