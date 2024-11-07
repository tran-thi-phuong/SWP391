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
                border-collapse: collapse;
                width: 100%;
            }
            th, td {
                border: 1px solid black;
                padding: 8px;
                text-align: left;
            }
            th {
                background-color: #f2f2f2;
            }
            .hidden {
                display: none;
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

                <%@include file="Header.jsp" %>
                <h1>Subject List</h1>

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
                            <td class="col-lessons">0</td>
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
            

            </body>
            <%@include file="Footer.jsp" %>
        </html>
