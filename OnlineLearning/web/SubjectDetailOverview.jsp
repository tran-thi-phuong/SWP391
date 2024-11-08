<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;600;700&display=swap" rel="stylesheet">  
        <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.5/font/bootstrap-icons.min.css" rel="stylesheet">
        <link href="css/Header_Footer.css" rel="stylesheet"> 
        <link rel="stylesheet" href="css/style.css">
        <title>Subject Details</title>
        <style>
            .tab {
                display: none;
            }
            .tab.active {
                display: block;
            }
            .tab-buttons {
                margin-bottom: 20px;
            }
            .tab-buttons button {
                padding: 10px 20px;
                margin-right: 10px;
                border: none;
                background-color: #f8f9fa;
                border-radius: 5px;
                cursor: pointer;
                transition: background-color 0.3s;
            }
            .tab-buttons button:hover {
                background-color: #e9ecef;
            }
            .tab-buttons button.active {
                background-color: #0d6efd;
                color: white;
            }
            .tab-content {
                margin-top: 20px;
                border: 1px solid #ddd;
                padding: 20px;
                border-radius: 5px;
                background-color: white;
            }
            .form-group {
                margin-bottom: 15px;
            }
            .error-message {
                color: red;
                font-size: 0.9em;
                margin-top: 5px;
            }
            .success-message {
                color: green;
                font-size: 0.9em;
                margin-top: 5px;
            }
            .action-buttons {
                margin-top: 20px;
            }
            .table-responsive {
                margin-top: 20px;
            }
            .add-new-btn {
                margin-top: 20px;
            }
        </style>
    </head>
    <body class="bg-light">
        <%@include file="Header.jsp" %>

        <div class="container mt-4">
            <h2 class="mb-4">Course Details</h2>

            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger">${errorMessage}</div>
            </c:if>
            <c:if test="${not empty successMessage}">
                <div class="alert alert-success">${successMessage}</div>
            </c:if>

            <div class="tab-buttons">
                <button onclick="location.href = '${pageContext.request.contextPath}/SubjectDetailOverview?id=${sessionScope.subjectID}'" class="active">Overview</button>
                <button onclick="location.href = '${pageContext.request.contextPath}/SubjectDetailDimension?id=${sessionScope.subjectID}'">Topic</button>
                <button onclick="location.href = '${pageContext.request.contextPath}/SubjectDetailPricePackage?id=${sessionScope.subjectID}'">Price Package</button>
            </div>


            <!-- Overview Tab -->
            <div id="overview" class="tab active tab-content">
                <h3 class="mb-4">Overview</h3>
                <form id="subjectForm" action="${pageContext.request.contextPath}/SubjectDetailOverview" method="POST" enctype="multipart/form-data">
                    <input type="hidden" name="subjectId" value="${subject.subjectID}">

                    <div class="form-group">
                        <label class="form-label">Course Thumbnail:</label>
                        <div>
                            <img src="${subject.thumbnail}" alt="Subject Thumbnail" 
                                 class="img-thumbnail" style="width: 150px; height: auto;">
                        </div>
                        <input type="file" name="thumbnail">
                    </div>
                    <div class="form-group">
                        <label for="subjectName" class="form-label">Course Name:</label>
                        <input type="text" class="form-control" id="subjectName" name="subjectName" 
                               value="${subject.title}" required>
                    </div>

                    <div class="form-group">
                        <label for="category" class="form-label">Category:</label>
                        <select class="form-control" id="category" name="category" required>
                            <option value="">Select Category</option>
                            <c:forEach items="${categories}" var="cat">
                                <option value="${cat.subjectCategoryId}" ${subject.subjectCategoryId == cat.subjectCategoryId ? 'selected' : ''}>
                                    ${cat.title}
                                </option>
                            </c:forEach>
                        </select>
                    </div>


                    <div class="form-group">
                        <label for="status" class="form-label">Status:</label>
                        <select class="form-control" id="status" name="status" required>
                            <option value="Active" ${subject.status == 'Active' ? 'selected' : ''}>Active</option>
                            <option value="Inactive" ${subject.status == 'Inactive' ? 'selected' : ''}>Inactive</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="description" class="form-label">Description:</label>
                        <textarea class="form-control" id="description" name="description" rows="4" required>${subject.description}</textarea>
                    </div>

                    <div class="action-buttons">
                        <button type="submit" class="btn btn-primary">Save Changes</button>
                        <button type="button" class="btn btn-secondary" onclick="window.history.back()">Back</button>
                    </div>
                </form>
            </div>
        </div>
            <%@include file="Footer.jsp" %>

