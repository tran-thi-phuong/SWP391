<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Registration List</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;600;700&display=swap" rel="stylesheet">  
        <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.5/font/bootstrap-icons.min.css" rel="stylesheet">
        <link href="css/Header_Footer.css" rel="stylesheet">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
        <link rel="stylesheet" href="css/registration_list.css">
    </head>
    <body>
        <%@include file="Header.jsp" %>
        <div class="container mt-5">
            <h1>List of Registrations</h1>
            <br>
            <form id="searchForm">
                <div class="row mb-3">
                    <div class="col">
                        <input type="text" class="form-control" id="emailSearch" placeholder="Search by Email" onkeyup="fetchRegistrations()"/>
                    </div>
                    <div class="col">
                        <select class="form-select" id="campaignSearch" onchange="fetchRegistrations()">
                            <option value="">Select Campaign</option>
                            <c:forEach var="campaign" items="${campaignList}">
                                <option value="${campaign.campaignId}">${campaign.campaignName}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="col">
                        <select class="form-select" id="subjectSearch" onchange="fetchRegistrations()">
                            <option value="">Select Subject</option>
                            <c:forEach var="subject" items="${subjectList}">
                                <option value="${subject.id}">${subject.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col">
                        <div class="input-group">
                            <span class="input-group-text">From</span>
                            <input type="date" class="form-control" id="registrationFrom" onchange="fetchRegistrations()"/>
                        </div>
                    </div>
                    <div class="col">
                        <div class="input-group">
                            <span class="input-group-text">To</span>
                            <input type="date" class="form-control" id="registrationTo" onchange="fetchRegistrations()"/>
                        </div>
                    </div>
                    <div class="col">
                        <select class="form-select" id="statusSearch" onchange="fetchRegistrations()">
                            <option value="">Select Status</option>
                            <option value="Active">Active</option>
                            <option value="Inactive">Inactive</option>
                            <option value="Processing">Completed</option>
                        </select>
                    </div>
                </div>


            </form>

            <div id="registrationList">
                <c:if test="${not empty listR}">
                    <table class="table table-bordered">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Email</th>
                                <th>Registration Time</th>
                                <th>Subject</th>
                                <th>Campaign</th>
                                <th>Package</th>
                                <th>Total Cost</th>
                                <th>Valid From</th>
                                <th>Valid To</th>
                                <th>Status</th>
                                <th>Last Updated By</th>
                                <th>Note</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="registration" items="${listR}">
                                <tr>
                                    <td>
                                        <a href="registrationDetail?id=${registration.registrationId}" class="text-decoration-none">
                                            ${registration.registrationId}
                                        </a>
                                    </td>
                                    <td>${customerEmail[registration.userId]}</td>
                                    <td>${registration.registrationTime}</td>
                                    <td>${subjectTitle[registration.subjectId]}</td>
                                    <td>${registration.campaignName}</td> 
                                    <td>${packageName[registration.packageId]}</td>
                                    <td>${registration.totalCost}</td>
                                    <td>${registration.validFrom}</td>
                                    <td>${registration.validTo}</td>
                                    <td>${registration.status}</td>
                                    <td>${staffUsername[registration.staffId]}</td>
                                    <td>${registration.note}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:if>
                <c:if test="${empty listR}">
                    <p>No registrations found.</p>
                </c:if>
            </div>

            <nav aria-label="Page navigation example">
                <ul class="pagination">
                    <c:if test="${currentPage > 1}">
                        <li class="page-item">
                            <a class="page-link" href="listRegistration?page=${currentPage - 1}">Previous</a>
                        </li>
                    </c:if>
                    <c:forEach var="i" begin="1" end="${totalPages}">
                        <li class="page-item <c:if test='${i == currentPage}'>active</c:if>'">
                            <a class="page-link" href="listRegistration?page=${i}">${i}</a>
                        </li>
                    </c:forEach>
                    <c:if test="${currentPage < totalPages}">
                        <li class="page-item">
                            <a class="page-link" href="listRegistration?page=${currentPage + 1}">Next</a>
                        </li>
                    </c:if>
                </ul>
            </nav>
        </div>
        <%@include file="Footer.jsp" %>

        <script>
            function fetchRegistrations() {
                const email = $('#emailSearch').val();
                const subjectId = $('#subjectSearch').val();
                const registrationFrom = $('#registrationFrom').val();
                const registrationTo = $('#registrationTo').val();
                const status = $('#statusSearch').val();
                const campaignId = $('#campaignSearch').val(); // Get the selected campaign ID

                $.ajax({
                    url: 'RegistrationList', // URL của servlet xử lý tìm kiếm
                    type: 'GET',
                    data: {
                        email: email,
                        subjectId: subjectId,
                        registrationFrom: registrationFrom,
                        registrationTo: registrationTo,
                        status: status,
                        campaignId: campaignId // Include the campaign ID in the request
                    },
                    success: function (data) {
                        $('#registrationList').html(data);
                    },
                    error: function () {
                        console.error('Error fetching registration list');
                    }
                });
            }

        </script>
    </body>
</html>
