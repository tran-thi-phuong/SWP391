<%-- 
    Document   : LessonView
    Created on : Oct 26, 2024, 10:57:13 PM
    Author     : sonna
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="model.Lesson" %>
<%@ page import="model.Subject" %>

<html>
    <head>
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;600;700&display=swap" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.5/font/bootstrap-icons.min.css" rel="stylesheet">
        <link href="css/Header_Footer.css" rel="stylesheet"> 
        <link href="css/SubjectView.css" rel="stylesheet"> 
    </head>
    <body>
        <%@include file="Header.jsp" %>
        <div class="container mt-4">
            <div class="row mb-3">
                <div class="col-md-12">
                    <a href="SubjectView?subjectId=${subjectId}" class="btn btn-secondary">Back to Subjects</a>
                </div>
            </div>
            <!-- Row for main content and subject view sidebar -->
            <div class="row">
                <div class="col-md-8">
                    <c:choose>
                        <c:when test="${not empty lesson}">
                            <h3>${lesson.title}</h3>
                            <p><strong>Description:</strong> ${lesson.description}</p>
                            <p><strong>Content:</strong> ${lesson.content}</p>

                            <c:if test="${lesson.mediaLink != null}">
                                <video controls width="600">
                                    <source src="${lesson.mediaLink}">
                                </video>
                            </c:if>

                                <c:choose>
                                    <c:when test="${lesson.status == 'Completed'}">
                                        
                                    </c:when>
                                    <c:otherwise>
                                        <form action="LessonView" method="post">
                                            <input type="hidden" name="lessonID" value="${lesson.lessonID}" />
                                            <input type="hidden" name="subjectId" value="${subjectId}" /> 
                                            <button type="submit" class="btn btn-primary mt-2">Mark as Completed</button>
                                        </form>
                                    </c:otherwise>
                                </c:choose>
                            </p>
                        </c:when>
                        <c:otherwise>
                            <p>No lesson found.</p>
                        </c:otherwise>
                    </c:choose>
                </div>

                <div class="col-md-4">
                    <h3>Subject List</h3>
                    <c:forEach var="subject" items="${subjects}">
                        <c:forEach var="lessonTopic" items="${subject.lessonTopics}">
                            <div class="lesson-topic" onclick="toggleLessons('${lessonTopic.topicID}')">
                                <p>${lessonTopic.name}</p>
                            </div>
                            <div id="lessons-${lessonTopic.topicID}" class="lessons-container" style="display: block;">
                                <c:forEach var="lesson" items="${lessonTopic.lessons}">
                                    <div class="lesson-item">
                                        <p><a href="LessonView?lessonId=${lesson.lessonID}&subjectId=${subject.subjectID}">${lesson.title}</a></p>
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
                </div>
            </div>
        </div>

        <!-- JavaScript to toggle the display of lessons under each lesson type -->
        <script>
            function toggleLessons(typeID) {
                const lessonsContainer = document.getElementById(`lessons-${typeID}`);
                lessonsContainer.style.display = lessonsContainer.style.display === "none" ? "block" : "none";
            }
            function toggleLessons(lessonTypeId) {
                const lessonsContainer = document.getElementById('lessons-' + lessonTypeId);
                if (lessonsContainer.style.display === 'none' || lessonsContainer.style.display === '') {
                    lessonsContainer.style.display = 'block';
                } else {
                    lessonsContainer.style.display = 'none';
                }
            }
        </script>
        

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <%@include file="Footer.jsp" %>
    </body>
</html>
