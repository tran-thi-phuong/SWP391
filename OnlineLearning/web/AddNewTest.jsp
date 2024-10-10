<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="model.Test" %>
<html>
    <head>
        <title>Add New Quiz</title>
        <style>
            .form-container {
                max-width: 600px;
                margin: 0 auto;
                padding: 20px;
                border: 1px solid #ccc;
                border-radius: 10px;
                background-color: #f9f9f9;
            }
            .form-container h1 {
                text-align: center;
            }
            .form-container p {
                margin-bottom: 15px;
            }
            .form-container strong {
                display: inline-block;
                width: 150px;
            }
            .form-container input[type="text"],
            .form-container input[type="number"],
            .form-container select {
                width: calc(100% - 160px);
                padding: 5px;
                border: 1px solid #ccc;
                border-radius: 5px;
            }
            .form-container button {
                margin-right: 10px;
                padding: 10px 20px;
                border: none;
                border-radius: 5px;
                background-color: #007bff;
                color: white;
                cursor: pointer;
            }
            .form-container button:hover {
                background-color: #0056b3;
            }
        </style>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;600;700&display=swap" rel="stylesheet">  
        <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.5/font/bootstrap-icons.min.css" rel="stylesheet">
        <link href="css/Header_Footer.css" rel="stylesheet">
    </head>
    <%@ include file="Header.jsp" %>
    <body onload="initializeFileInput()">
        <c:set var="subjectDAO" value="<%=new dal.SubjectDAO()%>" />
        <div style="text-align: center; margin-bottom: 20px;">
            <button onclick="window.location.href = 'QuizList'" style="padding: 10px 20px; border: none; border-radius: 5px; background-color: #28a745; color: white; cursor: pointer;">
                Back to Quiz List
            </button>
        </div>
        <div class="form-container">
            <h1>Add New Quiz</h1>
            <form action="AddQuiz" method="post" enctype="multipart/form-data">
                <p>
                    <strong>Title:</strong>
                    <input type="text" name="title" required/>
                </p>
                <p>
                    <strong>Description:</strong>
                    <input type="text" name="description" required/>
                </p>
                <p>
                    <strong>Media Type:</strong>
                    <select id="mediaType" name="mediaType" onchange="toggleMediaFields()" required>
                        <option value="">Select Media Type</option>
                        <option value="image" ${currentTest.mediaType == 'image' ? 'selected' : ''}>Image</option>
                        <option value="video" ${currentTest.mediaType == 'video' ? 'selected' : ''}>Video</option>
                    </select>
                </p>
                <p>
                <div id="mediaURLField" class="form-group" style="display: ${currentTest.mediaURL != '' ? 'block' : 'none'};">
                    <strong for="mediaFile">Media URL:</strong>
                    <input type="file" id="mediaFile" name="mediaURL" accept="image/*,video/*" />
                    <small>(Upload an image or video)</small>
                </div>
                </p>
                <p>
                    <strong>Media Description:</strong>
                    <input type="text" name="mediaDescription"/>
                </p>
                <p>
                    <strong>Type:</strong>
                    <input type="text" name="type" required/> 
                </p>
                <p>
                    <strong>Duration:</strong>
                    <input type="number" name="duration" required/> minutes
                </p>
                <p>
                    <strong>Pass Condition:</strong>
                    <input type="text" name="passCondition" required/> %
                </p>
                <p>
                    <strong>Level:</strong>
                    <input type="text" name="level" required/>
                </p>
                <p>
                    <strong>Number of Questions:</strong>
                    <input type="number" name="quantity" value="0" readonly/>
                </p>
                <p>
                    <strong>Subject:</strong>
                    <select id="subjectId" name="subjectId" required>
                        <option value="">Select Subject</option>
                        <c:forEach var="subject" items="${subjectDAO.getAllSubjects()}">
                            <option value="${subject.subjectID}">${subject.title}</option>
                        </c:forEach>
                    </select>
                </p>
                <button type="submit">Add Quiz</button>
            </form>
        </div>
    </body>
    <script>
            function toggleMediaFields() {
                var mediaType = document.getElementById("mediaType").value;
                var mediaURLField = document.getElementById("mediaURLField");
                var mediaFileInput = document.getElementById("mediaFile");

                if (mediaType === "image") {
                    mediaURLField.style.display = "block";
                    mediaFileInput.accept = "image/*"; // Accept only images
                } else if (mediaType === "video") {
                    mediaURLField.style.display = "block";
                    mediaFileInput.accept = "video/*"; // Accept only videos
                } else {
                    mediaURLField.style.display = "none";
                    mediaFileInput.accept = ""; // No file accepted
                }
            }
            function initializeFileInput() {
                const mediaType = document.getElementById('mediaType').value;
                if (mediaType) {
                    setFileInputAccept(mediaType); // Set accept attribute based on current media type
                    document.getElementById('mediaURLField').style.display = 'block'; // Ensure the field is visible
                }
                else{
                    document.getElementById('mediaURLField').style.display = 'none';
                }
            }
            function setFileInputAccept(mediaType) {
                const fileInput = document.getElementById('mediaFile');
                if (mediaType === 'image') {
                    fileInput.accept = 'image/*'; // Accept image files
                } else if (mediaType === 'video') {
                    fileInput.accept = 'video/*'; // Accept video files
                }
            }
        </script>
    <%@ include file="Footer.jsp" %>
</html>
