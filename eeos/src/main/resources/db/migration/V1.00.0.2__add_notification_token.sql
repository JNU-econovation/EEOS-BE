CREATE TABLE `notification_token` (
    `Notification_token_id` bigint NOT NULL AUTO_INCREMENT,
    `created_date` datetime NOT NULL,
    `is_deleted` tinyint(1) NOT NULL,
    `updated_date` datetime NOT NULL,
    `Notification_token_member_id` bigint NOT NULL,
    `Notification_token_provider` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `Notification_token_push_token` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `Notification_token_last_activate_at` datetime NOT NULL,
    `Notification_token_notification_permission` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
    PRIMARY KEY (`Notification_token_id`),
    UNIQUE KEY `uk_notification_token_push_token` (`Notification_token_push_token`),
    KEY `idx_notification_token_member_id` (`Notification_token_member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;