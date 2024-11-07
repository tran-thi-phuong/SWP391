<%-- 
    Document   : SubjectView
    Created on : Oct 26, 2024, 5:56:15 PM
    Author     : sonna
--%>  

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> <%-- Importing JSTL core library to enable JSP Standard Tag Library features like loops and conditionals --%>
<%@ page import="model.Users" %> <%-- Importing the Users model to access user-related data --%>
<html>
     <head>
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;600;700&display=swap" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.5/font/bootstrap-icons.min.css" rel="stylesheet"> 
        <link href="css/Header_Footer.css" rel="stylesheet">
        <link href="css/SubjectView.css" rel="stylesheet">
        <script>
            // Function to toggle the visibility of lessons based on the lesson topic ID
            function toggleLessons(lessonTopicId) {
                const lessonsContainer = document.getElementById('lessons-' + lessonTopicId);
                lessonsContainer.style.display = (lessonsContainer.style.display === 'none' || lessonsContainer.style.display === '') ? 'block' : 'none';
            }

            // Function to hide or show the lesson topic and its lessons when checkbox is clicked
            function toggleLessonTopicVisibility(checkbox, lessonTopicId) {
                const topicElement = document.getElementById('topic-' + lessonTopicId);
                const lessonsContainer = document.getElementById('lessons-' + lessonTopicId);

                if (checkbox.checked) {
                    // Hide topic and lessons if checkbox is checked
                    topicElement.style.display = 'none';
                    lessonsContainer.style.display = 'none';
                } else {
                    // Show topic and lessons if checkbox is unchecked
                    topicElement.style.display = 'block';
                    lessonsContainer.style.display = 'block';
                }
            }
        </script>
    </head>
    <body>
        <%@ include file="Header.jsp" %> <%-- Include the header section --%>

        <div class="container mt-4">
            <c:if test="${not empty subjects}"> <%-- Check if subjects list is not empty --%>
                <c:forEach var="subject" items="${subjects}"> <%-- Loop through each subject --%>
                    <h1>${subject.title}</h1>

                    <!-- Display checkboxes for each lesson topic -->
                    <div class="lesson-topic-checkboxes mb-3">
                        <c:forEach var="lessonTopic" items="${subject.lessonTopics}">
                            <label>
                                <input type="checkbox" name="lessonTopic" value="${lessonTopic.topicID}" 
                                       onchange="toggleLessonTopicVisibility(this, '${lessonTopic.topicID}')">
                                ${lessonTopic.name}
                            </label>
                        </c:forEach>
                    </div>

                    <!-- Display list of lesson topics and associated lessons -->
                    <c:forEach var="lessonTopic" items="${subject.lessonTopics}">
                        <div id="topic-${lessonTopic.topicID}" class="lesson-topic" onclick="toggleLessons('${lessonTopic.topicID}')">
                            <p>${lessonTopic.name}</p>
                        </div>
                        <div id="lessons-${lessonTopic.topicID}" class="lessons-container" style="display: block;">
                            <c:forEach var="lesson" items="${lessonTopic.lessons}">
                                <div class="lesson-item">
                                    <c:choose>
                                        <%-- Display lesson link for logged-in customers, otherwise just the title --%>
                                        <c:when test="${not empty sessionScope.user}">
                                            <c:if test="${sessionScope.user.role == 'Customer'}">
                                                <p><a href="LessonView?lessonId=${lesson.lessonID}&subjectId=${subject.subjectID}">${lesson.title}</a></p>
                                            </c:if>
                                        </c:when>
                                        <c:otherwise>
                                            <p>${lesson.title}</p>
                                        </c:otherwise>
                                    </c:choose>
                                    
                                    <%-- Show status icon if the lesson is completed --%>
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

            <%-- Display message if there are no subjects for the user --%>
            <c:if test="${empty subjects}">
                <p>Subject will be updated soon.</p>
            </c:if>
        </div>

        <%@ include file="Footer.jsp" %> <%-- Include the footer section --%>
    </body>
</html>
