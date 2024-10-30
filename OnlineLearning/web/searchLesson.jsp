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
    <div class="btn btn-success btn-add" onclick="window.location.href = 'lessonDetail.jsp'">Add new lesson</div>
    <div class="card search-card">
        <ul class="list-group">
            <c:forEach items="${lessonList}" var="lesson">
                <c:if test="${requestScope.selectStatus eq 'all' && requestScope.selectTopic eq 'all'}">
                    <li class="list-group-item">
                        <span class="subject-info">
                            <a href="#" class="text-primary">${lesson.title}</a>
                            <span class="lesson-id-section">Lesson ID: ${lesson.lessonID}</span>
                            <span class="order-section">Order: ${lesson.order}</span>
                        </span>
                    <c:if test="${lesson.status eq 'Active'}">
                        <button class="btn btn-danger" onclick="window.location.href = 'updateLessonStatus?action=deactive&lessonId=${lesson.lessonID}&courseId=${requestScope.courseId}&courseName=${requestScope.courseName}'">Deactive</button>
                    </c:if>
                    <c:if test="${lesson.status eq 'Inactive'}">
                        <button class="btn btn-success" onclick="window.location.href = 'updateLessonStatus?action=active&lessonId=${lesson.lessonID}&courseId=${requestScope.courseId}&courseName=${requestScope.courseName}'">Active</button>
                    </c:if>
                    </li>
                </c:if>
                <c:if test="${requestScope.selectStatus eq lesson.status && requestScope.selectTopic eq lesson.topicID.toString()}">
                    <li class="list-group-item">
                        <span class="subject-info">
                            <a href="#" class="text-primary">${lesson.title}</a>
                            <span class="lesson-id-section">Lesson ID: ${lesson.lessonID}</span>
                            <span class="order-section">Order: ${lesson.order}</span>
                        </span>
                    <c:if test="${lesson.status eq 'Active'}">
                        <button class="btn btn-danger" onclick="window.location.href = 'updateLessonStatus?action=deactive&lessonId=${lesson.lessonID}&courseId=${requestScope.courseId}&courseName=${requestScope.courseName}'">Deactive</button>
                    </c:if>
                    <c:if test="${lesson.status eq 'Inactive'}">
                        <button class="btn btn-success" onclick="window.location.href = 'updateLessonStatus?action=active&lessonId=${lesson.lessonID}&courseId=${requestScope.courseId}&courseName=${requestScope.courseName}'">Active</button>
                    </c:if>
                    </li>
                </c:if>
                <c:if test="${requestScope.selectStatus eq lesson.status && requestScope.selectTopic eq 'all'}">
                    <li class="list-group-item">
                        <span class="subject-info">
                            <a href="#" class="text-primary">${lesson.title}</a>
                            <span class="lesson-id-section">Lesson ID: ${lesson.lessonID}</span>
                            <span class="order-section">Order: ${lesson.order}</span>
                        </span>
                    <c:if test="${lesson.status eq 'Active'}">
                        <button class="btn btn-danger" onclick="window.location.href = 'updateLessonStatus?action=deactive&lessonId=${lesson.lessonID}&courseId=${requestScope.courseId}&courseName=${requestScope.courseName}'">Deactive</button>
                    </c:if>
                    <c:if test="${lesson.status eq 'Inactive'}">
                        <button class="btn btn-success" onclick="window.location.href = 'updateLessonStatus?action=active&lessonId=${lesson.lessonID}&courseId=${requestScope.courseId}&courseName=${requestScope.courseName}'">Active</button>
                    </c:if>
                    </li>
                </c:if>
                <c:if test="${requestScope.selectTopic eq lesson.topicID.toString() && requestScope.selectStatus eq 'all'}">
                    <li class="list-group-item">
                        <span class="subject-info">
                            <a href="#" class="text-primary">${lesson.title}</a>
                            <span class="lesson-id-section">Lesson ID: ${lesson.lessonID}</span>
                            <span class="order-section">Order: ${lesson.order}</span>
                        </span>
                    <c:if test="${lesson.status eq 'Active'}">
                        <button class="btn btn-danger" onclick="window.location.href = 'updateLessonStatus?action=deactive&lessonId=${lesson.lessonID}&courseId=${requestScope.courseId}&courseName=${requestScope.courseName}'">Deactive</button>
                    </c:if>
                    <c:if test="${lesson.status eq 'Inactive'}">
                        <button class="btn btn-success" onclick="window.location.href = 'updateLessonStatus?action=active&lessonId=${lesson.lessonID}&courseId=${requestScope.courseId}&courseName=${requestScope.courseName}'">Active</button>
                    </c:if>
                    </li>
                </c:if>
            </c:forEach>
        </ul>
    </div>
</div>
<script>
    document.addEventListener("DOMContentLoaded", function () {
        setInitialVisibility();
    });

    function setInitialVisibility() {
        document.querySelectorAll('.list-group-item').forEach((item) => {
            item.style.display = '';
        });
    }

    function changeTopicLimit() {
        var limit = document.getElementById('topicLimit').value;
        if (limit === '' || limit.toLowerCase() === 'all') {
            document.querySelectorAll('.list-group-item').forEach((item) => {
                item.style.display = '';
            });
            return;
        }
        limit = parseInt(limit);
        if (!isNaN(limit) && limit > 0) {
            document.querySelectorAll('.list-group-item').forEach((item, index) => {
                item.style.display = index < limit ? '' : 'none';
            });
        }
    }
    document.querySelectorAll('.section-toggle').forEach(function (checkbox) {
        checkbox.addEventListener('change', function () {
            document.querySelectorAll(this.dataset.target).forEach(function (target) {
                target.style.display = checkbox.checked ? '' : 'none';
            });
        });
    });
</script>
