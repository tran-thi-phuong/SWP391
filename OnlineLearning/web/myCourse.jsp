<%-- 
    Document   : myCourse
    Created on : Oct 8, 2024, 3:43:15 PM
    Author     : sonna
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head>
        <!-- Preconnect to Google Fonts for performance -->
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <!-- Montserrat font styles -->
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;600;700&display=swap" rel="stylesheet">
        <!-- Bootstrap CSS for styling -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">
        <!-- Bootstrap Icons -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.5/font/bootstrap-icons.min.css" rel="stylesheet">
        <!-- Custom CSS for Header and Footer -->
        <link href="css/Header_Footer.css" rel="stylesheet"> 
        <!-- Custom CSS for Registration page -->
        <link href="css/myRegistration.css" rel="stylesheet"> 

        <title>My Course</title>
    </head>
    <body>
        <!-- Include the header from another JSP file -->
        <%@include file="Header.jsp" %>
        
        <p></p>
        
        <!-- Header section for the My Course page -->
        <div style="padding: 0 15px; margin-bottom: 10px; display: flex; justify-content: space-between; align-items: center;">
            <h1>My Course</h1>
            <div class="search-container" style="text-align: right;">
                <!-- Search form for courses -->
                <form action="myCourse" method="get">
                    <input type="text" name="searchQuery" placeholder="Search course..." value="${searchQuery}" />
                    <button type="submit">Search</button>
                </form>
            </div>
        </div>

        <!-- Section for selecting number of items per page -->
        <div style="padding: 0 15px; display: inline-block;">
            <form action="myCourse" method="get" style="display: inline-flex; gap: 10px;">
                <label for="pageSize">Items per page:</label>
                <select name="pageSize" id="pageSize" onchange="updatePageSize(this)">
                    <option value="5" ${pageSize == 5 ? 'selected' : ''}>5</option>
                    <option value="10" ${pageSize == 10 ? 'selected' : ''}>10</option>
                    <option value="15" ${pageSize == 15 ? 'selected' : ''}>15</option>
                </select>
            </form>
        </div>

        <!-- JavaScript to handle page size selection and store it in local storage -->
        <script>
            function updatePageSize(select) {
                const selectedValue = select.value;
                localStorage.setItem('pageSize', selectedValue);
                select.form.submit(); // Submit the form after selection
            }

            document.addEventListener('DOMContentLoaded', function () {
                const savedPageSize = localStorage.getItem('pageSize');
                if (savedPageSize) {
                    const pageSizeSelect = document.getElementById('pageSize');
                    pageSizeSelect.value = savedPageSize; // Set the selected value based on local storage
                }
            });
        </script>

        <!-- Display the list of courses if not empty -->
        <c:if test="${not empty courseList}">
            <div id="registrations">
                <c:forEach var="course" items="${courseList}">
                    <div class="registration-card">
                        <div>
                            <!-- Display course details -->
                            <p class="registration-card-text"><img class="card-img-top" src="images/${course.thumbnail}" alt="${course.subjectName}"></p>
                            <p class="registration-card-title subjectName">
                            <a href="SubjectView?subjectId=${course.subjectId}">
                                ${course.subjectName}
                            </a>
                            </p>
                            <p class="registration-card-text validFrom"><strong>Valid From:</strong> ${course.validFrom}</p>
                            <p class="registration-card-text validTo"><strong>Valid To:</strong> ${course.validTo}</p>
                            <p class="registration-card-text staffName"><strong>Teacher:</strong> ${course.staffName}</p>
                            <p class="registration-card-text"><strong>Completion:</strong> ${course.progress}%</p>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:if>

        <!-- Message for no courses found -->
        <c:if test="${empty courseList}">
            <p>Not found</p>
        </c:if>

        <!-- Pagination controls -->
        <div style="text-align: center">
            <div style="text-align: center">
                <c:if test="${currentPage > 1}">
                    <a class="btn btn-secondary" href="myCourse?page=${currentPage - 1}&pageSize=${pageSize}&searchQuery=${searchQuery}">Previous</a>
                </c:if>

                <c:forEach begin="1" end="${totalPages}" var="i">
                    <c:choose>
                        <c:when test="${currentPage eq i}">
                            <span class="btn btn-primary">${i}</span> <!-- Current page is highlighted -->
                        </c:when>
                        <c:otherwise>
                            <a class="btn btn-secondary" href="myCourse?page=${i}&pageSize=${pageSizeStr}&searchQuery=${searchQuery}">${i}</a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>

                <c:if test="${currentPage < totalPages}">
                    <a class="btn btn-secondary" href="myCourse?page=${currentPage + 1}&pageSize=${pageSizeStr}&searchQuery=${searchQuery}">Next</a>
                </c:if>
            </div>
        </div>

        <!-- Include the footer from another JSP file -->
        <%@include file="Footer.jsp" %>
    </body>
</html>
