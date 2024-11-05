<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;600;700&display=swap" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.5/font/bootstrap-icons.min.css" rel="stylesheet">
        <link href="css/Header_Footer.css" rel="stylesheet">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
        <link rel="stylesheet" href="css/campaign_list.css">
        <title>Campaign List</title>
    </head>
    <%@ include file="Header.jsp" %>
    <body>
        <div class="container mt-4">
            <h2>Campaign List</h2>

            <!-- Display success or error message -->
            <c:if test="${not empty error}">
                <div class="alert alert-danger" role="alert">
                    ${error}
                </div>
            </c:if>
            <c:if test="${not empty success}">
                <div class="alert alert-success" role="alert">
                    ${success}
                </div>
            </c:if>

            <!-- Search Form -->
            <form method="get" action="campaignList" id="searchForm">
                <div class="row mb-3">
                    <div class="col-md-3">
                        <label for="campaignName">Campaign Name</label>
                        <input type="text" name="campaignName" id="campaignName" class="form-control" placeholder="Campaign Name" value="${param.campaignName}">
                    </div>
                    <div class="col-md-2">
                        <label for="startDate">Start Date</label>
                        <input type="date" name="startDate" id="startDate" class="form-control" placeholder="Start Date" value="${param.startDate}">
                    </div>
                    <div class="col-md-2">
                        <label for="endDate">End Date</label>
                        <input type="date" name="endDate" id="endDate" class="form-control" placeholder="End Date" value="${param.endDate}">
                    </div>
                    <div class="col-md-2">
                        <label for="status">Status</label>
                        <select name="status" id="status" class="form-control">
                            <option value="">Select Status</option>
                            <option value="Not Start" ${param.status == 'Not Start' ? 'selected' : ''}>Not Start</option>
                            <option value="Active" ${param.status == 'Active' ? 'selected' : ''}>Active</option>
                            <option value="End" ${param.status == 'End' ? 'selected' : ''}>End</option>
                        </select>
                    </div>
                    <div class="col-md-2">
                        <label>&nbsp;</label> <!-- Giữ khoảng trống cho nút để căn chỉnh -->
                        <button type="submit" class="btn btn-primary d-block">Search</button>
                    </div>
                </div>
            </form>


            <!-- New Campaign Button -->
            <div class="mb-3">
                <a href="AddCampaign" class="btn btn-primary">New</a>
            </div>

            <!-- Campaign Table -->
            <table class="table table-striped">
                <thead class="table-header">
                    <tr>
                        <th>Campaign ID</th>
                        <th>Campaign Name</th>
                        <th>Description</th>
                        <th>Start Date</th>
                        <th>End Date</th>
                        <th>Status</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="campaign" items="${campaigns}">
                        <tr>
                            <td><a href="updateCampaign?id=${campaign.campaignId}" class="text-decoration-none">${campaign.campaignId}</a></td>
                            <td>${campaign.campaignName}</td>
                            <td>${campaign.description}</td>
                            <td>${campaign.startDate}</td>
                            <td>${campaign.endDate}</td>
                            <td>${campaign.status}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${campaign.status == 'Active'}">
                                        <form action="stopCampaign" method="post" style="display:inline;">
                                            <input type="hidden" name="campaignId" value="${campaign.campaignId}">
                                            <button type="submit" class="btn btn-danger">Stop</button>
                                        </form>
                                    </c:when>
                                    <c:when test="${campaign.status == 'Not Start'}">
                                        <form action="startCampaign" method="post" style="display:inline;">
                                            <input type="hidden" name="campaignId" value="${campaign.campaignId}">
                                            <button type="submit" class="btn btn-success">Start</button>
                                        </form>
                                    </c:when>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <!-- Pagination Controls -->
            <nav aria-label="Page navigation example">
                <ul class="pagination">
                    <c:if test="${currentPage > 1}">
                        <li class="page-item">
                            <a class="page-link" href="campaignList?page=${currentPage - 1}">Previous</a>
                        </li>
                    </c:if>
                    <c:forEach var="i" begin="1" end="${totalPages}">
                        <li class="page-item <c:if test='${i == currentPage}'>ctive</c:if>'">
                            <a class="page-link" href="campaignList?page=${i}">${i}</a>
                        </li>

                    </c:forEach>
                    <c:if test="${currentPage < totalPages}">
                        <li class="page-item">
                            <a class="page-link" href="campaignList?page=${currentPage + 1}">Next</a>
                        </li>
                    </c:if>
                </ul>
            </nav>
        </div>
        <script>
            function submitForm() {
                document.getElementById('searchForm').submit();
            }
        </script>
        <%@ include file="Footer.jsp" %>
    </body>
</html>
