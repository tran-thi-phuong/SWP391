<%-- 
    Document   : lessonDetail
    Created on : Oct 27, 2024, 4:47:41 PM
    Author     : tuant
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Lesson details</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;600;700&display=swap" rel="stylesheet">  
        <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.5/font/bootstrap-icons.min.css" rel="stylesheet">
        <link href="css/Header_Footer.css" rel="stylesheet">
        <link rel="stylesheet" href="css/lessonDetail.css"/>
        <link href="https://cdn.quilljs.com/1.3.6/quill.snow.css" rel="stylesheet">
        <script src="https://cdn.quilljs.com/1.3.6/quill.min.js"></script>
    </head>
    <body>
        <%@include file="Header.jsp" %>
        <c:if test="${sessionScope.error}">
            <div class="alert alert-danger alert-size" role="alert">
                Order is already existed.
            </div>
        </c:if>
        <% session.removeAttribute("error");%>
        <c:if test="${sessionScope.success}">
            <div class="alert alert-success alert-size" role="alert">
                Changes have been saved successfully
            </div>
        </c:if>
        <% session.removeAttribute("success");%>
        <form action="lessonDetail" method="post">
            <div class="form-group row-1">
                <div class="">
                    <label for="desciption">Desciption</label>
                    <textarea class="form-control"  id="description" name="description" required>${lesson.description}</textarea>
                </div>
                <div class="topic-div">
                    <label for="topic">Topic</label>
                    <select class="form-select topic" name="topic-select">
                        <c:forEach items="${requestScope.lessonTopic}" var="topic">
                            <option value="${topic.topicID}" ${lesson.topicID == topic.topicID ? "selected" : ""}>${topic.topicName}</option>
                        </c:forEach>

                    </select>
                </div>
            </div>
            <div class="form-group row-2">
                <div class="">
                    <label for="lesson-name" >Name</label>
                    <input class="form-control" type="text" id="lesson-name" name="lesson-name" value="${lesson.title}" required>
                </div>
                <div class="order">
                    <label for="order">Order</label>
                    <input class="form-control" type="number" min="1" name="order" id="order" value="${lesson.order}" required>
                </div>
                <div class="status-div">
                    <label for="status">Status</label>
                    <select class="form-select status" id="status" name="status-select">
                        <option value="Active" ${lesson.status eq 'Active' ? "selected" : ""}>Active</option>
                        <option value="Inactive" ${lesson.status eq 'Inactive' ? "selected" : ""}>Inactive</option>
                    </select>
                </div>
            </div>
            <div class="form-group row-3">
                <div class="html-content">
                    <label for="content" >HTML content</label>
                    <div id="editor-container">${lesson.content}</div>
                    <input type="hidden" name="content" id="content">
                    <input type="hidden" name="action" value="${requestScope.action}">
                    <input type="hidden" name="lessonID" value="${requestScope.lesson.lessonID}">
                    <input type="hidden" name="courseID" value="${requestScope.courseID}">
                </div>
            </div>
            <div class="btn-save">
                <button type="button" class="btn btn-dark" onclick="submitContent()">Submit</button>
                <c:if test="${requestScope.action eq 'update'}">
                    <button type="button" onclick="doDelete(${lesson.lessonID}, '${requestScope.courseID}', '${requestScope.courseName}')" class="btn btn-danger">Delete</button>
                </c:if>
                <a class="btn btn-dark" href="subjectLesson?courseId=${requestScope.courseID}&courseName=${requestScope.courseName}">Back</a>
            </div>
        </form>
        <script>
            // define basePath for accessing resourses
            const contextPath = window.location.pathname.split('/')[1];
            const basePath = window.location.protocol + '//' + window.location.host + '/' + contextPath;

            // define toolbar for editor
            var toolbarOptions = [
                ['bold', 'italic', 'underline'],
                [{'list': 'ordered'}, {'list': 'bullet'}],
                [{'align': ['', 'center', 'right', 'justify']}],
                [{'header': [1, 2, 3, 4, 5, 6, false]}],
                ['link', 'image', 'video']
            ];
            // initialize a quill editor using quilljs library
            var quill = new Quill('#editor-container', {
                theme: 'snow',
                modules: {
                    toolbar: toolbarOptions
                }
            });
            // allow user select image from local device
            quill.getModule('toolbar').addHandler('image', () => {
                selectLocalImage();
            });
            // handle images from clipboard
            quill.clipboard.addMatcher('IMG', function (node, delta) {
                const Delta = Quill.import('delta');
                return new Delta().insert({
                    image: node.src
                });
            });
            // handle video from clipboard
            quill.clipboard.addMatcher('IFRAME', function (node, delta) {
                const Delta = Quill.import('delta');
                return new Delta().insert({
                    video: node.src
                });
            });
            // handling when the text in editor is changed
            quill.on('text-change', function (delta, oldDelta, source) {
                if (source === 'user') {
                    const contents = quill.getContents();
                    contents.ops.forEach((op, index) => {
                        if (op.insert && (op.insert.image || op.insert.video)) {
                            const format = contents.ops[index].attributes || {};
                            const element = quill.root.querySelector(
                                    op.insert.image ? `img[src="${op.insert.image}"]` : `iframe[src="${op.insert.video}"]`
                                    );
                            if (element) {
                                element.style.width = '65%';
                                if (element.tagName === 'IFRAME') {
                                    element.style.height = 'auto';
                                }

                                let container = element.parentElement;
                                if (!container.classList.contains('media-container')) {
                                    container = document.createElement('div');
                                    container.className = 'media-container';
                                    element.parentNode.insertBefore(container, element);
                                    container.appendChild(element);
                                }

                                container.className = 'media-container';
                                if (format.align) {
                                    container.classList.add(`align-${format.align}`);
                                }
                            }
                        }
                    });
                }
            });
            //func for inserting image into editor
            function selectLocalImage() {
                const input = document.createElement('input');
                input.setAttribute('type', 'file');
                input.setAttribute('accept', 'image/*');
                input.click();

                input.onchange = () => {
                    const file = input.files[0];
                    if (file) {
                        uploadImage(file);
                    }
                };
            }
            // func for saving image when image is inserted
            function uploadImage(file) {
                const formData = new FormData();
                formData.append('file', file);

                fetch('lessonImage', {
                    method: 'POST',
                    body: formData
                })
                        .then(response => response.json())
                        .then(result => {
                            if (result.success && result.filename) {
                                const range = quill.getSelection(true);
                                const index = range ? range.index : 0;
                                const imageUrl = basePath + '/images/' + result.filename;
                                quill.insertEmbed(index, 'image', imageUrl, 'user');
                                quill.setSelection(index + 1);
                            } else {
                                console.error('Upload failed:', result.error);
                            }
                        })
                        .catch(error => {
                            console.error('Error:', error);
                        });
            }
            //replace image url by url after saving the image to server
            function processImagePaths(content) {
                const div = document.createElement('div');
                div.innerHTML = content;

                const images = div.getElementsByTagName('img');
                for (let img of images) {
                    const src = img.getAttribute('src');
                    if (src && src.includes('/images/')) {
                        const filename = src.split('/images/').pop();
                        img.setAttribute('src', 'images/' + filename);
                    }
                }

                return div.innerHTML;
            }
            //func for submitting content in editor
            function submitContent() {
                let content = quill.root.innerHTML;
                content = processImagePaths(content);
                document.getElementById('content').value = content;
                document.forms[0].submit();
            }
            function doDelete(lessonID, courseID, courseName) {
    // Confirm the deletion action
    if (confirm("Are you sure you want to delete this lesson?")) {
        // Redirect to the delete URL if confirmed
        window.location.href = `deleteLesson?lessonID=${lesson.lessonID}&courseID=${courseID}&courseName=${requestScope.courseName}`;
    }
}

        </script>
    </body>
</html>
