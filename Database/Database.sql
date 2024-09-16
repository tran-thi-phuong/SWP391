-- Tạo cơ sở dữ liệu OnlineLearning
CREATE DATABASE OnlineLearning;
GO

-- Sử dụng cơ sở dữ liệu OnlineLearning
USE OnlineLearning;
GO

-- Tạo bảng User
CREATE TABLE Users (
    UserId INT PRIMARY KEY IDENTITY(1,1),
    username NVARCHAR(50) NOT NULL UNIQUE,
    password NVARCHAR(255) NOT NULL,
    name NVARCHAR(100) NOT NULL,
	gender NVARCHAR(10) NOT NULL,
    phone NVARCHAR(15),
    email NVARCHAR(100) NOT NULL,
	address NVARCHAR(255) NOT NULL,
	avatar NVARCHAR(255) NOT NULL,
    role NVARCHAR(20) Not Null, 
	status NVARCHAR(10) NOT NULL,
);
GO

-- Tạo bảng Blog_Category
CREATE TABLE Blog_Category (
    Blog_CategoryId INT PRIMARY KEY IDENTITY(1,1),
    title NVARCHAR(100) NOT NULL
);
GO

-- Tạo bảng Subject_Category
CREATE TABLE Subject_Category (
    Subject_CategoryId INT PRIMARY KEY IDENTITY(1,1),
    title NVARCHAR(100) NOT NULL
);
GO

-- Tạo bảng Blog
CREATE TABLE Blog (
    BlogId INT PRIMARY KEY IDENTITY(1,1),
    UserId INT,
    title NVARCHAR(255) NOT NULL,
    content NVARCHAR(MAX) NOT NULL,
    create_at DATETIME DEFAULT GETDATE(),
    Blog_CategoryId INT,
    FOREIGN KEY (UserId) REFERENCES Users(UserId),
    FOREIGN KEY (Blog_CategoryId) REFERENCES Blog_Category(Blog_CategoryId)
);
GO

-- Tạo bảng Subject
CREATE TABLE Subjects (
    SubjectId INT PRIMARY KEY IDENTITY(1,1),
    title NVARCHAR(255) NOT NULL,
    description NVARCHAR(MAX),
    Subject_CategoryId INT,
    status NVARCHAR(50),
    thumbnail NVARCHAR(MAX),
    FOREIGN KEY (Subject_CategoryId) REFERENCES Subject_Category(Subject_CategoryId)
);
GO

-- Tạo bảng Test
CREATE TABLE Test (
    TestId INT PRIMARY KEY IDENTITY(1,1),
    SubjectId INT,
    title NVARCHAR(255) NOT NULL,
    description NVARCHAR(MAX),
    type NVARCHAR(50),
    duration INT,
    pass_rate DECIMAL(5, 2),
    level NVARCHAR(50),
    quantity INT,
    FOREIGN KEY (SubjectId) REFERENCES Subjects(SubjectId)
);
GO

-- Tạo bảng Lesson
CREATE TABLE Lesson (
    LessonId INT PRIMARY KEY IDENTITY(1,1),
    SubjectId INT,
    title NVARCHAR(255) NOT NULL,
    type NVARCHAR(50),
    content NVARCHAR(MAX),
    video_link NVARCHAR(255),
    FOREIGN KEY (SubjectId) REFERENCES Subjects(SubjectId)
);
GO

-- Tạo bảng Package_Price
CREATE TABLE Package_Price (
    PackageId INT PRIMARY KEY IDENTITY(1,1),
    SubjectId INT,
    name NVARCHAR(255) NOT NULL,
    duration_time INT,
    sale_price DECIMAL(10, 2),
    price DECIMAL(10, 2),
    FOREIGN KEY (SubjectId) REFERENCES Subjects(SubjectId)
);
GO
-- Tạo bảng Registrations
CREATE TABLE Registrations (
    RegistrationId INT PRIMARY KEY IDENTITY(1,1),
    UserId INT,
    SubjectId INT,
    PackageId INT,
    registration_time DATETIME DEFAULT GETDATE(),
    valid_to DATE,
    status NVARCHAR(50),
    FOREIGN KEY (UserId) REFERENCES Users(UserId),
    FOREIGN KEY (SubjectId) REFERENCES Subjects(SubjectId),
	FOREIGN KEY (PackageId) REFERENCES Package_Price(PackageId)
	
);
GO


