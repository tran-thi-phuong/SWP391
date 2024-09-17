-- Tạo cơ sở dữ liệu OnlineLearning
CREATE DATABASE OnlineLearning;
GO

-- Sử dụng cơ sở dữ liệu OnlineLearning
USE OnlineLearning;
GO

-- Tạo bảng Users
CREATE TABLE Users(
    UserID INT PRIMARY KEY IDENTITY(1,1),
    Username NVARCHAR(50) NOT NULL UNIQUE,
    Password NVARCHAR(255) NOT NULL,
    Name NVARCHAR(100) NOT NULL,
	Gender NVARCHAR(10),
    Phone NVARCHAR(15),
    Email NVARCHAR(100) NOT NULL,
	Address NVARCHAR(255),
	Avatar NVARCHAR(255),
    Role NVARCHAR(20) Not Null, 
	Status NVARCHAR(10) NOT NULL
);
GO

-- Tạo bảng Blog_Category
CREATE TABLE Blog_Category (
    Blog_CategoryID INT PRIMARY KEY IDENTITY(1,1),
    Title NVARCHAR(100) NOT NULL
);
GO

-- Tạo bảng Subject_Category
CREATE TABLE Subject_Category (
    Subject_CategoryID INT PRIMARY KEY IDENTITY(1,1),
    Title NVARCHAR(100) NOT NULL
);
GO

-- Tạo bảng Blogs
CREATE TABLE Blogs (
    BlogId INT PRIMARY KEY IDENTITY(1,1),
    UserID INT,
    Title NVARCHAR(255) NOT NULL,
    Content NVARCHAR(MAX) NOT NULL,
    Create_At DATETIME DEFAULT GETDATE(),
    Blog_CategoryID INT,
    FOREIGN KEY (UserID) REFERENCES Users(UserID),
    FOREIGN KEY (Blog_CategoryID) REFERENCES Blog_Category(Blog_CategoryID)
);
GO

-- Tạo bảng Subjects
CREATE TABLE Subjects (
    SubjectID INT PRIMARY KEY IDENTITY(1,1),
    Title NVARCHAR(255) NOT NULL,
    Description NVARCHAR(MAX),
    Subject_CategoryID INT,
    Status NVARCHAR(50),
    Thumbnail NVARCHAR(MAX),
    Update_Date Datetime,
    FOREIGN KEY (Subject_CategoryID) REFERENCES Subject_Category(Subject_CategoryID)
);
GO

-- Tạo bảng Tests
CREATE TABLE Tests (
    TestID INT PRIMARY KEY IDENTITY(1,1),
    SubjectID INT,
    Title NVARCHAR(255) NOT NULL,
    Description NVARCHAR(MAX),
    Type NVARCHAR(50),
    Duration INT,
    Pass_Condition DECIMAL(5, 2),
    Level NVARCHAR(50),
    Quantity INT,
    FOREIGN KEY (SubjectID) REFERENCES Subjects(SubjectID)
);
GO

-- Tạo bảng Lessons
CREATE TABLE Lessons (
    LessonID INT PRIMARY KEY IDENTITY(1,1),
    SubjectID INT,
    Title NVARCHAR(255) NOT NULL,
    Type NVARCHAR(50),
    Content NVARCHAR(MAX),
    Video_Link NVARCHAR(255),
    FOREIGN KEY (SubjectID) REFERENCES Subjects(SubjectID)
);
GO

-- Tạo bảng Package_Price
CREATE TABLE Package_Price (
    PackageID INT PRIMARY KEY IDENTITY(1,1),
    SubjectID INT,
    Name NVARCHAR(255) NOT NULL,
    Duration_time INT,
    Sale_Price DECIMAL(10, 2),
    Price DECIMAL(10, 2),
    FOREIGN KEY (SubjectID) REFERENCES Subjects(SubjectID)
);
GO
-- Tạo bảng Registrations
CREATE TABLE Registrations (
    RegistrationID INT PRIMARY KEY IDENTITY(1,1),
    UserID INT,
    SubjectID INT,
    PackageID INT,
    Registration_Time DATETIME DEFAULT GETDATE(),
    Valid_To DATE,
    Status NVARCHAR(50),
    FOREIGN KEY (UserID) REFERENCES Users(UserID),
    FOREIGN KEY (SubjectID) REFERENCES Subjects(SubjectID),
	FOREIGN KEY (PackageID) REFERENCES Package_Price(PackageID)
	
);
GO


