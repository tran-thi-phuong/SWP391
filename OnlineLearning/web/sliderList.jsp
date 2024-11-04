<%-- 
    Document   : sliderJSP
    Created on : Oct 9, 2024, 12:57:48 AM
    Author     : sonna
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> <!-- Importing JSTL core library for tag support -->
<html>
    <head>
        <title>Slider List</title> <!-- Title of the page -->
        <link rel="preconnect" href="https://fonts.googleapis.com"> <!-- Preconnecting to Google Fonts -->
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;600;700&display=swap" rel="stylesheet"> <!-- Including Montserrat font -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous"> <!-- Including Bootstrap CSS -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.5/font/bootstrap-icons.min.css" rel="stylesheet"> <!-- Including Bootstrap Icons -->
        <link href="css/Header_Footer.css" rel="stylesheet">  <!-- Including custom CSS for Header and Footer -->
        <script src="js/sliderList.js"></script> <!-- Including JavaScript file for slider list functionalities -->
        <style>
            /* Inline styles for table layout */
            table {
                width: 100%; /* Table width set to 100% */
                border-collapse: collapse; /* Collapse table borders */
            }
            th, td {
                border: 1px solid #dddddd; /* Light gray border for table cells */
                text-align: left; /* Align text to the left */
                padding: 8px; /* Padding for table cells */
            }
            th {
                background-color: #f2f2f2; /* Light gray background for header cells */
            }
            .hidden {
                display: none; /* Class to hide elements */
            }
        </style>
    </head>
    <body>
        <%@include file="Header.jsp" %> <!-- Including the Header JSP -->
        <h1>Slider List</h1> <!-- Main heading for the page -->
        <div class="filter-form">
            <form action="sliderList" method="GET"> <!-- Form to filter sliders -->
                <input type="text" name="searchQuery" placeholder="Search by title or backlink" value="${searchQuery}" /> <!-- Input field for search query -->

                <select name="status"> <!-- Dropdown for status filter -->
                    <option value="All" <c:if test="${statusFilter == null || statusFilter == 'All'}">selected</c:if>>All</option> <!-- Option for All status -->
                    <option value="Show" <c:if test="${statusFilter == 'Show'}">selected</c:if>>Show</option> <!-- Option for Show status -->
                    <option value="Hide" <c:if test="${statusFilter == 'Hide'}">selected</c:if>>Hide</option> <!-- Option for Hide status -->
                    </select>

                    <button type="submit">Filter</button> <!-- Button to submit the filter form -->
                </form>
            </div>

            <div>
                <!-- Checkboxes to toggle the visibility of different columns in the table -->
                <input type="checkbox" onclick="toggleColumn('col-id')" checked> ID
                <input type="checkbox" onclick="toggleColumn('col-title')" checked> Title
                <input type="checkbox" onclick="toggleColumn('col-image')" checked> Image
                <input type="checkbox" onclick="toggleColumn('col-backlink')" checked> Backlink
                <input type="checkbox" onclick="toggleColumn('col-status')" checked> Status
            </div>

            <table>
                <thead>
                    <tr>
                        <th class="col-id">Slider ID</th> <!-- Column header for Slider ID -->
                        <th class="col-title">Title</th> <!-- Column header for Title -->
                        <th class="col-image">Image</th> <!-- Column header for Image -->
                        <th class="col-backlink">Backlink</th> <!-- Column header for Backlink -->
                        <th class="col-status">Status</th> <!-- Column header for Status -->
                    </tr>
                </thead>
                <tbody>
                <c:forEach var="s" items="${slider}"> <!-- Loop through each slider item -->
                    <tr>
                        <td class="col-id">${s.sliderID}</td> <!-- Display Slider ID -->
                        <td class="col-title">${s.title}</td> <!-- Display Slider Title -->
                        <td class="col-image">
                            <img src="${s.image}" alt="${s.title}" style="width: 100px; height: auto;"> <!-- Display Slider Image -->
                        </td>
                        <td class="col-backlink">${s.backlink}</td> <!-- Display Slider Backlink -->
                        <td class="col-status">
                            <!-- Display Show/Hide buttons based on the current status -->
                            <form action="sliderList" method="post">
                                <input type="hidden" name="sliderID" value="${s.sliderID}"> <!-- Hidden input for Slider ID -->
                                <c:choose>
                                    <c:when test="${s.status eq 'Hide'}">
                                        <button type="submit" name="action" value="Show" class="btn btn-success">Show</button> <!-- Button to show the slider -->
                                    </c:when>
                                    <c:otherwise>
                                        <button type="submit" name="action" value="Hide" class="btn btn-danger">Hide</button> <!-- Button to hide the slider -->
                                    </c:otherwise>
                                </c:choose>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <p></p>
        <%@include file="Footer.jsp" %> <!-- Including the Footer JSP -->
    </body>
</html>
