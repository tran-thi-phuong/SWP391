<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.Date" %>
<%@ page import="model.Test" %> 
<%@ page import="dal.SubjectDAO" %> 
<%@ page import="model.SystemSetting" %> 
<%@ page import="dal.SettingDAO" %> 
<%@ page import="dal.TestDAO" %> 
<!DOCTYPE html>
<html lang="en">
    <head>

        <meta charset="UTF-8">
        <title>Test List</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;600;700&display=swap" rel="stylesheet">  
        <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.5/font/bootstrap-icons.min.css" rel="stylesheet">
        <link href="css/Header_Footer.css" rel="stylesheet">
        <link rel="stylesheet" href="css/quiz-list-style.css">
        <!-- Link to your CSS file -->
    </head>
    <style>
        .add-new, .setting {
            padding: 10px 15px;
            background-color: #28a745; /* Green color for add button */
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            margin-left: 10px;
        }

        .add-new:hover, .setting:hover {
            background-color: #218838;
        }
        a{
            color:blue;
        }
        table th{
            background-color: #A8B1F9;
        }
    </style>
    <%@include file="Header.jsp" %>
    <body>

                <c:set var="subjectDAO" value="<%=new dal.SubjectDAO()%>" />
                <c:set var="testDAO" value="<%=new dal.TestDAO()%>"/>
                <button class="setting" onclick="document.getElementById('settingsModal').style.display = 'block'">Settings</button> <!-- Settings Button -->
                <c:if test="${sessionScope.user.role == 'Instructor'}">
                    <button class="add-new" onclick="window.location.href = 'AddNewTest.jsp'">Add New</button>
                </c:if>
                <div id="settingsModal" class="modal">
                    <div class="modal-content">
                        <span class="close" onclick="document.getElementById('settingsModal').style.display = 'none'">&times;</span>
                        <h2>Update Settings</h2>
                        <form id="settingsForm" class="settings-form" method="post" action="SettingServlet" onsubmit="return validateNumberOfItems()">
                            <label for="numberOfItems">Number of Items per Page:</label>
                            <input type="number" id="numberOfItems" name="numberOfItems" value="${sessionScope.setting.numberOfItems != null ? sessionScope.setting.numberOfItems : 10}">
                            <label><input type="checkbox" name="title" ${sessionScope.setting.title != null ? (sessionScope.setting.title ? 'checked' : '') : 'checked'}> Title</label>
                            <label><input type="checkbox" name="subject" ${sessionScope.setting.subject != null ? (sessionScope.setting.subject ? 'checked' : '') : 'checked'}> Subject</label>
                            <label><input type="checkbox" name="description" ${sessionScope.setting.description != null ? (sessionScope.setting.description ? 'checked' : '') : 'checked'}> Description</label>
                            <label><input type="checkbox" name="level" ${sessionScope.setting.level != null ? (sessionScope.setting.level ? 'checked' : '') : 'checked'}> Level</label>
                            <label><input type="checkbox" name="numberOfQuestions" ${sessionScope.setting.quantity != null ? (sessionScope.setting.quantity ? 'checked' : '') : 'checked'}> Number of Questions</label>
                            <label><input type="checkbox" name="duration" ${sessionScope.setting.duration != null ? (sessionScope.setting.duration ? 'checked' : '') : 'checked'}> Duration</label>
                            <label><input type="checkbox" name="passCondition" ${sessionScope.setting.passCondition != null ? (sessionScope.setting.passCondition ? 'checked' : '') : 'checked'}> Pass Condition</label>
                            <label><input type="checkbox" name="passRate" ${sessionScope.setting.passRate != null ? (sessionScope.setting.passRate ? 'checked' : '') : 'checked'}> Pass Rate</label>
                            <label><input type="checkbox" name="quizType" ${sessionScope.setting.quizType != null ? (sessionScope.setting.quizType ? 'checked' : '') : 'checked'}> Quiz Type</label>
                            <button type="submit">Save</button>
                        </form>
                    </div>
                </div>
                <form method="get" action="QuizList"> <!-- Action to the controller to handle search -->
                    <label for="search">Search:</label>
                    <input type="text" id="search" name="search" value="${param.search != null ? param.search : ''}" placeholder="Search...">
                    <label for="subjectId">Subject:</label>
                    <select id="subjectId" name="subjectId">
                        <option value="">Select Subject</option>
                        <c:forEach var="subject" items="${subjectDAO.getAllSubjects()}">
                            <option value="${subject.subjectID}" ${param.subjectId != null && param.subjectId == subject.subjectID ? 'selected' : ''}>${subject.title}</option>
                        </c:forEach>
                    </select>

                    <label for="quizType">Quiz Type:</label>
                    <c:set var="quizTypes" value="${testDAO.getAllQuizTypes()}"/>
                    <select id="quizType" name="quizType">
                        <option value="">Select Quiz Type</option>
                        <c:forEach var="quizType" items="${quizTypes}">
                            <option value="${quizType}" ${param.quizType != null && param.quizType == quizType ? 'selected' : ''}>
                                ${quizType}
                            </option>
                        </c:forEach>
                    </select>

                    <button type="submit">Search</button>
                </form>
                <c:choose>
                    <c:when test="${not empty sessionScope.setting}">
                        <form action="TestController" method="post">
                            <table class="test-table">
                                <thead>
                                    <tr>
                                        <th>Test ID</th>
                                        <th style="${sessionScope.setting.title ? '' : 'display:none;'}">Title</th>
                                        <th style="${sessionScope.setting.subject ? '' : 'display:none;'}">Subject Name</th>
                                        <th style="${sessionScope.setting.description ? '' : 'display:none;'}">Description</th>
                                        <th style="${sessionScope.setting.level ? '' : 'display:none;'}">Level</th>
                                        <th style="${sessionScope.setting.quantity ? '' : 'display:none;'}">Number of Questions</th>
                                        <th style="${sessionScope.setting.duration ? '' : 'display:none;'}">Duration</th>
                                        <th style="${sessionScope.setting.passCondition ? '' : 'display:none;'}">Pass Condition</th>
                                        <th style="${sessionScope.setting.passRate ? '' : 'display:none;'}">Pass Rate</th>
                                        <th style="${sessionScope.setting.quizType ? '' : 'display:none;'}">Type</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="test" items="${testList}">
                                        <tr>
                                            <td> <a href="<c:url value='QuizDetail' />?id=${test.testID}">
                                                    ${test.testID}
                                                </a></td>

                                            <td style="${sessionScope.setting.title ? '' : 'display:none;'}">${test.title}</td>
                                            <td style="${sessionScope.setting.subject ? '' : 'display:none;'}">
                                                <c:set var="subjectName" value="${subjectDAO.getSubjectById(test.subjectID).title}" />
                                                ${subjectName}
                                            </td>
                                            <td style="${sessionScope.setting.description ? '' : 'display:none;'}">${test.description}</td>
                                            <td style="${sessionScope.setting.level ? '' : 'display:none;'}">${test.level}</td>
                                            <td style="${sessionScope.setting.quantity ? '' : 'display:none;'}">${test.quantity}</td>
                                            <td style="${sessionScope.setting.duration ? '' : 'display:none;'}">${test.duration}</td>
                                            <td style="${sessionScope.setting.passCondition ? '' : 'display:none;'}">${test.passCondition}</td>
                                            <td style="${sessionScope.setting.passRate ? '' : 'display:none;'}">pass rate</td>
                                            <td style="${sessionScope.setting.quizType ? '' : 'display:none;'}">${test.type}</td>


                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <!-- Display test list without any settings -->
                        <form action="TestController" method="post">
                            <table class="test-table">
                                <thead>
                                    <tr>
                                        <th>Test ID</th>
                                        <th>Title</th>
                                        <th>Subject Name</th>
                                        <th>Description</th>
                                        <th>Level</th>
                                        <th>Number of Questions</th>
                                        <th>Duration</th>
                                        <th>Pass Condition</th>
                                        <th>Pass Rate</th>
                                        <th>Type</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="test" items="${testList}">
                                        <tr>
                                            <td> <a href="<c:url value='QuizDetail' />?id=${test.testID}">
                                                    ${test.testID}
                                                </a></td>
                                            <td>${test.title}</td>
                                            <td>
                                                <c:set var="subjectName" value="${subjectDAO.getSubjectById(test.subjectID).title}" />
                                                ${subjectName}
                                            </td>

                                            <td>${test.description}</td>
                                            <td>${test.level}</td>
                                            <td>${test.quantity}</td>
                                            <td>${test.duration}</td>
                                            <td>${test.passCondition}</td>
                                            <td>pass rate</td>
                                            <td>${test.type}</td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                            <input type="hidden" name="numberOfItems" value="10" />
                        </form>
                    </c:otherwise>
                </c:choose>
                <div style="text-align: center;">
                    <p>Total Pages: ${totalPages}</p>
                    <p>Current Page: ${currentPage}</p>

                    <c:choose>
                        <c:when test="${totalPages > 5}">
                            <c:if test="${currentPage > 1}">
                                <c:if test="${currentPage > 2}">
                                    <a href="?page=1&search=${param.search}&subjectId=${param.subjectId}&quizType=${param.quizType}&numberOfItems=${numberOfItems}">1</a>
                                    <c:if test="${currentPage > 3}">
                                        <span>...</span>
                                    </c:if>
                                </c:if>
                                <a href="?page=${currentPage - 1}&search=${param.search}&subjectId=${param.subjectId}&quizType=${param.quizType}&numberOfItems=${numberOfItems}">${currentPage - 1}</a>
                            </c:if>

                            <span>${currentPage}</span>

                            <c:if test="${currentPage < totalPages}">
                                <a href="?page=${currentPage + 1}&search=${param.search}&subjectId=${param.subjectId}&quizType=${param.quizType}&numberOfItems=${numberOfItems}">${currentPage + 1}</a>
                            </c:if>

                            <c:if test="${currentPage < totalPages - 1}">
                                <span>...</span>
                                <a href="?page=${totalPages}&search=${param.search}&subjectId=${param.subjectId}&quizType=${param.quizType}&numberOfItems=${numberOfItems}">${totalPages}</a>
                            </c:if>
                        </c:when>

                        <c:otherwise>
                            <c:forEach var="i" begin="1" end="${totalPages}">
                                <a href="?page=${i}&search=${param.search}&subjectId=${param.subjectId}&quizType=${param.quizType}&numberOfItems=${numberOfItems}">${i}</a>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </div>


            
    </body>
    <%@include file="Footer.jsp" %>
    <script>
        window.onclick = function (event) {
            if (event.target == document.getElementById('settingsModal')) {
                document.getElementById('settingsModal').style.display = "none";
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
