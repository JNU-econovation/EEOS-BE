CREATE TABLE `notification_log` (
                                   `NotificationLog_id` bigint NOT NULL AUTO_INCREMENT,
                                   `created_date` datetime NOT NULL,
                                   `is_deleted` tinyint(1) NOT NULL,
                                   `updated_date` datetime NOT NULL,
                                   `NotificationLog_calendar_id` bigint NOT NULL,
                                   `NotificationLog_push_token` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                                   `NotificationLog_category` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
                                   `NotificationLog_title` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                                   `NotificationLog_body` text COLLATE utf8mb4_unicode_ci NOT NULL,
                                   `NotificationLog_notification_status` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
                                   `NotificationLog_error_code` varchar(50) COLLATE utf8mb4_unicode_ci NULL,
                                   `NotificationLog_scheduled_at` datetime NOT NULL,
                                   `NotificationLog_sent_at` datetime NULL,
                                   `NotificationLog_provider` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
                                   PRIMARY KEY (`NotificationLog_id`),
                                   KEY `idx_notification_log_calendar_id` (`NotificationLog_calendar_id`),
                                   KEY `idx_notification_log_status` (`NotificationLog_notification_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
