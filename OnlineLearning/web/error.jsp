<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error Page</title>
    <link rel="stylesheet" href="styles.css"> <!-- Optional: Link to your CSS file -->
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f2f2f2;
            color: #333;
            text-align: center;
            padding: 50px;
        }
        .error-container {
            background-color: white;
            border: 1px solid #ccc;
            border-radius: 5px;
            padding: 20px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            display: inline-block;
        }
        h1 {
            color: #d9534f; /* Bootstrap danger color */
        }
        a {
            color: #0275d8; /* Bootstrap primary color */
        }
    </style>
</head>
<body>
    <div class="error-container">
        <h1>Error</h1>
        <p><strong>Oops!</strong> Something went wrong.</p>
        <p>${errorMessage}</p>
        <p>Please try again later or return to the <a href="index.jsp">homepage</a>.</p>
    </div>
</body>
</html>
