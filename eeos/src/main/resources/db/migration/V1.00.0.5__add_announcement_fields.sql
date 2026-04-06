ALTER TABLE announcement
    ADD COLUMN announcement_announced_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    ADD COLUMN announcement_deadline DATE NULL;
