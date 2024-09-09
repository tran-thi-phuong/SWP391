
CREATE DATABASE OnlineLearning;
GO
USE OnlineLearning;

CREATE TABLE Account (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(255) NOT NULL
);

INSERT INTO Account (username, password) VALUES ('a@gmail.com', '123');