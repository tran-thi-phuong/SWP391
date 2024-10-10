<%-- 
    Document   : QuestionQuiz
    Created on : Oct 10, 2024, 11:53:45 AM
    Author     : 84336
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ page import="model.Question" %>
<html>
    <head>
        <title>Select Questions for Quiz</title>
        <style>
            body {
                font-family: Arial, sans-serif;
            }
            .question-list {
                max-width: 600px;
                margin: 0 auto;
            }
            .question-item {
                margin-bottom: 10px;
            }
            button {
                padding: 10px 15px;
                background-color: #007bff;
                color: white;
                border: none;
                border-radius: 5px;
                cursor: pointer;
            }
            button:hover {
                background-color: #0056b3;
            }
        </style>
    </head>
    <body>
        <div class="question-list">
            <h1>Select Questions for Quiz</h1>
            <form action="EditQuiz" method="post" onsubmit="return validateSelection()">
                <input type="hidden" name="testId" value="${testId}"/>
                <input type="text" id="searchBox" placeholder="Search questions..." onkeyup="filterQuestions()" />
                <h2>Available Questions</h2>
                <div id="questionList">
                    <c:forEach var="question" items="${questionsBySubject}">
                        <div class="question-item">
                            <input type="checkbox" name="selectedQuestions" value="${question.questionID}"
                                   <c:if test="${fn:contains(questionsByTest, question.questionID)}">checked</c:if> />
                            <span class="question-content">${question.content}</span>
                        </div>

                    </c:forEach>
                </div>

                <div>
                    <button type="submit" name="action" value="updateQuestionQuiz">Update Questions</button>
                </div>
            </form>

        </div>
    </body>
    <script>
        function filterQuestions() {
            const searchBox = document.getElementById('searchBox');
            const filter = searchBox.value.toLowerCase();
            const questionList = document.getElementById('questionList');
            const questions = questionList.getElementsByClassName('question-item');

            for (let i = 0; i < questions.length; i++) {
                const questionContent = questions[i].getElementsByClassName('question-content')[0];
                if (questionContent) {
                    const textValue = questionContent.textContent || questionContent.innerText;
                    if (textValue.toLowerCase().indexOf(filter) > -1) {
                        questions[i].style.display = ""; // Show the question
                    } else {
                        questions[i].style.display = "none"; // Hide the question
                    }
                }
            }
        }
        function validateSelection() {
            const checkboxes = document.querySelectorAll('input[name="selectedQuestions"]');
            let isChecked = false;

            for (let checkbox of checkboxes) {
                if (checkbox.checked) {
                    isChecked = true;
                    break;
                }
            }

            if (!isChecked) {
                alert("Please select at least one question.");
                return false; // Prevent form submission
            }

            return true; // Allow form submission
        }

    </script>
</html>
