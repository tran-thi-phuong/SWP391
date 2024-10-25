<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> <!-- Thêm dòng này -->
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;600;700&display=swap" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.5/font/bootstrap-icons.min.css" rel="stylesheet">
        <link href="css/Header_Footer.css" rel="stylesheet">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
        <link rel="stylesheet" href="css/updateCampaign.css">
        <title>Update Campaign</title>
    </head>
    <%@include file="Header.jsp" %>
    <body> 
        <c:choose>
            <c:when test="${empty sessionScope.user}">
                <c:redirect url="login.jsp"/>
            </c:when>
            <c:when test="${sessionScope.user.role != 'Admin' && sessionScope.user.role != 'Marketing'}">
                <c:redirect url="/Homepage"/>
            </c:when>
            <c:otherwise>
                <div class="container2 mt-5">

                    <h2>Campaign Details</h2>

                    <!-- Display success or error message -->
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger" role="alert">
                            ${error}
                        </div>
                    </c:if>
                    <div class="update">
                        <form action="updateCampaign" method="post">
                            <input type="hidden" name="campaignId" value="${campaign.campaignId}">
                            <div class="mb-3">
                                <label for="campaignName" class="form-label">Campaign Name</label>
                                <input type="text" class="form-control" id="campaignName" name="campaignName" value="${campaign.campaignName}" required>
                            </div>
                            <div class="mb-3">
                                <label for="campaignDescription" class="form-label">Description</label>
                                <textarea class="form-control" id="campaignDescription" name="description" required>${campaign.description}</textarea>
                            </div>
                            <div class="mb-3">
                                <label for="campaignStartDate" class="form-label">Start Date</label>
                                <input type="date" class="form-control" id="campaignStartDate" name="startDate" value="${fn:substring(campaign.startDate, 0, 10)}" required>
                            </div>
                            <div class="mb-3">
                                <label for="campaignEndDate" class="form-label">End Date</label>
                                <input type="date" class="form-control" id="campaignEndDate" name="endDate" value="${fn:substring(campaign.endDate, 0, 10)}" required>
                            </div>
                            <div class="mb-3">
                                <label for="campaignStatus" class="form-label">Status</label>
                                <select class="form-control" id="campaignStatus" name="status" required>
                                    <option value="Not Start" ${campaign.status == 'Not Start' ? 'selected' : ''}>Not Start</option>
                                    <option value="Processing" ${campaign.status == 'Processing' ? 'selected' : ''}>Processing</option>
                                    <option value="Completed" ${campaign.status == 'End' ? 'selected' : ''}>End</option>
                                </select>
                            </div>
                            <button type="submit" class="btn btn-primary">Save changes</button>
                            <button type="button" class="btn btn-secondary" onclick="window.location.href = 'campaignList';">Cancel</button>
                        </form>

                    </div>
                </div>
            </c:otherwise>
        </c:choose>

    </body>
    <%@include file="Footer.jsp" %>
</html>
