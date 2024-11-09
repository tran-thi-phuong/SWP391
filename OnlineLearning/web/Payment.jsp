<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Payment QR Code</title>

        <!-- CSS styles -->
        <style>
            body {
                font-family: Arial, sans-serif;
                background-color: #FFFDD0;
                color: #333;
                margin: 0;
                padding: 0;
            }

            h1, h4 {
                text-align: center;
                color: #5a5a5a;
            }

            .container {
                width: 100%;
                max-width: 800px;
                margin: 0 auto;
                padding: 20px;
                background-color: #fff;
                border-radius: 8px;
                box-shadow: 0 0 15px rgba(0, 0, 0, 0.1);

            }

            .alert {
                padding: 15px;
                margin-bottom: 20px;
                border-radius: 5px;
                font-size: 16px;
                text-align: center;
                border: 2px solid #4caf50; /* Add a border with a color that matches the notification */
            }

            .alert-success {
                color: #4caf50;
                background-color: #e8f5e9; /* Light background for contrast */
            }



            img {
                display: block;
                margin: 20px auto;
                width: 450px; /* Set the width to a smaller value */
                height: auto; /* Maintain aspect ratio */
            }

            .payment-details {
                text-align: center;
                margin-top: 30px;
                font-size: 18px;
                color: #333;
            }

            .payment-details strong {
                font-weight: bold;
            }
            .back-button {
                display: inline-block;
                padding: 10px 20px;
                background-color: #FA8072;
                color: white;
                text-align: center;
                text-decoration: none;
                font-size: 16px;
                border-radius: 5px;
                box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
                transition: background-color 0.3s ease;
            }

            .back-button:hover {
                background-color: #E0B0FF;
            }

        </style>

    </head>
    <body>
        <div class="container">
            <!-- Display notifications if available -->
            <c:if test="${not empty sessionScope.notification}">
                <div class="alert alert-success">
                    ${sessionScope.notification} <!-- Display the notification -->
                </div>
                <!-- Remove the notification after displaying it -->
                <c:remove var="notification" />
            </c:if>
            <a href="Homepage" class="back-button">Back to Homepage</a>

            <h4>Please scan the QR Code below to complete your registration.</h4>

            <!-- Display the QR Code image -->
            <img src="images/QR.jpg" alt="QR Code for Payment" />

            <!-- Display the payment amount -->
            <div class="payment-details">
                <c:if test="${not empty sessionScope.price}">
                    <p><strong>Total amount to pay: </strong>${sessionScope.price} USD!</p>
                </c:if>
            </div>
        </div>
    </body>
</html>
