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
              .media-container {
                margin-bottom: 15px;
            }
            .media-preview {
                display: flex;
                align-items: center;
                margin-top: 10px;
            }
            .media-preview img,
            .media-preview video {
                max-width: 100%;
                max-height: 100%;
                margin-right: 10px;
            }
                .media-container img,
            .media-container video {
                max-width: 100%;
                height: auto;
            }
            
        </style>
        <script>
            function addMedia() {
                const mediaList = document.getElementById("mediaList");
                const mediaHTML = `
                    <div class="media-container">
                        <input type="file" name="mediaFiles" accept="image/*,video/*" onchange="previewMedia(this)" required/>
                         <input type="text" name="mediaDescription" placeholder="Media Description" required/>
        <div class="media-preview"></div>        
        <button type="button" onclick="removeMedia(this)">Remove</button>
                    
                    </div>`;
                mediaList.insertAdjacentHTML('beforeend', mediaHTML);
            }

            function removeMedia(button) {
                const mediaContainer = button.parentElement;
                mediaContainer.remove();
            }

            function previewMedia(input) {
                const mediaPreview = input.nextElementSibling.nextElementSibling;
                mediaPreview.innerHTML = ''; // Clear previous preview
                const file = input.files[0];

                if (file) {
                    const reader = new FileReader();
                    reader.onload = function (e) {
                        const mediaElement = document.createElement(file.type.startsWith('image/') ? 'img' : 'video');
                        mediaElement.src = e.target.result;
                        mediaElement.controls = file.type.startsWith('video/');
                        mediaPreview.appendChild(mediaElement);
                    };
                    reader.readAsDataURL(file);
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
        <style>
             .media-container {
                margin-bottom: 15px;
            }
            .media-preview {
                display: flex;
                align-items: center;
                margin-top: 10px;
            }
            .media-preview img,
            .media-preview video {
                max-width: 100%;
                max-height: 100%;
                margin-right: 10px;
            }
                .media-container img,
            .media-container video {
                max-width: 100%;
                height: auto;
            
            }
        </style>
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
                 <h2>Current Media Files</h2>
                <c:forEach var="media" items="${mediaList}">
                    <div class="media-container">
                        <strong>Media:</strong>
                        <c:choose>
                            <c:when test="${media.mediaLink.endsWith('.jpg' || media.mediaLink.endsWith('.png'))}">
                                <img src="${media.mediaLink}" alt="Media Image" />
                            </c:when>
                            <c:when test="${media.mediaLink.endsWith('.mp4')}">
                                <video controls>
                                    <source src="${media.mediaLink}" type="video/mp4">
                                    Your browser does not support the video tag.
                                </video>
                            </c:when>
                            <c:otherwise>
                                <img src="${media.mediaLink}" alt="Media Image" />
                            </c:otherwise>
                        </c:choose>
                        <p><em>${media.description}</em></p>
                        <input type="text" value="${media.mediaId}" name="current-media"hidden/>
                        <button type="button" onclick="removeMedia(this)">Remove</button>
                    </div>
                    <hr>
                </c:forEach>
                    <h2>Media Files</h2>
                <div id="mediaList">
                    <div class="media-container">
                        <input type="file" name="mediaFiles" accept="image/*,video/*" onchange="previewMedia(this)" required/>
                        <input type="text" name="mediaDescription" placeholder="Media Description" required/>
                        <div class="media-preview"></div>
                        <button type="button" onclick="removeMedia(this)">Remove</button>
                    </div>
                </div>
                <button type="button" onclick="addMedia()">Add Media</button>

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
