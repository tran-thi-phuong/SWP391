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
        <link rel="stylesheet" href="css/create_registration.css">
        <title>Create Registration</title>
    </head>
    <%@include file="Header.jsp" %>
    <body class="create">
                 <c:choose>
            <c:when test="${empty sessionScope.user || (sessionScope.user.role != 'Sale')}">
                <c:redirect url="login.jsp"/>
            </c:when>
            <c:otherwise>
        <div class="container2">
            <h2>Create New Registration</h2>
            <form action="addRegistrationBySale" method="post">
                <div class="form-group">
                    <label for="subjectID">Subject:</label>
                    <select class="col-sm-10" name="subjectID" id="subjectID" required>
                        <option value="">Select a Subject</option>
                        <c:forEach items="${subject}" var="sub">
                            <option value="${sub.value.subjectID}" ${sub.value.subjectID == selectedSubjectID ? 'selected' : ''}>
                                ${sub.value.title}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label for="packageID">Package: </label>
                    <select class="col-sm-10" name="packageID" id="packageID" required>
                        <option value="">Select a package</option>
                        <c:forEach items="${price}" var="sub">
                            <option value="${sub.packageId}" ${sub.packageId == param.packageID ? 'selected' : ''}>
                                ${sub.name}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label for="totalCost">Total Cost:</label>
                    <input type="number" step="0.01" id="totalCost" name="totalCost" value="${totalCost}" required readonly>
                </div>
                <div class="form-group">
                    <label for="email">Email:</label>
                    <input type="email" id="email" name="email" required placeholder="Enter your email" 
                           pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$" 
                           title="Please enter a valid email address (e.g., example@example.com)">
                    <small id="emailError" style="color: red; display: none;"></small> <!-- Error message placeholder -->
                </div>
                <div class="form-group">
                    <label for="note">Note:</label>
                    <textarea id="note" name="note" rows="4"></textarea>
                </div>
                <div class="form-group button-group">
                    <button type="submit">Save</button>
                    <button type="button" class="cancel-button" onclick="window.location.href = 'listRegistration';">Cancel</button>
                </div>
            </form>
        </div>
            </c:otherwise>
                 </c:choose>
        <script>
            const emailInput = document.getElementById('email');
            const emailError = document.getElementById('emailError');

            // Function to validate email
            function validateEmail() {
                const emailPattern = /^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$/;
                if (!emailPattern.test(emailInput.value)) {
                    emailError.textContent = 'Please enter a valid email address.';
                    emailError.style.display = 'block'; 
                } else {
                    emailError.textContent = ''; 
                    emailError.style.display = 'none'; 
                }
            }

            // Event listener for input change
            emailInput.addEventListener('input', validateEmail);

            $(document).ready(function () {

                $('#subjectID').change(function () {
                    var subjectID = $(this).val();
                    if (subjectID) {
                        window.location.href = 'addRegistrationBySale?subjectID=' + subjectID;
                    }
                });

                $('#packageID').change(function () {
                    var subjectID = $('#subjectID').val();
                    var packageID = $(this).val();
                   
                    if (subjectID && packageID) {
                        window.location.href = 'addRegistrationBySale?subjectID=' + subjectID + '&packageID=' + packageID;
                    }   
                });
            });
        </script>
    </body>
    <%@include file="Footer.jsp" %>
</html>
