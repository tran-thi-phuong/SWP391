<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<!DOCTYPE html>
<html>
<head>
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
            <c:forEach items="${subjects}" var="status">
                <option value="${status}" ${param.status == status ? 'selected' : ''}>${status.status}</option>
            </c:forEach>
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
    <a href="newSubject.jsp">Add new course</a>

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
                <td class="col-lessons">${course.title}</td>
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

</html>
