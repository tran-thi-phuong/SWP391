<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="model.Question" %>
<%@ page import="model.Answer" %>
<%@ page import="model.QuestionMedia" %>

<html>
    <head>
        <title>Question and Answer Details</title>
        <style>
            .form-container {
                max-width: 600px;
                margin: 0 auto;
                padding: 20px;
                border: 1px solid #ccc;
                border-radius: 10px;
                background-color: #f9f9f9;
            }
            .form-container h1 {
                text-align: center;
            }
            .form-container p {
                margin-bottom: 15px;
            }
            .form-container strong {
                display: inline-block;
                width: 150px;
            }
            .form-container input[type="text"],
            .form-container input[type="number"] {
                width: calc(100% - 160px);
                padding: 5px;
                border: 1px solid #ccc;
                border-radius: 5px;
            }
            .form-container button {
                margin-right: 10px;
                padding: 10px 20px;
                border: none;
                border-radius: 5px;
                background-color: #007bff;
                color: white;
                cursor: pointer;
            }
            .form-container button:hover {
                background-color: #0056b3;
            }
            .correct {
                background-color: #d4edda; /* Light green for correct answers */
            }
            .explanation {
                font-style: italic;
                margin-top: 10px;
            }
            .media-container img,
            .media-container video {
                max-width: 100%;
                height: auto;
            }
        </style>
        <meta charset="UTF-8">
        <title>Question and Answer Detail</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <%@include file="Header.jsp" %>
    <body>
        <div style="text-align: center; margin-bottom: 20px;">
            <button onclick="window.location.href = 'QuestionList'" style="padding: 10px 20px; border: none; border-radius: 5px; background-color: #28a745; color: white; cursor: pointer;">
                Back to Question List
            </button>
        </div>
        <c:set var="questionDAO" value="<%=new dal.QuestionDAO()%>" />
        <c:set var="answerDAO" value="<%=new dal.AnswerDAO()%>" />
        <c:set var="mediaDAO" value="<%=new dal.QuestionMediaDAO()%>" />
        <div class="form-container">
            <h1>Question and Answer Details</h1>
            <form action="QuestionDetail" method="post">
                <input type="hidden" name="questionId" value="${currentQuestion != null ? currentQuestion.questionID : ''}"/>

                <p>
                    <strong>Content:</strong>
                    <input type="text" value="${currentQuestion != null ? currentQuestion.content : ''}" readonly/>
                </p>

                <p>
                    <strong>Status:</strong>
                    <input type="text" value="${currentQuestion != null ? currentQuestion.status : ''}" readonly/>
                </p>

                <p>
                    <strong>Level:</strong>
                    <input type="text" value="${currentQuestion != null ? currentQuestion.level : ''}" readonly/>
                </p>

                <p>
                    <strong>Lesson ID:</strong>
                    <input type="number" value="${currentQuestion != null ? currentQuestion.lessonID : ''}" readonly/>
                </p>

                <h2>Media Files</h2>
                <c:forEach var="media" items="${mediaDAO.getMediaByQuestionId(currentQuestion.questionID)}">
                    <div class="media-container">
                        <strong>Media:</strong>
                        <c:choose>
                            <c:when test="${media.mediaLink.endsWith('.jpg' || media.mediaLink.endsWith('.png'))}">
                                <img src="${media.mediaLink}" alt="Media Image" />
                            </c:when>
                            <c:when test="${media.mediaLink.endsWith('.mp4')}">
                                <video controls>
                                    <source src="${media.mediaLink}" type="video/mp4">
                                    Your browser does not support the video tag.
                                </video>
                            </c:when>
                            <c:otherwise>
                                <span>No preview available for this media type.</span>
                            </c:otherwise>
                        </c:choose>
                        <p><em>${media.description}</em></p>
                    </div>
                    <hr>
                </c:forEach>

                <h2>Answer Details</h2>
                <c:forEach var="answer" items="${answerDAO.getAnswersByQuestionId(currentQuestion.questionID)}">
                    <div class="${answer.isCorrect ? 'correct' : ''}">
                        <strong>Answer:</strong> ${answer.content} <br>
                        <c:if test="${not empty answer.explanation}">
                            <span class="explanation">Explanation: ${answer.explanation}</span>
                        </c:if>
                    </div>
                    <hr>
                </c:forEach>

                <button type="submit" name="action" value="edit">Edit Question</button>
                <button type="submit" name="action" value="delete" onclick="return confirmDelete()">Delete Question</button>
            </form>
        </div>
    </body>
    <%@include file="Footer.jsp" %>
    <script>
        function confirmDelete() {
            return confirm("Are you sure you want to delete this question? This action cannot be undone.");
        }
    </script>
</html>
