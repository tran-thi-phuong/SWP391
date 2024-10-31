<%-- 
    Document   : myRegistration
    Created on : Oct 7, 2024, 10:34:03 PM
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
        <script src="js/registrationPopup.js"></script>
        <script src="js/registrationPopup.js"></script>
        <title>My Registration</title>
    </head>
    <body>
        
                <%@include file="Header.jsp" %>
                <p></p>
                <div style="padding: 0 15px; margin-bottom: 10px; display: flex; justify-content: space-between; align-items: center;">
                    <h1>My Registration</h1>
                    <div class="search-container" style="text-align: right;">
                        <form action="myRegistration" method="get">


                            <input type="text" name="searchQuery" placeholder="Search course..." value="${searchQuery}" />
                            <button type="submit">Search</button>
                        </form>
                    </div>
                </div>

                <div style="padding: 0 15px; display: inline-block;">
                    <form action="myRegistration" method="get" style="display: inline-flex; gap: 10px;">
                        <input type="hidden" name="status" value="Submitted" />
                        <button type="submit">Submitted</button>
                    </form>

                    <form action="myRegistration" method="get" style="display: inline-flex; gap: 10px;">
                        <input type="hidden" name="status" value="In-progress" />
                        <button type="submit">In-progress</button>
                    </form>

                    <form action="myRegistration" method="get" style="display: inline-flex; gap: 10px;">
                        <input type="hidden" name="status" value="Finish" />
                        <button type="submit">Finish</button>
                    </form>

                    <form action="myRegistration" method="get" style="display: inline-flex; gap: 10px;">
                        <input type="hidden" name="status" value="All" />
                        <button type="submit">All</button>
                    </form>
                </div>

                <div style="padding: 0 15px; display: inline-block;">
                    <form id="toggleForm">
                        <label><input type="checkbox" class="section-toggle" data-target=".subjectName" checked> Subject</label>
                        <label><input type="checkbox" class="section-toggle" data-target=".package" checked> Package</label>
                        <label><input type="checkbox" class="section-toggle" data-target=".registrationTime" checked> Registration Time</label>
                        <label><input type="checkbox" class="section-toggle" data-target=".totalCost" checked> Cost</label>
                        <label><input type="checkbox" class="section-toggle" data-target=".validFrom" checked> Valid From</label>
                        <label><input type="checkbox" class="section-toggle" data-target=".validTo" checked> Valid To</label>
                        <label><input type="checkbox" class="section-toggle" data-target=".status" checked> Status</label>
                        <label><input type="checkbox" class="section-toggle" data-target=".staffName" checked> Teacher</label>
                    </form>
                </div>

                <div style="padding: 0 15px; display: inline-block;">
                    <form action="myRegistration" method="get" style="display: inline-flex; gap: 10px;">
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

                <c:if test="${not empty registrationList}">
                    <div id="registrations">
                        <c:forEach var="registration" items="${registrationList}">
                            <div class="registration-card">
                                <div>
                                    <p class="registration-card-text"><img src="${registration.subjectName}" alt="${registration.subjectName}"></p>
                                    <p class="registration-card-title subjectName">${registration.subjectName}</p>
                                    <p class="registration-card-text package"><strong>Package:</strong> ${registration.packageId}</p>
                                    <p class="registration-card-text registrationTime"><strong>Registration Time:</strong> ${registration.registrationTime}</p>
                                    <p class="registration-card-text totalCost"><strong>Cost:</strong> ${registration.totalCost}</p>
                                    <p class="registration-card-text validFrom"><strong>Valid From:</strong> ${registration.validFrom}</p>
                                    <p class="registration-card-text validTo"><strong>Valid To:</strong> ${registration.validTo}</p>
                                    <p class="registration-card-text status"><strong>Status:</strong> ${registration.status}</p>
                                    <p class="registration-card-text staffName"><strong>Teacher:</strong> ${registration.staffName}</p>
                                </div>
                                <div class="registration-card-footer">
                                    <c:if test="${registration.status == 'Submitted'}">
                                        <form action="myRegistration" method="post" style="display: inline;">
                                            <input type="hidden" name="registrationId" value="${registration.registrationId}" />
                                            <input type="hidden" name="action" value="cancel" />
                                            <button type="submit" class="btn btn-danger btn-sm">Cancel</button>
                                        </form>
                                    </c:if>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:if>

                <c:if test="${empty registrationList}">
                    <p>Not found</p>
                </c:if>

                <div style="text-align: center">
                    <div style="text-align: center">
                        <c:if test="${currentPage > 1}">
                            <a class="btn btn-secondary" href="myRegistration?page=${currentPage - 1}&pageSize=${pageSize}&searchQuery=${searchQuery}&status=${statusFilter}">Previous</a>
                            <a class="btn btn-secondary" href="myRegistration?page=${currentPage - 1}&pageSize=${pageSize}&searchQuery=${searchQuery}&status=${statusFilter}">Previous</a>
                        </c:if>

                        <c:forEach begin="1" end="${totalPages}" var="i">
                            <c:choose>
                                <c:when test="${currentPage eq i}">
                                    <span class="btn btn-primary">${i}</span>
                                </c:when>
                                <c:otherwise>
                                    <a class="btn btn-secondary" href="myRegistration?page=${i}&pageSize=${pageSizeStr}&searchQuery=${searchQuery}&status=${statusFilter}">${i}</a>
                                </c:otherwise>
                            </c:choose>
                            <c:choose>
                                <c:when test="${currentPage eq i}">
                                    <span class="btn btn-primary">${i}</span>
                                </c:when>
                                <c:otherwise>
                                    <a class="btn btn-secondary" href="myRegistration?page=${i}&pageSize=${pageSizeStr}&searchQuery=${searchQuery}&status=${statusFilter}">${i}</a>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>

                        <c:if test="${currentPage < totalPages}">
                            <a class="btn btn-secondary" href="myRegistration?page=${currentPage + 1}&pageSize=${pageSizeStr}&searchQuery=${searchQuery}&status=${statusFilter}">Next</a>
                            <a class="btn btn-secondary" href="myRegistration?page=${currentPage + 1}&pageSize=${pageSizeStr}&searchQuery=${searchQuery}&status=${statusFilter}">Next</a>
                        </c:if>
                    </div>



                    <%@include file="Footer.jsp" %>
              
    </body>
</html>