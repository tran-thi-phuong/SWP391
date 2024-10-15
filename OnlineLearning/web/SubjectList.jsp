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
        <form action="SubjectList" method="GET">
            <input type="text" name="search" placeholder="search by name" value="${param.search}">

            <select name="category">
                <option value="">All</option>
                <c:forEach items="${categories}" var="category">
                    <option value="${category.subjectCategoryId}" ${param.category == category.subjectCategoryId ? 'selected' : ''}>${category.title}</option>
                </c:forEach>
            </select>

            <select name="status">
                <option value="">Tất cả trạng thái</option>
                    <option value="Active">Active</option>
                    <option value="Inactive">Inactive</option>
            </select>

            <input type="submit" value="Tìm kiếm">
        </form>
        <div>
            <!--        <input type="checkbox" onclick="toggleColumn('col-id')" checked> ID
                    <input type="checkbox" onclick="toggleColumn('col-name')" checked> Name
                    <input type="checkbox" onclick="toggleColumn('col-category')" checked> Category-->
            <input type="checkbox" onclick="toggleColumn('col-lessons')" checked> Number of lesson
            <input type="checkbox" onclick="toggleColumn('col-owner')" checked> Owner
            <input type="checkbox" onclick="toggleColumn('col-status')" checked> Status
        </div>
        <a href="newSubject"  class="btn btn-success"><span>Add New Product</span></a>


        <table>
            <tr>
                <th class="col-id">ID</th>
                <th class="col-name">Name</th>
                <th class="col-category">Category</th>
                <th class="col-lessons">Number of lesson</th>
                <th class="col-owner">Owner</th>
                <th class="col-status">Status</th>
                <th>Action</th>
            </tr>
            <c:forEach items="${subjects}" var="course">
                <tr>
                    <td class="col-id">${course.subjectID}</td>
                    <td class="col-name">${course.title}</td>
                    <td class="col-category">${course.subjectCategoryId}</td>
                    <td class="col-lessons">0</td>
                    <td class="col-owner">${course.userName}</td>
                    <td class="col-status">${course.status}</td>
                    <td>
                        <a href="editCourse.jsp?id=${course.subjectID}">Chỉnh sửa</a>
                    </td>
                </tr>
            </c:forEach>
        </table>

        <!-- Phân trang -->
        <c:if test="${totalPages > 1}">
            <div class="pagination">
                <c:forEach begin="1" end="${totalPages}" var="i">
                    <c:choose>
                        <c:when test="${currentPage eq i}">
                            <span>${i}</span>
                        </c:when>
                        <c:otherwise>
                            <a href="?page=${i}&search=${param.search}&category=${param.category}&status=${param.status}">${i}</a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </div>
        </c:if>

    </body>
    <%@include file="Footer.jsp" %>
</html>
