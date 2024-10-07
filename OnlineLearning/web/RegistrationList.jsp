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
            <form id="searchForm" action="listRegistration" method="get">
                <div class="row mb-3">
                    <div class="col">
                        <input type="text" class="form-control" id="emailSearch" name="email" placeholder="Search by Email" value="${param.email}" oninput="filterRegistrations()"/>
                    </div>
                    <div class="col">
                        <select class="form-select" id="campaignSearch" name="campaignId" onchange="filterRegistrations()">
                            <option value="">Select Campaign</option>
                            <c:forEach items="${listC.keySet()}" var="caId">
                                <option value="${caId}" ${caId == param.campaignId ? "selected" : ""}>${listC[caId].campaignName}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col">
                        <input type="text" class="form-control" id="subjectSearch" name="subject" placeholder="Search by Subject" value="${param.subjectID}" oninput="filterRegistrations()"/>
                    </div>
                    <div class="col">
                        <div class="input-group">
                            <span class="input-group-text">From</span>
                            <input type="date" class="form-control" id="registrationFrom" name="validFrom" value="${param.validFrom}" onchange="filterRegistrations()"/>
                        </div>
                    </div>
                    <div class="col">
                        <div class="input-group">
                            <span class="input-group-text">To</span>
                            <input type="date" class="form-control" id="registrationTo" name="validTo" value="${param.validTo}" onchange="filterRegistrations()"/>
                        </div>
                    </div>
                    <div class="col">
                        <select class="form-select" id="statusSearch" name="status" onchange="filterRegistrations()">
                            <option value="">Select Status</option>
                            <option value="Active" ${param.status == "Active" ? "selected" : ""}>Active</option>
                            <option value="Expired" ${param.status == "Expired" ? "selected" : ""}>Expired</option>
                            <option value="Processing" ${param.status == "Processing" ? "selected" : ""}>Processing</option>
                        </select>
                    </div>
                </div>
            </form>

            <div>
                <h5 style=" color: #FF0000">Show/Hide Columns:</h5>
                <label><input type="checkbox" checked onchange="toggleColumn(0)"> ID</label>
                <label><input type="checkbox" checked onchange="toggleColumn(1)"> Email</label>
                <label><input type="checkbox" checked onchange="toggleColumn(2)"> Registration Time</label>
                <label><input type="checkbox" onchange="toggleColumn(3)"> Subject</label>
                <label><input type="checkbox" onchange="toggleColumn(4)"> Campaign</label>
                <label><input type="checkbox" onchange="toggleColumn(5)"> Package</label>
                <label><input type="checkbox" onchange="toggleColumn(6)"> Total Cost</label>
                <label><input type="checkbox" onchange="toggleColumn(7)"> Valid From</label>
                <label><input type="checkbox" onchange="toggleColumn(8)"> Valid To</label>
                <label><input type="checkbox" onchange="toggleColumn(9)"> Status</label>
                <label><input type="checkbox" onchange="toggleColumn(10)"> Last Updated By</label>
                <label><input type="checkbox" onchange="toggleColumn(11)"> Note</label>
            </div>

            <div class="registrationTable" id="registrationList">
                <c:if test="${not empty listR}">
                    <table class="table table-bordered" id="registrationTable">
                        <thead>
                            <tr>
                                <th onclick="sortTable(0)">ID <i class="bi bi-chevron-expand"></i></th>
                                <th onclick="sortTable(1)">Email <i class="bi bi-chevron-expand"></i></th>
                                <th onclick="sortTable(2)">Registration Time <i class="bi bi-chevron-expand"></i></th>
                                <th onclick="sortTable(3)">Subject <i class="bi bi-chevron-expand"></i></th>
                                <th onclick="sortTable(4)">Campaign <i class="bi bi-chevron-expand"></i></th>
                                <th onclick="sortTable(5)">Package <i class="bi bi-chevron-expand"></i></th>
                                <th onclick="sortTable(6)">Total Cost <i class="bi bi-chevron-expand"></i></th>
                                <th onclick="sortTable(7)">Valid From <i class="bi bi-chevron-expand"></i></th>
                                <th onclick="sortTable(8)">Valid To <i class="bi bi-chevron-expand"></i></th>
                                <th onclick="sortTable(9)">Status <i class="bi bi-chevron-expand"></i></th>
                                <th onclick="sortTable(10)">Last Updated By <i class="bi bi-chevron-expand"></i></th>
                                <th onclick="sortTable(11)">Note <i class="bi bi-chevron-expand"></i></th>
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
                                    <td data-campaign-name="${registration.campaignName}">${registration.campaignName}</td>
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


            window.onload = function () {
                for (let i = 3; i <= 11; i++) {
                    toggleColumn(i);
                }
            };

            function toggleColumn(index) {
                const table = document.getElementById('registrationTable');
                const rows = table.getElementsByTagName('tr');

                for (let i = 0; i < rows.length; i++) {
                    const cell = rows[i].getElementsByTagName('th')[index] || rows[i].getElementsByTagName('td')[index];
                    if (cell) {
                        cell.style.display = cell.style.display === 'none' ? '' : 'none';
                    }
                }
            }

            function sortTable(index) {
                const table = document.getElementById('registrationTable');
                const rows = Array.from(table.rows).slice(1);
                const isAscending = table.getAttribute('data-order') === 'asc';

                rows.sort((a, b) => {
                    const aText = a.cells[index].textContent.trim();
                    const bText = b.cells[index].textContent.trim();

                    return aText.localeCompare(bText, undefined, {numeric: true}) * (isAscending ? 1 : -1);
                });

                rows.forEach(row => table.appendChild(row));
                table.setAttribute('data-order', isAscending ? 'desc' : 'asc');
            }


            function filterRegistrations() {
                const email = document.getElementById('emailSearch').value.toLowerCase().trim();
                const subject = document.getElementById('subjectSearch').value.toLowerCase().trim();
                const validFromInput = document.getElementById('registrationFrom').value;
                const validToInput = document.getElementById('registrationTo').value;
                const status = document.getElementById('statusSearch').value;
                const selectedCampaignName = document.getElementById('campaignSearch').options[document.getElementById('campaignSearch').selectedIndex].text;

                // Convert the input dates to Date objects
                const filterFromDate = validFromInput ? new Date(validFromInput) : null;
                const filterToDate = validToInput ? new Date(validToInput) : null;

                // Set the time of toDate to end of day for inclusive comparison
                if (filterToDate) {
                    filterToDate.setHours(23, 59, 59, 999);
                }

                const table = document.getElementById('registrationTable');
                const rows = table.getElementsByTagName('tr');

                for (let i = 1; i < rows.length; i++) { // Start from 1 to skip the header
                    const emailCell = rows[i].getElementsByTagName('td')[1];
                    const subjectCell = rows[i].getElementsByTagName('td')[3];
                    const validFromCell = rows[i].getElementsByTagName('td')[2];
                    const validToCell = rows[i].getElementsByTagName('td')[8];
                    const statusCell = rows[i].getElementsByTagName('td')[9];
                    const campaignCell = rows[i].getElementsByTagName('td')[4];

                    const emailText = emailCell.textContent || emailCell.innerText;
                    const subjectText = subjectCell.textContent || subjectCell.innerText;
                    const validFromText = (validFromCell.textContent || validFromCell.innerText).trim();
                    const validToText = (validToCell.textContent || validToCell.innerText).trim();
                    const statusText = statusCell.textContent || statusCell.innerText;
                    const campaignName = campaignCell.getAttribute("data-campaign-name") || "";

                    const rowValidFromDate = new Date(validFromText);
                    const rowValidToDate = new Date(validToText);

                    let showRow = true;

                    // Email filter
                    if (email && !emailText.toLowerCase().includes(email)) {
                        showRow = false;
                    }

                    // Subject filter (modified to use text input)
                    if (subject && !subjectText.toLowerCase().includes(subject)) {
                        showRow = false;
                    }
                    // Date range filter
                    if (filterFromDate || filterToDate) {
                        // Check if rowValidFromDate is within the date range
                        if (filterFromDate && rowValidFromDate < filterFromDate) {
                            showRow = false;
                        }
                        // Check if rowValidToDate is within the date range
                        if (filterToDate && rowValidToDate > filterToDate) {
                            showRow = false;
                        }
                    }

                    // Status filter
                    if (status && status !== "Select Status" && statusText.trim() !== status) {
                        showRow = false;
                    }

                    // Campaign filter
                    if (selectedCampaignName && selectedCampaignName !== "Select Campaign" && campaignName !== selectedCampaignName) {
                        showRow = false;
                    }

                    // Display or hide the row based on filter
                    rows[i].style.display = showRow ? "" : "none";
                }
            }

            function formatDate(dateString) {
                if (!dateString)
                    return null;
                const [year, month, day] = dateString.split('-');
                return new Date(year, month - 1, day);
            }

            // Add all necessary event listeners
            document.getElementById('emailSearch').addEventListener('input', filterRegistrations);
            document.getElementById('subjectSearch').addEventListener('change', filterRegistrations);
            document.getElementById('registrationFrom').addEventListener('change', filterRegistrations);
            document.getElementById('registrationTo').addEventListener('change', filterRegistrations);
            document.getElementById('statusSearch').addEventListener('change', filterRegistrations);
            document.getElementById('campaignSearch').addEventListener('change', filterRegistrations);

            document.getElementById('registrationFrom').addEventListener('change', function () {
                const fromDate = this.value;
                const toDateInput = document.getElementById('registrationTo');
                if (fromDate) {
                    toDateInput.min = fromDate;
                }
            });

            document.getElementById('registrationTo').addEventListener('change', function () {
                const toDate = this.value;
                const fromDateInput = document.getElementById('registrationFrom');
                if (toDate) {
                    fromDateInput.max = toDate;
                }
            });
            let sortDirection = true; 
        </script>

    </body>
</html>
