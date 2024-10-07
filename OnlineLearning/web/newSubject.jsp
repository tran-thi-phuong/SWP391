<%-- 
    Document   : newSubject
    Created on : Oct 7, 2024, 4:50:51 PM
    Author     : Admin
--%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add New Course</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            padding: 20px;
            background-color: #f8f8f8;
        }
        h1 {
            text-align: center;
        }
        form {
            max-width: 600px;
            margin: auto;
            background: #fff;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }
        label {
            display: block;
            margin-bottom: 8px;
        }
        input[type="text"],
        input[type="file"],
        select,
        textarea {
            width: 100%;
            padding: 10px;
            margin-bottom: 15px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        input[type="checkbox"] {
            margin-right: 10px;
        }
        button {
            background-color: #4CAF50;
            color: white;
            padding: 10px 15px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            width: 100%;
        }
        button:hover {
            background-color: #45a049;
        }
    </style>
</head>
<body>

    <h1>Add New Course</h1>

    <form action="addCourse" method="POST" enctype="multipart/form-data">
        <label for="courseName">Course Name:</label>
        <input type="text" id="courseName" name="courseName" required>

        <label for="thumbnail">Thumbnail Image:</label>
        <input type="file" id="thumbnail" name="thumbnail" accept="image/*" required>

        <label for="category">Category:</label>
        <select id="category" name="category" required>
            <option value="">Select a category</option>
            <option value="1">Category 1</option>
            <option value="2">Category 2</option>
            <option value="3">Category 3</option>
            <!-- Add more categories as needed -->
        </select>

        <label for="status">Status:</label>
        <select id="status" name="status" required>
            <option value="">Select status</option>
            <option value="active">Active</option>
            <option value="inactive">Inactive</option>
            <!-- Add more statuses as needed -->
        </select>

        <label for="description">Description:</label>
        <textarea id="description" name="description" rows="4" required></textarea>

        <button type="submit">Add Course</button>
    </form>

</body>
</html>
