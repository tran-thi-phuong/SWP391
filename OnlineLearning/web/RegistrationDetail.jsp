<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <title>Registration Detail</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;600;700&display=swap" rel="stylesheet">  
        <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.5/font/bootstrap-icons.min.css" rel="stylesheet">
        <link href="css/Header_Footer.css" rel="stylesheet">
        <link rel="stylesheet" href="css/registration_detail.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-KyZXEAg3QhqLMpG8r+Knujsl7/4hb7ue/h4j5NYU/5X3QU5XhzX4JK2xyTg9Brqx" crossorigin="anonymous"></script>
    </head>
    <body>

        <%@ include file="Header.jsp" %>
        <div class="container mt-5">
            <h1 class="text-center">Registration Detail</h1>

            <!-- Error message display -->
            <c:if test="${not empty error}">
                <div class="alert alert-danger" role="alert">
                    ${error}
                </div>
            </c:if>
            <% 
                String errorMessage = (String) request.getAttribute("errorMessage");
                if (errorMessage != null) { 
            %>
            <div class="error" style="color: red">
                <%= errorMessage %>
            </div>
            <% 
                } 
            %>

            <!-- Registration details form -->
            <c:if test="${not empty registration}">
                <div class="card">
                    <div class="card-body">
                        <form action="updateRegistration" method="post">
                            <input type="hidden" name="registrationId" value="${registration.registrationId}">
                            <div class="mb-3">
                                <label for="registrationId" class="form-label"><strong>Registration ID:</strong></label>
                                <input type="text" class="form-control" id="registrationId" value="${registration.registrationId}" readonly>
                            </div>

                            <input type="hidden" name="customerEmail" value="${customerEmail}">
                            <div class="mb-3">
                                <label for="email" class="form-label"><strong>Email:</strong></label>
                                <a href="#" id="userEmail" class="form-control" style="cursor: pointer;" data-userid="${registration.userId}"
                                   data-bs-toggle="modal" data-bs-target="#userDetailModal">
                                    ${customerEmail}
                                </a>
                            </div>
                            <div class="mb-3">
                                <label for="registrationTime" class="form-label"><strong>Registration Time:</strong></label>
                                <input type="text" class="form-control" id="registrationTime" value="${registration.registrationTime}" readonly>
                            </div>
                            <div class="mb-3">
                                <label for="subject" class="form-label"><strong>Subject:</strong></label>
                                <input type="text" class="form-control" id="subject" value="${subjectTitle}" readonly>
                            </div>
                            <div class="mb-3">
                                <label for="campaign" class="form-label"><strong>Campaign:</strong></label>
                                <input type="text" class="form-control" id="campaign" value="${registration.campaignName}" readonly>
                            </div>
                            <div class="mb-3">
                                <label for="package" class="form-label"><strong>Package:</strong></label>
                                <input type="text" class="form-control" id="package" value="${packageName}" readonly>
                            </div>
                            <div class="mb-3">
                                <label for="totalCost" class="form-label"><strong>Total Cost:</strong></label>
                                <input type="text" class="form-control" id="totalCost" value="${registration.totalCost}" readonly>
                            </div>
                            <div class="mb-3">
                                <label for="validFrom" class="form-label"><strong>Valid From:</strong></label>
                                <input type="text" class="form-control" id="validFrom" value="${registration.validFrom}" readonly>
                            </div>
                            <div class="mb-3">
                                <label for="validTo" class="form-label"><strong>Valid To:</strong></label>
                                <input type="text" class="form-control" id="validTo" value="${registration.validTo}" readonly>
                            </div>
                            <div class="mb-3">
                                <label for="lastUpdateBy" class="form-label"><strong>Last Update By:</strong></label>
                                <input type="text" class="form-control" id="lastUpdateBy" value="${saleUsername}" readonly>
                            </div>
                            <div class="mb-3">
                                <label for="status" class="form-label"><strong>Status:</strong></label>
                                <select class="form-control" id="status" name="status" required ${registration.status == 'Inactive' ? 'disabled' : ''}>
                                    <c:if test="${registration.status == 'Processing'}">
                                        <option value="Processing" selected>Processing</option>
                                        <option value="Active">Active</option>
                                    </c:if>
                                    <c:if test="${registration.status == 'Active'}">
                                        <option value="Active" selected>Active</option>
                                        <option value="Inactive">Inactive</option>
                                    </c:if>
                                    <c:if test="${registration.status == 'Inactive'}">
                                        <option value="Inactive" selected>Inactive</option>
                                    </c:if>
                                         <c:if test="${registration.status == 'Cancelled'}">
                                        <option value="Cancelled" selected>Cancelled</option>
                                    </c:if>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label for="note" class="form-label"><strong>Note:</strong></label>
                                <textarea class="form-control" id="note" name="note" rows="3">${registration.note}</textarea>
                            </div>

                            <button type="submit" class="btn btn-success">Save Changes</button>
                        </form>
                    </div>
                </div>
            </c:if>

            <div class="mt-4">
                <a href="listRegistration" class="btn btn-primary">Back to Registration List</a>
            </div>
        </div>

        <div class="modal fade" id="userDetailModal" tabindex="-1" aria-labelledby="userDetailModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="userDetailModalLabel">User Details</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <p><strong>Full Name:</strong> <span id="modalUserName">${userDetails.name}</span></p>
                        <p><strong>Phone:</strong> <span id="modalUserPhone">${userDetails.phone}</span></p>
                        <p><strong>Email:</strong> <span id="modalUserEmail">${userDetails.email}</span></p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>

        <script>
            $(document).ready(function () {
                $('#userEmail').on('click', function () {
                    const userId = $(this).data('userid');
                    // Sử dụng AJAX để lấy thông tin người dùng từ server
                    $.ajax({
                        url: 'getUserDetails', // URL của servlet để lấy thông tin người dùng
                        method: 'GET',
                        data: { userId: userId },
                        success: function(data) {
                            $('#modalUserName').text(data.name);
                            $('#modalUserPhone').text(data.phone);
                            $('#modalUserEmail').text(data.email);
                        }
                    });
                });
            });
        </script>

        <%@ include file="Footer.jsp" %>
    </body>
</html>
