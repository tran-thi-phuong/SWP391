<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="model.Test" %> 
<html>
    <head>
        <title>Quiz Details</title>
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
            .form-container input[type="number"],
            .form-container select {
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
            .media-container img,
            .media-container video {
                max-width: 100%;
                height: auto;
            }
            /* Modal styles */
            .modal {
                display: none; /* Hidden by default */
                position: fixed; /* Stay in place */
                z-index: 1; /* Sit on top */
                left: 0;
                top: 0;
                width: 100%; /* Full width */
                height: 100%; /* Full height */
                overflow: auto; /* Enable scroll if needed */
                background-color: rgba(0,0,0,0.4); /* Black w/ opacity */
            }
            .modal-content {
                background-color: #fefefe;
                margin: 15% auto; /* 15% from the top and centered */
                padding: 20px;
                border: 1px solid #888;
                width: 80%; /* Could be more or less, depending on screen size */
            }
            .close {
                color: #aaa;
                float: right;
                font-size: 28px;
                font-weight: bold;
            }
            .close:hover,
            .close:focus {
                color: black;
                text-decoration: none;
                cursor: pointer;
            }
        </style>
        <meta charset="UTF-8">
        <title>Test List</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;600;700&display=swap" rel="stylesheet">  
        <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.5/font/bootstrap-icons.min.css" rel="stylesheet">
        <link href="css/Header_Footer.css" rel="stylesheet">
    </head>
    <%@include file="Header.jsp" %>
    <body>
        <div style="text-align: center; margin-bottom: 20px;">
            <button onclick="window.location.href = 'QuizList'" style="padding: 10px 20px; border: none; border-radius: 5px; background-color: #28a745; color: white; cursor: pointer;">
                Back to Quiz List
            </button>
        </div>
        <c:set var="subjectDAO" value="<%=new dal.SubjectDAO()%>" />
        <c:set var="questionDAO" value="<%=new dal.QuestionDAO()%>" />
        <div class="form-container">
            <h1>Quiz Details</h1>
            <form action="QuizDetail" method="post">
                <input type="hidden" name="testId" value="${currentTest != null ? currentTest.testID : ''}"/>

                <p>
                    <strong>Title:</strong>
                    <input type="text" value="${currentTest != null ? currentTest.title : ''}" readonly/>
                </p>

                <p>
                    <strong>Description:</strong>
                    <input type="text" value="${currentTest != null ? currentTest.description : ''}" readonly/>
                </p>
                <p>
                    <strong>Media Type:</strong>
                    <input type="text" value="${currentTest != null ? currentTest.mediaType : ''}" readonly/>
                </p>
                <c:if test="${not empty currentTest.mediaURL}">
                    <p class="media-container">
                        <strong>Media:</strong>
                        <c:choose>
                            <c:when test="${currentTest.mediaType == 'image'}">
                                <img src="${currentTest.mediaURL}" alt="Media Image"/>
                            </c:when>
                            <c:when test="${currentTest.mediaType == 'video'}">
                                <video controls>
                                    <source src="${currentTest.mediaURL}" type="video/mp4">
                                    Your browser does not support the video tag.
                                </video>
                            </c:when>
                            <c:otherwise>
                                <span>No media available</span>
                            </c:otherwise>
                        </c:choose>
                        <span><br>${currentTest.mediaDescription}</span>
                    </p>
                </c:if>
                <p>
                    <strong>Duration:</strong>
                    <input type="number" value="${currentTest != null ? currentTest.duration : ''}" readonly/> minutes
                </p>
                <p>
                    <strong>Pass Condition:</strong>
                    <input type="text" value="${currentTest != null ? currentTest.passCondition : ''}%" readonly/>
                </p>
                <p>
                    <strong>Level:</strong>
                    <input type="text" value="${currentTest != null ? currentTest.level : ''}" readonly/>
                </p>
                <p>
                    <strong>Number of Questions:</strong>
                    <input type="number" value="${currentTest != null ? currentTest.quantity : ''}" readonly/>
                </p>
                <p>
                    <strong>Subject:</strong>
                    <select id="subjectId" name="subjectId" disabled>
                        <option value="">Select Subject</option>
                        <c:forEach var="subject" items="${subjectDAO.getAllSubjects()}">
                            <option value="${subject.subjectID}" ${currentTest != null && subject.subjectID == currentTest.subjectID ? 'selected' : ''}>
                                ${subject.title}
                            </option>
                        </c:forEach>
                    </select>
                </p>
                <button type="button" onclick="showQuestionList()">View Question List</button>
                <button type="submit" name="action" value="edit" 
                        ${attemptCount > 0 ? 'disabled' : ''}>Edit</button>
                <button type="submit" name="action" value="delete" 
                        ${attemptCount > 0 ? 'disabled' : ''}
                        onclick="return confirmDelete()">Delete</button>
            </form>
            <div id="questionModal" class="modal">
                <div class="modal-content">
                    <span class="close" onclick="closeModal()">&times;</span>
                    <h2>Questions for Quiz: ${currentTest != null ? currentTest.title : ''}</h2>
                    <ul>
                        <c:forEach var="question" items="${questionDAO.getQuestionsByTestId(currentTest.testID)}">
                            <li>${question.content}</li>
                            </c:forEach>
                    </ul>
                </div>
            </div>
        </div>
    </body>
    <%@include file="Footer.jsp" %>
    <script>
        function showQuestionList() {
            document.getElementById('questionModal').style.display = 'block';
        }

        function closeModal() {
            document.getElementById('questionModal').style.display = 'none';
        }
        function confirmDelete() {
            return confirm("Are you sure you want to delete this test? This action cannot be undone.");
        }
        window.onclick = function (event) {
            if (event.target == document.getElementById('questionModal')) {
                closeModal();
            }
        }

    </script>
</html>