<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;600;700&display=swap" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.5/font/bootstrap-icons.min.css" rel="stylesheet">
    <link href="css/Header_Footer.css" rel="stylesheet">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
    <link rel="stylesheet" href="css/addCampaign.css">
    <title>Add Campaign</title>
</head>

<%@ include file="Header.jsp" %>

<body>
    <div class="container2">
        <h2>Add New Campaign</h2>

        <!-- Display success or error messages -->
        <c:if test="${not empty error}">
            <div class="alert alert-danger" role="alert">
                ${error}
            </div>
        </c:if>
        <c:if test="${not empty success}">
            <div class="alert alert-success" role="alert">
                ${success}
            </div>
        </c:if>

        <form action="AddCampaign" method="post" enctype="multipart/form-data" onsubmit="return validateForm()">
            <div class="form-group">
                <label for="campaignName">Campaign Name:</label>
                <input type="text" id="campaignName" name="campaignName" required class="form-control" />
            </div>
            <div class="form-group">
                <label for="description">Description:</label>
                <textarea id="description" name="description" required class="form-control"></textarea>
            </div>
            <div class="form-group">
                <label for="startDate">Start Date:</label>
                <input type="date" id="startDate" name="startDate" required class="form-control" />
            </div>
            <div class="form-group">
                <label for="endDate">End Date:</label>
                <input type="date" id="endDate" name="endDate" required class="form-control" />
            </div>

            <!-- Multiple Media Files with Descriptions -->
            <div id="mediaFilesContainer">
                <div class="form-group media-entry position-relative">
                    <button type="button" class="btn-close position-absolute top-0 end-0 mt-1 me-1" aria-label="Close" onclick="removeMediaEntry(this)"></button>
                    <label for="mediaFile">Upload File:</label>
                    <input type="file" name="mediaFiles" class="form-control" onchange="previewMedia(this)" accept="image/*,audio/*,video/*,application/pdf,application/octet-stream"/>

                     <div class="media-preview mt-2"></div>
                    <label for="mediaDescription">Description:</label>
                    <input type="text" name="mediaDescriptions" class="form-control" placeholder="Description" />
                    <!-- Preview area for media files -->
                   
                </div>
            </div>

            <!-- Add More Media Button -->
            <button type="button" class="btn btn-link mt-3" id="addMoreMedia">Add More Media File</button>

            <!-- Button Row -->
            <div class="d-flex justify-content-between mt-3">
                <button type="submit" class="btn btn-primary">Add Campaign</button>
                <form action="campaignList" method="get" style="margin: 0;">
                    <button type="submit" class="btn btn-secondary">Cancel</button>
                </form>
            </div>
        </form>
    </div>

    <script>
        $(document).ready(function() {
            $('#addMoreMedia').on('click', function() {
                const newMediaEntry = `
                    <div class="form-group media-entry position-relative mt-3">
                        <button type="button" class="btn-close position-absolute top-0 end-0 mt-1 me-1" aria-label="Close" onclick="removeMediaEntry(this)"></button>
                        <label for="mediaFile">Upload File:</label>
                        <input type="file" name="mediaFiles" class="form-control" onchange="previewMedia(this)" />
                        <label for="mediaDescription">Description:</label>
                        <input type="text" name="mediaDescriptions" class="form-control" placeholder="Description" />
                        <div class="media-preview mt-2"></div>
                    </div>
                `;
                $('#mediaFilesContainer').append(newMediaEntry);
            });
        });

        function validateForm() {
            const campaignName = document.getElementById("campaignName").value.trim();
            const description = document.getElementById("description").value.trim();
            const startDate = new Date(document.getElementById("startDate").value);
            const endDate = new Date(document.getElementById("endDate").value);

            // Check for empty values after trimming
            if (!campaignName) {
                alert("Campaign name cannot be empty or whitespace only.");
                return false;
            }
            if (!description) {
                alert("Description cannot be empty or whitespace only.");
                return false;
            }
            if (startDate >= endDate) {
                alert("Start date must be earlier than end date.");
                return false;
            }

            // Validate media descriptions
            const mediaEntries = document.querySelectorAll('.media-entry');
            for (const entry of mediaEntries) {
                const mediaDescription = entry.querySelector('input[name="mediaDescriptions"]').value.trim();
                if (!mediaDescription) {
                    alert("Media description cannot be empty or whitespace only.");
                    return false;
                }
            }

            return true;
        }

        function previewMedia(input) {
            const mediaPreview = $(input).siblings('.media-preview').get(0);
            mediaPreview.innerHTML = ''; // Clear previous preview
            const file = input.files[0];

            if (file) {
                const reader = new FileReader();
                reader.onload = function (e) {
                    const mediaElement = document.createElement(file.type.startsWith('image/') ? 'img' : 'video');
                    mediaElement.src = e.target.result;
                    mediaElement.controls = file.type.startsWith('video/');
                    mediaElement.className = "img-thumbnail"; // Apply thumbnail style
                    mediaPreview.appendChild(mediaElement);
                };
                reader.readAsDataURL(file);
            }
        }

        function removeMediaEntry(button) {
            $(button).closest('.media-entry').remove();
        }
    </script>
</body>

<%@ include file="Footer.jsp" %>
</html>
