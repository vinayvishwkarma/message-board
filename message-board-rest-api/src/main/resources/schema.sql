CREATE TABLE IF NOT EXISTS message (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content VARCHAR(1000) NOT NULL,
    sender VARCHAR(255) NOT NULL,
    url VARCHAR(255) NOT NULL
);