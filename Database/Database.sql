DROP DATABASE OnlineLearning;

-- Tạo cơ sở dữ liệu OnlineLearning
CREATE DATABASE OnlineLearning;
GO

-- Sử dụng cơ sở dữ liệu OnlineLearning
USE OnlineLearning;
GO

-- Tạo bảng Users
CREATE TABLE Users(
    UserID INT PRIMARY KEY IDENTITY(1,1),
    Username NVARCHAR(50) UNIQUE,
    Password NVARCHAR(255) NOT NULL,
    Name NVARCHAR(100),
	Gender NVARCHAR(10),
    Phone NVARCHAR(15),
    Email NVARCHAR(100) NOT NULL,
	Address NVARCHAR(255),
	Avatar NVARCHAR(255),
    Role NVARCHAR(20) Not Null, 
	Status NVARCHAR(10) NOT NULL,
    Token NVARCHAR(MAX));
GO
CREATE TABLE VerificationCode (
    UserID INT NOT NULL primary key,
    Code VARCHAR(50) NOT NULL,
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);
go
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
CREATE TABLE Registrations (
    RegistrationID INT PRIMARY KEY IDENTITY(1,1),
    UserID INT,
    SubjectID INT,
    PackageID INT,
    Total_Cost NVARCHAR(15),
    Registration_Time DATETIME DEFAULT GETDATE(),
    Valid_From DATE,
    Valid_To DATE,
    Status NVARCHAR(50),
    StaffID INT,
    Note NVARCHAR(MAX),
    FOREIGN KEY (UserID) REFERENCES Users(UserID),
    FOREIGN KEY (SubjectID) REFERENCES Subjects(SubjectID),
    FOREIGN KEY (PackageID) REFERENCES Package_Price(PackageID),
    FOREIGN KEY (StaffID) REFERENCES Users(UserID),
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
CREATE TABLE Campaigns (
    CampaignID INT PRIMARY KEY IDENTITY(1,1),
    CampaignName NVARCHAR(255) NOT NULL,
    Description NVARCHAR(MAX),
    StartDate DATE,
    EndDate DATE,
    Image NVARCHAR(255),
	Status nvarchar(20) not null	
);
GO
CREATE TABLE Campaign_Subject (
    CampaignID INT,
    SubjectID INT,
	Discount INT,
    PRIMARY KEY (CampaignID, SubjectID),
    FOREIGN KEY (CampaignID) REFERENCES Campaigns(CampaignID),
    FOREIGN KEY (SubjectID) REFERENCES Subjects(SubjectID)
);
GO

INSERT INTO Subject_Category (Title) 
VALUES 
('Mathematics'),
('Science'),
('History');
GO

INSERT INTO Subjects (Title, Description, Subject_CategoryID, Status, Thumbnail, Update_Date) 
VALUES 
('Algebra', 'A subject focusing on algebraic expressions, equations, and functions.', 1, 'Active', 'algebra_thumbnail.jpg', '2024-01-10 09:30:00'),
('Geometry', 'Study of shapes, sizes, and properties of space.', 1, 'Active', 'geometry_thumbnail.jpg', '2024-02-15 14:45:00'),
('Biology', 'Introduction to the study of life and living organisms.', 2, 'Active', 'biology_thumbnail.jpg', '2024-03-20 11:00:00'),
('Physics', 'Study of matter, energy, and their interactions.', 2, 'Active', 'physics_thumbnail.jpg', '2024-04-25 16:20:00'),
('Chemistry', 'Exploring the properties, composition, and behavior of matter.', 2, 'Inactive', 'chemistry_thumbnail.jpg', '2024-05-30 08:15:00'),
('World History', 'A comprehensive overview of global historical events and trends.', 3, 'Active', 'world_history_thumbnail.jpg', '2024-06-05 13:50:00'),
('Ancient Civilizations', 'Study of ancient cultures and civilizations.', 3, 'Active', 'ancient_civilizations_thumbnail.jpg', '2024-07-10 17:30:00'),
('Calculus', 'Advanced mathematics focused on limits, functions, derivatives, and integrals.', 1, 'Inactive', 'calculus_thumbnail.jpg', '2024-08-15 09:40:00'),
('Environmental Science', 'Study of the environment and solutions to environmental problems.', 2, 'Active', 'environmental_science_thumbnail.jpg', '2024-09-20 12:10:00'),
('Modern History', 'Study of the most recent historical events and developments.', 3, 'Inactive', 'modern_history_thumbnail.jpg', '2024-10-25 14:05:00');
GO
