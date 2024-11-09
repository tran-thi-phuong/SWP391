<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>User Details</title>
        <link rel="stylesheet" href="css/userdetail.css">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;600;700&display=swap" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.5/font/bootstrap-icons.min.css" rel="stylesheet">
        <link href="css/Header_Footer.css" rel="stylesheet">
        <link rel="stylesheet" href="css/userdetail.css">
    </head>
    <%@ include file="Header.jsp" %>
    <body class="user-detail-page">
        <div class="container mt-5">
            <h1 class="text-center page-title">User Details</h1>
            <br>
            <!-- Display messages -->
            <c:if test="${not empty sessionScope.successMessage}">
                <div class="alert alert-success" role="alert">
                    ${sessionScope.successMessage}
                </div>
                <c:remove var="successMessage"/>
            </c:if>

            <c:if test="${not empty sessionScope.errorMessage}">
                <div class="alert alert-danger" role="alert">
                    ${sessionScope.errorMessage}
                </div>
                <c:remove var="errorMessage"/>
            </c:if>

            <c:if test="${not empty user}">
                <div class="user-detail-card card shadow-sm">
                    <div class="card-body">
                        <form method="POST" action="UpdateUser" class="user-detail-form">
                            <input type="hidden" name="userId" value="${user.userID}" />
                            <div class="row mb-3">
                                <div class="col-md-3">
                                    <!-- Avatar -->
                                    <img src="${empty user.avatar ? 'images/default-avatar.jpg' : user.avatar}" alt="Avatar" class="img-fluid rounded-circle" style="width: 150px;">
                                </div>
                                <div class="col-md-9">
                                    <!-- Full Name, Gender, Email, Phone, Address, Role, Status in the same row -->
                                    <div class="row">
                                        <div class="col-md-4">
                                            <div class="mb-3 custom-class">
                                                <label for="fullName" class="form-label"><strong>Full Name</strong></label>
                                                <input type="text" id="fullName" class="form-control" value="${user.name}" readonly />
                                            </div>
                                        </div>
                                        <div class="col-md-4">
                                            <div class="mb-3 custom-class">
                                                <label for="email" class="form-label"><strong>Email</strong></label>
                                                <input type="email" id="email" class="form-control" value="${user.email}" readonly />
                                            </div>
                                        </div>
                                        <div class="col-md-4">
                                            <div class="mb-3 custom-class">
                                                <label for="gender" class="form-label"><strong>Gender</strong></label>
                                                <input type="text" id="gender" class="form-control" value="${user.gender}" readonly />
                                            </div>
                                        </div>
                                    </div>

                                    <div class="row">
                                        <div class="col-md-4">
                                            <div class="mb-3 custom-class">
                                                <label for="phone" class="form-label"><strong>Mobile Number</strong></label>
                                                <input type="text" id="phone" class="form-control" value="${user.phone}" readonly />
                                            </div>
                                        </div>
                                        <div class="col-md-4">
                                            <div class="mb-3 custom-class">
                                                <label for="address" class="form-label"><strong>Address</strong></label>
                                                <input type="text" id="address" class="form-control" value="${user.address}" readonly />
                                            </div>
                                        </div>
                                    </div>

                                    <div class="row">
                                        <div class="col-md-4">
                                            <div class="mb-3 custom-class">
                                                <label for="role" class="form-label"><strong>Role</strong></label>
                                                <select id="role" name="role" class="form-select custom-select" disabled>
                                                    <option value="Admin" ${user.role == 'Admin' ? 'selected' : ''}>Admin</option>
                                                    <option value="Instructor" ${user.role == 'Instructor' ? 'selected' : ''}>Instructor</option>
                                                    <option value="Marketing" ${user.role == 'Marketing' ? 'selected' : ''}>Marketing</option>
                                                    <option value="Customer" ${user.role == 'Customer' ? 'selected' : ''}>Customer</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-md-4">
                                            <div class="mb-3 custom-class">
                                                <label for="status" class="form-label"><strong>Status</strong></label>
                                                <select id="status" name="status" class="form-select custom-select" disabled>
                                                    <option value="Active" ${user.status == 'Active' ? 'selected' : ''}>Active</option>
                                                    <option value="Inactive" ${user.status == 'Inactive' ? 'selected' : ''}>Inactive</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="mb-3 text-center">
                                        <button type="button" class="btn btn-warning" id="editButton" onclick="enableEditing()">Edit</button>
                                        <button type="submit" class="btn btn-success" id="saveButton" style="display:none;">Save</button>
                                       <button type="button" class="btn btn-danger" id="deleteButton" onclick="doDelete(${user.userID})">Delete</button>

                                    </div>
                                </div>
                            </div>
                        </form>

                    </div>
                </div>
            </c:if>

            <c:if test="${empty user}">
                <div class="alert alert-danger" role="alert">
                    User not found.
                </div>
            </c:if>

            <br>
            <a href="UserList" class="btn btn-secondary">Back to User List</a>
        </div>
        <%@include file="Footer.jsp" %>

        <script>
            // Function to enable editing for role and status
            function enableEditing() {
                document.getElementById("role").disabled = false;
                document.getElementById("status").disabled = false;
                document.getElementById("editButton").style.display = "none";
                document.getElementById("saveButton").style.display = "inline-block";
            }

            // Confirm delete action
            function doDelete(userId) {
    // Confirm the deletion action
    if (confirm("Are you sure you want to delete this user?")) {
        // Redirect to the delete URL with the userId if confirmed
        window.location.href = `DeleteUser?userId=${user.userID}`;
    }
}

        </script>
    </body>
</html>
