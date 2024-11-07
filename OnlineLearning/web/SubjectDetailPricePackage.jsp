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
                <button onclick="location.href = '${pageContext.request.contextPath}/SubjectDetailDimension?id=${sessionScope.subjectID}'">Topic</button>
                <button onclick="location.href = '${pageContext.request.contextPath}/SubjectDetailPricePackage?id=${sessionScope.subjectID}'" class="active">Price Package</button>
            </div>

            <div class="tab-content">
                <h3 class="mb-4">Course Price Packages</h3>
                <div class="table-responsive">
                    <table class="table table-striped">
                        <thead>
                            <tr>
                                <th>#</th>
                                <th>Package</th>
                                <th>Duration (days)</th>
                                <th>List Price</th>
                                <th>Sale Price</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${pricePackages}" var="pkg" varStatus="status">
                                <tr id="package-${pkg.packageId}">
                                    <td>${status.index + 1}</td>
                                    <td>${pkg.name}</td>
                                    <td>${pkg.durationTime}</td>
                                    <td>${pkg.price}</td>
                                    <td>${pkg.salePrice}</td>
                                    <td>
                                        <button class="btn btn-sm btn-primary" onclick="editPackage(${pkg.packageId})">
                                            <i class="bi bi-pencil"></i> Edit
                                        </button>
                                        <button class="btn btn-sm btn-danger" onclick="deletePackage(${pkg.packageId})">
                                            <i class="bi bi-trash"></i> Delete
                                        </button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                <button class="btn btn-success add-new-btn" onclick="showAddPackageModal()">
                    <i class="bi bi-plus-circle"></i> Add New Package
                </button>

                <!-- Modal for Adding New Package -->
                <div class="modal fade" id="addPackageModal" tabindex="-1" role="dialog" aria-labelledby="addPackageModalLabel" aria-hidden="true">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="addPackageModalLabel">Add New Package</h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                <form id="addPackageForm">
                                    <div class="form-group">
                                        <label for="packageName">Package Name</label>
                                        <input type="text" class="form-control" id="packageName" required>
                                    </div>
                                    <div class="form-group">
                                        <label for="durationTime">Duration (days)</label>
                                        <input type="number" class="form-control" id="durationTime" required>
                                    </div>
                                    <div class="form-group">
                                        <label for="price">List Price</label>
                                        <input type="number" class="form-control" id="price" required>
                                    </div>
                                </form>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                                <button type="button" class="btn btn-primary" onclick="addPackage()">Add Package</button>
                            </div>
                        </div>
                    </div>
                </div>


                <!-- Modal for Editing Package -->
                <div class="modal fade" id="editPackageModal" tabindex="-1" role="dialog" aria-labelledby="editPackageModalLabel" aria-hidden="true">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="editPackageModalLabel">Edit Package</h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                <form id="editPackageForm">
                                    <input type="hidden" id="editPackageId">
                                    <div class="form-group">
                                        <label for="editPackageName">Package Name</label>
                                        <input type="text" class="form-control" id="editPackageName" required>
                                    </div>
                                    <div class="form-group">
                                        <label for="editDurationTime">Duration (days)</label>
                                        <input type="number" class="form-control" id="editDurationTime" required>
                                    </div>
                                    <div class="form-group">
                                        <label for="editPrice">List Price</label>
                                        <input type="number" class="form-control" id="editPrice" required>
                                    </div>
                                </form>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                                <button type="button" class="btn btn-primary" onclick="updatePackage()">Update Package</button>
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
                                    function showAddPackageModal() {
                                        $('#addPackageModal').modal('show');
                                    }

                                    function editPackage(packageId) {
                                        const row = $('#package-' + packageId);
                                        const name = row.find('td:nth-child(2)').text();
                                        const durationTime = row.find('td:nth-child(3)').text();
                                        const price = row.find('td:nth-child(4)').text();

                                        $('#editPackageId').val(packageId);
                                        $('#editPackageName').val(name);
                                        $('#editDurationTime').val(durationTime);
                                        $('#editPrice').val(price);
                                        $('#editPackageModal').modal('show');
                                    }

                                    function addPackage() {
                                        const subjectId = '${sessionScope.subjectID}'; // Lấy subjectId từ session
                                        const name = $('#packageName').val();
                                        const durationTime = $('#durationTime').val();
                                        const price = $('#price').val();

                                        $.post('SubjectDetailPricePackage', {
                                            action: 'add',
                                            subjectId: subjectId,
                                            packageName: name,
                                            durationTime: durationTime,
                                            price: price
                                        }, function () {
                                            location.reload(); // Làm mới trang sau khi thêm
                                        }).fail(function () {
                                            alert('Error adding package');
                                        });
                                    }

                                    function updatePackage() {
                                        const id = $('#editPackageId').val();
                                        const subjectId = '${sessionScope.subjectID}'; // Lấy subjectId từ session
                                        const name = $('#editPackageName').val();
                                        const durationTime = $('#editDurationTime').val();
                                        const price = $('#editPrice').val();

                                        $.post('SubjectDetailPricePackage', {
                                            action: 'update',
                                            id: id,
                                            subjectId: subjectId,
                                            name: name,
                                            durationTime: durationTime,
                                            price: price
                                        }, function () {
                                            location.reload(); // Làm mới trang sau khi cập nhật
                                        }).fail(function () {
                                            alert('Error updating package');
                                        });
                                    }


                                    function deletePackage(packageId) {
                                        if (confirm('Are you sure you want to delete this package?')) {
                                            $.ajax({
                                                url: 'SubjectDetailPricePackage',
                                                type: 'POST',
                                                data: {action: 'delete', id: packageId},
                                                success: function () {
                                                    $('#package-' + packageId).remove(); // Xóa hàng khỏi bảng
                                                },
                                                error: function () {
                                                    alert('Error deleting package');
                                                }
                                            });
                                        }
                                    }
        </script>
    </body>
</html>
