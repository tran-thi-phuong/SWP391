<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.Date" %>
<%@ page import="model.Test" %> 
<%@ page import="dal.SubjectDAO" %> 
<%@ page import="model.SystemSetting" %> 
<%@ page import="dal.SettingDAO" %> 
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Test List</title>
        <link rel="stylesheet" type="text/css" href="css/quiz-list-style.css"> <!-- Link to your CSS file -->
    </head>
    <body>
        <h1>Test List</h1>
        <button onclick="document.getElementById('settingsModal').style.display = 'block'">Settings</button> <!-- Settings Button -->

        <!-- Settings Modal -->
        <div id="settingsModal" class="modal">
            <div class="modal-content">
                <span class="close" onclick="document.getElementById('settingsModal').style.display = 'none'">&times;</span>
                <h2>Update Settings</h2>
                <form id="settingsForm" class="settings-form" method="post" action="SettingServlet"> <!-- Action to SettingServlet -->
                    <label for="numberOfItems">Number of Items per Page:</label>
                    <input type="number" id="numberOfItems" name="numberOfItems" value="${sessionScope.setting.numberOfItems != null ? sessionScope.setting.numberOfItems : 10}">

                    <label><input type="checkbox" name="quizID" ${sessionScope.setting.quizID != null ? (sessionScope.setting.quizID ? 'checked' : '') : 'checked'}> Quiz ID</label>
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
        <form method="get" action="TestController"> <!-- Action to the controller to handle search -->
            <label for="subjectId">Subject:</label>
            <input type="text" id="subjectId" name="subjectId" placeholder="Enter Subject ID">

            <label for="quizType">Quiz Type:</label>
            <input type="text" id="quizType" name="quizType" placeholder="Enter Quiz Type">

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
                                    <td>${test.testID}</td>
                                    <td style="${sessionScope.setting.title ? '' : 'display:none;'}">
                                        <c:set var="subjectName" value="${SubjectDAO.getSubjectNameById(test.subjectID)}" />
                                        ${subjectName}
                                    </td>
                                    <td style="${sessionScope.setting.name ? '' : 'display:none;'}">${test.title}</td>
                                    <td style="${sessionScope.setting.description ? '' : 'display:none;'}">${test.description}</td>
                                    <td style="${sessionScope.setting.level ? '' : 'display:none;'}">${test.type}</td>
                                    <td style="${sessionScope.setting.quantity ? '' : 'display:none;'}">${test.quantity}</td>
                                    <td style="${sessionScope.setting.duration ? '' : 'display:none;'}">${test.duration}</td>
                                    <td style="${sessionScope.setting.passRate ? '' : 'display:none;'}">pass rate</td>
                                    <td style="${sessionScope.setting.quizType ? '' : 'display:none;'}">${test.quizType}</td>


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
                                    <td>${test.testID}</td>
                                    <td>
                                        <c:set var="subjectName" value="${SubjectDAO.getSubjectNameById(test.subjectID)}" />
                                        ${subjectName}
                                    </td>
                                    <td>${test.title}</td>
                                    <td>${test.description}</td>
                                    <td>${test.type}</td>
                                    <td>${test.quantity}</td>
                                    <td>${test.duration}</td>
                                    <td>pass rate</td>
                                    <td>${test.quizType}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <input type="hidden" name="numberOfItems" value="10" />
                </form>
            </c:otherwise>
        </c:choose>
        <div style="text-align: center;">
            <c:choose>
                <c:when test="${totalPages > 5}">
                    <c:if test="${currentPage > 1}">
                        <c:if test="${currentPage > 2}">
                            <a href="?page=1&search=${param.search}&subjectId=${param.subjectId}&type=${param.type}&numberOfItems=${numberOfItems}">1</a>
                            <c:if test="${currentPage > 3}">
                                <span>...</span>
                            </c:if>
                        </c:if>
                        <a href="?page=${currentPage - 1}&search=${param.search}&subjectId=${param.subjectId}&type=${param.type}&numberOfItems=${numberOfItems}">${currentPage - 1}</a>
                    </c:if>

                    <span>${currentPage}</span>

                    <c:if test="${currentPage < totalPages}">
                        <a href="?page=${currentPage + 1}&search=${param.search}&subjectId=${param.subjectId}&type=${param.type}&numberOfItems=${numberOfItems}">${currentPage + 1}</a>
                    </c:if>

                    <c:if test="${currentPage < totalPages - 1}">
                        <span>...</span>
                        <a href="?page=${totalPages}&search=${param.search}&subjectId=${param.subjectId}&type=${param.type}&numberOfItems=${numberOfItems}">${totalPages}</a>
                    </c:if>
                </c:when>

                <c:otherwise>
                    <c:forEach var="i" begin="1" end="${totalPages}">
                        <a href="?page=${i}&search=${param.search}&subjectId=${param.subjectId}&type=${param.type}&numberOfItems=${numberOfItems}">${i}</a>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </div>


    </body>

    <script>
        // Modal close functionality
        window.onclick = function (event) {
            if (event.target == document.getElementById('settingsModal')) {
                document.getElementById('settingsModal').style.display = "none";
            }
        }
    </script>
</html>
