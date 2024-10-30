<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="model.Question" %>
<%@ page import="model.QuestionMedia" %>
<html>
    <head>
        <title>Edit Question</title>
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
            .form-container input[type="number"] {
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
        </style>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <c:set var="questionDAO" value="<%=new dal.QuestionDAO()%>" />
        <c:set var="answerDAO" value="<%=new dal.AnswerDAO()%>" />
        <c:set var="mediaDAO" value="<%=new dal.QuestionMediaDAO()%>" />
        <div class="form-container">
            <h1>Edit Question</h1>
            <form action="QuestionDetail" method="post" enctype="multipart/form-data">
                <input type="hidden" name="questionId" value="${currentQuestion.questionID}"/>

                <p>
                    <strong>Content:</strong>
                    <input type="text" name="content" value="${currentQuestion.content}" required/>
                </p>

                <p>
                    <strong>Status:</strong>
                    <input type="text" name="status" value="${currentQuestion.status}" required/>
                </p>

                <p>
                    <strong>Level:</strong>
                    <input type="text" name="level" value="${currentQuestion.level}" required/>
                </p>

                <p>
                    <strong>Lesson ID:</strong>
                    <input type="number" name="lessonID" value="${currentQuestion.lessonID}" required/>
                </p>
                <h2>Current Media Files</h2>
                <c:forEach var="media" items="${mediaDAO.getMediaByQuestionId(currentQuestion.questionID)}">
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
                                <span>No preview available for this media type.</span>
                            </c:otherwise>
                        </c:choose>
                        <p><em>${media.description}</em></p>
                        <input type="text" value="${media.mediaID}" name="current-media"hidden/>
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

                <div style="text-align: center; margin-top: 20px;">
                    <button type="submit" name="action" value="save">Save Changes</button>
                    <button type="button" onclick="window.location.href = 'QuestionDetail?id=${currentQuestion.questionID}'">Cancel</button>
                </div>
            </form>
        </div>

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
        </script>
    </body>
</html>
