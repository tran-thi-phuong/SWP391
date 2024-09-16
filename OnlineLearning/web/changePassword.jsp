<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Change Password</title>
    <link rel="stylesheet" href="style.css">
    <link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
</head>
<body>
    <!-- Button to open the pop-up -->
    <button id="changePasswordBtn">Change Password</button>

    <!-- Pop-up overlay -->
    <div class="popup-overlay hidden" id="popupOverlay"></div>

    <!-- Change Password Pop-Up -->
    <div class="popup hidden" id="changePasswordPopup">
        <div class="popup-content">
            <span class="close-btn" id="closePopup">&times;</span>
            <header>Change Password</header>
            <c:if test="${not empty errorMessage}">
                <div class="error-message">${errorMessage}</div>
            </c:if>
            <c:if test="${not empty successMessage}">
                <div class="success-message ">${successMessage}</div>
            </c:if>
            <form id="changePasswordForm" action="changePassword" method="post">
                <div class="input-box">
                    <label for="oldPassword">Old Password:</label>
                    <input type="password" id="oldPassword" name="oldPassword" required>
                </div>
                <div class="input-box">
                    <label for="newPassword">New Password:</label>
                    <input type="password" id="newPassword" name="newPassword" required>
                </div>
                <div class="input-box">
                    <label for="confirmNewPassword">Confirm New Password:</label>
                    <input type="password" id="confirmNewPassword" name="confirmNewPassword" required>
                </div>
                <div class="input-box">
                    <input type="submit" class="submit" value="Change Password">
                </div>
            </form>
        </div>
    </div>

    <script>
        $(document).ready(function() {
            $('#changePasswordBtn').click(function() {
                $('#popupOverlay').removeClass('hidden');
                $('#changePasswordPopup').removeClass('hidden');
            });

            $('#closePopup, #popupOverlay').click(function() {
                $('#popupOverlay').addClass('hidden');
                $('#changePasswordPopup').addClass('hidden');
            });
        });
    </script>
</body>
</html>
