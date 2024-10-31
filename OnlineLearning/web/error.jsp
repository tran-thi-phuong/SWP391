<%-- 
    Document   : error
    Created on : Oct 5, 2024, 1:21:07 PM
    Author     : 84336
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Error</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                display: flex;
                flex-direction: column;
                align-items: center;
                justify-content: center;
                height: 100vh;
                margin: 0;
                background-color: #f2f2f2;
            }
            .error-message {
                color: red;
                font-size: 18px;
                margin-bottom: 20px;
            }
            .button {
                padding: 10px 20px;
                font-size: 16px;
                color: white;
                background-color: #007bff;
                border: none;
                border-radius: 5px;
                cursor: pointer;
            }
            .button:hover {
                background-color: #0056b3;
            }
        </style>
    </head>
    <body>

        <div class="error-message">
            <%
                    // Get the error message from the request parameter
                    String errorParam = request.getParameter("message");
                    // Get the error message from the request attribute
                    String errorMessage = (String) request.getAttribute("errorMessage");

                    // Display the error message from the parameter if available, otherwise fallback to the attribute
                    if (errorParam != null && !errorParam.isEmpty()) {
                        out.print(errorParam);
                    } else if (errorMessage != null) {
                        out.print(errorMessage);
                    } else {
                        out.print("An error occurred.");
                    }
            %>        
        </div>

        <form action="Homepage" method="get">
            <button type="submit" class="button">Go to Homepage</button>
        </form>

    </body>
</html>

