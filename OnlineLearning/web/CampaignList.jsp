<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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

        <!-- New Campaign Button -->
        <div class="mb-3">
            <a href="AddCampaign" class="btn btn-primary">New Campaign</a>
        </div>

        <table class="table table-striped">
            <thead class="table-header">
                <tr>
                    <th class="campaign-id">Campaign ID</th>
                    <th class="campaign-name">Campaign Name</th>
                    <th class="campaign-description">Description</th>
                    <th class="campaign-start-date">Start Date</th>
                    <th class="campaign-end-date">End Date</th>
                    <th class="campaign-status">Status</th>
                    <th class="campaign-action">Action</th>
                </tr>
            </thead>
            <tbody class="campaign-body">
                <c:forEach var="entry" items="${campaigns}">
                    <c:set var="campaign" value="${entry.value}" />
                    <tr class="campaign-row">
                        <td class="campaign-id-value">
                            <a href="updateCampaign?id=${campaign.campaignId}" class="text-decoration-none">
                                ${campaign.campaignId}
                            </a>
                        </td>
                        <td class="campaign-name-value">${campaign.campaignName}</td>
                        <td class="campaign-description-value">${campaign.description}</td>
                        <td class="campaign-start-date-value">${campaign.startDate}</td>
                        <td class="campaign-end-date-value">${campaign.endDate}</td>
                        <td class="campaign-status-value">${campaign.status}</td>
                        <td class="campaign-action-cell">
                            <c:choose>
                                <c:when test="${campaign.status == 'Processing'}">
                                    <form action="stopCampaign" method="post">
                                        <input type="hidden" name="campaignId" value="${campaign.campaignId}">
                                        <button type="submit" class="btn btn-danger">Stop</button>
                                    </form>
                                </c:when>
                                <c:when test="${campaign.status == 'Not Start'}">
                                    <form action="startCampaign" method="post">
                                        <input type="hidden" name="campaignId" value="${campaign.campaignId}">
                                        <button type="submit" class="btn btn-success">Start</button>
                                    </form>
                                </c:when>
                                <c:otherwise>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</body>
<%@ include file="Footer.jsp" %>
</html>
