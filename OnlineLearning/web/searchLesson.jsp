
<div class="lesson-list">
    <div class="numOfTopic">
        <label for="topicLimit">Number of lessons:</label>
    <select id="topicLimit" onchange="changeTopicLimit()">
        <option value="all">All</option>
        <option value="5">5</option>
        <option value="10">10</option>
        <option value="15">15</option>
    </select>
    </div>
    <div class="btn btn-success btn-add">Add new lesson</div>
    <div class="card search-card">
        <ul class="list-group">
            <c:forEach items="${lessonList}" var="lesson">
                <c:if test="${requestScope.selectStatus eq 'all' && requestScope.selectTopic eq 'all'}">
                    <li class="list-group-item">
                        <span>
                            <a href="#" class="text-primary">${lesson.title}</a><br>
                            <span>Lesson ID: ${lesson.lessonID}</span>
                        </span>
                    <c:if test="${lesson.status eq 'Active'}">
                        <button class="btn btn-danger" onclick="window.location.href = 'updateLessonStatus?action=deactive&lessonId=${lesson.lessonID}&courseId=${requestScope.courseId}&courseName=${requestScope.courseName}'">Deactive</button>
                    </c:if>
                    <c:if test="${lesson.status eq 'Inactive'}">
                        <button class="btn btn-success" onclick="window.location.href = 'updateLessonStatus?action=active&lessonId=${lesson.lessonID}&courseId=${requestScope.courseId}&courseName=${requestScope.courseName}'">Active</button>
                    </c:if>
                    </li>
                </c:if>
                <c:if test="${requestScope.selectStatus eq lesson.status && requestScope.selectTopic eq lesson.typeID.toString()}">
                    <li class="list-group-item">
                        <span>
                            <a href="#" class="text-primary">${lesson.title}</a><br>
                            <span>Lesson ID: ${lesson.lessonID}</span>
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
                        <span>
                            <a href="#" class="text-primary">${lesson.title}</a><br>
                            <span>Lesson ID: ${lesson.lessonID}</span>
                        </span>
                    <c:if test="${lesson.status eq 'Active'}">
                        <button class="btn btn-danger" onclick="window.location.href = 'updateLessonStatus?action=deactive&lessonId=${lesson.lessonID}&courseId=${requestScope.courseId}&courseName=${requestScope.courseName}'">Deactive</button>
                    </c:if>
                    <c:if test="${lesson.status eq 'Inactive'}">
                        <button class="btn btn-success" onclick="window.location.href = 'updateLessonStatus?action=active&lessonId=${lesson.lessonID}&courseId=${requestScope.courseId}&courseName=${requestScope.courseName}'">Active</button>
                    </c:if>
                    </li>
                </c:if>
                <c:if test="${requestScope.selectTopic eq lesson.typeID.toString() && requestScope.selectStatus eq 'all'}">
                    <li class="list-group-item">
                        <span>
                            <a href="#" class="text-primary">${lesson.title}</a><br>
                            <span>Lesson ID: ${lesson.lessonID}</span>
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
    var totalItems = document.querySelectorAll('.list-group-item').length;
    function setInitialVisibility() {

        document.querySelectorAll('.list-group-item').forEach((item) => {
            item.style.display = '';
        });
    }
    function changeTopicLimit() {
        var limit = document.getElementById('topicLimit').value;
        if (limit === 'all') {
            document.querySelectorAll('.list-group-item').forEach((item) => {
                item.style.display = '';
            });
        } else {
            limit = parseInt(limit); 
            document.querySelectorAll('.list-group-item').forEach((item, index) => {
                if (index < limit) {
                    item.style.display = '';
                } else {
                    item.style.display = 'none';
                }
            });
        }
    }
    setInitialVisibility();
</script>