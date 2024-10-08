<%-- 
    Document   : myCourse
    Created on : Oct 8, 2024, 3:43:15 PM
    Author     : sonna
--%>

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
            <label><input type="checkbox" data-attr="packageId"> Package</label>
            <label><input type="checkbox" data-attr="totalCost"> Cost</label>
            <label><input type="checkbox" data-attr="status"> Status</label>
            <label><input type="checkbox" data-attr="validFrom"> Valid From</label>
            <label><input type="checkbox" data-attr="validTo"> Valid To</label>
            <label><input type="checkbox" data-attr="staffName"> Teacher</label>
            <label><input type="checkbox" data-attr="note"> Note</label>
        </div>

        <c:if test="${not empty registrationList}">
            <div id="registrations">
                <c:forEach var="registration" items="${courseList}">
                    <div class="registration-card">
                        <div>
                            <p class="registration-card-title">${courseList.subjectName}</p>
                            <p class="registration-card-text"><strong>Package</strong> ${courseList.packageId}</p>
                            <p class="registration-card-text"><strong>Cost</strong> ${courseList.totalCost}</p>
                            <p class="registration-card-text"><strong>Status</strong> ${courseList.status}</p>
                            <p class="registration-card-text"><strong>ValidFrom</strong> ${courseList.validFrom}</p>
                            <p class="registration-card-text"><strong>ValidTo</strong> ${courseList.validTo}</p>
                            <p class="registration-card-text"><strong>Teacher</strong> ${courseList.staffName}</p>
                            <p class="registration-card-text"><strong>Note</strong> ${courseList.note}</p>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:if>

        <c:if test="${empty registrationList}">
            <p>Not found</p>
        </c:if>

        <div>
            <c:if test="${currentPage > 1}">
                <a href="myRegistration?page=${currentPage - 1}&status=${status}"></a>
            </c:if>

            <c:forEach begin="1" end="${totalPages}" var="i">
                <c:if test="${i == currentPage}">
                    <strong>${i}</strong> 
                </c:if>
                <c:if test="${i != currentPage}">
                    <a href="myRegistration?page=${i}&status=${status}">${i}</a>
                </c:if>
            </c:forEach>

            <c:if test="${currentPage < totalPages}">
                <a href="myRegistration?page=${currentPage + 1}&status=${status}"></a>
            </c:if>
        </div>

        <%@include file="Footer.jsp" %>
    </body>
</html>