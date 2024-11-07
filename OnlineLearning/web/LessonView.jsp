<%-- 
    Document   : LessonView
    Created on : Oct 26, 2024, 10:57:13 PM
    Author     : sonna
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%-- Setting content type and language --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> <%-- Importing JSTL core tag library for control statements --%>
<%@ page import="model.Lesson" %> <%-- Importing Lesson model to access lesson data --%>
<%@ page import="model.Subject" %> <%-- Importing Subject model to access subject data --%>

<html>
    <head>
        <%-- Link to Google Fonts for Montserrat font styling --%>
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;600;700&display=swap" rel="stylesheet">

        <%-- Link to Bootstrap CSS for responsive layout and pre-styled components --%>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">

        <%-- Link to Bootstrap Icons for icons used in the page --%>
        <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.5/font/bootstrap-icons.min.css" rel="stylesheet">

        <%-- Link to custom CSS files for header/footer styling and subject view --%>
        <link href="css/Header_Footer.css" rel="stylesheet"> 
        <link href="css/SubjectView.css" rel="stylesheet"> 
    </head>
    <body>
        <%@include file="Header.jsp" %> <%-- Including header section of the page --%>

        <div class="container mt-4">
            <div class="row mb-3">
                <div class="col-md-12">
                    <%-- Back button to navigate to the SubjectView page with the current subjectId --%>
                    <a href="SubjectView?subjectId=${subjectId}" class="btn btn-secondary">Back to Subjects</a>
                </div>
            </div>
            
            <%-- Main content row with lesson details and subject list sidebar --%>
            <div class="row">
                <div class="col-md-8">
                    <%-- Check if lesson data is available and display its details --%>
                    <c:choose>
                        <c:when test="${not empty lesson}">
                            <h3>${lesson.title}</h3>
                            <p><strong>Description:</strong> ${lesson.description}</p>
                            <p><strong>Content:</strong> ${lesson.content}</p>

                            <%-- Check lesson status to determine if "Mark as Completed" button should be shown --%>
                            <c:choose>
                                <c:when test="${lesson.status == 'Completed'}">
                                    <%-- No action needed if lesson is already marked as completed --%>
                                </c:when>
                                <c:otherwise>
                                    <%-- Display a form to submit "Mark as Completed" action for the lesson --%>
                                    <form action="LessonView" method="post">
                                        <input type="hidden" name="lessonID" value="${lesson.lessonID}" />
                                        <input type="hidden" name="subjectId" value="${subjectId}" /> 
                                        <button type="submit" class="btn btn-primary mt-2">Mark as Completed</button>
                                    </form>
                                </c:otherwise>
                            </c:choose>
                        </c:when>
                        <c:otherwise>
                            <p>No lesson found.</p> <%-- Message displayed if no lesson data is available --%>
                        </c:otherwise>
                    </c:choose>
                </div>

                <%-- Sidebar displaying the list of subjects and their lessons --%>
                <div class="col-md-4">
                    <%-- Loop through each subject to display its lesson topics and lessons --%>
                    <c:forEach var="subject" items="${subjects}">
                        <c:forEach var="lessonTopic" items="${subject.lessonTopics}">
                            <%-- Each lesson topic has a clickable header to toggle its lessons display --%>
                            <div class="lesson-topic" onclick="toggleLessons('${lessonTopic.topicID}')">
                                <p>${lessonTopic.name}</p>
                            </div>
                            <%-- Display container for lessons under each lesson topic --%>
                            <div id="lessons-${lessonTopic.topicID}" class="lessons-container" style="display: block;">
                                <c:forEach var="lesson" items="${lessonTopic.lessons}">
                                    <div class="lesson-item">
                                        <%-- Link to lesson view page for each lesson title --%>
                                        <p><a href="LessonView?lessonId=${lesson.lessonID}&subjectId=${subject.subjectID}">${lesson.title}</a></p>
                                        <%-- Show completed icon if user is logged in and lesson is completed --%>
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

        <%-- JavaScript function to toggle display of lessons under each lesson topic --%>
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


        <%@include file="Footer.jsp" %> <%-- Including footer section of the page --%>
    </body>
</html>  
