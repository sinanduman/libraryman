DROP TABLE IF EXISTS member;
CREATE TABLE member(
    member_id int not null auto_increment primary key,
    name VARCHAR(255),
    surname VARCHAR(255),
    email VARCHAR(255),
    phone_number VARCHAR(255),
    join_date VARCHAR(255)
);

DROP TABLE IF EXISTS book;
CREATE TABLE book(
    book_id int not null auto_increment primary key,
    isbn VARCHAR(255),
    title VARCHAR(255),
    author VARCHAR(255),
    category VARCHAR(255),
    publisher VARCHAR(255),
    publishing_date datetime,
    edition VARCHAR(255),
    available boolean
);

DROP TABLE IF EXISTS Librarian;
CREATE TABLE Librarian (
    user_id int not null auto_increment primary key,
    username VARCHAR(255),
    password VARCHAR(255),
    role VARCHAR(255)
);