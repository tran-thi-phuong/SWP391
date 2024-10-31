<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="model.Test" %>
<%@ page import="model.Subject" %>
<%@ page import="java.util.List" %>
<%@ page import="dal.SubjectDAO" %>

<c:set var="subjectDAO" value="<%=new dal.SubjectDAO()%>" />

<html>
    <head>
        <title>Edit Quiz</title>
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
                margin-bottom: 20px;
            }

            .form-group {
                margin-bottom: 15px;
            }

            .form-group label {
                display: block;
                font-weight: bold;
                margin-bottom: 5px;
            }

            .form-group input[type="text"],
            .form-group input[type="number"],
            .form-group select,
            .form-group input[type="file"] {
                width: 100%;
                padding: 8px;
                box-sizing: border-box;
                border: 1px solid #ccc;
                border-radius: 5px;
            }

            .form-group small {
                display: block;
                margin-top: 5px;
                color: #666;
            }

            .form-group button {
                display: block;
                width: 100%;
                padding: 10px;
                background-color: #007bff;
                color: #fff;
                border: none;
                border-radius: 5px;
                cursor: pointer;
            }

            .form-group button:hover {
                background-color: #0056b3;
            }

            #mediaURLField {
                display: none;
            }
        </style>
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
            }
            function setFileInputAccept(mediaType) {
                const fileInput = document.getElementById('mediaFile');
                if (mediaType === 'image') {
                    fileInput.accept = 'image/*'; // Accept image files
                } else if (mediaType === 'video') {
                    fileInput.accept = 'video/*'; // Accept video files
                }
            }
            function validateForm() {
                const duration = document.getElementById("duration").value;
                const passCondition = document.getElementById("passCondition").value;

                if (duration <= 0) {
                    alert("Duration must be greater than 0 minutes.");
                    return false; // Prevent form submission
                }

                if (passCondition < 0 || passCondition > 100) {
                    alert("Pass Condition must be between 0 and 100.");
                    return false; // Prevent form submission
                }

                return true; // Allow form submission
            }
        </script>
        <meta charset="UTF-8">
        <title>Test List</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;600;700&display=swap" rel="stylesheet">  
        <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.5/font/bootstrap-icons.min.css" rel="stylesheet">
        <link href="css/Header_Footer.css" rel="stylesheet">
    </head>
    <%@include file="Header.jsp" %>
    <body onload="initializeFileInput()">
        <div style="text-align: center; margin-bottom: 20px;">
            <a href="QuizDetail?id=${currentTest.testID}" style="text-decoration: none;">
                <button type="button" style="padding: 10px 20px; background-color: #6c757d; color: #fff; border: none; border-radius: 5px; cursor: pointer;">
                    Back to Quiz Details
                </button>
            </a>
        </div>
                
        <div class="form-container">
            <h1>Edit Quiz</h1>
            <form action="EditQuiz" method="post" enctype="multipart/form-data" onsubmit="return validateForm()">
                <input type="hidden" name="testId" value="${currentTest.testID}"/>

                <div class="form-group">
                    <label for="title">Title:</label>
                    <input type="text" id="title" name="title" value="${currentTest.title}" required/>
                </div>

                <div class="form-group">
                    <label for="description">Description:</label>
                    <input type="text" id="description" name="description" value="${currentTest.description}" required/>
                </div>

                <div class="form-group">
                    <label for="mediaType">Media Type:</label>
                    <select id="mediaType" name="mediaType" onchange="toggleMediaFields()" required>
                        <option value="">Select Media Type</option>
                        <option value="image" ${currentTest.mediaType == 'image' ? 'selected' : ''}>Image</option>
                        <option value="video" ${currentTest.mediaType == 'video' ? 'selected' : ''}>Video</option>
                    </select>
                </div>

                <div id="mediaURLField" class="form-group" style="display: ${currentTest.mediaURL != '' ? 'block' : 'none'};">
                    <label for="mediaFile">Media URL:</label>
                    <input type="file" id="mediaFile" name="mediaURL" accept="image/*,video/*" />
                    <small>(Upload an image or video)</small>
                </div>
                <div id="currentMediaDisplay" style="display: ${currentTest.mediaURL != '' ? 'block' : 'none'};">
                    <h4>Current Media:</h4>
                    <img id="currentImage" src="${currentTest.mediaType == 'image' ? currentTest.mediaURL : ''}" alt="Current Image" style="max-width: 200px; display: ${currentTest.mediaType == 'image' ? 'block' : 'none'};" />
                    <video id="currentVideo" src="${currentTest.mediaType == 'video' ? currentTest.mediaURL : ''}" controls style="max-width: 200px; display: ${currentTest.mediaType == 'video' ? 'block' : 'none'};"></video>
                </div>
                <div class="form-group">
                    <label for="mediaDescription">Media Description:</label>
                    <input type="text" id="mediaDescription" name="mediaDescription" value="${currentTest.mediaDescription}" />
                </div>

                <div class="form-group">
                    <label for="type">Type:</label>
                    <select id="type" name="type" required>
                        <option value="">Select Type</option>
                        <option value="Test" ${currentTest.type == 'Test' ? 'selected' : ''}>Test</option>
                        <option value="Quiz" ${currentTest.type == 'Quiz' ? 'selected' : ''}>Quiz</option>
                    </select>
                </div>


                <div class="form-group">
                    <label for="duration">Duration:</label>
                    <input type="number" id="duration" name="duration" value="${currentTest.duration}" required/> minutes
                </div>

                <div class="form-group">
                    <label for="passCondition">Pass Condition:</label>
                    <input type="text" id="passCondition" name="passCondition" value="${currentTest.passCondition}" required/> %
                </div>

                <div class="form-group">
                    <label for="level">Level:</label>
                    <select id="level" name="level" required>
                        <option value="">Select Level</option>
                        <option value="Beginner" ${currentTest.level == 'Beginner' ? 'selected' : ''}>Beginner</option>
                        <option value="Advanced" ${currentTest.level == 'Advanced' ? 'selected' : ''}>Advanced</option>
                        <option value="Expert" ${currentTest.level == 'Expert' ? 'selected' : ''}>Expert</option>
                    </select>
                </div>


                <div class="form-group">
                    <label for="quantity">Number of Questions:</label>
                    <input type="number" id="quantity" name="quantity" value="${currentTest.quantity}" readonly/>
                </div>

                <div class="form-group">
                    <label for="subjectId">Subject:</label>
                    <select id="subjectId" name="subjectId" required>
                        <option value="">Select Subject</option>
                        <c:forEach var="subject" items="${subjectDAO.getAllSubjects()}">
                            <option value="${subject.subjectID}" 
                                    ${currentTest.subjectID == subject.subjectID ? 'selected' : ''}>
                                ${subject.title}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <button type="submit" name="action" value="edit">Save Changes</button>
                    <button type="submit" name="action" value="updateQuestions">Update Questions</button>
                </div>
            </form>

        </div>
    </body>
    <%@include file="Footer.jsp" %>
</html>
