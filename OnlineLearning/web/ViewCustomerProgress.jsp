<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Customer Progress</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;600;700&display=swap" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.5/font/bootstrap-icons.min.css" rel="stylesheet">
    <link href="css/Header_Footer.css" rel="stylesheet">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
    
    <style>
        body {
            font-family: 'Montserrat', sans-serif;
            background-color: #AFEEEE;
            color: #333;
        }

        h1 {
            font-weight: 600;
            color: #0056b3;
            text-align: center;
            margin-top: 20px;
            margin-bottom: 30px;
        }

        table {
            width: 80%;
            margin: 0 auto;
            border-collapse: collapse;
             background-color: #FFC0CB; 
        }

        table th, table td {
            padding: 12px 15px;
            text-align: center;
            border: 1px solid #800000;
        }

        table th {
            background-color: #0056b3;
            color: white;
        }

        table tr:nth-child(even) {
            background-color: #f2f2f2;
        }

        table tr:hover {
            background-color: #d1e7fd;
        }

        .message {
            text-align: center;
            font-size: 1.2em;
            color: #e74c3c;
            margin-top: 20px;
        }

        .container2 {
            padding: 40px;
            margin-top: 50px;
        }

        .card {
            border-radius: 10px;
        }

        .sort-btn {
            cursor: pointer;
            background-color: transparent;
            border: none;
            color: #0056b3;
            font-size: 16px;
            padding: 5px;
        }

        .sort-btn:hover {
            text-decoration: underline;
        }
    </style>

    <script>
        // Function to sort the table by column
        function sortTable(n) {
            var table = document.getElementById("customerProgressTable");
            var rows = table.rows;
            var switching = true;
            var dir = "asc"; // Set the sorting direction to ascending
            var switchCount = 0;

            while (switching) {
                switching = false;
                var shouldSwitch = false;
                for (var i = 1; i < (rows.length - 1); i++) {
                    var x = rows[i].getElementsByTagName("TD")[n];
                    var y = rows[i + 1].getElementsByTagName("TD")[n];

                    if (dir === "asc" && x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase()) {
                        shouldSwitch = true;
                        break;
                    } else if (dir === "desc" && x.innerHTML.toLowerCase() < y.innerHTML.toLowerCase()) {
                        shouldSwitch = true;
                        break;
                    }
                }
                if (shouldSwitch) {
                    rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
                    switching = true;
                    switchCount++;
                } else {
                    if (switchCount === 0 && dir === "asc") {
                        dir = "desc";
                        switching = true;
                    }
                }
            }
        }
    </script>

</head>

<%@ include file="Header.jsp" %>

<body>
    <div class="container2">
        <div class="card p-4 shadow-sm">
            <h1>Customer Progress for Course  " ${subjectTitle} "</h1>
           

            <!-- Only display the table if the customerProgressList is not empty -->
            <c:if test="${not empty customerProgressList}">
                <h4 style="color: #FF6347"> This course has ${customer} students have been studying.</h4>
                 <br>
                <table id="customerProgressTable" class="table table-bordered table-striped">
                    <thead>
                        <tr>
                            <th>
                                Username
                                <button class="sort-btn" onclick="sortTable(0)">
                                    <i class="bi bi-sort-alpha-down"></i>
                                </button>
                            </th>
                            <th>
                                Email
                                <button class="sort-btn" onclick="sortTable(1)">
                                    <i class="bi bi-sort-alpha-down"></i>
                                </button>
                            </th>
                            <th>
                                Phone
                                <button class="sort-btn" onclick="sortTable(2)">
                                    <i class="bi bi-sort-alpha-down"></i>
                                </button>
                            </th>
                            <th>
                                Progress (%)
                                <button class="sort-btn" onclick="sortTable(3)">
                                    <i class="bi bi-sort-numeric-down"></i>
                                </button>
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${customerProgressList}" var="customerCourse">
                            <tr>
                                <td>${customerCourse.userName}</td>
                                <td>${customerCourse.email}</td>
                                <td>${customerCourse.phone}</td>
                                <td>${customerCourse.progress}%</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:if>

            <!-- Optional: Display a message if no customers are found -->
            <c:if test="${empty customerProgressList}">
                <p class="message">No customer progress data available for this course.</p>
            </c:if>
        </div>
    </div>

</body>

<%@ include file="Footer.jsp" %>

</html>
