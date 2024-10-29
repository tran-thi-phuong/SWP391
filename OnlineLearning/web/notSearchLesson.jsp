<div class="lesson-list">
    <div class="numOfTopic">
        <label for="topicLimit">Number of topics:</label>
    <select id="topicLimit" onchange="changeTopicLimit()">
        <option value="all">All</option>
        <option value="5">5</option>
        <option value="10">10</option>
        <option value="15">15</option>
    </select>
    </div>
    <div class="btn btn-success btn-add">Add new lesson</div>
    <c:forEach items="${lessonType}" var="type">
        <div class="lesson-type mb-1">
            <c:if test="${requestScope.selectTopic eq 'all'}">
                <div class="btn btn-primary mb-2 btn-size" 
                     type="button" 
                     id="btn${type.typeID}"
                     onclick="toggleContent('collapse${type.typeID}')">
                    <span>${type.typeName}</span><div><i class="bi bi-plus"></i></div>
                </div>
            </c:if>
            <c:if test="${requestScope.selectTopic eq type.typeID.toString()}">
                <div class="btn btn-primary mb-2 btn-size" 
                     type="button" 
                     id="btn${type.typeID}"
                     onclick="toggleContent('collapse${type.typeID}')">
                    <span>${type.typeName}</span><div><i class="bi bi-plus"></i></div>
                </div>
            </c:if> 
            <div class="collapse" id="collapse${type.typeID}">
                <div class="card">
                    <ul class="list-group">
                        <c:forEach items="${lessonList}" var="lesson">
                            <c:if test="${lesson.typeID == type.typeID}">

                                <c:if test="${requestScope.selectStatus eq 'all'}">
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
                                <c:if test="${requestScope.selectStatus eq lesson.status}">
                                    <li class="list-group-item">
                                        <span>
                                            <a href="#" class="text-primary">${lesson.title}</a><br>
                                            <span>Lesson ID: ${lesson.lessonID}</span>
                                        </span>
                                    <c:if test="${lesson.status eq 'Active'}">
                                        <button class="btn btn-danger" onclick="window.location.href = 'updateLessonStatus?action=deactive&lessonId=${lesson.lessonID}&courseId=${requestScope.courseId}&courseName=${course.title}'">Deactive</button>
                                    </c:if>
                                    <c:if test="${lesson.status eq 'Inactive'}">
                                        <button class="btn btn-success" onclick="window.location.href = 'updateLessonStatus?action=active&lessonId=${lesson.lessonID}&courseId=${requestScope.courseId}&courseName=${requestScope.courseName}'">Active</button>
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
    var totalItems = document.querySelectorAll('.lesson-type').length;
    function setInitialVisibility() {

        document.querySelectorAll('.lesson-type').forEach((item) => {
            item.style.display = '';
        });
    }
    function changeTopicLimit() {
        var limit = document.getElementById('topicLimit').value;
        if (limit === 'all') {
            document.querySelectorAll('.lesson-type').forEach((item) => {
                item.style.display = '';
            });
        } else {
            limit = parseInt(limit); 
            document.querySelectorAll('.lesson-type').forEach((item, index) => {
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
