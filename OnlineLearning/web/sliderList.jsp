<%-- 
    Document   : sliderJSP
    Created on : Oct 9, 2024, 12:57:48 AM
    Author     : sonna
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <head>
        <title>Slider List</title>
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;600;700&display=swap" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.5/font/bootstrap-icons.min.css" rel="stylesheet">
        <link href="css/Header_Footer.css" rel="stylesheet"> 
        <script src="js/sliderList.js"></script>
        <style>
            table {
                width: 100%;
                border-collapse: collapse;
            }
            th, td {
                border: 1px solid #dddddd;
                text-align: left;
                padding: 8px;
            }
            th {
                background-color: #f2f2f2;
            }
            .hidden {
                display: none;
            }
        </style>
    </head>
    <body>
                <%@include file="Header.jsp" %>
                <h1>Slider List</h1>
                <div class="filter-form">
                    <form action="sliderList" method="GET">
                        <input type="text" name="searchQuery" placeholder="Search by title or backlink"
                               value="${searchQuery}" />

                        <select name="status">
                            <option value="All" <c:if test="${statusFilter == null || statusFilter == 'All'}">selected</c:if>>All</option>
                            <option value="Show" <c:if test="${statusFilter == 'Show'}">selected</c:if>>Show</option>
                            <option value="Hide" <c:if test="${statusFilter == 'Hide'}">selected</c:if>>Hide</option>
                            </select>

                            <button type="submit">Filter</button>
                        </form>
                    </div>

                    <div>
                        <input type="checkbox" onclick="toggleColumn('col-id')" checked> ID
                        <input type="checkbox" onclick="toggleColumn('col-title')" checked> Title
                        <input type="checkbox" onclick="toggleColumn('col-image')" checked> Image
                        <input type="checkbox" onclick="toggleColumn('col-backlink')" checked> Backlink
                        <input type="checkbox" onclick="toggleColumn('col-status')" checked> Status
                    </div>
                    <table>
                        <thead>
                            <tr>
                                <th class="col-id">Slider ID</th>
                                <th class="col-title">Title</th>
                                <th class="col-image">Image</th>
                                <th class="col-backlink">Backlink</th>
                                <th class="col-status">Status</th>
                            </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="s" items="${slider}">
                            <tr>
                                <td class="col-id">${s.sliderID}</td>
                                <td class="col-title">${s.title}</td>
                                <td class="col-image">
                                    <img src="${s.image}" alt="${s.title}" style="width: 100px; height: auto;">
                                </td>
                                <td class="col-backlink">${s.backlink}</td>
                                <td class="col-status">
                                    <!-- Display Show/Hide buttons based on the current status -->
                                    <form action="sliderList" method="post">
                                        <input type="hidden" name="sliderID" value="${s.sliderID}">
                                        <c:choose>
                                            <c:when test="${s.status eq 'Hide'}">
                                                <button type="submit" name="action" value="Show" class="btn btn-success">Show</button>
                                            </c:when>
                                            <c:otherwise>
                                                <button type="submit" name="action" value="Hide" class="btn btn-danger">Hide</button>
                                            </c:otherwise>
                                        </c:choose>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <p></p>
                <%@include file="Footer.jsp" %>
          
            </body>
        </html>