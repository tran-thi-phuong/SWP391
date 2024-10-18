<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;600;700&display=swap" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.5/font/bootstrap-icons.min.css" rel="stylesheet">
        <link href="css/Header_Footer.css" rel="stylesheet">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
        <link rel="stylesheet" href="css/addCampaign.css">
        <title>Add Campaign</title>
    </head>
    <%@ include file="Header.jsp" %>
    <body>
        <div class="container2 ">
            <h2>Add New Campaign</h2>

            <!-- Display success or error message -->
            <c:if test="${not empty error}">
                <div class="alert alert-danger" role="alert">
                    ${error}
                </div>
            </c:if>
            <c:if test="${not empty success}">
                <div class="alert alert-success" role="alert">
                    ${success}
                </div>
            </c:if>

            <form action="AddCampaign" method="post" enctype="multipart/form-data">
                <div class="form-group">
                    <label for="campaignName">Campaign Name:</label>
                    <input type="text" name="campaignName" required class="form-control" />
                </div>
                <div class="form-group">
                    <label for="description">Description:</label>
                    <textarea name="description" required class="form-control"></textarea>
                </div>
                <div class="form-group">
                    <label for="startDate">Start Date:</label>
                    <input type="date" name="startDate" required class="form-control" />
                </div>
                <div class="form-group">
                    <label for="endDate">End Date:</label>
                    <input type="date" name="endDate" required class="form-control" />
                </div>
                <div class="form-group">
                    <label for="status">Status:</label>
                    <input type="text" name="status" required class="form-control" />
                </div>
                <div class="form-group">
                    <label for="image">Upload Image:</label>
                    <input type="file" name="image" class="form-control" />
                </div>

                <!-- Button Row -->
                <div class="d-flex justify-content-between mt-3">
                    <button type="submit" class="btn btn-primary">Add Campaign</button>
                    <form action="campaignList" method="get" style="margin: 0;">
                        <button type="submit" class="btn btn-secondary">Cancel</button>
                    </form>
                </div>
            </form>

        </div>
    </body>
    <%@ include file="Footer.jsp" %>
</html>
