-- OAuth2 Client 등록 테이블
CREATE TABLE oauth_client (
    oauth_client_id         BIGINT          NOT NULL AUTO_INCREMENT,
    oauth_client_client_id  VARCHAR(36)     NOT NULL,
    oauth_client_client_secret VARCHAR(255) NULL,
    oauth_client_client_name VARCHAR(100)   NOT NULL,
    oauth_client_client_type VARCHAR(10)    NOT NULL,
    created_date            DATETIME(6)     NOT NULL,
    updated_date            DATETIME(6)     NOT NULL,
    is_deleted              TINYINT(1)      NOT NULL DEFAULT 0,
    PRIMARY KEY (oauth_client_id),
    UNIQUE KEY uk_oauth_client_client_id (oauth_client_client_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- OAuth2 Client Redirect URI 테이블
CREATE TABLE oauth_client_redirect_uri (
    redirect_uri_id   BIGINT       NOT NULL AUTO_INCREMENT,
    oauth_client_id   BIGINT       NOT NULL,
    redirect_uri      VARCHAR(512) NOT NULL,
    PRIMARY KEY (redirect_uri_id),
    CONSTRAINT fk_redirect_uri_client
        FOREIGN KEY (oauth_client_id) REFERENCES oauth_client (oauth_client_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
