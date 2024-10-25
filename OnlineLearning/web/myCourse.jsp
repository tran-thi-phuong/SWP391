<%-- 
    Document   : myCourse
    Created on : Oct 8, 2024, 3:43:15 PM
    Author     : sonna
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <head>
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;600;700&display=swap" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.5/font/bootstrap-icons.min.css" rel="stylesheet">
        <link href="css/Header_Footer.css" rel="stylesheet"> 
        <link href="css/myRegistration.css" rel="stylesheet"> 
        <script src="js/myRegistration.js"></script>
        <title>My Course</title>
    </head>
    <body>
        <c:choose>
            <c:when test="${empty sessionScope.user}">
                <c:redirect url="login.jsp"/>
            </c:when>
            <c:when test="${sessionScope.user.role != 'Customer'}">
                <c:redirect url="/Homepage"/>
            </c:when>
            <c:otherwise>
                <%@include file="Header.jsp" %>
                <p></p>
                <div style="padding: 0 15px; margin-bottom: 10px; display: flex; justify-content: space-between; align-items: center;">
                    <h1>My Course</h1>
                    <div class="search-container" style="text-align: right;">
                        <form action="myCourse" method="get">
                            <input type="text" name="searchQuery" placeholder="Search course..." value="${searchQuery}" />
                            <button type="submit">Search</button>
                        </form>
                    </div>
                </div>


                <div style="padding: 0 15px; display: inline-block;">
                    <form id="toggleForm">
                        <label><input type="checkbox" class="section-toggle" data-target=".subjectName" checked> Subject</label>
                        <label><input type="checkbox" class="section-toggle" data-target=".package" checked> Package</label>
                        <label><input type="checkbox" class="section-toggle" data-target=".totalCost" checked> Cost</label>
                        <label><input type="checkbox" class="section-toggle" data-target=".validFrom" checked> Valid From</label>
                        <label><input type="checkbox" class="section-toggle" data-target=".validTo" checked> Valid To</label>
                        <label><input type="checkbox" class="section-toggle" data-target=".staffName" checked> Teacher</label>
                    </form>
                </div>
                <div style="padding: 0 15px; display: inline-block;">
                    <form action="myCourse" method="get" style="display: inline-flex; gap: 10px;">
                        <label for="pageSize">Items per page:</label>
                        <select name="pageSize" id="pageSize" onchange="updatePageSize(this)">
                            <option value="5" ${pageSize == 5 ? 'selected' : ''}>5</option>
                            <option value="10" ${pageSize == 10 ? 'selected' : ''}>10</option>
                            <option value="15" ${pageSize == 15 ? 'selected' : ''}>15</option>
                        </select>
                    </form>
                    <form id="toggleForm">
                        <label><input type="checkbox" class="section-toggle" data-target=".subjectName" checked> Subject</label>
                        <label><input type="checkbox" class="section-toggle" data-target=".package" checked> Package</label>
                        <label><input type="checkbox" class="section-toggle" data-target=".totalCost" checked> Cost</label>
                        <label><input type="checkbox" class="section-toggle" data-target=".validFrom" checked> Valid From</label>
                        <label><input type="checkbox" class="section-toggle" data-target=".validTo" checked> Valid To</label>
                        <label><input type="checkbox" class="section-toggle" data-target=".staffName" checked> Teacher</label>
                    </form>
                </div>
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

                <script>
                    function updatePageSize(select) {
                        const selectedValue = select.value;
                        localStorage.setItem('pageSize', selectedValue);
                        select.form.submit();
                    }

                    document.addEventListener('DOMContentLoaded', function () {
                        const savedPageSize = localStorage.getItem('pageSize');
                        if (savedPageSize) {
                            const pageSizeSelect = document.getElementById('pageSize');
                            pageSizeSelect.value = savedPageSize;
                        }
                    });
                </script>


                <c:if test="${not empty courseList}">
                    <div id="registrations">
                        <c:forEach var="course" items="${courseList}">
                            <div class="registration-card">
                                <div>
                                    <p class="registration-card-text"><img src="${course.subjectName}" alt="${course.subjectName}"></p>
                                    <a class="registration-card-title subjectName" href="#">
                                        ${course.subjectName}
                                    </a>
                                    <p class="registration-card-text package"><strong>Package:</strong> ${course.packageId}</p>
                                    <p class="registration-card-text totalCost"><strong>Cost:</strong> ${course.totalCost}</p>
                                    <p class="registration-card-text validFrom"><strong>Valid From:</strong> ${course.validFrom}</p>
                                    <p class="registration-card-text validTo"><strong>Valid To:</strong> ${course.validTo}</p>
                                    <p class="registration-card-text staffName"><strong>Teacher:</strong> ${course.staffName}</p>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:if>

                <c:if test="${empty courseList}">
                    <p>Not found</p>
                </c:if>

                <div style="text-align: center">
                    <div style="text-align: center">
                        <c:if test="${currentPage > 1}">
                            <a class="btn btn-secondary" href="myCourse?page=${currentPage - 1}&pageSize=${pageSize}&searchQuery=${searchQuery}">Previous</a>
                            <a class="btn btn-secondary" href="myCourse?page=${currentPage - 1}&pageSize=${pageSize}&searchQuery=${searchQuery}">Previous</a>
                        </c:if>

                        <c:forEach begin="1" end="${totalPages}" var="i">
                            <c:choose>
                                <c:when test="${currentPage eq i}">
                                    <span class="btn btn-primary">${i}</span>
                                </c:when>
                                <c:otherwise>
                                    <a class="btn btn-secondary" href="myCourse?page=${i}&pageSize=${pageSizeStr}&searchQuery=${searchQuery}">${i}</a>
                                </c:otherwise>
                            </c:choose>
                            <c:choose>
                                <c:when test="${currentPage eq i}">
                                    <span class="btn btn-primary">${i}</span>
                                </c:when>
                                <c:otherwise>
                                    <a class="btn btn-secondary" href="myCourse?page=${i}&pageSize=${pageSizeStr}&searchQuery=${searchQuery}">${i}</a>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>

                        <c:if test="${currentPage < totalPages}">
                            <a class="btn btn-secondary" href="myCourse?page=${currentPage + 1}&pageSize=${pageSizeStr}&searchQuery=${searchQuery}">Next</a>
                            <a class="btn btn-secondary" href="myCourse?page=${currentPage + 1}&pageSize=${pageSizeStr}&searchQuery=${searchQuery}">Next</a>
                        </c:if>
                    </div>
                </div>

                    <%@include file="Footer.jsp" %>
                </c:otherwise>
                </c:choose>
            </body>
        </html>