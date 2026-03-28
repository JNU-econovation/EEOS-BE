-- oauth_client_client_type 컬럼을 VARCHAR에서 ENUM으로 변경
-- V1.00.0.6 이 VARCHAR로 적용된 환경 대응
ALTER TABLE oauth_client
    MODIFY COLUMN oauth_client_client_type ENUM('WEB','APP') NOT NULL;
