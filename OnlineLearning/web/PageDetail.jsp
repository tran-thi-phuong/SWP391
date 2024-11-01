<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Page Detail</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;600;700&display=swap" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.5/font/bootstrap-icons.min.css" rel="stylesheet">
        <link href="css/Header_Footer.css" rel="stylesheet">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
        <link rel="stylesheet" href="css/pageDetail.css">
        <script>
            function toggleEdit() {
                // Toggle the readonly attribute on the input fields
                var pageNameInput = document.getElementById("pageName");
                var pageUrlInput = document.getElementById("pageUrl");
                var statusSelect = document.getElementById("status");

                // Check if the inputs are read-only
                if (pageNameInput.readOnly) {
                    // Make fields editable
                    pageNameInput.readOnly = false;
                    pageUrlInput.readOnly = false;
                    statusSelect.disabled = false; // Enable status dropdown
                    document.getElementById("editButton").innerText = "Save Changes"; // Change button text
                } else {
                    // Save the changes (this will trigger form submission)
                    document.getElementById("editForm").submit();
                }
            }

            function confirmDelete() {
                return confirm("Are you sure you want to delete this page?");
            }
        </script>
    </head>
    <%@ include file="Header.jsp" %>
    <body class="page-detail">

        <div class="container mt-4">
            <h1>Page Detail</h1>

            <c:if test="${not empty message}">
                <div class="alert alert-danger" role="alert">
                    ${message}
                </div>
            </c:if>

            <c:if test="${not empty pageDetail}">
                <form action="pageDetail" method="post" id="editForm">
                    <input type="hidden" name="pageId" value="${pageDetail.pageId}" />
                    <div class="card mb-4">
                        <div class="card-header">
                            Page Information
                        </div>
                        <div class="card-body">
                            <div class="mb-3">
                                <label for="pageName" class="form-label">Page Name:</label>
                                <input type="text" class="form-control" id="pageName" name="pageName" value="${pageDetail.pageName}" required readOnly>
                            </div>
                            <div class="mb-3">
                                <label for="pageUrl" class="form-label">Page URL:</label>
                                <div class="input-group">
                                    <input type="text" class="form-control" id="pageUrl" name="pageUrl" value="${pageDetail.pageUrl}" required readOnly>
                                    <a id="urlLink" href="${pageDetail.pageUrl}" target="_blank" class="btn btn-outline-secondary" style="display:none;">Open</a>
                                </div>
                            </div>
                            <div class="mb-3">
                                <label for="status" class="form-label">Status:</label>
                                <select class="form-select" id="status" name="status" required disabled>
                                    <option value="Active" <c:if test="${pageDetail.status == 'Active'}">selected</c:if>>Active</option>
                                    <option value="Inactive" <c:if test="${pageDetail.status == 'Inactive'}">selected</c:if>>Inactive</option>
                                    </select>
                                </div>
                            </div>
                        </div>

                        <div class="btn3">
                            <button type="button" id="editButton" class="btn btn-warning" onclick="toggleEdit()">Edit</button>
                            <div class="btn btn-danger" onclick="window.location.href = 'deletePage?pageId=${requestScope.pageDetail.pageId}'">Delete</div>
                        <a href="RolePermission" class="btn btn-primary">Back to Role Permissions</a>
                    </div>


                </form>
                <!-- Delete button -->





            </c:if>
        </div>

        <%@ include file="Footer.jsp" %>
    </body>
</html>
