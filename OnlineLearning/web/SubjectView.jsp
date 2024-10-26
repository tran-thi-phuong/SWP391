<%-- 
    Document   : SubjectView
    Created on : Oct 26, 2024, 5:56:15 PM
    Author     : sonna
--%>  

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="model.Users" %>
<html>
    <head>
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;600;700&display=swap" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.5/font/bootstrap-icons.min.css" rel="stylesheet">
        <link href="css/Header_Footer.css" rel="stylesheet"> 
        <link href="css/SubjectView.css" rel="stylesheet"> 
        <script>
            function toggleLessons(lessonTypeId) {
                const lessonsContainer = document.getElementById('lessons-' + lessonTypeId);
                if (lessonsContainer.style.display === 'none' || lessonsContainer.style.display === '') {
                    lessonsContainer.style.display = 'block';
                } else {
                    lessonsContainer.style.display = 'none';
                }
            }
        </script>
    </head>
    <body>
        <%@include file="Header.jsp" %>
        <c:if test="${not empty subjects}">
            <c:forEach var="subject" items="${subjects}">
                <h2>${subject.title}</h2>
                <p><a href="authorInformation?username=${subject.userName}">Lecture: ${subject.ownerName}</a></p>

                <c:forEach var="lessonType" items="${subject.lessonTypes}">
                    <div class="lesson-type" onclick="toggleLessons('${lessonType.typeID}')">
                        <p>${lessonType.name}</p>
                    </div>
                    <div id="lessons-${lessonType.typeID}" class="lessons-container">
                        <h4>Bài học:</h4>
                        <c:forEach var="lesson" items="${lessonType.lessons}">
                            <div class="lesson-item">
                                <p>${lesson.title}</p>
                                <c:if test="${not empty sessionScope.user}">
                                    <p>${lesson.status}</p>
                                </c:if>
                            </div>
                        </c:forEach>
                    </div>
                </c:forEach>
            </c:forEach>
        </c:if>
        <c:if test="${empty subjects}">
            <p>Không có môn học nào cho người dùng này.</p>
        </c:if>
        <%@include file="Footer.jsp" %>
    </body>
</html>