-- Tạo bảng Sliders
CREATE TABLE Sliders (
    SliderID INT PRIMARY KEY IDENTITY(1,1),
    BlogID INT,
    SubjectID INT,
    Title NVARCHAR(255) NOT NULL,
    Image NVARCHAR(255),
    Content NVARCHAR(MAX),
    FOREIGN KEY (BlogID) REFERENCES Blogs(BlogID),
    FOREIGN KEY (SubjectID) REFERENCES Subjects(SubjectID)
);
GO

-- Tạo bảng Questions
CREATE TABLE Questions (
    QuestionID INT PRIMARY KEY IDENTITY(1,1),
    SubjectID INT,
    LessonID INT,
    Content NVARCHAR(MAX) NOT NULL,
    Level NVARCHAR(50),
    FOREIGN KEY (SubjectID) REFERENCES Subjects(SubjectID),
    FOREIGN KEY (LessonID) REFERENCES Lessons(LessonID)
);
GO

-- Tạo bảng Test_Question
CREATE TABLE Test_Question (
    TestID INT,
    QuestionID INT,
    PRIMARY KEY (TestID, QuestionID),
    FOREIGN KEY (TestID) REFERENCES Tests(TestID),
    FOREIGN KEY (QuestionID) REFERENCES Questions(QuestionID)
);
GO

-- Tạo bảng Answers
CREATE TABLE Answers (
    AnswerID INT PRIMARY KEY IDENTITY(1,1),
    QuestionID INT,
    Content NVARCHAR(MAX) NOT NULL,
    isCorrect BIT,
    FOREIGN KEY (QuestionID) REFERENCES Questions(QuestionID)
);
GO

-- Tạo bảng Result
CREATE TABLE Result (
    ResultID INT PRIMARY KEY IDENTITY(1,1),
    QuestionID INT,
    AttemptID INT,
	UserAnswer NVARCHAR(255),
    FOREIGN KEY (QuestionID) REFERENCES Questions(QuestionID),
);
GO

-- Tạo bảng User_Attempt
CREATE TABLE User_Attempt (
    AttemptID INT PRIMARY KEY IDENTITY(1,1),
    UserID INT,
    TestID INT,
    Duration INT,
    Mark DECIMAL(5, 2),
    FOREIGN KEY (UserID) REFERENCES Users(UserID),
    FOREIGN KEY (TestID) REFERENCES Tests(TestID)
);
GO

-- Tạo bảng Customer_Subject
CREATE TABLE Customer_Subject (
    UserID INT,
    SubjectID INT,
    Progress DECIMAL(5, 2),
    PRIMARY KEY (UserID, SubjectID),
    FOREIGN KEY (UserID) REFERENCES Users(UserID),
    FOREIGN KEY (SubjectID) REFERENCES Subjects(SubjectID)
);
GO

-- Tạo bảng Payment
CREATE TABLE Payment (
    PaymentID INT PRIMARY KEY IDENTITY(1,1),
    UserID INT,
    PaymentDate DATETIME DEFAULT GETDATE(),
    Amount DECIMAL(10, 2),
    PaymentMethod NVARCHAR(50),
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);
GO

-- Tạo bảng System_Setting
CREATE TABLE System_Setting (
    SettingID INT PRIMARY KEY IDENTITY(1,1),
    UserID INT,
    Setting_Key NVARCHAR(255) NOT NULL,
    Setting_Value NVARCHAR(255),
    Created_At DATETIME DEFAULT GETDATE(),
    Updated_At DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (UserId) REFERENCES Users(UserID)
);
GO