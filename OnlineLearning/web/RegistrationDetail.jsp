<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Registration Detail</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;600;700&display=swap" rel="stylesheet">  
    <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.5/font/bootstrap-icons.min.css" rel="stylesheet">
    <link href="css/Header_Footer.css" rel="stylesheet">
    <link rel="stylesheet" href="css/registration_detail.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
    <link href="css/registration_detail" rel="stylesheet">
</head>
<body>
    <%@include file="Header.jsp" %>
    <div class="container mt-5">
        <h1 class="text-center">Registration Detail</h1>
        
        <c:if test="${not empty registration}">
            <div class="card">
                <div class="card-body">
                    <h5 class="card-title">Registration ID: ${registration.registrationId}</h5>
                    <p><strong>Email:</strong> ${customerEmail}</p>
                    <p><strong>Registration Time:</strong> ${registration.registrationTime}</p>
                    <p><strong>Subject:</strong> ${subjectTitle}</p>
                    <p><strong>Campaign:</strong> ${registration.campaignName}</p>
                    <p><strong>Package:</strong> ${packageName}</p>
                    <p><strong>Total Cost:</strong> ${registration.totalCost}</p>
                    <p><strong>Valid From:</strong> ${registration.validFrom}</p>
                    <p><strong>Valid To:</strong> ${registration.validTo}</p>
                    <p><strong>Status:</strong> ${registration.status}</p>
                    <p><strong>Last Updated By:</strong> ${saleUsername}</p>
                    <p><strong>Note:</strong> ${registration.note}</p>
                </div>
            </div>
        </c:if>
        
        <div class="mt-4">
            <a href="listRegistration" class="btn btn-primary">Back to Registration List</a>
        </div>
    </div>
    <%@include file="Footer.jsp" %>
</body>
</html>
