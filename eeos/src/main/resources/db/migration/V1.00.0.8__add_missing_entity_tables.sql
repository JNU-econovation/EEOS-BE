-- 엔티티에는 존재하지만 마이그레이션에 누락된 테이블/컬럼을 추가한다.
-- 기존 DB에는 수동으로 생성되어 있을 수 있으므로 모든 구문은 멱등하게 작성한다.

CREATE TABLE IF NOT EXISTS `account` (
    `account_id`           bigint       NOT NULL AUTO_INCREMENT,
    `created_date`         datetime(6)  NOT NULL,
    `is_deleted`           tinyint(1)   NOT NULL DEFAULT 0,
    `updated_date`         datetime(6)  NOT NULL,
    `account_member_id`    bigint       NOT NULL,
    `account_login_id`     varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `account_login_passwd` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    PRIMARY KEY (`account_id`),
    UNIQUE KEY `uk_account_member_id` (`account_member_id`),
    UNIQUE KEY `uk_account_login_id` (`account_login_id`),
    KEY `idx_member_id` (`account_member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `authority` (
    `id`                bigint       NOT NULL AUTO_INCREMENT,
    `created_date`      datetime(6)  NOT NULL,
    `is_deleted`        tinyint(1)   NOT NULL DEFAULT 0,
    `updated_date`      datetime(6)  NOT NULL,
    `authority_member_id` bigint     NOT NULL,
    `authority_role`    enum('ROLE_ADMIN','ROLE_USER') COLLATE utf8mb4_unicode_ci NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_authority_member_role` (`authority_member_id`, `authority_role`),
    KEY `idx_member_id` (`authority_member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `calendar` (
    `calendar_id`       bigint       NOT NULL AUTO_INCREMENT,
    `created_date`      datetime(6)  NOT NULL,
    `is_deleted`        tinyint(1)   NOT NULL DEFAULT 0,
    `updated_date`      datetime(6)  NOT NULL,
    `calendar_writer`   bigint       NOT NULL,
    `calendar_type`     enum('EVENT','PRESENTATION','ETC') COLLATE utf8mb4_unicode_ci NOT NULL,
    `calendar_title`    varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `calendar_url`      varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `calendar_start_at` datetime(6)  NOT NULL,
    `calendar_end_at`   datetime(6)  NOT NULL,
    PRIMARY KEY (`calendar_id`),
    KEY `idx_calendar_start_at` (`calendar_start_at`),
    KEY `idx_calendar_end_at` (`calendar_end_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `comment` (
    `comment_id`               bigint       NOT NULL AUTO_INCREMENT,
    `created_date`             datetime(6)  NOT NULL,
    `is_deleted`               tinyint(1)   NOT NULL DEFAULT 0,
    `updated_date`             datetime(6)  NOT NULL,
    `comment_program_id`       bigint       NOT NULL,
    `comment_team`             bigint       NOT NULL,
    `comment_writer`           bigint       NOT NULL,
    `comment_content`          text         COLLATE utf8mb4_unicode_ci NOT NULL,
    `comment_super_comment_id` bigint       NOT NULL,
    `comment_comment_type`     enum('ANONYMOUS','NON_ANONYMOUS') COLLATE utf8mb4_unicode_ci NOT NULL,
    PRIMARY KEY (`comment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `presentation` (
    `presentation_id`         bigint      NOT NULL AUTO_INCREMENT,
    `created_date`            datetime(6) NOT NULL,
    `is_deleted`              tinyint(1)  NOT NULL DEFAULT 0,
    `updated_date`            datetime(6) NOT NULL,
    `presentation_program_id` bigint      NOT NULL,
    `presentation_team_id`    bigint      NOT NULL,
    PRIMARY KEY (`presentation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `program_rank_counter` (
    `program_rank_counter_id`        bigint NOT NULL,
    `program_rank_counter_next_rank` bigint NOT NULL,
    PRIMARY KEY (`program_rank_counter_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `semester_period` (
    `semester_period_id`         bigint      NOT NULL AUTO_INCREMENT,
    `created_date`               datetime(6) NOT NULL,
    `is_deleted`                 tinyint(1)  NOT NULL DEFAULT 0,
    `updated_date`               datetime(6) NOT NULL,
    `semester_period_start_date` datetime(6) NOT NULL,
    `semester_period_end_date`   datetime(6) NOT NULL,
    PRIMARY KEY (`semester_period_id`),
    KEY `idx_semester_period_created_date_id` (`created_date` DESC, `semester_period_id` DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `team` (
    `team_id`      bigint       NOT NULL AUTO_INCREMENT,
    `created_date` datetime(6)  NOT NULL,
    `is_deleted`   tinyint(1)   NOT NULL DEFAULT 0,
    `updated_date` datetime(6)  NOT NULL,
    `team_name`    varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `team_status`  tinyint(1)   NOT NULL DEFAULT 1,
    PRIMARY KEY (`team_id`),
    UNIQUE KEY `uk_team_name` (`team_name`),
    KEY `idx_team_name` (`team_name`),
    KEY `idx_team_status` (`team_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `weight_policy` (
    `weight_policy_id`          bigint       NOT NULL AUTO_INCREMENT,
    `created_date`              datetime(6)  NOT NULL,
    `is_deleted`                tinyint(1)   NOT NULL DEFAULT 0,
    `updated_date`              datetime(6)  NOT NULL,
    `weight_policy_sign_type`   enum('PLUS','MINUS') COLLATE utf8mb4_unicode_ci NOT NULL,
    `weight_policy_attend_type` enum('ATTEND','ABSENT','LATE','NONRESPONSE','NONRELATED') COLLATE utf8mb4_unicode_ci NOT NULL,
    `weight_policy_value`       int          NOT NULL,
    PRIMARY KEY (`weight_policy_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 기존 테이블의 누락 컬럼 (MySQL 8은 ADD COLUMN IF NOT EXISTS를 지원하지 않아 information_schema로 분기)

SET @sql = IF((SELECT COUNT(*) FROM information_schema.COLUMNS
               WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'member' AND COLUMN_NAME = 'member_is_admin') = 0,
    'ALTER TABLE `member` ADD COLUMN `member_is_admin` tinyint(1) NOT NULL DEFAULT 0',
    'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @sql = IF((SELECT COUNT(*) FROM information_schema.COLUMNS
               WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'member' AND COLUMN_NAME = 'member_department') = 0,
    'ALTER TABLE `member` ADD COLUMN `member_department` enum(''PRESIDENT'',''MARKETING'',''MANAGEMENT'',''EVENT'',''NONE'') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT ''NONE''',
    'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @sql = IF((SELECT COUNT(*) FROM information_schema.COLUMNS
               WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'program' AND COLUMN_NAME = 'program_url') = 0,
    'ALTER TABLE `program` ADD COLUMN `program_url` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT ''''',
    'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @sql = IF((SELECT COUNT(*) FROM information_schema.COLUMNS
               WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'program' AND COLUMN_NAME = 'program_attend_mode') = 0,
    'ALTER TABLE `program` ADD COLUMN `program_attend_mode` enum(''ATTEND'',''LATE'',''END'') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT ''END''',
    'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @sql = IF((SELECT COUNT(*) FROM information_schema.COLUMNS
               WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'attend' AND COLUMN_NAME = 'attend_rank') = 0,
    'ALTER TABLE `attend` ADD COLUMN `attend_rank` bigint DEFAULT NULL',
    'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @sql = IF((SELECT COUNT(*) FROM information_schema.COLUMNS
               WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'attend' AND COLUMN_NAME = 'attend_penalty_score') = 0,
    'ALTER TABLE `attend` ADD COLUMN `attend_penalty_score` int NOT NULL DEFAULT 0',
    'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- @Enumerated(STRING) 컬럼은 Hibernate 6에서 MySQL ENUM 타입으로 검증되므로 init 시 varchar로 생성된 컬럼을 ENUM으로 맞춘다.
ALTER TABLE `member`
    MODIFY COLUMN `member_oath_server_type` enum('SLACK','GITHUB','EEOS') COLLATE utf8mb4_unicode_ci NOT NULL,
    MODIFY COLUMN `member_active_status` enum('ALL','AM','CM','RM','OB') COLLATE utf8mb4_unicode_ci NOT NULL;

ALTER TABLE `program`
    MODIFY COLUMN `program_category` enum('ALL','WEEKLY','PRESIDENT_TEAM','EVENT_TEAM','ETC') COLLATE utf8mb4_unicode_ci NOT NULL,
    MODIFY COLUMN `program_type` enum('DEMAND','NOTIFICATION') COLLATE utf8mb4_unicode_ci NOT NULL;

ALTER TABLE `attend`
    MODIFY COLUMN `attend_status` enum('ATTEND','ABSENT','LATE','NONRESPONSE','NONRELATED') COLLATE utf8mb4_unicode_ci NOT NULL;

ALTER TABLE `team_building`
    MODIFY COLUMN `team_building_status` enum('PROGRESS','COMPLETE','END') COLLATE utf8mb4_unicode_ci NOT NULL;

ALTER TABLE `team_building_target`
    MODIFY COLUMN `team_building_input_status` enum('COMPLETE','INCOMPLETE') COLLATE utf8mb4_unicode_ci NOT NULL;
