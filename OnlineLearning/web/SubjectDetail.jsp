<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Subject Details</title>
    <style>
        .tab {
            display: none;
        }
        .tab.active {
            display: block;
        }
        .tab-buttons button {
            padding: 10px;
            cursor: pointer;
        }
        .tab-content {
            margin-top: 20px;
            border: 1px solid #ddd;
            padding: 20px;
            border-radius: 5px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
        }
        th, td {
            padding: 8px;
            text-align: left;
            border: 1px solid #ddd;
        }
        th {
            background-color: #f2f2f2;
        }
        .action-links a {
            margin-right: 10px;
            color: blue;
            text-decoration: underline;
            cursor: pointer;
        }
    </style>
    <script>
        function showTab(tabName) {
            let tabs = document.getElementsByClassName("tab");
            for (let tab of tabs) {
                tab.classList.remove("active");
            }
            document.getElementById(tabName).classList.add("active");
        }
    </script>
</head>
<body>

<h2>Subject Details</h2>

<div class="tab-buttons">
    <button onclick="showTab('overview')">Overview</button>
    <button onclick="showTab('dimension')">Dimension</button>
    <button onclick="showTab('pricePackage')">Price Package</button>
</div>

<div id="overview" class="tab active tab-content">
    <h3>Overview</h3>
    <form>
        <label>Subject Name:</label> <input type="text" name="subjectName" placeholder="Text box"><br><br>
        <label>Category:</label> 
        <select name="category">
            <option>Combo Box</option>
            <!-- Add more options here -->
        </select><br><br>
        <label>Featured Subject:</label> <input type="checkbox" name="featured"><br><br>
        <label>Status:</label> 
        <select name="status">
            <option>Status</option>
            <!-- Add more options here -->
        </select><br><br>
        <label>Description:</label><br>
        <textarea name="description" placeholder="Subject Description"></textarea><br><br>
        <button type="submit">Submit</button>
        <button type="button">Back</button>
    </form>
</div>

<div id="dimension" class="tab tab-content">
    <h3>Subject Details - Dimension</h3>
    <table>
        <tr>
            <th>#</th>
            <th>Type</th>
            <th>Dimension</th>
            <th>Action</th>
        </tr>
        <tr>
            <td>1</td><td>Domain</td><td>Business</td>
            <td class="action-links"><a href="#">Edit</a> <a href="#">Delete</a></td>
        </tr>
        <tr>
            <td>2</td><td>Domain</td><td>Process</td>
            <td class="action-links"><a href="#">Edit</a> <a href="#">Delete</a></td>
        </tr>
        <tr>
            <td>3</td><td>Group</td><td>Initiating</td>
            <td class="action-links"><a href="#">Edit</a> <a href="#">Delete</a></td>
        </tr>
        <!-- Add more rows as needed -->
    </table>
    <a href="#">Add New</a>
</div>

<div id="pricePackage" class="tab tab-content">
    <h3>Subject Details - Price Package</h3>
    <table>
        <tr>
            <th>#</th>
            <th>Package</th>
            <th>Duration</th>
            <th>List Price</th>
            <th>Sale Price</th>
            <th>Status</th>
            <th>Action</th>
        </tr>
        <tr>
            <td>1</td><td>Gói truy cập 3 tháng</td><td>3</td><td>3600</td><td>3200</td><td>Active</td>
            <td class="action-links"><a href="#">Edit</a> <a href="#">Deactivate</a></td>
        </tr>
        <tr>
            <td>2</td><td>Gói truy cập 6 tháng</td><td>6</td><td>5000</td><td>4500</td><td>Inactive</td>
            <td class="action-links"><a href="#">Edit</a> <a href="#">Activate</a></td>
        </tr>
        <tr>
            <td>3</td><td>Gói truy cập vô thời hạn</td><td>0</td><td>10000</td><td>9800</td><td>Active</td>
            <td class="action-links"><a href="#">Edit</a> <a href="#">Deactivate</a></td>
        </tr>
        <!-- Add more rows as needed -->
    </table>
    <a href="#">Add New</a>
</div>

</body>
</html>
