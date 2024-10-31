<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Question" %> 
<%@ page import="dal.QuestionDAO" %> 
<%@ page import="dal.LessonDAO" %> 
<%@ page import="model.QuestionSetting" %> 
<%@ page import="dal.SettingDAO" %> 
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Question List</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;600;700&display=swap" rel="stylesheet">  
        <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.5/font/bootstrap-icons.min.css" rel="stylesheet">
        <link href="css/Header_Footer.css" rel="stylesheet">
        <link rel="stylesheet" href="css/quiz-list-style.css">
    </head>
    <style>
        .add-new, .QuestionSetting {
            padding: 10px 15px;
            background-color: #28a745; /* Green color for add button */
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            margin-left: 10px;
        }

        .add-new:hover, .QuestionSetting:hover {
            background-color: #218838;
        }
        a {
            color: blue;
        }
        table th {
            background-color: #A8B1F9;
        }
    </style>
    <%@include file="Header.jsp" %>
    <body>
        <c:set var="questionDAO" value="<%=new dal.QuestionDAO()%>" />
        <c:set var="lessonDAO" value="<%=new dal.LessonDAO()%>"/>
        <button class="QuestionSetting" onclick="document.getElementById('QuestionSettingsModal').style.display = 'block'">Settings</button>
        <button class="add-new" onclick="window.location.href = 'AddNewQuestion.jsp'">Add New</button>

        <div id="QuestionSettingsModal" class="modal">
            <div class="modal-content">
                <span class="close" onclick="document.getElementById('QuestionSettingsModal').style.display = 'none'">&times;</span>
                <h2>Update Settings</h2>
                <form id="QuestionSettingsForm" class="QuestionSettings-form" method="post" action="QuestionSettingServlet" onsubmit="return validateNumberOfItems()">
                    <label for="numberOfItems">Number of Items per Page:</label>
                    <input type="number" id="numberOfItems" name="numberOfItems" 
                           value="${sessionScope.QuestionSetting.numberOfItems != null ? sessionScope.QuestionSetting.numberOfItems : 10}" min="1" required>
                    <br>
                    <label>
                        <input type="checkbox" name="showContent" 
                               ${sessionScope.QuestionSetting.showContent == null || sessionScope.QuestionSetting.showContent ? 'checked' : ''}> Show Content
                    </label>
                    <br>
                    <label>
                        <input type="checkbox" name="showLessonID" 
                               ${sessionScope.QuestionSetting.showLessonID == null || sessionScope.QuestionSetting.showLessonID ? 'checked' : ''}> Show Lesson
                    </label>
                    <br>
                    <label>
                        <input type="checkbox" name="showStatus" 
                               ${sessionScope.QuestionSetting.showStatus == null || sessionScope.QuestionSetting.showStatus ? 'checked' : ''}> Show Status
                    </label>
                    <br>
                    <label>
                        <input type="checkbox" name="showLevel" 
                               ${sessionScope.QuestionSetting.showLevel == null || sessionScope.QuestionSetting.showLevel ? 'checked' : ''}> Show Level
                    </label>
                    <br>
                    <button type="submit">Save</button>
                </form>
            </div>
        </div>


        <br>
        <br>
        <form method="get" action="QuestionList"> <!-- Action to handle search -->
            <label for="search">Search:</label>
            <input type="text" id="search" name="search" value="${param.search != null ? param.search : ''}" placeholder="Search...">
            <br>

            <label for="lesson">Select Lesson:</label>
            <select id="lesson" name="lesson">
                <option value="">-- Select Lesson --</option>
                <c:forEach var="lesson" items="${lessonList}">
                    <option value="${lesson.lessonID}" 
                            <c:if test="${lesson.lessonID == param.lesson}">selected</c:if>
                            >${lesson.content}</option>
                </c:forEach>
            </select>
            <br>

            <label for="status">Select Status:</label>
            <select id="status" name="status">
                <option value="">-- Select Status --</option>
                <option value="Active" <c:if test="${param.status == 'Active'}">selected</c:if>>Active</option>
                <option value="Inactive" <c:if test="${param.status == 'Inactive'}">selected</c:if>>Inactive</option>
                </select>
                <br>

                <label for="level">Select Level:</label>
                <select id="level" name="level">
                    <option value="">-- Select Level --</option>
                    <option value="Easy" <c:if test="${param.level == 'Easy'}">selected</c:if>>Easy</option>
                <option value="Medium" <c:if test="${param.level == 'Medium'}">selected</c:if>>Medium</option>
                <option value="Hard" <c:if test="${param.level == 'Hard'}">selected</c:if>>Hard</option>
                    <!-- Add more levels as needed -->
                </select>
                <br>

                <label for="order">Order By:</label>
                <select id="order" name="order">
                    <option value="">-- Select Order --</option>
                    <option value="QuestionID_asc" <c:if test="${param.order == 'QuestionID_asc'}">selected</c:if>>ID Ascending</option>
                <option value="QuestionID_desc" <c:if test="${param.order == 'QuestionID_desc'}">selected</c:if>>ID Descending</option>
                <option value="content_asc" <c:if test="${param.order == 'content_asc'}">selected</c:if>>Content Ascending</option>
                <option value="content_desc" <c:if test="${param.order == 'content_desc'}">selected</c:if>>Content Descending</option>
                <option value="level_asc" <c:if test="${param.order == 'level_asc'}">selected</c:if>>Level Ascending</option>
                <option value="level_desc" <c:if test="${param.order == 'level_desc'}">selected</c:if>>Level Descending</option>
                </select>
                <br>

                <button type="submit">Search</button>
            </form>



        <c:choose>
            <c:when test="${not empty questionList}">
                <form action="QuestionController" method="post">
                    <table class="test-table">
                        <thead>
                            <tr>
                                <th>Question ID</th>
                                <th style="${sessionScope.QuestionSetting.showContent == null || sessionScope.QuestionSetting.showContent ? '' : 'display:none;'}">Content</th>
                                <th style="${sessionScope.QuestionSetting.showLevel == null || sessionScope.QuestionSetting.showLevel ? '' : 'display:none;'}">Level</th>
                                <th style="${sessionScope.QuestionSetting.showStatus == null || sessionScope.QuestionSetting.showStatus ? '' : 'display:none;'}">Status</th>
                                <th style="${sessionScope.QuestionSetting.showLessonID == null || sessionScope.QuestionSetting.showLessonID ? '' : 'display:none;'}">Lesson</th> <!-- New Column -->
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="question" items="${questionList}">
                                <tr>
                                    <td>
                                        <a href="<c:url value='QuestionDetail' />?id=${question.questionID}">${question.questionID}</a>
                                    </td>
                                    <td style="${sessionScope.QuestionSetting.showContent == null || sessionScope.QuestionSetting.showContent ? '' : 'display:none;'}">${question.content}</td>
                                    <td style="${sessionScope.QuestionSetting.showLevel == null || sessionScope.QuestionSetting.showLevel ? '' : 'display:none;'}">${question.level}</td>
                                    <td style="${sessionScope.QuestionSetting.showStatus == null || sessionScope.QuestionSetting.showStatus ? '' : 'display:none;'}">${question.status}</td>
                                    <td style="${sessionScope.QuestionSetting.showLessonID == null || sessionScope.QuestionSetting.showLessonID ? '' : 'display:none;'}">
                                        <c:set var="lesson" value="${lessonDAO.getLessonById(question.lessonID)}" />

                                        <c:if test="${lesson != null}">
                                            ${lesson.content}
                                        </c:if>


                                    </td> <!-- Lesson Column -->
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>

                </form>


                <div style="text-align: center;">
                    <p>Total Pages: ${totalPages}</p>
                    <p>Current Page: ${currentPage}</p>
                    <c:choose>
                        <c:when test="${totalPages > 5}">
                            <c:if test="${currentPage > 1}">
                                <c:if test="${currentPage > 2}">
                                    <a href="?page=1&search=${param.search}&lessonId=${param.lessonId}&status=${param.status}&level=${param.level}">1</a>
                                    <c:if test="${currentPage > 3}">
                                        <span>...</span>
                                    </c:if>
                                </c:if>
                                <a href="?page=${currentPage - 1}&search=${param.search}&lessonId=${param.lessonId}&status=${param.status}&level=${param.level}">${currentPage - 1}</a>
                            </c:if>

                            <span>${currentPage}</span>

                            <c:if test="${currentPage < totalPages}">
                                <a href="?page=${currentPage + 1}&search=${param.search}&lessonId=${param.lessonId}&status=${param.status}&level=${param.level}">${currentPage + 1}</a>
                            </c:if>

                            <c:if test="${currentPage < totalPages - 1}">
                                <span>...</span>
                                <a href="?page=${totalPages}&search=${param.search}&lessonId=${param.lessonId}&status=${param.status}&level=${param.level}">${totalPages}</a>
                            </c:if>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="i" begin="1" end="${totalPages}">
                                <a href="?page=${i}&search=${param.search}&lessonId=${param.lessonId}&status=${param.status}&level=${param.level}">${i}</a>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:when>
            <c:otherwise>
                <p>No questions available.</p>
            </c:otherwise>
        </c:choose>
    </body>
    <%@include file="Footer.jsp" %>
    <script>
        window.onclick = function (event) {
            if (event.target == document.getElementById('QuestionSettingsModal')) {
                document.getElementById('QuestionSettingsModal').style.display = "none";
            }
        }
        function validateNumberOfItems() {
            const numberOfItems = document.getElementById("numberOfItems").value;

            if (numberOfItems <= 0) {
                alert("The number of items per page must be greater than 0.");
                return false; // Prevent form submission
            }
            return true; // Allow form submission
        }
    </script>
</html>
