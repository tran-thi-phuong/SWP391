<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;600;700&display=swap" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.5/font/bootstrap-icons.min.css" rel="stylesheet">
        <link href="css/Header_Footer.css" rel="stylesheet">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
        <link rel="stylesheet" href="css/updateCampaign.css">
        <title>Update Campaign</title>
    </head>
    <%@ include file="Header.jsp" %>
    <body>
        <c:if test="${not empty sessionScope.success}">
            <div class="alert alert-success" role="alert">
                ${sessionScope.success}
            </div>
            <c:remove var="success" scope="session"/>
        </c:if>
        <c:if test="${not empty sessionScope.error}">
            <div class="alert alert-danger" role="alert">
                ${sessionScope.error}
            </div>
            <c:remove var="error" scope="session"/>
        </c:if>


        <div class="container2 mt-5">
            <h2>Campaign Details</h2>

            <!-- Display success or error message -->
            <c:if test="${not empty error}">
                <div class="alert alert-danger" role="alert">
                    ${error}
                </div>
            </c:if>

            <div class="update">
                <form action="updateCampaign" method="post" enctype="multipart/form-data" onsubmit="return validateForm();">
                    <input type="hidden" name="campaignId" value="${campaign.campaignId}">

                    <div class="mb-3">
                        <label for="campaignName" class="form-label">Campaign Name</label>
                        <input type="text" class="form-control" id="campaignName" name="campaignName" value="${campaign.campaignName}" required readonly oninput="this.value = this.value.trim()">
                    </div>

                    <div class="mb-3">
                        <label for="campaignDescription" class="form-label">Description</label>
                        <textarea class="form-control" id="campaignDescription" name="description" required readonly oninput="this.value = this.value.trim()">${campaign.description}</textarea>
                    </div>

                    <div class="mb-3">
                        <label for="campaignStartDate" class="form-label">Start Date</label>
                        <input type="date" class="form-control" id="campaignStartDate" name="startDate" value="${fn:substring(campaign.startDate, 0, 10)}" required readonly>
                    </div>

                    <div class="mb-3">
                        <label for="campaignEndDate" class="form-label">End Date</label>
                        <input type="date" class="form-control" id="campaignEndDate" name="endDate" value="${fn:substring(campaign.endDate, 0, 10)}" required readonly>
                    </div>


                    <!-- Media files section -->
                    <div id="mediaContainer">
                        <c:forEach var="media" items="${cm}">
                            <div class="media-container">
                                <strong>Media:</strong>
                                <c:choose>
                                    <c:when test="${fn:endsWith(media.mediaLink, '.jpg') || fn:endsWith(media.mediaLink, '.png')}">
                                        <img src="${media.mediaLink}" alt="Media Image" style="max-width: 100px; max-height: 100px;" />
                                    </c:when>
                                    <c:when test="${fn:endsWith(media.mediaLink, '.mp4')}">
                                        <video controls style="max-width: 100px; max-height: 100px;">
                                            <source src="${media.mediaLink}" type="video/mp4">
                                            Your browser does not support the video tag.
                                        </video>
                                    </c:when>
                                    <c:otherwise>
                                        <img src="${media.mediaLink}" alt="Media Image" style="max-width: 100px; max-height: 100px;" />
                                    </c:otherwise>
                                </c:choose>
                                <p><em>${media.description}</em></p>
                                <input type="hidden" value="${media.mediaID}" name="current-media"/>
                                <button type="button" onclick="removeMedia(this)">Remove</button>
                            </div>
                            <hr>
                        </c:forEach>
                        <div id="mediaList">
                            <div class="media-container">
                                <button type="button" class="btn-close position-absolute top-0 end-0 mt-1 me-1" aria-label="Close" onclick="removeMediaEntry(this)"></button>
                                <label for="mediaFile">Upload File:</label>
                                <input type="file" name="mediaFiles" class="form-control" onchange="previewMedia(this)" accept="image/*,audio/*,video/*,application/pdf,application/octet-stream"/>

                                <div class="media-preview mt-2"></div>
                                <label for="mediaDescription">Description:</label>
                                <input type="text" name="mediaDescriptions" class="form-control" placeholder="Description" />
                            </div>
                        </div>
                    </div>

                    <button type="button" class="btn btn-secondary mt-3" onclick="addMedia()" style="display:none;" id="addMediaButton">Add More Media</button>
                    <button type="button" class="btn btn-primary mt-3" onclick="enableEditing()" id="editButton">Edit</button>
                    <button type="submit" class="btn btn-primary mt-3" style="display:none;" id="saveButton">Save changes</button>

                    <button type="button" class="btn btn-secondary mt-3" onclick="window.location.href = 'campaignList';">Cancel</button>
                </form>
            </div>
        </div>

        <script>
            function enableEditing() {
                document.getElementById('campaignName').removeAttribute('readonly');
                document.getElementById('campaignDescription').removeAttribute('readonly');
                document.getElementById('campaignStartDate').removeAttribute('readonly');
                document.getElementById('campaignEndDate').removeAttribute('readonly');

                // Show Save and Add Media buttons, hide Edit button
                document.getElementById('saveButton').style.display = 'inline-block';
                document.getElementById('addMediaButton').style.display = 'inline-block';
                document.getElementById('editButton').style.display = 'none';
            }





            // Function to add more media input fields dynamically
            function addMedia() {
                const mediaList = document.getElementById("mediaList");
                const mediaHTML = `
                        <div class="form-group media-entry position-relative mt-3">
                            <button type="button" class="btn-close position-absolute top-0 end-0 mt-1 me-1" aria-label="Close" onclick="removeMediaEntry(this)"></button>
                            <label for="mediaFile">Upload File:</label>
                            <input type="file" name="mediaFiles" class="form-control" onchange="previewMedia(this)" />
                            <label for="mediaDescription">Description:</label>
                            <input type="text" name="mediaDescriptions" class="form-control" placeholder="Description" />
                            <div class="media-preview mt-2"></div>
                        </div>
                    `;
                mediaList.insertAdjacentHTML('beforeend', mediaHTML);
            }

            // Function to remove a media input field
            function removeMedia(button) {
                button.parentElement.remove();
            }

            // Function to preview media
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
                        mediaElement.className = "img-thumbnail";
                        mediaPreview.appendChild(mediaElement);
                    };
                    reader.readAsDataURL(file);
                }
            }

            // Function to validate the form before submission
            function validateForm() {
                const mediaInputs = document.querySelectorAll('input[type="file"][name="mediaFiles"]');
                let valid = true;
                let mediaSelected = false;

                mediaInputs.forEach(input => {
                    if (input.files.length > 0) {
                        mediaSelected = true;
                    }
                });


                // Validate date fields
                const startDate = document.getElementById('campaignStartDate').value;
                const endDate = document.getElementById('campaignEndDate').value;
                if (startDate && endDate && new Date(startDate) > new Date(endDate)) {
                    alert("Start Date cannot be after End Date.");
                    valid = false;
                }

                return valid;
            }
            function removeMediaEntry(button) {
                $(button).closest('.media-entry').remove();
            }
        </script>
        <%@ include file="Footer.jsp" %>
    </body>
</html>
