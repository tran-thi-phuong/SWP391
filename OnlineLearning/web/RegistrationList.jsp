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
    <%@include file="Header.jsp" %>
    <body class="regist">
                
                <div class="container2 mt-5">
                    <h1>List of Registrations</h1>
                    <br>
                    <br>
                    <c:if test="${sessionScope.user.role == 'Marketing'}">
                        <div style="display: flex; justify-content: flex-end; padding: 10px;">
                            <form action="addRegistrationBySale" method="get">
                                <button type="submit" id="add" style="background-color: #90EE90; color: black;
                                        border: 1px solid #FF6347; padding: 10px 20px; cursor: pointer; border-radius: 5px;">
                                    New Registration
                                </button>
                            </form>
                        </div>
                    </c:if>
                    <form action="campaignList" method="get">
                        <button type="submit" id="add" style="background-color: #FF0000; color: white;
                                border: 1px solid #FF6347 ; padding: 10px 20px; cursor: pointer; border-radius: 5px;">
                            Campaign List
                        </button>
                    </form>
                    <br>
                    <form id="searchForm" action="listRegistration" method="get">
                        <div class="row mb-3">
                            <div class="col">
                                <input type="text" class="form-control" id="emailSearch" name="email"
                                       placeholder="Search by Email" value="${param.email != null ? param.email : ''}"/>
                            </div>
                            <div class="col">
                                <select class="form-select" id="campaignSearch" name="campaign">
                                    <option value="">Select Campaign</option>
                                    <c:forEach items="${listC.keySet()}" var="caId">
                                        <option value="${caId}"
                                                <c:if test="${not empty param.campaignId && param.campaignId == caId}">selected</c:if>>
                                            ${listC[caId].campaignName}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col">
                                <input type="text" class="form-control" id="subjectSearch"  name="subject" 
                                       placeholder="Search by Subject" value="${param.subjectID != null ? param.subjectID : ''}"  oninput="submitForm()"/>
                            </div>
                            <div class="col">
                                <div class="input-group">
                                    <span class="input-group-text">From</span>
                                    <input type="date" class="form-control" id="registrationFrom"
                                           name="validFrom" value="${param.validFrom}"/>
                                </div>
                            </div>
                            <div class="col">
                                <div class="input-group">
                                    <span class="input-group-text">To</span>
                                    <input type="date" class="form-control" id="registrationTo"
                                           name="validTo" value="${param.validTo}"/>
                                </div>
                            </div>
                            <div class="col">
                                <select class="form-select" id="statusSearch" name="status">
                                    <option value="">Select Status</option>
                                    <option value="Active" ${param.status == "Active" ? "selected" : ""}>Active</option>
                                    <option value="Inactive" ${param.status == "Inactive" ? "selected" : ""}>Expired</option>
                                    <option value="Processing" ${param.status == "Processing" ? "selected" : ""}>Processing</option>
                                    <option value="Cancelled" ${param.status == "Cancelled" ? "selected" : ""}>Cancelled</option>
                                </select>
                            </div>
                        </div>
                        <div class="row mb-3">
                            <div class="col text-end">
                                <button type="submit" class="btn btn-primary" onclick="submitForm()">Save</button>
                            </div>
                        </div>
                    </form>

                    <div>
                        <form id="settingsForm" class="settings-form" method="post" action="registrationSetting">
                            <label for="numberOfItems">Number of Items per Page:</label>
                            <input type="number" id="numberOfItems" name="numberOfItems" 
                                   value="${sessionScope.setting.numberOfItems != null ? sessionScope.setting.numberOfItems : 20}">

                            <h5 style="color: #FF0000">Show/Hide Columns:</h5>
                            <div class="checkbox">
                            <label><input type="checkbox" name="RegistrationID" onchange="toggleColumn(0)" 
                                          ${sessionScope.setting.registrationId != null ? (sessionScope.setting.registrationId ? 'checked' : '') : 'checked'}> ID</label>
                            <label><input type="checkbox" name="email" onchange="toggleColumn(1)" 
                                          ${sessionScope.setting.email != null ? (sessionScope.setting.email ? 'checked' : '') : 'checked'}> Email</label>
                            <label><input type="checkbox" name="registrationTime" onchange="toggleColumn(2)" 
                                          ${sessionScope.setting.registrationTime != null ? (sessionScope.setting.registrationTime ? 'checked' : '') : 'checked'}> Registration Time</label>
                            <label><input type="checkbox" name="subject" onchange="toggleColumn(3)" 
                                          ${sessionScope.setting.subject != null ? (sessionScope.setting.subject ? 'checked' : '') : 'checked'}> Subject</label>
                            <label><input type="checkbox" name="campaign" onchange="toggleColumn(4)" 
                                          ${sessionScope.setting.campaign != null ? (sessionScope.setting.campaign ? 'checked' : '') : 'checked'}> Campaign</label>
                            <label><input type="checkbox" name="package" onchange="toggleColumn(5)" 
                                          ${sessionScope.setting.packageId != null ? (sessionScope.setting.packageId ? 'checked' : '') : 'checked'}> Package</label>
                                          <br><label><input type="checkbox" name="totalCost" onchange="toggleColumn(6)" 
                                          ${sessionScope.setting.totalCost != null ? (sessionScope.setting.totalCost ? 'checked' : '') : 'checked'}> Total Cost</label>
                            <label><input type="checkbox" name="validFrom" onchange="toggleColumn(7)" 
                                          ${sessionScope.setting.validFrom != null ? (sessionScope.setting.validFrom ? 'checked' : '') : 'checked'}> Valid From</label>
                            <label><input type="checkbox" name="validTo" onchange="toggleColumn(8)" 
                                          ${sessionScope.setting.validTo != null ? (sessionScope.setting.validTo ? 'checked' : '') : 'checked'}> Valid To</label>
                            <label><input type="checkbox" name="status" onchange="toggleColumn(9)" 
                                          ${sessionScope.setting.status != null ? (sessionScope.setting.status ? 'checked' : '') : 'checked'}> Status</label>
                            <label><input type="checkbox" name="staff" onchange="toggleColumn(10)" 
                                          ${sessionScope.setting.staff != null ? (sessionScope.setting.staff ? 'checked' : '') : 'checked'}> Last Updated By</label>
                            <label><input type="checkbox" name="note" onchange="toggleColumn(11)" 
                                          ${sessionScope.setting.note != null ? (sessionScope.setting.note ? 'checked' : '') : 'checked'}> Note</label>
                            </div>
                            <button type="submit" onclick="saveSettings()">Apply</button>
                        </form>
                    </div>

                    <div class="registrationTable" id="registrationList">
                        <c:if test="${not empty listR}">
                            <table class="table table-bordered" id="registrationTable">
                                <thead>
                                    <tr>
                                        <th style="${sessionScope.setting.registrationId ? '' : 'display:none;'}" onclick="sortTable(0)">ID <i class="bi bi-chevron-expand"></i></th>
                                        <th style="${sessionScope.setting.email ? '' : 'display:none;'}" onclick="sortTable(1)">Email <i class="bi bi-chevron-expand"></i></th>
                                        <th style="${sessionScope.setting.registrationTime ? '' : 'display:none;'}"  onclick="sortTable(2)">Registration Time <i class="bi bi-chevron-expand"></i></th>
                                        <th style="${sessionScope.setting.subject ? '' : 'display:none;'}" onclick="sortTable(3)">Subject <i class="bi bi-chevron-expand"></i></th>
                                        <th style="${sessionScope.setting.campaign ? '' : 'display:none;'}" onclick="sortTable(4)">Campaign <i class="bi bi-chevron-expand"></i></th>
                                        <th style="${sessionScope.setting.packageId ? '' : 'display:none;'}" onclick="sortTable(5)">Package <i class="bi bi-chevron-expand"></i></th>
                                        <th  style="${sessionScope.setting.totalCost ? '' : 'display:none;'}"onclick="sortTable(6)">Total Cost <i class="bi bi-chevron-expand"></i></th>
                                        <th style="${sessionScope.setting.validFrom ? '' : 'display:none;'}" onclick="sortTable(7)">Valid From <i class="bi bi-chevron-expand"></i></th>
                                        <th style="${sessionScope.setting.validTo ? '' : 'display:none;'}"onclick="sortTable(8)">Valid To <i class="bi bi-chevron-expand"></i></th>
                                        <th style="${sessionScope.setting.status ? '' : 'display:none;'}"onclick="sortTable(9)">Status <i class="bi bi-chevron-expand"></i></th>
                                        <th style="${sessionScope.setting.staff ? '' : 'display:none;'}" onclick="sortTable(10)">Last Updated By <i class="bi bi-chevron-expand"></i></th>
                                        <th style="${sessionScope.setting.note ? '' : 'display:none;'}" onclick="sortTable(11)">Note <i class="bi bi-chevron-expand"></i></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="registration" items="${listR}">
                                        <tr>
                                            <td style="${sessionScope.setting.registrationId ? '' : 'display:none;'}" >
                                                <a href="registrationDetail?id=${registration.registrationId}" class="text-decoration-none">
                                                    ${registration.registrationId}
                                                </a>
                                            </td>
                                            <td  style="${sessionScope.setting.email ? '' : 'display:none;'}">${customerEmail[registration.userId]}</td>
                                            <td style="${sessionScope.setting.registrationTime ? '' : 'display:none;'}">${registration.registrationTime}</td>
                                            <td style="${sessionScope.setting.subject ? '' : 'display:none;'}">${subjectTitle[registration.subjectId]}</td>
                                            <td style="${sessionScope.setting.campaign ? '' : 'display:none;'}"  data-campaign-name="${registration.campaignName}">${registration.campaignName}</td>
                                            <td style="${sessionScope.setting.packageId ? '' : 'display:none;'}">${packageName[registration.packageId]}</td>
                                            <td style="${sessionScope.setting.totalCost ? '' : 'display:none;'}">${registration.totalCost}</td>
                                            <td style="${sessionScope.setting.validFrom ? '' : 'display:none;'}">${registration.validFrom}</td>
                                            <td style="${sessionScope.setting.validTo ? '' : 'display:none;'}">${registration.validTo}</td>
                                            <td style="${sessionScope.setting.status ? '' : 'display:none;'}">${registration.status}</td>
                                            <td style="${sessionScope.setting.staff ? '' : 'display:none;'}">${staffUsername[registration.staffId]}</td>
                                            <td style="${sessionScope.setting.note ? '' : 'display:none;'}">${registration.note}</td>
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
                                <li class="page-item <c:if test='${i == currentPage}'>ctive</c:if>'">
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
               <script>
                    function submitForm() {
                        const searchForm = document.getElementById('searchForm');
                        const settingsForm = document.getElementById('settingsForm');
                    }
                    function sortTable(columnIndex) {
                        const table = document.getElementById("registrationTable");
                        const tbody = table.getElementsByTagName("tbody")[0];
                        const rows = Array.from(tbody.getElementsByTagName("tr"));
                        const isAscending = table.dataset.sortOrder === 'asc';

                        // Determine sort order
                        table.dataset.sortOrder = isAscending ? 'desc' : 'asc';

                        rows.sort((rowA, rowB) => {
                            const cellA = rowA.getElementsByTagName("td")[columnIndex].innerText;
                            const cellB = rowB.getElementsByTagName("td")[columnIndex].innerText;

                            // Log the cells being compared
                            console.log(`Comparing ${cellA} to ${cellB} in ${isAscending ? 'ascending' : 'descending'} order.`);

                            // You might want to handle different data types
                            const compareA = isNaN(cellA) ? cellA : parseFloat(cellA);
                            const compareB = isNaN(cellB) ? cellB : parseFloat(cellB);

                            return isAscending ? (compareA > compareB ? 1 : -1) : (compareA < compareB ? 1 : -1);
                        });

                        // Append sorted rows back to the table
                        rows.forEach(row => tbody.appendChild(row));
                    }
                </script>
                <%@include file="Footer.jsp" %>
            </body>
        </html>
