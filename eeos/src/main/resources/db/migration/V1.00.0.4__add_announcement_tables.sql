CREATE TABLE slack_announce_event (
    slack_announce_event_id       BIGINT       NOT NULL AUTO_INCREMENT,
    slack_announce_event_event_id VARCHAR(255) NOT NULL,
    slack_announce_event_team_id  VARCHAR(50)  NOT NULL,
    slack_announce_event_channel_id VARCHAR(50) NOT NULL,
    slack_announce_event_user_id  VARCHAR(50)  NOT NULL,
    slack_announce_event_message_ts VARCHAR(50) NOT NULL,
    created_date  DATETIME(6) NOT NULL,
    updated_date  DATETIME(6) NOT NULL,
    is_deleted    TINYINT(1)  NOT NULL DEFAULT 0,
    CONSTRAINT pk_slack_announce_event PRIMARY KEY (slack_announce_event_id),
    CONSTRAINT uq_slack_announce_event_event_id UNIQUE (slack_announce_event_event_id),
    KEY idx_slack_announce_event_created_date (created_date)
);

CREATE TABLE announcement (
    announcement_id                      BIGINT       NOT NULL AUTO_INCREMENT,
    announcement_slack_announce_event_id BIGINT       NOT NULL,
    announcement_title                   VARCHAR(255) NULL,
    announcement_body                    TEXT         NOT NULL,
    created_date  DATETIME(6) NOT NULL,
    updated_date  DATETIME(6) NOT NULL,
    is_deleted    TINYINT(1)  NOT NULL DEFAULT 0,
    CONSTRAINT pk_announcement PRIMARY KEY (announcement_id),
    CONSTRAINT uq_announcement_event_id UNIQUE (announcement_slack_announce_event_id),
    KEY idx_announcement_created_date (created_date)
);
