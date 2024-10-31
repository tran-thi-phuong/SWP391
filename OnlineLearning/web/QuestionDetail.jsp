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
            .modal {
                display: none;
                position: fixed;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background-color: rgba(0, 0, 0, 0.5);
            }
            .modal-content {
                background-color: white;
                margin: 15% auto;
                padding: 20px;
                width: 80%;
                max-width: 500px;
            }
            .close {
                float: right;
                font-size: 20px;
                cursor: pointer;
            }
            .answer-item {
                margin-bottom: 15px;
            }
        </style>
        <meta charset="UTF-8">
        <title>Question and Answer Detail</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;600;700&display=swap" rel="stylesheet">  
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.5/font/bootstrap-icons.min.css" rel="stylesheet">
        <link href="css/Header_Footer.css" rel="stylesheet">
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
                <!-- Summarize button -->
                <button type="buton" name="action" value="summarize">Summarize</button>

                <!-- Placeholder for summary -->
                <div id="summaryResult">
                    <c:if test="${not empty sessionScope.summarize}">
                        <strong>Summary:</strong> ${sessionScope.summarize}
                        <c:set var="dummy" value="${sessionScope.summarize}"/> 
                        <c:remove var="summarize" scope="session"/>
                    </c:if>
                </div>
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
                                <video controls preload="auto">
                                    <source src="${media.mediaLink}" type="video/mp4">
                                    Your browser does not support the video tag.
                                </video>
                            </c:when>
                            <c:otherwise>
                                <img src="${media.mediaLink}" alt="Media Image" />
                            </c:otherwise>
                        </c:choose>
                        <p><em>${media.description}</em></p>
                    </div>
                    <hr>
                </c:forEach>

                <h2>Answer Details</h2>
                <c:forEach var="answer" items="${answers}">
                    <div class="${answer.isCorrect ? 'correct' : ''}">
                        <strong>Answer:</strong> ${answer.content} <br>
                        <c:if test="${not empty answer.explanation}">
                            <span class="explanation">Explanation: ${answer.explanation}</span>
                        </c:if>
                    </div>
                    <hr>
                </c:forEach>
                <button type="button" onclick="openEditModal()">Edit Answers</button>     



                <button type="submit" name="action" value="edit">Edit Question</button>
                <button type="submit" name="action" value="delete" onclick="return confirmDelete()">Delete Question</button>
            </form>
        </div>
        <div id="editModal" class="modal" style="display: none;">
            <div class="modal-content">
                <span class="close" onclick="if (confirmCancel())
                            closeEditModal()">&times;</span>
                <h3>Edit Answers</h3>
                <form id="editForm" action="EditAnswer" method="post">
                    <input type="hidden" name="questionId" value="${param.id}" />

                    <div id="answerList">
                        <c:forEach var="answer" items="${answers}" varStatus="status">
                            <div class="answer-item" data-answer-id="${answer.answerID}">
                                <label>Answer:</label>
                                <br>
                                <textarea name="answers.content" oninput="trackChanges()">${answer.content}</textarea>
                                <br>
                                <label>Explanation:</label>
                                <br>
                                <textarea name="answers.explanation" oninput="trackChanges()">${answer.explanation}</textarea>
                                <br>
                                <label>Correct Answer:</label>
                                <select name="answers.correct" multiple>
                                    <option value="true" ${answer.isCorrect ? 'selected' : ''}>Yes</option>
                                    <option value="false" ${!answer.isCorrect ? 'selected' : ''}>No</option>
                                </select>

                                <br>
                                <button type="button" onclick="confirmDeleteNew(this)">Delete</button>
                            </div>
                        </c:forEach>
                    </div>

                    <!-- Button to add a new answer -->
                    <button type="button" onclick="addNewAnswer()">Add New Answer</button>
                    <button type="submit">Save All Changes</button>
                    <button type="button" onclick="confirmCancel()">Cancel</button>
                </form>
            </div>
        </div>
        <%@include file="Footer.jsp" %>
    </body>

    <script>
        let changesMade = true;
        function confirmDelete() {
            return confirm("Are you sure you want to delete this question? This action cannot be undone.");
        }
        function openEditModal() {
            document.getElementById("editModal").style.display = "block";
        }

        function closeEditModal() {
            document.getElementById("editModal").style.display = "none";
        }

        function confirmCancel() {
            if (changesMade) { // Only confirm if changes were made
                if (confirm("Changes you made will not be saved. Do you want to continue?")) {
                    closeEditModal();
                }
            } else {
                closeEditModal(); // Close the modal directly if no changes were made
            }
        }


        function addNewAnswer() {
            const answerList = document.getElementById("answerList");
            const newIndex = answerList.children.length; // Use the current length as the new index
            const newAnswerHTML = `
        <div class="answer-item">
            <label>Answer:</label>
        <br>
            <textarea name="answers.content"></textarea>
        <br>
            <label>Explanation:</label>
            <br>
            <textarea name="answers.explanation"></textarea>
    <br>
        <label>Correct Answer:</label>
                                <select name="answers.correct" multiple>
                                    <option value="true"}>Yes</option>
                                    <option value="false" selected>No</option>
                                </select>
            <br>
            <button type="button" onclick="confirmDeleteNew(this)">Delete</button>
        </div>
    `;
            answerList.insertAdjacentHTML('beforeend', newAnswerHTML);
            changesMade = true;
        }

        function confirmDeleteNew(button) {
            const answerItem = button.parentElement;
            if (confirm("Are you sure you want to delete this answer? This action cannot be undone.")) {
                answerItem.remove();
                changesMade = true;
            }
        }


        function trackChanges() {
            changesMade = true; // Set to true whenever there is an input change
        }
        function deleteAnswer(answerId) {
            const answerItem = document.querySelector(`[data-answer-id='${answerId}']`);
            if (answerItem) {
                answerItem.remove();
                changesMade = true;
            } else {
                console.error("Answer item not found for ID:", answerId);
            }
        }

        document.addEventListener("DOMContentLoaded", function () {
            const checkbox = document.getElementById("correctCheckbox");

            // Update the checkbox value based on its state
            checkbox.addEventListener("change", function () {
                if (this.checked) {
                    this.value = "true"; // Set value to true if checked
                } else {
                    this.value = "false"; // Set value to false if unchecked
                }
            });
        });
        function summarizeContent(questionId) {
            if (questionId === 0) {
                alert("Invalid question ID.");
                return;
            }

            // Sending an AJAX request to the servlet
            fetch(`Summarize?questionId=${questionId}`)
                    .then(response => {
                        if (!response.ok) {
                            throw new Error('Network response was not ok');
                        }
                        return response.json();
                    })
                    .then(data => {
                        // Update the inner HTML with the summary
                        document.getElementById("summaryResult").innerHTML = `<strong>Summary:</strong> ${data.summary}`;
                    })
                    .catch(error => {
                        console.error('There was a problem with the fetch operation:', error);
                    });
        }

    </script>
</html>
