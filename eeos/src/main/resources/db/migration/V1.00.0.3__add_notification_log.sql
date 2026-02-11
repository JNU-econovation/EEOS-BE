CREATE TABLE `notification_log` (
                                   `notification_log_id` bigint NOT NULL AUTO_INCREMENT,
                                   `created_date` datetime NOT NULL,
                                   `is_deleted` tinyint(1) NOT NULL,
                                   `updated_date` datetime NOT NULL,
                                   `notification_log_calendar_id` bigint NOT NULL,
                                   `notification_log_push_token` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                                   `notification_log_category` varchar(50) NOT NULL,
                                   `notification_log_title` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                                   `notification_log_body` text COLLATE utf8mb4_unicode_ci NOT NULL,
                                   `notification_log_notification_status` varchar(50) NOT NULL,
                                   `notification_log_error_code` varchar(50) NULL,
                                   `notification_log_scheduled_at` datetime NOT NULL,
                                   `notification_log_sent_at` datetime NULL,
                                   `notification_log_provider` varchar(50) NOT NULL,
                                   PRIMARY KEY (`notification_log_id`),
                                   KEY `idx_notification_log_calendar_id` (`notification_log_calendar_id`),
                                   KEY `idx_notification_log_status` (`notification_log_notification_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
