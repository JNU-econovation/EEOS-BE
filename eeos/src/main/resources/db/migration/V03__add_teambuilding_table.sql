CREATE TABLE team_building (
    team_building_id BIGINT NOT NULL AUTO_INCREMENT,
    created_date DATETIME(6) NOT NULL,
    is_deleted BIT NOT NULL,
    updated_date DATETIME(6) NOT NULL,
    team_building_content VARCHAR(255) NOT NULL,
    team_building_max_team_size INTEGER NOT NULL,
    team_building_title VARCHAR(255) NOT NULL,
    PRIMARY KEY (team_building_id)
) ENGINE=InnoDB;

CREATE TABLE team_building_target
(
    team_building_target_id BIGINT NOT NULL AUTO_INCREMENT,
    created_date DATETIME(6) NOT NULL,
    is_deleted BIT NOT NULL,
    updated_date DATETIME(6) NOT NULL,
    team_building_input_content VARCHAR(255),
    team_building_target_member_id BIGINT NOT NULL,
    team_building_target_team_building_id BIGINT NOT NULL,
    PRIMARY KEY (team_building_target_id)
) ENGINE=InnoDB;
