<%-- 
    Document   : TryCourse
    Created on : Nov 24, 2024, 5:21:03 PM
    Author     : Admin
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <%-- Link to Google Fonts for Montserrat font styling --%>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;600;700&display=swap" rel="stylesheet">
    
    <%-- Link to Bootstrap CSS for responsive layout and pre-styled components --%>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">
    
    <%-- Link to Bootstrap Icons for icons used in the page --%>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.5/font/bootstrap-icons.min.css" rel="stylesheet">
    
    <%-- Link to custom CSS files for header/footer styling and subject view --%>
    <link href="css/Header_Footer.css" rel="stylesheet">
    <title>${subject.title} - Trial</title>
    <style>
        /* Global Styles */
        body {
            font-family: 'Montserrat', sans-serif;
            background-color: #f9f9f9;
            margin: 0;
            padding: 0;
        }

        /* Header and Footer styling */
        header, footer {
            background-color: #343a40;
            color: #fff;
            text-align: center;
            padding: 15px 0;
        }

        header h1, footer p {
            margin: 0;
            font-weight: 600;
        }

        /* Page Title */
        h1 {
            color: #1d3557;
            font-size: 2.5rem;
            text-align: center;
            margin-top: 40px;
            font-weight: 700;
        }

        h3 {
            color: #457b9d;
            font-size: 1.75rem;
            margin: 20px 0;
            text-align: center;
            font-weight: 600;
        }

        p {
            font-size: 1.1rem;
            line-height: 1.6;
            color: #333;
            margin: 0 20px;
            padding: 0 10px;
            text-align: justify;
        }

        /* Container for content */
        .container2 {
            max-width: 960px;
            margin: 0 auto;
            padding: 20px;
        }

        /* Bootstrap Customization for Full Width */
        .container2-fluid {
            padding: 0;
        }

        /* Styling for lesson content */
        .lesson-content {
            background-color: #fff;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
            border-radius: 10px;
            margin-top: 30px;
            padding: 20px;
        }

        /* Add a subtle gradient background */
        body {
            background: linear-gradient(to right, #f0f8ff, #e0f7fa);
        }

        /* Styling for buttons or links (if any) */
        a.btn {
            background-color: #1d3557;
            color: white;
            padding: 10px 20px;
            border-radius: 5px;
            text-decoration: none;
        }

        a.btn:hover {
            background-color: #457b9d;
        }
    </style>
</head>
<%@include file="Header.jsp" %>
<body>
    <div class="container2">
        
        <h1>${subject.title}</h1> <!-- Subject title -->
        <h3>Lesson: ${lesson.title}</h3> <!-- First lesson title -->
        <div class="lesson-content">
            <p>${lesson.content}</p> <!-- First lesson content -->
        </div>
        <!-- Register Now button -->
        <h3>If you want to continue studying, please click this button!!!  </h3>
        <div class="text-center mt-4">
            <a class="btn btn-primary" href="CourseList">Register Now</a>
        </div>
    </div>
</body>
<%@include file="Footer.jsp" %>
</html>
