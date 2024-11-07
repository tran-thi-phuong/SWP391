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
                <button onclick="location.href = '${pageContext.request.contextPath}/SubjectDetailOverview?id=${sessionScope.subjectID}'">Overview</button>
                <button onclick="location.href = '${pageContext.request.contextPath}/SubjectDetailDimension?id=${sessionScope.subjectID}'" class="active">Dimension</button>
                <button onclick="location.href = '${pageContext.request.contextPath}/SubjectDetailPricePackage?id=${sessionScope.subjectID}'">Price Package</button>
            </div>


            <div class="tab-content">
                <h3 class="mb-4">Course Topics</h3>
                <div class="table-responsive">
                    <table class="table table-striped">
                        <thead>
                            <tr>
                                <th>#</th>
                                <th>Topic</th>
                                <th>Order</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${dimensions}" var="dim" varStatus="status">
                                <tr id="dimension-${dim.topicID}">
                                    <td>${status.index + 1}</td>
                                    <td>${dim.topicName}</td>
                                    <td>${dim.order}</td>
                                    <td>
                                        <button class="btn btn-sm btn-primary" onclick="editDimension(${dim.topicID})">
                                            <i class="bi bi-pencil"></i> Edit
                                        </button>
                                        <button class="btn btn-sm btn-danger" onclick="deleteDimension(${dim.topicID})">
                                            <i class="bi bi-trash"></i> Delete
                                        </button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                <button class="btn btn-success add-new-btn" onclick="showAddDimensionModal()">
                    <i class="bi bi-plus-circle"></i> Add New Topic
                </button>
                <!-- Modal for Adding New Dimension -->
                <div class="modal fade" id="addDimensionModal" tabindex="-1" role="dialog" aria-labelledby="addDimensionModalLabel" aria-hidden="true">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="addDimensionModalLabel">Add New Topic</h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                <form id="addDimensionForm">
                                    <div class="form-group">
                                        <label for="dimensionName">Topic Name</label>
                                        <input type="text" class="form-control" id="dimensionName" required>
                                    </div>
                                    <div class="form-group">
                                        <label for="dimensionOrder">Order</label>
                                        <input type="number" class="form-control" id="dimensionOrder" required>
                                    </div>
                                </form>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                                <button type="button" class="btn btn-primary" onclick="addDimension()">Add Topic</button>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Modal for Editing Dimension -->
                <div class="modal fade" id="editDimensionModal" tabindex="-1" role="dialog" aria-labelledby="editDimensionModalLabel" aria-hidden="true">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="editDimensionModalLabel">Edit Topic</h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                <form id="editDimensionForm">
                                    <input type="hidden" id="editDimensionId">
                                    <div class="form-group">
                                        <label for="editDimensionName">Topic Name</label>
                                        <input type="text" class="form-control" id="editDimensionName" required>
                                    </div>
                                    <div class="form-group">
                                        <label for="editDimensionOrder">Order</label>
                                        <input type="number" class="form-control" id="editDimensionOrder" required>
                                    </div>
                                </form>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                                <button type="button" class="btn btn-primary" onclick="updateDimension()">Update Topic</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <%@include file="Footer.jsp" %>

        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
        <script>
                                    function showAddDimensionModal() {
                                        $('#addDimensionModal').modal('show');
                                    }

                                    function addDimension() {
                                        const subjectId = '${sessionScope.subjectID}';
                                        const topicName = $('#dimensionName').val();
                                        const order = $('#dimensionOrder').val();

                                        $.post('SubjectDetailDimension', {
                                            action: 'add',
                                            subjectId: subjectId,
                                            dimensionName: topicName,
                                            order: order
                                        }, function () {
                                            location.reload();
                                        })
                                                .fail(function () {
                                                    alert('Error adding dimension');
                                                });
                                    }

                                    function editDimension(dimensionId) {
                                        const row = $('#dimension-' + dimensionId);
                                        const topicName = row.find('td:nth-child(2)').text();
                                        const order = row.find('td:nth-child(3)').text();

                                        $('#editDimensionId').val(dimensionId);
                                        $('#editDimensionName').val(topicName);
                                        $('#editDimensionOrder').val(order);
                                        $('#editDimensionModal').modal('show');
                                    }

                                    function updateDimension() {
                                        const id = $('#editDimensionId').val();
                                        const subjectId = '${sessionScope.subjectID}';
                                        const topicName = $('#editDimensionName').val();
                                        const order = $('#editDimensionOrder').val();

                                        $.post('SubjectDetailDimension', {
                                            action: 'update',
                                            id: id,
                                            subjectId: subjectId,
                                            dimensionName: topicName,
                                            order: order
                                        }, function () {
                                            location.reload();
                                        })
                                                .fail(function () {
                                                    alert('Error updating dimension');
                                                });
                                    }

                                    function deleteDimension(dimensionId) {
                                        if (confirm('Are you sure you want to delete this dimension?')) {
                                            const subjectId = '${sessionScope.subjectID}'; // Lấy subjectId từ session

                                            $.ajax({
                                                url: 'SubjectDetailDimension',
                                                type: 'POST',
                                                data: {
                                                    action: 'delete',
                                                    id: dimensionId,
                                                    subjectId: subjectId // Gửi subjectId cùng với dimensionId
                                                },
                                                success: function () {
                                                    $('#dimension-' + dimensionId).remove();
                                                },
                                                error: function () {
                                                    alert('Error deleting dimension');
                                                }
                                            });
                                        }
                                    }
        </script>
    </body>
</html>