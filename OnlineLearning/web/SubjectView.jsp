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
            function toggleLessons(lessonTopicId) {
                const lessonsContainer = document.getElementById('lessons-' + lessonTopicId);
                lessonsContainer.style.display = (lessonsContainer.style.display === 'none' || lessonsContainer.style.display === '') ? 'block' : 'none';
            }
        </script>
    </head>
    <body>
        <%@ include file="Header.jsp" %>

        <div class="container mt-4">
            <c:if test="${not empty subjects}">
                <c:forEach var="subject" items="${subjects}">
                    <h1>${subject.title}</h1>
                    <c:forEach var="lessonTopic" items="${subject.lessonTopics}">
                        <div class="lesson-topic" onclick="toggleLessons('${lessonTopic.topicID}')">
                            <p>${lessonTopic.name}</p>
                        </div>
                        <div id="lessons-${lessonTopic.topicID}" class="lessons-container" style="display: block;">
                            <c:forEach var="lesson" items="${lessonTopic.lessons}">
                                <div class="lesson-item">
                                    <c:choose>
                                        <c:when test="${not empty sessionScope.user}">
                                            <c:if test="${sessionScope.user.role == 'Customer'}">
                                                <p><a href="LessonView?lessonId=${lesson.lessonID}&subjectId=${subject.subjectID}">${lesson.title}</a></p>
                                            </c:if>
                                        </c:when>
                                        <c:otherwise>
                                            <p>${lesson.title}</p> 
                                        </c:otherwise>
                                    </c:choose>
                                    <c:if test="${not empty sessionScope.user}">
                                        <c:choose>
                                            <c:when test="${lesson.status == 'Completed'}">
                                                <span class="status-icon">
                                                    <i class="bi bi-check-circle-fill text-success"></i> Completed
                                                </span>
                                            </c:when>
                                        </c:choose>
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
        </div>

        <%@ include file="Footer.jsp" %>
    </body>
</html>
