use eeos;

ALTER TABLE member
    ADD COLUMN member_oath_server_type VARCHAR(255) NOT NULL;
ALTER TABLE member
    ADD COLUMN member_active_status VARCHAR(255) NOT NULL;
ALTER TABLE member
    DROP COLUMN member_generation;
CREATE INDEX idx_member_active_status ON member (member_active_status);
CREATE INDEX idx_member_name ON member (member_name);
DROP INDEX idx_generation_name ON member;

CREATE INDEX idx_attend_status ON attend (attend_status);
