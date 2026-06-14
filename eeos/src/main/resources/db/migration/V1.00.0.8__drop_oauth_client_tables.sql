-- OAuth 클라이언트 관리를 auth-api(Spring Authorization Server)로 이전
-- EEOS-BE 자체 OAuth2 서버 기능 제거에 따라 관련 테이블 삭제

ALTER TABLE oauth_client_redirect_uri
    DROP FOREIGN KEY fk_redirect_uri_client;

DROP TABLE IF EXISTS oauth_client_redirect_uri;
DROP TABLE IF EXISTS oauth_client;
