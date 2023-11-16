use dataanalyticshub_db;
-- Table for user registration
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    Firstname VARCHAR(255) NOT NULL,
    Lastname VARCHAR(255) NOT NULL,
    Username VARCHAR(255) NOT NULL UNIQUE,
    Password VARCHAR(255) NOT NULL,
    VIPstatus VARCHAR(50) NOT NULL
);
select * from users;
UPDATE users SET Firstname = "a", Lastname = "b", Password = "123" WHERE Username = "ans07";
-- Table for user login history (optional)
CREATE TABLE login_history (
    login_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    login_time DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);
CREATE TABLE posts (
    PostID INT AUTO_INCREMENT PRIMARY KEY,
    Content TEXT,
    Likes INT,
    Author VARCHAR(255),
    DateTime DATETIME,
    Shares INT
);
select * from posts;
