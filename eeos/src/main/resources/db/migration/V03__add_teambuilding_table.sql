create table restrict_team_building (
    restrict_team_building_id bigint not null auto_increment,
    created_date datetime(6) not null,
    is_deleted bit not null,
    updated_date datetime(6) not null,
    restrict_team_building_total_active_count bigint not null,
    version bigint,
    primary key (restrict_team_building_id)
) engine=InnoDB;


create table team_building (
    team_building_id bigint not null auto_increment,
    created_date datetime(6) not null,
    is_deleted bit not null,
    updated_date datetime(6) not null,
    team_building_content varchar(255) not null,
    team_building_max_team_size integer not null,
    team_building_member_id bigint not null,
    team_building_status varchar(255) not null,
    team_building_title varchar(255) not null,
    primary key (team_building_id)
) engine=InnoDB;


create table team_building_result (
    team_building_result_id bigint not null auto_increment,
    created_date datetime(6) not null,
    is_deleted bit not null,
    updated_date datetime(6) not null,
    team_building_result_member_ids varchar(255) not null,
    team_building_result_status bigint not null,
    primary key (team_building_result_id)
) engine=InnoDB;

create table team_building_target (
    team_building_target_id bigint not null auto_increment,
    created_date datetime(6) not null,
    is_deleted bit not null,
    updated_date datetime(6) not null,
    team_building_input_content varchar(255),
    team_building_input_status varchar(255) not null,
    team_building_target_member_id bigint not null,
    team_building_target_team_building_id bigint not null,
    primary key (team_building_target_id)
) engine=InnoDB