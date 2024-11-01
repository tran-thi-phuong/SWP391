
<div class="lesson-list">
    <div class="numOfTopic">
        <label for="topicLimit">Number of topics:</label>
        <input id="topicLimit" type="number" placeholder="All" min="1" onchange="changeTopicLimit()">
        <div class="checkBox checkBox-lesson">
            <label><input type="checkbox" class="section-toggle" data-target=".subject-id-section" checked> Subject ID</label>
            <label><input type="checkbox" class="section-toggle" data-target=".lesson-id-section" checked> Lesson ID</label>
            <label><input type="checkbox" class="section-toggle" data-target=".order-section" checked> Lesson order</label>
        </div>
    </div>

    <div class="btn btn-success btn-add" onclick="window.location.href = 'lessonDetail?courseID=${requestScope.courseId}&action=add'">Add new lesson</div>
    <c:forEach items="${lessonType}" var="type">
        <div class="lesson-type mb-1">
            <c:if test="${requestScope.selectTopic eq 'all'}">
                <div class="btn btn-primary mb-2 btn-size" 
                     type="button" 
                     id="btn${type.topicID}"
                     onclick="toggleContent('collapse${type.topicID}')">
                    <span>${type.topicName}</span><div><i class="bi bi-plus"></i></div>
                </div>
            </c:if>
            <c:if test="${requestScope.selectTopic eq type.topicID.toString()}">
                <div class="btn btn-primary mb-2 btn-size" 
                     type="button" 
                     id="btn${type.topicID}"
                     onclick="toggleContent('collapse${type.topicID}')">
                    <span>${type.topicName}</span><div><i class="bi bi-plus"></i></div>
                </div>
            </c:if> 
            <div class="collapse" id="collapse${type.topicID}">
                <div class="card">
                    <ul class="list-group">
                        <c:forEach items="${lessonList}" var="lesson">
                            <c:if test="${lesson.topicID == type.topicID}">

                                <c:if test="${requestScope.selectStatus eq 'all'}">
                                    <li class="list-group-item">
                                        <span class="subject-info">
                                            <a href="lessonDetail?lessonID=${lesson.lessonID}&courseID=${requestScope.courseId}&action=update" class="text-primary">${lesson.title}</a>
                                            <span class="lesson-id-section">Lesson ID: ${lesson.lessonID}</span>
                                            <span class="order-section">Order: ${lesson.order}</span>
                                        </span>
                                    <c:if test="${lesson.status eq 'Active'}">
                                        <button class="btn btn-danger btn-status" onclick="window.location.href = 'updateLessonStatus?action=deactive&lessonId=${lesson.lessonID}&courseId=${requestScope.courseId}&courseName=${requestScope.courseName}'">Deactive</button>
                                    </c:if>
                                    <c:if test="${lesson.status eq 'Inactive'}">
                                        <button class="btn btn-success btn-status" onclick="window.location.href = 'updateLessonStatus?action=active&lessonId=${lesson.lessonID}&courseId=${requestScope.courseId}&courseName=${requestScope.courseName}'">Active</button>
                                    </c:if>
                                    </li>
                                </c:if>
                                <c:if test="${requestScope.selectStatus eq lesson.status}">
                                    <li class="list-group-item">
                                        <span class="subject-info">
                                            <a href="lessonDetail?lessonID=${lesson.lessonID}&courseID=${requestScope.courseId}&action=update" class="text-primary">${lesson.title}</a>
                                            <span class="lesson-id-section">Lesson ID: ${lesson.lessonID}</span>
                                            <span class="order-section">Order: ${lesson.order}</span>
                                        </span>
                                    <c:if test="${lesson.status eq 'Active'}">
                                        <button class="btn btn-danger btn-status" onclick="window.location.href = 'updateLessonStatus?action=deactive&lessonId=${lesson.lessonID}&courseId=${requestScope.courseId}&courseName=${course.title}'">Deactive</button>
                                    </c:if>
                                    <c:if test="${lesson.status eq 'Inactive'}">
                                        <button class="btn btn-success btn-status" onclick="window.location.href = 'updateLessonStatus?action=active&lessonId=${lesson.lessonID}&courseId=${requestScope.courseId}&courseName=${requestScope.courseName}'">Active</button>
                                    </c:if>
                                    </li>
                                </c:if>
                            </c:if>
                        </c:forEach>
                    </ul>
                </div>
            </div>
        </div>
    </c:forEach>
</div>
<script>
    // func for displaying topics after changing number of topic
    function setInitialVisibility() {
        document.querySelectorAll('.lesson-type').forEach((item) => {
            item.style.display = '';
        });
    }
    // //func when the number of topic is changed
    function changeTopicLimit() {
        const limitInput = document.getElementById('topicLimit');
        const limit = limitInput.value ? parseInt(limitInput.value) : 'all';

        if (limit === 'all') {
            document.querySelectorAll('.lesson-type').forEach((item) => {
                item.style.display = '';
            });
        } else {
            document.querySelectorAll('.lesson-type').forEach((item, index) => {
                item.style.display = index < limit ? '' : 'none';
            });
        }
    }
    setInitialVisibility();
    // configuring displayed information
    document.querySelectorAll('.section-toggle').forEach(function (checkbox) {
        checkbox.addEventListener('change', function () {
            document.querySelectorAll(this.dataset.target).forEach(function (target) {
                target.style.display = checkbox.checked ? '' : 'none';
            });
        });
    });
</script>