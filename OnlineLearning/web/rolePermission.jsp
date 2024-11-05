<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
    <head>
        <title>Manage Authorization</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;600;700&display=swap" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.5/font/bootstrap-icons.min.css" rel="stylesheet">
        <link href="css/Header_Footer.css" rel="stylesheet">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
        <link rel="stylesheet" href="css/rolePermission.css">
    </head>
    <%@ include file="Header.jsp" %>
    <body class="role">
        <br>

        <c:if test="${not empty message}">
            <p class="message">${message}</p>
        </c:if>

        <form class="add-role-form" action="RolePermission" method="post">
            <input type="hidden" name="action" value="add">

            <div class="input-groups">
                <label class="form-label">Page: </label>
                <input type="text" class="form-control" name="page" required>
            </div>

            <div class="input-groups">
                <label class="form-label">PageURL: </label>
                <input type="text" class="form-control" name="pageURL" required>
            </div>

            <div class="input-groups">
                <label class="form-label">Status: </label>
                <select class="form-select" name="status">
                    <option value="Active">Active</option>
                    <option value="Inactive">Inactive</option>
                </select>
            </div>

            <button type="submit" class="btn btn-primary">Add</button>
        </form>

        <hr>

        <h1>List Page</h1>

        <form class="update-role-form" action="RolePermission" method="post">
            <input type="hidden" name="action" value="updateMultiple">
            <button type="submit" class="btn btn-2 btn-success">Save Changes</button>
            <table class="table table-bordered">
                <thead>
                    <tr> 
                        <th>ID</th>
                        <th>Page Name</th>
                        <th>Customer</th>
                        <th>Admin</th>
                        <th>Marketing</th>
                        <th>Instructor</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="page" items="${allPages}">
                        <tr>
                            <td><a href="pageDetail?pageId=${page.pageId}">${page.pageId}</a></td>
                            <td>${page.pageName}</td>

                            <!-- Customer checkbox -->
                            <td>
                                <input type="checkbox" name="roles[${page.pageId}]" value="Customer"
                                       <c:if test="${rolePermissionDAO.hasPermission('Customer', page.pageId)}">checked</c:if> />
                            </td>

                            <!-- Admin checkbox -->
                            <td>
                                <input type="checkbox" name="roles[${page.pageId}]" value="Admin"
                                       <c:if test="${rolePermissionDAO.hasPermission('Admin', page.pageId)}">checked</c:if> />
                            </td>

                            <!-- Marketing checkbox -->
                            <td>
                                <input type="checkbox" name="roles[${page.pageId}]" value="Marketing"
                                       <c:if test="${rolePermissionDAO.hasPermission('Marketing', page.pageId)}">checked</c:if> />
                            </td>

                            <!-- Instructor checkbox -->
                            <td>
                                <input type="checkbox" name="roles[${page.pageId}]" value="Instructor"
                                       <c:if test="${rolePermissionDAO.hasPermission('Instructor', page.pageId)}">checked</c:if> />
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </form>
    </body>
    <%@ include file="Footer.jsp" %>
</html>
