CREATE TABLE `member` (
                          `member_id` bigint NOT NULL AUTO_INCREMENT,
                          `created_date` datetime NOT NULL,
                          `is_deleted` tinyint(1) NOT NULL,
                          `updated_date` datetime NOT NULL,
                          `member_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                          `member_oath_server_type` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                          `member_active_status` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                          PRIMARY KEY (`member_id`),
                          KEY `idx_member_active_status` (`member_active_status`),
                          KEY `idx_member_name` (`member_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


CREATE TABLE `oauth_member` (
                                `oauth_member_id` bigint NOT NULL AUTO_INCREMENT,
                                `created_date` datetime(6) NOT NULL,
                                `is_deleted` tinyint(1) NOT NULL,
                                `updated_date` datetime(6) NOT NULL,
                                `oauth_member_member_id` bigint NOT NULL,
                                `oauth_member_oauth_id` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                                PRIMARY KEY (`oauth_member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `program` (
                           `program_id` bigint NOT NULL AUTO_INCREMENT,
                           `created_date` datetime NOT NULL,
                           `is_deleted` tinyint(1) NOT NULL,
                           `updated_date` datetime NOT NULL,
                           `program_content` text COLLATE utf8mb4_unicode_ci NOT NULL,
                           `program_date` timestamp NOT NULL,
                           `program_title` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                           `program_category` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                           `program_type` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                           `program_writer` bigint NOT NULL,
                           PRIMARY KEY (`program_id`),
                           KEY `idx_program_date` (`program_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `attend` (
                          `attend_id` bigint NOT NULL AUTO_INCREMENT,
                          `created_date` datetime NOT NULL,
                          `is_deleted` tinyint(1) NOT NULL,
                          `updated_date` datetime NOT NULL,
                          `attend_program_id` bigint NOT NULL,
                          `attend_member_id` bigint NOT NULL,
                          `attend_status` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL,
                          PRIMARY KEY (`attend_id`),
                          KEY `idx_program` (`attend_program_id`),
                          KEY `idx_attend_status` (`attend_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `team_building` (
                                 `team_building_id` bigint NOT NULL AUTO_INCREMENT,
                                 `created_date` datetime(6) NOT NULL,
                                 `is_deleted` bit(1) NOT NULL,
                                 `updated_date` datetime(6) NOT NULL,
                                 `team_building_content` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                                 `team_building_max_team_size` int NOT NULL,
                                 `team_building_member_id` bigint NOT NULL,
                                 `team_building_status` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                                 `team_building_title` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                                 PRIMARY KEY (`team_building_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `team_building_target` (
                                        `team_building_target_id` bigint NOT NULL AUTO_INCREMENT,
                                        `created_date` datetime(6) NOT NULL,
                                        `is_deleted` bit(1) NOT NULL,
                                        `updated_date` datetime(6) NOT NULL,
                                        `team_building_input_content` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                                        `team_building_input_status` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                                        `team_building_target_member_id` bigint NOT NULL,
                                        `team_building_target_team_building_id` bigint NOT NULL,
                                        PRIMARY KEY (`team_building_target_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `team_building_result` (
                                        `team_building_result_id` bigint NOT NULL AUTO_INCREMENT,
                                        `created_date` datetime(6) NOT NULL,
                                        `is_deleted` bit(1) NOT NULL,
                                        `updated_date` datetime(6) NOT NULL,
                                        `team_building_result_member_ids` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                                        `team_building_result_status` bigint NOT NULL,
                                        PRIMARY KEY (`team_building_result_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `restrict_team_building` (
                                          `restrict_team_building_id` bigint NOT NULL AUTO_INCREMENT,
                                          `created_date` datetime(6) NOT NULL,
                                          `is_deleted` bit(1) NOT NULL,
                                          `updated_date` datetime(6) NOT NULL,
                                          `restrict_team_building_total_active_count` bigint NOT NULL,
                                          `version` bigint DEFAULT NULL,
                                          PRIMARY KEY (`restrict_team_building_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;








