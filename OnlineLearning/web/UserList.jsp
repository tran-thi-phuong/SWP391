<%-- 
    Document   : UserList
    Created on : Nov 5, 2024, 10:23:55 PM
    Author     : Admin
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
         <link rel="stylesheet" href="css/userlist.css">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;600;700&display=swap" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.5/font/bootstrap-icons.min.css" rel="stylesheet">
        <link href="css/Header_Footer.css" rel="stylesheet">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
        <link rel="stylesheet" href="css/userlist.css">
    </head>
    <%@include file="Header.jsp" %>
    <body class="user-list-page">
        <div class="container mt-5">
            <h1 class="text-center page-title">Manage Users</h1>
            <br>

            <!-- Filter and Search Form -->
            <form action="UserList" method="get" class="mb-4 filter-form">
                <div class="row mb-3 filter-row">
                    <div class="col">
                        <input type="text" class="form-control" id="email" name="email"
                               placeholder="Search by Email" value="${param.email != null ? param.email : ''}"/>
                    </div>
                    <div class="col">
                        <input type="text" class="form-control" id="phone" name="phone"
                               placeholder="Search by Phone Number" value="${param.phone != null ? param.phone : ''}"/>
                    </div>
                    <div class="col">
                        <input type="text" class="form-control" id="name" name="name"
                               placeholder="Search by Name" value="${param.name != null ? param.name : ''}"/>
                    </div>
                    <div class="col">
                        <select name="gender" class="form-select">
                            <option value="">Select Gender</option>
                            <option value="Male">Male</option>
                            <option value="Female">Female</option>
                            <option value="Other">Other</option>
                        </select>
                    </div>
                    <div class="col">
                        <select name="status" class="form-select">
                            <option value="">Select Status</option>
                            <option value="Active">Active</option>
                            <option value="Inactive">Inactive</option>
                        </select>
                    </div>
                    <div class="col">
                        <select name="role" class="form-select">
                            <option value="">Select Role</option>
                            <option value="Admin">Admin</option>
                            <option value="Instructor">Instructor</option>
                            <option value="Marketing">Marketing</option>
                            <option value="Customer">Customer</option>
                        </select>
                    </div>
                    <div class="col">
                        <select name="sortColumn" class="form-select">
                            <option value="UserID">Sort by ID</option>
                            <option value="Name">Sort by Full Name</option>
                            <option value="Email">Sort by Email</option>
                            <option value="Phone">Sort by Phone Number</option>
                        </select>
                    </div>
                    <div class="col">
                        <select name="sortOrder" class="form-select">
                            <option value="ASC">Ascending</option>
                            <option value="DESC">Descending</option>
                        </select>
                    </div>

                    <div class="col-auto">
                        <button type="submit" class="btn btn-primary">Apply</button>
                    </div>
                </div>
            </form>

            <c:if test="${not empty users}">
                <table class="table table-striped user-table">
                    <thead>
                        <tr class="table-header">
                            <th>ID</th>
                            <th>Full Name</th>
                            <th>Gender</th>
                            <th>Email</th>
                            <th>Mobile Number</th>
                            <th>Role</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="user" items="${users}">
                            <tr class="user-row">
                                <td><a href="UserDetail?userID=${user.userID}" class="user-id-link">${user.userID}</a></td>
                                <td>${user.name}</td>
                                <td>${user.gender}</td>
                                <td>${user.email}</td>
                                <td>${user.phone}</td>
                                <td>${user.role}</td>
                                <td>${user.status}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:if>

            <c:if test="${empty users}">
                <p class="no-users-text">No users found.</p>
            </c:if>

            <!-- Pagination Controls -->
            <nav aria-label="Page navigation example" class="pagination-nav">
                <ul class="pagination">
                    <c:if test="${currentPage > 1}">
                        <li class="page-item">
                            <a class="page-link" href="UserList?page=${currentPage - 1}">Previous</a>
                        </li>
                    </c:if>
                    <c:forEach var="i" begin="1" end="${totalPages}">
                        <li class="page-item <c:if test='${i == currentPage}'>active</c:if>'">
                            <a class="page-link" href="UserList?page=${i}">${i}</a>
                        </li>
                    </c:forEach>
                    <c:if test="${currentPage < totalPages}">
                        <li class="page-item">
                            <a class="page-link" href="UserList?page=${currentPage + 1}">Next</a>
                        </li>
                    </c:if>
                </ul>
            </nav>
        </div>
        <%@include file="Footer.jsp" %>
    </body>
</html>
