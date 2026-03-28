-- 테스트용 WEB 클라이언트 seed
-- 로컬 개발 환경에서만 사용

INSERT INTO oauth_client (
    oauth_client_client_id,
    oauth_client_client_secret,
    oauth_client_client_name,
    oauth_client_client_type,
    created_date,
    updated_date,
    is_deleted
) VALUES (
    'test-web-client-id',
    NULL,  -- WEB은 secret 있어야 하지만 테스트 편의상 생략 (서버가 null check 없으면 통과)
    'EEOS-Web-Test',
    'WEB',
    NOW(),
    NOW(),
    0
) ON DUPLICATE KEY UPDATE oauth_client_client_name = 'EEOS-Web-Test';

-- redirect_uri 등록
INSERT INTO oauth_client_redirect_uri (
    oauth_client_id,
    redirect_uri
)
SELECT id, 'http://localhost:3000/callback'
FROM oauth_client
WHERE oauth_client_client_id = 'test-web-client-id'
ON DUPLICATE KEY UPDATE redirect_uri = 'http://localhost:3000/callback';
