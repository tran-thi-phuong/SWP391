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
        <link rel="stylesheet" href="css/CampaignSubject.css">
        <title>Manage Campaigns and Subjects</title>       
    </head>
    <%@ include file="Header.jsp" %>
    <body class="campaign-subject-body">

        <h2 class="campaign-subject-title">Assign Campaigns to Subjects</h2>
        <!-- Display error or success messages -->
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger">
                <strong>Error:</strong> ${errorMessage}
            </div>
        </c:if>

        <c:if test="${not empty successMessage}">
            <div class="alert alert-success">
                <strong>Success:</strong> ${successMessage}
            </div>
        </c:if>

        <!-- Start the form -->
        <form action="ManageCampaignSubject" method="POST">
            <div class="text-right">
                <button type="submit" class="btn btn-primary">Save</button>
            </div>
            <table class="table table-bordered campaign-subject-table">
                <thead>
                    <tr>
                        <th class="campaign-name-column">Campaign Name</th>  
                        <th class="select-subject-column">Select Subject</th>
                        <th class="discount-column">Discount (%)</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="campaign" items="${ongoingCampaigns}">
                        <!-- Display campaign name in a row that spans all columns -->
                        <tr class="campaign-row">
                            <td class="campaign-name" colspan="3"><strong>${campaign.campaignName}</strong></td>
                        </tr>

                        <!-- Display subjects for each campaign -->
                        <c:forEach var="subject" items="${activeSubjects}">
                            <tr class="subject-row">
                                <td class="empty-cell"></td> <!-- Empty cell for spacing under the campaign name -->
                                <td class="subject-cell">
                                    <!-- Check if the subject is part of this campaign -->
                                    <c:set var="key" value="${campaign.campaignId}_${subject.subjectID}" />

                                    <!-- Checkbox: Check if a discount exists for the campaign-subject pair -->
                                    <input type="checkbox" name="subjectSelection_${campaign.campaignId}_${subject.subjectID}" 
                                           value="${subject.subjectID}" 
                                           <c:if test="${currentDiscounts[key] != null}">checked</c:if> />
                                    ${subject.title}
                                </td>
                                <td class="discount-cell">
                                    <!-- Discount: Display current discount if available -->
                                    <input type="text" name="discount_${subject.subjectID}_${campaign.campaignId}" 
                                           placeholder="Enter discount" 
                                           class="discount-input" 
                                           <c:if test="${currentDiscounts[key] != null}">
                                               value="${currentDiscounts[key]}"
                                           </c:if> />
                                </td>
                            </tr>
                        </c:forEach>
                    </c:forEach>

                </tbody>
            </table>
        </form>

        <%@ include file="Footer.jsp" %>
    </body>
</html>
