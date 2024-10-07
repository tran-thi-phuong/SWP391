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
	UserID INT,
    Title NVARCHAR(255) NOT NULL,
    Description NVARCHAR(MAX),
    Subject_CategoryID INT,
    Status NVARCHAR(50),
    Thumbnail NVARCHAR(MAX),
    Update_Date Datetime,
	FOREIGN KEY (UserID) REFERENCES Users(UserID),
    FOREIGN KEY (Subject_CategoryID) REFERENCES Subject_Category(Subject_CategoryID)
);
GO

-- Tạo bảng Tests
CREATE TABLE Tests (
    TestID INT PRIMARY KEY IDENTITY(1,1),
    SubjectID INT,
    Title NVARCHAR(255) NOT NULL,
    Description NVARCHAR(MAX),
    MediaType NVARCHAR(10), -- 'image' or 'video'
    MediaURL NVARCHAR(MAX),  -- URL to the image or video
    MediaDescription NVARCHAR(MAX), -- Description of the media
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

INSERT INTO Users(Username, Password, Name, Gender, Email, Address, Avatar, Role, Status)
VALUES('moew', 'Moew1274@', 'Phuong', 'Female', 'phuongthai12070427@gmail.com', 'Ha Noi','hinh-nen-may-tinh-chibi-4k-co-gai-cute.jpeg', 'Sale', 'Active' );
GO
INSERT INTO Users (Username, Password, Name, Gender, Email, Address, Avatar, Role, Status)
VALUES
('customer1', 'password1', 'Customer One', 'Female', 'customer1@example.com', 'Address 1', 'avatar1.jpg', 'Customer', 'Active'),
('customer2', 'password2', 'Customer Two', 'Male', 'customer2@example.com', 'Address 2', 'avatar2.jpg', 'Customer', 'Active'),
('customer3', 'password3', 'Customer Three', 'Female', 'customer3@example.com', 'Address 3', 'avatar3.jpg', 'Customer', 'Active'),
('customer4', 'password4', 'Customer Four', 'Male', 'customer4@example.com', 'Address 4', 'avatar4.jpg', 'Customer', 'Active'),
('customer5', 'password5', 'Customer Five', 'Female', 'customer5@example.com', 'Address 5', 'avatar5.jpg', 'Customer', 'Active'),
('customer6', 'password6', 'Customer Six', 'Male', 'customer6@example.com', 'Address 6', 'avatar6.jpg', 'Customer', 'Active'),
('customer7', 'password7', 'Customer Seven', 'Female', 'customer7@example.com', 'Address 7', 'avatar7.jpg', 'Customer', 'Active'),
('customer8', 'password8', 'Customer Eight', 'Male', 'customer8@example.com', 'Address 8', 'avatar8.jpg', 'Customer', 'Active'),
('customer9', 'password9', 'Customer Nine', 'Female', 'customer9@example.com', 'Address 9', 'avatar9.jpg', 'Customer', 'Active'),
('customer10', 'password10', 'Customer Ten', 'Male', 'customer10@example.com', 'Address 10', 'avatar10.jpg', 'Customer', 'Active'),
('customer11', 'password11', 'Customer Eleven', 'Female', 'customer11@example.com', 'Address 11', 'avatar11.jpg', 'Customer', 'Active'),
('customer12', 'password12', 'Customer Twelve', 'Male', 'customer12@example.com', 'Address 12', 'avatar12.jpg', 'Customer', 'Active'),
('customer13', 'password13', 'Customer Thirteen', 'Female', 'customer13@example.com', 'Address 13', 'avatar13.jpg', 'Customer', 'Active'),
('customer14', 'password14', 'Customer Fourteen', 'Male', 'customer14@example.com', 'Address 14', 'avatar14.jpg', 'Customer', 'Active'),
('customer15', 'password15', 'Customer Fifteen', 'Female', 'customer15@example.com', 'Address 15', 'avatar15.jpg', 'Customer', 'Active'),
('customer16', 'password16', 'Customer Sixteen', 'Male', 'customer16@example.com', 'Address 16', 'avatar16.jpg', 'Customer', 'Active'),
('customer17', 'password17', 'Customer Seventeen', 'Female', 'customer17@example.com', 'Address 17', 'avatar17.jpg', 'Customer', 'Active'),
('customer18', 'password18', 'Customer Eighteen', 'Male', 'customer18@example.com', 'Address 18', 'avatar18.jpg', 'Customer', 'Active'),
('customer19', 'password19', 'Customer Nineteen', 'Female', 'customer19@example.com', 'Address 19', 'avatar19.jpg', 'Customer', 'Active'),
('customer20', 'password20', 'Customer Twenty', 'Male', 'customer20@example.com', 'Address 20', 'avatar20.jpg', 'Customer', 'Active'),
('customer21', 'password21', 'Customer Twenty-One', 'Female', 'customer21@example.com', 'Address 21', 'avatar21.jpg', 'Customer', 'Active'),
('customer22', 'password22', 'Customer Twenty-Two', 'Male', 'customer22@example.com', 'Address 22', 'avatar22.jpg', 'Customer', 'Active'),
('customer23', 'password23', 'Customer Twenty-Three', 'Female', 'customer23@example.com', 'Address 23', 'avatar23.jpg', 'Customer', 'Active'),
('customer24', 'password24', 'Customer Twenty-Four', 'Male', 'customer24@example.com', 'Address 24', 'avatar24.jpg', 'Customer', 'Active'),
('customer25', 'password25', 'Customer Twenty-Five', 'Female', 'customer25@example.com', 'Address 25', 'avatar25.jpg', 'Customer', 'Active'),
('customer26', 'password26', 'Customer Twenty-Six', 'Male', 'customer26@example.com', 'Address 26', 'avatar26.jpg', 'Customer', 'Active'),
('customer27', 'password27', 'Customer Twenty-Seven', 'Female', 'customer27@example.com', 'Address 27', 'avatar27.jpg', 'Customer', 'Active'),
('customer28', 'password28', 'Customer Twenty-Eight', 'Male', 'customer28@example.com', 'Address 28', 'avatar28.jpg', 'Customer', 'Active'),
('customer29', 'password29', 'Customer Twenty-Nine', 'Female', 'customer29@example.com', 'Address 29', 'avatar29.jpg', 'Customer', 'Active'),
('customer30', 'password30', 'Customer Thirty', 'Male', 'customer30@example.com', 'Address 30', 'avatar30.jpg', 'Customer', 'Active'),
('customer31', 'password31', 'Customer Thirty-One', 'Female', 'customer31@example.com', 'Address 31', 'avatar31.jpg', 'Customer', 'Active'),
('customer32', 'password32', 'Customer Thirty-Two', 'Male', 'customer32@example.com', 'Address 32', 'avatar32.jpg', 'Customer', 'Active'),
('customer33', 'password33', 'Customer Thirty-Three', 'Female', 'customer33@example.com', 'Address 33', 'avatar33.jpg', 'Customer', 'Active'),
('customer34', 'password34', 'Customer Thirty-Four', 'Male', 'customer34@example.com', 'Address 34', 'avatar34.jpg', 'Customer', 'Active'),
('customer35', 'password35', 'Customer Thirty-Five', 'Female', 'customer35@example.com', 'Address 35', 'avatar35.jpg', 'Customer', 'Active'),
('customer36', 'password36', 'Customer Thirty-Six', 'Male', 'customer36@example.com', 'Address 36', 'avatar36.jpg', 'Customer', 'Active'),
('customer37', 'password37', 'Customer Thirty-Seven', 'Female', 'customer37@example.com', 'Address 37', 'avatar37.jpg', 'Customer', 'Active'),
('customer38', 'password38', 'Customer Thirty-Eight', 'Male', 'customer38@example.com', 'Address 38', 'avatar38.jpg', 'Customer', 'Active'),
('customer39', 'password39', 'Customer Thirty-Nine', 'Female', 'customer39@example.com', 'Address 39', 'avatar39.jpg', 'Customer', 'Active'),
('customer40', 'password40', 'Customer Forty', 'Male', 'customer40@example.com', 'Address 40', 'avatar40.jpg', 'Customer', 'Active');

INSERT INTO Users (Username, Password, Name, Gender, Email, Address, Avatar, Role, Status)
VALUES
('sale1', 'password1', 'Sale One', 'Female', 'sale1@example.com', 'Address 1', 'avatar1.jpg', 'Sale', 'Active'),
('sale2', 'password2', 'Sale Two', 'Male', 'sale2@example.com', 'Address 2', 'avatar2.jpg', 'Sale', 'Active'),
('sale3', 'password3', 'Sale Three', 'Female', 'sale3@example.com', 'Address 3', 'avatar3.jpg', 'Sale', 'Active');


INSERT INTO Subject_Category (Title) 
VALUES 
('Mathematics'),
('Science'),
('History');
GO

INSERT INTO Subjects (Title,UserID, Description, Subject_CategoryID, Status, Thumbnail, Update_Date) 
VALUES 
('Algebra',1, 'A subject focusing on algebraic expressions, equations, and functions.', 1, 'Active', 'algebra_thumbnail.jpg', '2024-01-10 09:30:00'),
('Geometry',1, 'Study of shapes, sizes, and properties of space.', 1, 'Active', 'geometry_thumbnail.jpg', '2024-02-15 14:45:00'),
('Biology',2, 'Introduction to the study of life and living organisms.', 2, 'Active', 'biology_thumbnail.jpg', '2024-03-20 11:00:00'),
('Physics',3, 'Study of matter, energy, and their interactions.', 2, 'Active', 'physics_thumbnail.jpg', '2024-04-25 16:20:00'),
('Chemistry',2, 'Exploring the properties, composition, and behavior of matter.', 2, 'Inactive', 'chemistry_thumbnail.jpg', '2024-05-30 08:15:00'),
('World History',4, 'A comprehensive overview of global historical events and trends.', 3, 'Active', 'world_history_thumbnail.jpg', '2024-06-05 13:50:00'),
('Ancient Civilizations',2, 'Study of ancient cultures and civilizations.', 3, 'Active', 'ancient_civilizations_thumbnail.jpg', '2024-07-10 17:30:00'),
('Calculus',1, 'Advanced mathematics focused on limits, functions, derivatives, and integrals.', 1, 'Inactive', 'calculus_thumbnail.jpg', '2024-08-15 09:40:00'),
('Environmental Science',5, 'Study of the environment and solutions to environmental problems.', 2, 'Active', 'environmental_science_thumbnail.jpg', '2024-09-20 12:10:00'),
('Modern History',1, 'Study of the most recent historical events and developments.', 3, 'Inactive', 'modern_history_thumbnail.jpg', '2024-10-25 14:05:00');
GO
INSERT INTO Package_Price (SubjectID, Name, Duration_time, Sale_Price, Price)
VALUES 
(1, '3 Months', 90, 49.99, 59.99),
(2, '1 Months', 30, 79.99, 89.99),
(3, '2 Months', 60, 69.99, 79.99);
Go
INSERT INTO Users(Username, Password, Name, Gender, Email, Address, Avatar, Role, Status)
VALUES('hoa', '123', 'Phuong', 'Female', 'phuongthai12070427@gmail.com', 'Ha Noi','hinh-nen-may-tinh-chibi-4k-co-gai-cute.jpeg', 'Customer', 'Active' );
GO
INSERT INTO Users(Username, Password, Name, Gender, Email, Address, Avatar, Role, Status)
VALUES('lan', '123', 'Phuong', 'Female', 'phuongthai12070427@gmail.com', 'Ha Noi','hinh-nen-may-tinh-chibi-4k-co-gai-cute.jpeg', 'Customer', 'Active' );
GO

INSERT INTO Registrations (UserID, SubjectID, PackageID, Total_Cost, Registration_Time, Valid_From, Valid_To, Status, StaffID, Note)
VALUES 
(2, 2, 2, '89.99', GETDATE(), '2024-09-02', '2024-10-02', 'Expired', 1, 'Second registration'),
(3, 3, 3, '79.99', GETDATE(), '2024-09-01', '2024-11-01', 'Active', 1, 'Third registration');

INSERT INTO Registrations (UserID, SubjectID, PackageID, Total_Cost, Registration_Time, Valid_From, Valid_To, Status, StaffID, Note)
VALUES
(2, 1, 1, 1000, '2024-10-03 08:00:00', '2024-10-03', '2025-10-03', 'Active', 1, 'Note for registration 1'),
(3, 1, 2, 2000, '2024-10-03 08:10:00', '2024-10-03', '2025-10-03', 'Active', 1, 'Note for registration 2'),
(4, 1, 3, 3000, '2024-10-03 08:20:00', '2024-10-03', '2025-10-03', 'Active', 1, 'Note for registration 3'),
(5, 2, 1, 1500, '2024-10-03 08:30:00', '2024-10-03', '2025-10-03', 'Active', 4, 'Note for registration 4'),
(6, 2, 2, 2500, '2024-10-03 08:40:00', '2024-10-03', '2025-10-03', 'Active', 4, 'Note for registration 5'),
(7, 2, 3, 3500, '2024-10-03 08:50:00', '2024-10-03', '2025-10-03', 'Active', 4, 'Note for registration 6'),
(8, 3, 1, 1200, '2024-10-03 09:00:00', '2024-10-03', '2025-10-03', 'Active', 5, 'Note for registration 7'),
(9, 3, 2, 2200, '2024-10-03 09:10:00', '2024-10-03', '2025-10-03', 'Active', 5, 'Note for registration 8'),
(10, 3, 3, 3200, '2024-10-03 09:20:00', '2024-10-03', '2025-10-03', 'Active', 5, 'Note for registration 9'),
(11, 1, 1, 1000, '2024-10-03 09:30:00', '2024-10-03', '2025-10-03', 'Active', 6, 'Note for registration 10'),
(12, 1, 2, 2000, '2024-10-03 09:40:00', '2024-10-03', '2025-10-03', 'Active', 6, 'Note for registration 11'),
(13, 1, 3, 3000, '2024-10-03 09:50:00', '2024-10-03', '2025-10-03', 'Active', 6, 'Note for registration 12'),
(14, 2, 1, 1500, '2024-10-03 10:00:00', '2024-10-03', '2025-10-03', 'Active', 1, 'Note for registration 13'),
(15, 2, 2, 2500, '2024-10-03 10:10:00', '2024-10-03', '2025-10-03', 'Active', 1, 'Note for registration 14');
INSERT INTO Registrations (UserID, SubjectID, PackageID, Total_Cost, Registration_Time, Valid_From, Valid_To, Status, StaffID, Note)
VALUES
(17, 3, 1, 1200, '2024-10-03 10:30:00', '2024-10-03', '2025-10-03', 'Active', 4, 'Note for registration 15'),
(18, 3, 2, 2200, '2024-10-03 10:40:00', '2024-10-03', '2025-10-03', 'Active', 4, 'Note for registration 16'),
(19, 3, 3, 3200, '2024-10-03 10:50:00', '2024-10-03', '2025-10-03', 'Active', 4, 'Note for registration 17'),
(20, 1, 1, 1000, '2024-10-03 11:00:00', '2024-10-03', '2025-10-03', 'Active', 5, 'Note for registration 18'),
(21, 1, 2, 2000, '2024-10-03 11:10:00', '2024-10-03', '2025-10-03', 'Active', 5, 'Note for registration 19'),
(22, 1, 3, 3000, '2024-10-03 11:20:00', '2024-10-03', '2025-10-03', 'Active', 5, 'Note for registration 20'),
(23, 2, 1, 1500, '2024-10-03 11:30:00', '2024-10-03', '2025-10-03', 'Active', 6, 'Note for registration 21'),
(24, 2, 2, 2500, '2024-10-03 11:40:00', '2024-10-03', '2025-10-03', 'Active', 6, 'Note for registration 22'),
(25, 2, 3, 3500, '2024-10-03 11:50:00', '2024-10-03', '2025-10-03', 'Active', 6, 'Note for registration 23'),
(26, 3, 1, 1200, '2024-10-03 12:00:00', '2024-10-03', '2025-10-03', 'Active', 1, 'Note for registration 24'),
(27, 3, 2, 2200, '2024-10-03 12:10:00', '2024-10-03', '2025-10-03', 'Active', 1, 'Note for registration 25'),
(28, 3, 3, 3200, '2024-10-03 12:20:00', '2024-10-03', '2025-10-03', 'Active', 1, 'Note for registration 26'),
(29, 1, 1, 1000, '2024-10-03 12:30:00', '2024-10-03', '2025-10-03', 'Active', 4, 'Note for registration 27'),
(30, 1, 2, 2000, '2024-10-03 12:40:00', '2024-10-03', '2025-10-03', 'Active', 4, 'Note for registration 28'),
(31, 1, 3, 3000, '2024-10-03 12:50:00', '2024-10-03', '2025-10-03', 'Active', 4, 'Note for registration 29'),
(32, 2, 1, 1500, '2024-10-03 13:00:00', '2024-10-03', '2025-10-03', 'Active', 5, 'Note for registration 30'),
(33, 2, 2, 2500, '2024-10-03 13:10:00', '2024-10-03', '2025-10-03', 'Active', 5, 'Note for registration 31'),
(34, 2, 3, 3500, '2024-10-03 13:20:00', '2024-10-03', '2025-10-03', 'Active', 5, 'Note for registration 32'),
(35, 3, 1, 1200, '2024-10-03 13:30:00', '2024-10-03', '2025-10-03', 'Active', 6, 'Note for registration 33'),
(36, 3, 2, 2200, '2024-10-03 13:40:00', '2024-10-03', '2025-10-03', 'Active', 6, 'Note for registration 34'),
(37, 3, 3, 3200, '2024-10-03 13:50:00', '2024-10-03', '2025-10-03', 'Active', 6, 'Note for registration 35'),
(38, 1, 1, 1000, '2024-10-03 14:00:00', '2024-10-03', '2025-10-03', 'Active', 1, 'Note for registration 36'),
(39, 1, 2, 2000, '2024-10-03 14:10:00', '2024-10-03', '2025-10-03', 'Active', 1, 'Note for registration 37'),
(40, 1, 3, 3000, '2024-10-03 14:20:00', '2024-10-03', '2025-10-03', 'Active', 1, 'Note for registration 38'),
(41, 2, 1, 1500, '2024-10-03 14:30:00', '2024-10-03', '2025-10-03', 'Active', 4, 'Note for registration 39'),
(42, 2, 2, 2500, '2024-10-03 14:40:00', '2024-10-03', '2025-10-03', 'Active', 4, 'Note for registration 40');

-- Insert data into Campaigns table
INSERT INTO Campaigns (CampaignName, Description, StartDate, EndDate, Image, Status)
VALUES 
('Back to School', 'Discounts on all subjects to help students prepare for the new school year.', '2024-09-01', '2024-09-30', 'back_to_school.jpg', 'Active'),
('Holiday Special', 'Special discounts during the holiday season.', '2024-12-01', '2024-12-31', 'holiday_special.jpg', 'Active'),
('Summer Sale', 'Get ready for the summer with discounts on selected subjects.', '2024-06-01', '2024-06-30', 'summer_sale.jpg', 'Inactive');

-- Insert data into Campaign_Subject table
INSERT INTO Campaign_Subject (CampaignID, SubjectID, Discount)
VALUES 
(1, 1, 20), -- 20% discount on Algebra for Back to School campaign
(1, 2, 15), -- 15% discount on Geometry for Back to School campaign
(2, 3, 25), -- 25% discount on Biology for Holiday Special campaign
(2, 4, 30), -- 30% discount on Physics for Holiday Special campaign
(3, 5, 10), -- 10% discount on Chemistry for Summer Sale campaign
(3, 6, 20); -- 20% discount on World History for Summer Sale campaign
