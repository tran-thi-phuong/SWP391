<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title> Customer </title>
          <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
        <link rel="stylesheet" href="change_reset_password.css">
    </head>
    <body>
        <ul>
            <li><a href="infor.jsp" class="link">${sessionScope.account.username}</a></li>
            <li><a href="logout" class="link">Logout</a></li>

        </ul>
        <!-- Trigger button for popup -->
        <button id="changePasswordBtn" class="button-c">Change Password</button>
        <!-- Popup Overlay -->
        <div id="popupOverlay" class="popup-overlay"></div>

        <!-- Change Password Pop-Up -->
        <div class="popup" id="changePasswordPopup">
            <div class="popup-content">
                <span class="close-btn" id="closePopup">&times;</span>
                <header>Change Password</header>

                <c:if test="${not empty sessionScope.errorMessage}">
                    <div class="error-message">${sessionScope.errorMessage}</div>
                    <c:remove var="errorMessage"/>
                </c:if>

                <c:if test="${not empty sessionScope.successMessage}">
                    <div class="success-message">${sessionScope.successMessage}</div>
                    <c:remove var="successMessage"/>
                </c:if>

                <form id="changePasswordForm" action="changePassword" method="post">

                    <div class="input-box-c">
                        <label for="oldPassword">Old Password:</label>
                        <input type="password" id="oldPassword" name="oldPassword" required>
                    </div>
                    <div class="input-box-c">
                        <label for="newPassword">New Password:</label>
                        <input type="password" id="newPassword" name="newPassword" required>
                    </div>
                    <div class="input-box-c">
                        <label for="confirmNewPassword">Confirm New Password:</label>
                        <input type="password" id="confirmNewPassword" name="confirmNewPassword" required>
                    </div>

                    <div class="input-box-c">
                        <input type="submit" class="submit" value="Change Password">
                    </div>
                </form>
            </div>
        </div>

        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
<script>
    $(document).ready(function () {
        
        $('#changePasswordBtn').click(function () {
            $('#popupOverlay').fadeIn();
            $('#changePasswordPopup').fadeIn();
        });

       
        function closePopup() {
            $('#popupOverlay').fadeOut();
            $('#changePasswordPopup').fadeOut();
            clearInputs();
        }

        function clearInputs() {
            $('#oldPassword').val('');
            $('#newPassword').val('');
            $('#confirmNewPassword').val('');
            $('.error-message').remove();
        }

        $('#closePopup').click(function () {
            closePopup();
        });

        $('#popupOverlay').click(function () {
            closePopup();
        });

        // Keep the popup open if there's an error
        function handleError() {
            if ($('.error-message').length > 0) {
                $('#popupOverlay').fadeIn();
                $('#changePasswordPopup').fadeIn();
            }
        }

        // Call this function on page load if there are error messages
        handleError();
    });
</script>



    </body>
</html>