-- Tạo bảng Slider
CREATE TABLE Slider (
    SliderId INT PRIMARY KEY IDENTITY(1,1),
    BlogId INT,
    SubjectId INT,
    title NVARCHAR(255) NOT NULL,
    image NVARCHAR(255),
    content NVARCHAR(MAX),
    FOREIGN KEY (BlogId) REFERENCES Blog(BlogId),
    FOREIGN KEY (SubjectId) REFERENCES Subjects(SubjectId)
);
GO

-- Tạo bảng Question
CREATE TABLE Question (
    QuestionId INT PRIMARY KEY IDENTITY(1,1),
    SubjectId INT,
    LessonId INT,
    content NVARCHAR(MAX) NOT NULL,
    level NVARCHAR(50),
    FOREIGN KEY (SubjectId) REFERENCES Subjects(SubjectId),
    FOREIGN KEY (LessonId) REFERENCES Lesson(LessonId)
);
GO

-- Tạo bảng Test_Question
CREATE TABLE Test_Question (
    TestId INT,
    QuestionId INT,
    PRIMARY KEY (TestId, QuestionId),
    FOREIGN KEY (TestId) REFERENCES Test(TestId),
    FOREIGN KEY (QuestionId) REFERENCES Question(QuestionId)
);
GO

-- Tạo bảng Answer
CREATE TABLE Answer (
    AnswerId INT PRIMARY KEY IDENTITY(1,1),
    QuestionId INT,
    content NVARCHAR(MAX) NOT NULL,
    isCorrect BIT,
    FOREIGN KEY (QuestionId) REFERENCES Question(QuestionId)
);
GO

-- Tạo bảng Result
CREATE TABLE Result (
    ResultId INT PRIMARY KEY IDENTITY(1,1),
    QuestionId INT,
    AttemptId INT,
	UserAnswer NVARCHAR(255),
    FOREIGN KEY (QuestionId) REFERENCES Question(QuestionId),
);
GO

-- Tạo bảng User_Attempt
CREATE TABLE User_Attempt (
    AttemptId INT PRIMARY KEY IDENTITY(1,1),
    UserId INT,
    TestId INT,
    duration INT,
    mark DECIMAL(5, 2),
    FOREIGN KEY (UserId) REFERENCES Users(UserId),
    FOREIGN KEY (TestId) REFERENCES Test(TestId)
);
GO

-- Tạo bảng Customer_Subject
CREATE TABLE Customer_Subject (
    UserId INT,
    SubjectId INT,
    progress DECIMAL(5, 2),
    PRIMARY KEY (UserId, SubjectId),
    FOREIGN KEY (UserId) REFERENCES Users(UserId),
    FOREIGN KEY (SubjectId) REFERENCES Subjects(SubjectId)
);
GO

-- Tạo bảng Payment
CREATE TABLE Payment (
    PaymentId INT PRIMARY KEY IDENTITY(1,1),
    UserId INT,
    PaymentDate DATETIME DEFAULT GETDATE(),
    Amount DECIMAL(10, 2),
    PaymentMethod NVARCHAR(50),
    FOREIGN KEY (UserId) REFERENCES Users(UserId)
);
GO

-- Tạo bảng System_Setting
CREATE TABLE System_Setting (
    SettingId INT PRIMARY KEY IDENTITY(1,1),
    UserId INT,
    setting_key NVARCHAR(255) NOT NULL,
    setting_value NVARCHAR(255),
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (UserId) REFERENCES Users(UserId)
);
GO
