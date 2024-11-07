<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<!DOCTYPE html>
<html>
    <head>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;600;700&display=swap" rel="stylesheet">  
        <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.5/font/bootstrap-icons.min.css" rel="stylesheet">
        <link href="css/Header_Footer.css" rel="stylesheet"> 
        <meta charset="UTF-8">
        <title>Course List</title>
        <style>
            table {
                width: 100%;
                border-collapse: collapse;
                margin-top: 20px;
            }

            th, td {
                padding: 12px;
                text-align: left;
                vertical-align: middle;
            }

            th {
                background-color: #f8f9fa; /* Light grey for the header */
                color: #495057; /* Dark text color */
                font-weight: bold;
                border-top: 2px solid #dee2e6; /* Top border */
                border-bottom: 2px solid #dee2e6; /* Bottom border */
            }

            td {
                border-bottom: 1px solid #dee2e6;
            }

            /* Hover effect for table rows */
            tr:hover {
                background-color: #f1f3f5; /* Light grey on hover */
            }

            /* Align text in the action column */
            td a {
                color: #0d6efd; /* Bootstrap primary color */
                text-decoration: none;
            }

            /* Button style for "Add New Course" */
            .btn-success {
                margin: 10px 0;
            }

            /* Pagination style */
            .pagination {
                display: flex;
                justify-content: center;
                margin-top: 20px;
            }

            .pagination span, .pagination a {
                padding: 8px 12px;
                margin: 0 2px;
                border: 1px solid #dee2e6;
                color: #0d6efd; /* Bootstrap primary color */
                text-decoration: none;
            }

            .pagination span {
                background-color: #0d6efd;
                color: white;
            }
        </style>

        <script>
            function toggleColumn(columnClass) {
                var elements = document.getElementsByClassName(columnClass);
                for (var i = 0; i < elements.length; i++) {
                    if (elements[i].classList.contains("hidden")) {
                        elements[i].classList.remove("hidden");
                    } else {
                        elements[i].classList.add("hidden");
                    }
                }
            }
        </script>
    </head>
    <body>
        <c:choose>
            <c:when test="${empty sessionScope.user}">
                <c:redirect url="login.jsp"/>
            </c:when>
            <c:when test="${sessionScope.user.role != 'Admin' && sessionScope.user.role != 'Instructor'}">
                <c:redirect url="/Homepage"/>
            </c:when>
            <c:otherwise>

                <%@include file="Header.jsp" %>
                <h1>Course List</h1>

                <%-- Search and Filter Form --%>
                <form action="SubjectList" method="GET">
                    <%-- Search input with value persistence --%>
                    <input type="text" name="search" placeholder="search by name" value="${param.search}">

                    <%-- Category dropdown with dynamic options --%>
                    <select name="category">
                        <option value="">All</option>
                        <c:forEach items="${categories}" var="category">
                            <%-- Maintain selected category after form submission --%>
                            <option value="${category.subjectCategoryId}" 
                                    ${param.category == category.subjectCategoryId ? 'selected' : ''}>
                                ${category.title}
                            </option>
                        </c:forEach>
                    </select>

                    <%-- Status filter dropdown --%>
                    <select name="status">
                        <option value="">Tất cả trạng thái</option>
                        <option value="Active">Active</option>
                        <option value="Inactive">Inactive</option>
                    </select>

                    <input type="submit" value="Tìm kiếm">
                </form>

                <%-- Column Visibility Controls 
                     JavaScript functionality to toggle table columns
                --%>
                <div>
                    <%-- Commented out controls
                    <input type="checkbox" onclick="toggleColumn('col-id')" checked> ID
                    <input type="checkbox" onclick="toggleColumn('col-name')" checked> Name
                    <input type="checkbox" onclick="toggleColumn('col-category')" checked> Category
                    --%>
                    <input type="checkbox" onclick="toggleColumn('col-lessons')" checked> Number of lesson
                    <input type="checkbox" onclick="toggleColumn('col-owner')" checked> Owner
                    <input type="checkbox" onclick="toggleColumn('col-status')" checked> Status
                </div>
                <c:if test="${sessionScope.user.role == 'Admin'}">
                    <a href="newSubject"  class="btn btn-success"><span>Add New Course</span></a>
                </c:if>

                <%-- Subject List Table --%>
                <table>
                    <tr>
                        <%-- Table headers with corresponding CSS classes for toggle functionality --%>
                        <th class="col-id">ID</th>
                        <th class="col-name">Name</th>
                        <th class="col-category">Category</th>
                        <th class="col-lessons">Number of lesson</th>
                        <th class="col-owner">Owner</th>
                        <th class="col-status">Status</th>
                        <th>Action</th>
                    </tr>
                    <%-- Iterate through subjects and display data --%>
                    <c:forEach items="${subjects}" var="course">
                        <tr>
                            <td class="col-id">${course.subjectID}</td>
                            <td class="col-name">
                                <a href="subjectLesson?courseId=${course.subjectID}&courseName=${course.title}">
                                    ${course.title}
                                </a>
                            </td>
                            <td class="col-category">${course.subjectCategoryId}</td>
                            <td class="col-lessons">${lessonCounts[course.subjectID]}</td>
                            <td class="col-owner">${course.userName}</td>
                            <td class="col-status">${course.status}</td>
                            <td>
                                <a href="SubjectDetailOverview?id=${course.subjectID}">Chỉnh sửa</a>
                            </td>
                        </tr>
                    </c:forEach>
                </table>

                <%-- Pagination Controls --%>
                <c:if test="${totalPages > 1}">
                    <div class="pagination">
                        <c:forEach begin="1" end="${totalPages}" var="i">
                            <c:choose>
                                <%-- Highlight current page --%>
                                <c:when test="${currentPage eq i}">
                                    <span>${i}</span>
                                </c:when>
                                <%-- Links to other pages with search parameters preserved --%>
                                <c:otherwise>
                                    <a href="?page=${i}&search=${param.search}&category=${param.category}&status=${param.status}">
                                        ${i}
                                    </a>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </div>
                </c:if>
            </c:otherwise>
        </c:choose>

    </body>
    <%@include file="Footer.jsp" %>
</html>
