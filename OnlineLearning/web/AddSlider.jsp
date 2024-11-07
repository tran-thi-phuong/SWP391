<%-- 
    Document   : AddSlider
    Created on : Nov 7, 2024, 9:34:18 PM
    Author     : sonna
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <head>
        <title>Add Slider</title>
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;600;700&display=swap" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.5/font/bootstrap-icons.min.css" rel="stylesheet">
        <link href="css/Header_Footer.css" rel="stylesheet"> 
    </head>
    <body>
        <%@include file="Header.jsp" %> <!-- Including the Header JSP -->

        <h1>Add Slider</h1>

        <!-- Form to add a new slider -->
        <form action="AddSlider" method="POST" enctype="multipart/form-data">
            <div class="form-group">
                <label for="title">Title</label>
                <input type="text" id="title" name="title" class="form-control" required />
            </div>
            <div class="form-group">
                <label for="image">Choose image:</label>
                <input type="file" id="image" name="image" required />
            </div>
            <div class="form-group">
                <label for="backlink">Backlink</label>
                <input type="text" id="backlink" name="backlink" class="form-control" required />
            </div>
            <div class="form-group">
                <label for="status">Status</label>
                <select id="status" name="status" class="form-control">
                    <option value="Show">Show</option>
                    <option value="Hide">Hide</option>
                </select>
            </div>
            <button type="submit" class="btn btn-primary mt-3">Add</button>
        </form>

        <%@include file="Footer.jsp" %> <!-- Including the Footer JSP -->
    </body>
</html>
