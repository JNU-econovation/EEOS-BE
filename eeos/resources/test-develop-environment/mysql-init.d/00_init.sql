CREATE
    USER 'eeos-test'@'localhost' IDENTIFIED BY 'eeos-test';
CREATE
    USER 'eeos-test'@'%' IDENTIFIED BY 'eeos-test';

GRANT ALL PRIVILEGES ON *.* TO
    'eeos-test'@'localhost';
GRANT ALL PRIVILEGES ON *.* TO
    'eeos-test'@'%';

CREATE
    DATABASE eeos DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
