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
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <title>Lesson View</title>
    </head>
    <body>
        <div class="container mt-4">
            <div class="row mb-3">
                <div class="col-md-12">
                    <a href="SubjectView?subjectId=${subjectId}" class="btn btn-secondary">Back to Subjects</a> <!-- Nút Back -->
                </div>
            </div>
            <h2>Lesson Details</h2>
            <c:choose>
                <c:when test="${not empty lesson}">
                    <h3>${lesson.title}</h3>
                    <p><strong>Description:</strong> ${lesson.description}</p>
                    <p><strong>Content:</strong> ${lesson.content}</p>

                    <c:if test="${lesson.mediaLink != null}">
                        <p><strong>Media:</strong></p>
                        <video controls width="600">
                            <source src="${lesson.mediaLink}">
                        </video>
                    </c:if>

                    <p><strong>Status:</strong> 
                        <c:choose>
                            <c:when test="${lesson.status == 'Completed'}">
                                <span class="text-success">Completed</span>
                            </c:when>
                            <c:otherwise>
                                <span class="text-warning">Not Started</span>
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

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
