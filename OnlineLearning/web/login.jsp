<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <!-- BOXICONS -->
        <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
        <link rel="stylesheet" href="login.css">
    </head>
    <body>
        <div class="wrapper">
            <!----------------------------- Login Form ----------------------------------->
            <div class="form-box">
                <div class="login-container">
                    <div class="top">
                        <span>Don't have an account? <a href="register.jsp">Register</a></span>
                        <header>Login</header>
                    </div>

                    <% 
                        String error = (String)request.getAttribute("error");
                        if (error != null) {
                            out.println("<p style=\"color:red;\">" + error + "</p>");
                        }

                        String message = request.getParameter("message");
                        if (message != null) {
                            out.println("<p style=\"color:red;\">" + message + "</p>");
                        }
                    %>

                    <form id="loginForm" action="login" method="post">
                        <div class="input-box">
                            <input type="text" name="login" id="login" class="input-field" placeholder="Email or Username" required>
                            <i class="bx bx-user"></i>
                        </div>
                        <div class="input-box">
                            <input type="password" name="password" id="password" class="input-field" placeholder="Password" required>
                            <i class="bx bx-lock-alt"></i>
                        </div>
                        <div class="input-box">
                            <input type="submit" class="submit" value="Login">
                        </div>
                        <div class="two-col">
                            <div class="one">
                                <input type="checkbox" id="login-check" onchange="rememberMe()">
                                <label for="login-check"> Remember Me</label>
                            </div>
                            <div class="two">
                                <label><a href="findAccount.jsp">Forgot password?</a></label>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <script>
            // Function to handle Remember Me functionality
            function rememberMe() {
                var login = document.getElementById('login').value;
                var password = document.getElementById('password').value;
                var remember = document.getElementById('login-check').checked;

                if (remember) {
                    // Set cookies with expiration date (1 day in this case)
                    document.cookie = "login=" + login + ";expires=" + new Date(Date.now() + 86400e3).toUTCString();
                    document.cookie = "password=" + password + ";expires=" + new Date(Date.now() + 86400e3).toUTCString();
                } else {
                    // Remove cookies
                    document.cookie = "login=;expires=Thu, 01 Jan 1970 00:00:00 UTC";
                    document.cookie = "password=;expires=Thu, 01 Jan 1970 00:00:00 UTC";
                }
            }

            // Check if Remember Me was checked and restore values from cookies if available
            window.onload = function () {
                var login = getCookie("login");
                var password = getCookie("password");

                if (login !== "" && password !== "") {
                    document.getElementById('login').value = login;
                    document.getElementById('password').value = password;
                    document.getElementById('login-check').checked = true;
                }
            };

            // Function to retrieve cookie value by name
            function getCookie(name) {
                var cookieName = name + "=";
                var decodedCookie = decodeURIComponent(document.cookie);
                var cookieArray = decodedCookie.split(';');
                for (var i = 0; i < cookieArray.length; i++) {
                    var cookie = cookieArray[i].trim();
                    if (cookie.indexOf(cookieName) === 0) {
                        return cookie.substring(cookieName.length, cookie.length);
                    }
                }
                return "";
            }

            function myMenuFunction() {
                var i = document.getElementById("navMenu");
                if (i.className === "nav-menu") {
                    i.className += " responsive";
                } else {
                    i.className = "nav-menu";
                }
            }
        </script>
    </body>
</html>
