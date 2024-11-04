<%-- 
    Document   : myRegistration
    Created on : Oct 7, 2024, 10:34:03 PM
    Author     : sonna
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head>
        <!-- Preconnect to Google Fonts for faster loading -->
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;600;700&display=swap" rel="stylesheet">
        <!-- Include Bootstrap CSS for styling -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.5/font/bootstrap-icons.min.css" rel="stylesheet">
        <link href="css/Header_Footer.css" rel="stylesheet"> 
        <link href="css/myRegistration.css" rel="stylesheet"> 
        <!-- Include JavaScript files for interactivity -->
        <script src="js/myRegistration.js"></script>
        <script src="js/registrationPopup.js"></script>
        <title>My Registration</title>
    </head>
    <body>
        
        <%@include file="Header.jsp" %> <!-- Include header JSP -->
        
        <p></p>
        <div style="padding: 0 15px; margin-bottom: 10px; display: flex; justify-content: space-between; align-items: center;">
            <h1>My Registration</h1> <!-- Title of the page -->
            <div class="search-container" style="text-align: right;">
                <form action="myRegistration" method="get"> <!-- Search form for courses -->
                    <input type="text" name="searchQuery" placeholder="Search course..." value="${searchQuery}" />
                    <button type="submit">Search</button>
                </form>
            </div>
        </div>

        <div style="padding: 0 15px; display: inline-block;">
            <!-- Buttons to filter registrations by status -->
            <form action="myRegistration" method="get" style="display: inline-flex; gap: 10px;">
                <input type="hidden" name="status" value="Submitted" />
                <button type="submit">Submitted</button>
            </form>

            <form action="myRegistration" method="get" style="display: inline-flex; gap: 10px;">
                <input type="hidden" name="status" value="Active" />
                <button type="submit">Active</button>
            </form>

            <form action="myRegistration" method="get" style="display: inline-flex; gap: 10px;">
                <input type="hidden" name="status" value="In-active" />
                <button type="submit">In-active</button>
            </form>

            <form action="myRegistration" method="get" style="display: inline-flex; gap: 10px;">
                <input type="hidden" name="status" value="All" />
                <button type="submit">All</button>
            </form>
        </div>

        <div style="padding: 0 15px; display: inline-block;">
            <!-- Checkbox options to toggle visibility of columns in the registration list -->
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
            <!-- Dropdown to select the number of items displayed per page -->
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
            // Function to update the page size and store it in local storage
            function updatePageSize(select) {
                const selectedValue = select.value;
                localStorage.setItem('pageSize', selectedValue);
                select.form.submit();
            }

            // Restore the saved page size on page load
            document.addEventListener('DOMContentLoaded', function () {
                const savedPageSize = localStorage.getItem('pageSize');
                if (savedPageSize) {
                    const pageSizeSelect = document.getElementById('pageSize');
                    pageSizeSelect.value = savedPageSize;
                }
            });
        </script>

        <c:if test="${not empty registrationList}"> <!-- Check if there are any registrations to display -->
            <div id="registrations">
                <c:forEach var="registration" items="${registrationList}"> <!-- Loop through each registration -->
                    <div class="registration-card">
                        <div>
                            <p class="registration-card-text"><img src="${registration.subjectName}" alt="${registration.subjectName}"></p>
                            <p class="registration-card-title subjectName"><a href="SubjectView?subjectId=${registration.subjectId}">${registration.subjectName}</a></p>
                            <p class="registration-card-text package"><strong>Package:</strong> ${registration.packageId}</p>
                            <p class="registration-card-text registrationTime"><strong>Registration Time:</strong> ${registration.registrationTime}</p>
                            <p class="registration-card-text totalCost"><strong>Cost:</strong> ${registration.totalCost}</p>
                            <p class="registration-card-text validFrom"><strong>Valid From:</strong> ${registration.validFrom}</p>
                            <p class="registration-card-text validTo"><strong>Valid To:</strong> ${registration.validTo}</p>
                            <p class="registration-card-text status"><strong>Status:</strong> ${registration.status}</p>
                            <p class="registration-card-text staffName"><strong>Teacher:</strong> ${registration.staffName}</p>
                        </div>
                        <div class="registration-card-footer">
                            <c:if test="${registration.status == 'Submitted'}"> <!-- Show cancel button only for submitted registrations -->
                                <form action="myRegistration" method="post" style="display: inline;">
                                    <input type="hidden" name="registrationId" value="${registration.registrationId}" />
                                    <input type="hidden" name="action" value="cancel" />
                                    <button type="submit" class="btn btn-danger btn-sm">Cancel</button> <!-- Cancel button -->
                                </form>
                            </c:if>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:if>

        <c:if test="${empty registrationList}"> <!-- Message to show if no registrations found -->
            <p>Not found</p>
        </c:if>

        <div style="text-align: center">
            <div style="text-align: center">
                <c:if test="${currentPage > 1}"> <!-- Previous button, only shown if not on the first page -->
                    <a class="btn btn-secondary" href="myRegistration?page=${currentPage - 1}&pageSize=${pageSize}&searchQuery=${searchQuery}&status=${statusFilter}">Previous</a>
                </c:if>

                <!-- Pagination buttons -->
                <c:forEach begin="1" end="${totalPages}" var="i">
                    <c:choose>
                        <c:when test="${currentPage eq i}"> <!-- Highlight the current page -->
                            <span class="btn btn-primary">${i}</span>
                        </c:when>
                        <c:otherwise>
                            <a class="btn btn-secondary" href="myRegistration?page=${i}&pageSize=${pageSizeStr}&searchQuery=${searchQuery}&status=${statusFilter}">${i}</a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>

                <c:if test="${currentPage < totalPages}"> <!-- Next button, only shown if not on the last page -->
                    <a class="btn btn-secondary" href="myRegistration?page=${currentPage + 1}&pageSize=${pageSizeStr}&searchQuery=${searchQuery}&status=${statusFilter}">Next</a>
                </c:if>
            </div>
        </div>

        <%@include file="Footer.jsp" %> <!-- Include footer JSP -->
    </body>
</html>
