drop database OnlineLearning
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
	Create_At DATETIME DEFAULT GETDATE(),
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
	Status NVARCHAR(10) NOT NULL DEFAULT 'Show',
    FOREIGN KEY (UserID) REFERENCES Users(UserID),
    FOREIGN KEY (Blog_CategoryID) REFERENCES Blog_Category(Blog_CategoryID)
);
GO

-- Tạo bảng Subjects
CREATE TABLE Subjects (
    SubjectID INT PRIMARY KEY IDENTITY(1,1),
	OwnerID INT,
    Title NVARCHAR(255) NOT NULL,
    Description NVARCHAR(MAX),
    Subject_CategoryID INT,
    Status NVARCHAR(50),
    Thumbnail NVARCHAR(MAX),
    Update_Date Datetime,
	FOREIGN KEY (OwnerID) REFERENCES Users(UserID),
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
CREATE TABLE TestMedia (
    MediaID INT PRIMARY KEY IDENTITY(1,1),
    Media_Link NVARCHAR(MAX),
    TestID INT,
    Description NVARCHAR(50),
    FOREIGN KEY (TestID) REFERENCES Tests(TestID)
);
GO
CREATE TABLE LessonTopic (
    TopicID INT PRIMARY KEY IDENTITY(1,1),
    Name NVARCHAR(50)
);
GO

-- Tạo bảng Lessons
CREATE TABLE Lessons (
    LessonID INT PRIMARY KEY IDENTITY(1,1),
    SubjectID INT,
    Title NVARCHAR(255) NOT NULL,
    TopicID INT,
    Content NVARCHAR(MAX),
	[Order] int,
	Description NVARCHAR(255),
	Status NVARCHAR(50),
    FOREIGN KEY (SubjectID) REFERENCES Subjects(SubjectID),
	FOREIGN KEY (TopicID) REFERENCES LessonTopic(TopicID)
);
GO
CREATE TABLE LessonMedia (
    MediaID INT PRIMARY KEY IDENTITY(1,1),
	Media_Link NVARCHAR(MAX),
	LessonID INT,
    Description NVARCHAR(50),
	FOREIGN KEY (LessonID) REFERENCES Lessons(LessonID)
);
GO
CREATE TABLE Subject_LessonTopic (
    TopicID INT,
    SubjectID INT,
	[Order] INT,
	FOREIGN KEY (TopicID) REFERENCES LessonTopic(TopicID),
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
    FOREIGN KEY (StaffID) REFERENCES Users(UserID)
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
    LessonID INT,
	Status NVARCHAR(50) NOT NULL,
    Content NVARCHAR(MAX) NOT NULL,
    Level NVARCHAR(50),
    FOREIGN KEY (LessonID) REFERENCES Lessons(LessonID)
);
GO

CREATE TABLE QuestionMedia (
    MediaID INT PRIMARY KEY IDENTITY(1,1),
    QuestionID INT NOT NULL,
    MediaLink NVARCHAR(MAX) NOT NULL,
    Description NVARCHAR(255),
    FOREIGN KEY (QuestionID) REFERENCES Questions(QuestionID)
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
Explaination NVARCHAR(MAX) NOT NULL,
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
    FOREIGN KEY (QuestionID) REFERENCES Questions(QuestionID)
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
	SubjectID int,
    PaymentDate DATETIME DEFAULT GETDATE(),
    Amount DECIMAL(10, 2),
    PaymentMethod NVARCHAR(50),
    FOREIGN KEY (UserID) REFERENCES Users(UserID),
	FOREIGN KEY (SubjectID) REFERENCES Subjects(SubjectID)
);
GO

-- Tạo bảng System_Setting
CREATE TABLE System_Setting (
    SettingID INT PRIMARY KEY IDENTITY(1,1),
    UserID INT,
    QuizID BIT DEFAULT 0,                 -- Quiz ID (default selected, true/false)
    Title BIT NOT NULL DEFAULT 1,          -- Name (default selected, not editable)
    Subject BIT DEFAULT 0,  
    Description BIT DEFAULT 0,
    QuizType BIT DEFAULT 0,
    Duration BIT DEFAULT 0, 
    PassCondition BIT DEFAULT 0, 
    Level BIT DEFAULT 0,                   
    Quantity BIT DEFAULT 0,     
    PassRate BIT DEFAULT 0,
	RegistrationID BIT DEFAULT 0,
	CustomerEmail BIT DEFAULT 0,
	PackageID BIT DEFAULT 0,
	Total_Cost BIT DEFAULT 0,
	Registration_Time BIT DEFAULT 0,
	Valid_From BIT DEFAULT 0,
	Valid_To BIT DEFAULT 0,
	Status BIT DEFAULT 0,
	Staff BIT DEFAULT 0,
	Note BIT DEFAULT 0,
	campaign BIT DEFAULT 0,
ShowContent BIT NOT NULL DEFAULT 1,
    ShowLessonID BIT NOT NULL DEFAULT 1,
    ShowStatus BIT NOT NULL DEFAULT 1,
    ShowLevel BIT NOT NULL DEFAULT 1,                
    NumberOfItems INT DEFAULT 10,          -- Number of items per page
    Created_At DATETIME DEFAULT GETDATE(),
    Updated_At DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);
GO
CREATE TABLE Campaigns (
    CampaignID INT PRIMARY KEY IDENTITY(1,1),
    CampaignName NVARCHAR(255) NOT NULL,
    Description NVARCHAR(MAX),
    StartDate DATE,
    EndDate DATE,
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
CREATE TABLE CampaignMedia (
    MediaID INT PRIMARY KEY IDENTITY(1,1),
    CampaignID INT NOT NULL,
    MediaLink NVARCHAR(MAX) NOT NULL,
    Description NVARCHAR(255),
    FOREIGN KEY (CampaignID) REFERENCES Campaigns(CampaignID)
);
GO
CREATE TABLE Pages (
    PageID INT PRIMARY KEY IDENTITY(1,1),
    PageName NVARCHAR(100) NOT NULL,
    PageUrl NVARCHAR(255) NOT NULL,
    Status NVARCHAR(10) NOT NULL,
    Update_At DATETIME DEFAULT GETDATE()
);
CREATE TABLE Role_Permission (
    RolePermissionID INT PRIMARY KEY IDENTITY(1,1),
    Role NVARCHAR(50) NOT NULL,  
    PageID INT NOT NULL,
    Update_At DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (PageID) REFERENCES Pages(PageID)
);

CREATE TABLE Lesson_User (
    LessonID INT,
    UserID INT,
    Status NVARCHAR(50) NOT NULL, 
    PRIMARY KEY (LessonID, UserID),
    FOREIGN KEY (LessonID) REFERENCES Lessons(LessonID),
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);
GO

CREATE TABLE Blog_Media (
    MediaID INT PRIMARY KEY IDENTITY(1,1),
    BlogID INT NOT NULL,
    MediaType NVARCHAR(10), -- 'image' hoặc 'video'
    MediaLink NVARCHAR(MAX), -- Đường dẫn đến ảnh hoặc video
    Description NVARCHAR(255),
    FOREIGN KEY (BlogID) REFERENCES Blogs(BlogId) ON DELETE CASCADE
);
GO


