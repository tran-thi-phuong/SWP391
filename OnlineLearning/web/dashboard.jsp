<%-- 
    Document   : dashboard
    Created on : Sep 27, 2024, 9:57:01 AM
    Author     : tuant
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Dashboard</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;600;700&display=swap" rel="stylesheet">  
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.5/font/bootstrap-icons.min.css" rel="stylesheet">
        <link href="css/Header_Footer.css" rel="stylesheet">
        <link rel="stylesheet" href="css/sidebar.css"/>
        <link rel="stylesheet" href="css/dashboard.css"/>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-datalabels@2.0.0"></script>
        <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
    </head>
    <body>
        <c:choose>
            <c:when test="${empty sessionScope.user}">
                <c:redirect url="login.jsp"/>
            </c:when>
            <c:when test="${sessionScope.user.role != 'Admin' && sessionScope.user.role != 'Marketing'}">
                <c:redirect url="/Homepage"/>
            </c:when>
            <c:otherwise>
                <%@include file="Header.jsp" %>
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-md-3">
                            <%@include file="sidebar.jsp" %>
                        </div>
                        <c:choose>
                            <c:when test="${requestScope.action == 'courseStat'}">
                                <%@include file="courseStat.jsp" %>
                            </c:when>
                            <c:when test="${requestScope.action == 'registrationStat'}">
                                <%@include file="registrationStat.jsp" %>
                            </c:when>
                            <c:when test="${requestScope.action == 'revenueStat'}">
                                <%@include file="revenueStat.jsp" %>
                            </c:when>
                            <c:when test="${requestScope.action == 'customerStat'}">
                                <%@include file="customerStat.jsp" %>
                            </c:when>
                            <c:when test="${requestScope.action == 'campaignStat'}">
                                <%@include file="campaignStat.jsp" %>
                            </c:when>
                        </c:choose>
                    </div>
                </div>
                <%@include file="Footer.jsp" %>
            </c:otherwise>
        </c:choose>
            </body>
        </html>