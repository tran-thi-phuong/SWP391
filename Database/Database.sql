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
    Username NVARCHAR(50) NOT NULL UNIQUE,
    Password NVARCHAR(255) NOT NULL,
    Name NVARCHAR(100) NOT NULL,
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
-- Tạo bảng Campaigns
CREATE TABLE Campaigns (
    CampaignID INT PRIMARY KEY IDENTITY(1,1),
    CampaignName NVARCHAR(255) NOT NULL,
    Description NVARCHAR(MAX),
    StartDate DATE,
    EndDate DATE,
    Image NVARCHAR(255)  
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
    CampaignID INT,  -- Thêm cột CampaignID
    FOREIGN KEY (UserID) REFERENCES Users(UserID),
    FOREIGN KEY (SubjectID) REFERENCES Subjects(SubjectID),
    FOREIGN KEY (PackageID) REFERENCES Package_Price(PackageID),
    FOREIGN KEY (StaffID) REFERENCES Users(UserID),
    FOREIGN KEY (CampaignID) REFERENCES Campaigns(CampaignID)  -- Thiết lập khóa ngoại cho CampaignID
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

INSERT INTO Blog_Category (Title) 
VALUES 
('Language Learning Tips'),
('Cultural Insights'),
('Study Abroad'),
('Career Development'),
('Language Technology');
GO

INSERT INTO Blogs (UserID, Title, Content, Create_At, Blog_CategoryID) 
VALUES 
(1, 'How to Learn a Language Faster', 'Top tips to speed up your language learning process.', '2024-09-10 10:00:00', 1),
(2, 'Cultural Differences between English and Spanish Speakers', 'Understanding cultural nuances in language.', '2024-09-12 14:30:00', 2),
(1, 'Why Studying Abroad Helps You Master a Language', 'Exploring the benefits of language immersion programs.', '2024-09-15 08:45:00', 3),
(1, 'Building a Career as a Multilingual Professional', 'How learning multiple languages can boost your career.', '2024-09-20 11:00:00', 4),
(2, 'Top Language Learning Apps in 2024', 'A review of the best apps for language learners.', '2024-09-25 09:15:00', 5);
GO

SELECT b.*, u.Username, u.Name, bc.Title as CategoryTitle
FROM Blogs b
JOIN Users u ON b.UserID = u.UserID
JOIN Blog_Category bc ON b.Blog_CategoryID = bc.Blog_CategoryID
WHERE b.BlogID = 4;

INSERT INTO Blogs (UserID, Title, Content, Create_At, Blog_CategoryID) 
VALUES 
(2, 'Mastering Language Learning with Discipline', 
'Learning a language is a journey that requires not only time and effort but also a tremendous amount of discipline. While it is easy to get excited in the initial stages of learning a new language, it’s the long-term commitment that truly tests your mettle. The most successful language learners often adhere to a strict routine that combines various techniques: daily practice, exposure to the target language, interaction with native speakers, and a systematic review of what has been learned. In this blog post, we will explore strategies to maintain discipline in your language-learning journey. From creating a language study plan that fits into your daily schedule, to making use of modern tools such as language learning apps and online platforms, consistency is the key to success. Additionally, we will touch on the importance of motivation and setting realistic goals. Often, learners quit because they expect to achieve fluency too quickly. Instead, it’s important to celebrate small milestones along the way, such as understanding a conversation or being able to watch a movie without subtitles. By the end of this post, you will have practical tips and insights to help you stay disciplined and focused on your language learning goals.', 
'2024-09-30 10:00:00', 1),

(1, 'The Impact of Study Abroad on Language Fluency', 
'Studying abroad is one of the most effective ways to improve your language fluency. Immersion in a foreign culture provides opportunities to practice the language every day, in real-life situations, far beyond what can be achieved in a classroom. When you study abroad, every interaction becomes a language lesson: ordering food at a restaurant, asking for directions, or even making friends with local students. These experiences accelerate learning and make the language more relevant to your daily life. In this blog post, we will discuss the benefits of studying abroad, including enhanced language skills, cultural immersion, and personal growth. You’ll hear stories from language learners who’ve gone abroad and returned not only fluent but also more confident in their communication abilities. We’ll also address the challenges of studying abroad, such as culture shock, and how to overcome them. Whether you’re considering a semester abroad or a full-year immersion program, this post will provide valuable insights to help you make the most of your experience and return with improved language skills.', 
'2024-09-30 12:00:00', 3),

(2, 'Navigating the Job Market as a Multilingual Professional', 
'In today’s interconnected world, the ability to speak multiple languages is a highly prized skill. Employers across all industries—from tech and finance to hospitality and healthcare—seek candidates who can communicate with diverse populations. As a multilingual professional, you have the ability to bridge communication gaps, build relationships with international clients, and understand cultural nuances that can make or break business deals. In this blog post, we will explore how being multilingual can boost your career prospects. You’ll learn about the different industries that value language skills and how to market your language abilities effectively on your resume and during job interviews. Additionally, we’ll discuss strategies for maintaining your language skills after entering the workforce, as many professionals struggle to keep up with their language practice once they’re employed. Whether you’re just starting your career or looking to advance to a higher position, this guide will help you leverage your multilingualism for career success.', 
'2024-09-30 13:00:00', 4),

(1, 'Language Technology in 2024: The Future of Learning', 
'The field of language learning has undergone a revolution with the rise of new technologies. From artificial intelligence-powered language apps to virtual reality platforms, the tools available to today’s language learners are more advanced than ever before. In this blog post, we explore the cutting-edge technologies that are transforming how we learn languages. We will discuss the most popular apps, such as Duolingo, Babbel, and Rosetta Stone, and examine how AI is being used to create personalized learning experiences. Furthermore, we’ll dive into the world of virtual reality (VR) and augmented reality (AR), which are allowing learners to immerse themselves in simulated environments where they can practice their language skills in real-time. Imagine walking through a virtual Paris while practicing your French or taking a VR tour of Tokyo while speaking Japanese. These innovations are making language learning more engaging, interactive, and accessible to learners around the world. We’ll also take a look at the future of language technology and what we can expect in the coming years as AI continues to evolve.', 
'2024-09-30 14:00:00', 5);

DELETE FROM Registrations 
WHERE UserID = 1 
AND Status = 'Active';

INSERT INTO Registrations (UserID, SubjectID, PackageID, Status, Valid_From, Valid_To)
VALUES
(1, 11, 1, 'Cancelled','2024-08-15','2024-11-15'),
(1, 19, 3, 'Cancelled', '2024-09-10', '2024-12-10');
