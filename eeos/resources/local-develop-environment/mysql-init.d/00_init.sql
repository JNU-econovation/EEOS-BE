CREATE
    USER 'eeos-local'@'localhost' IDENTIFIED BY 'eeos-local';
CREATE
    USER 'eeos-local'@'%' IDENTIFIED BY 'eeos-local';

GRANT ALL PRIVILEGES ON *.* TO
    'eeos-local'@'localhost';
GRANT ALL PRIVILEGES ON *.* TO
    'eeos-local'@'%';

CREATE
    DATABASE eeos DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
