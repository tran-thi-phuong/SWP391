<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Subject Lesson</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;600;700&display=swap" rel="stylesheet">  
        <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.5/font/bootstrap-icons.min.css" rel="stylesheet">
        <link href="css/Header_Footer.css" rel="stylesheet"> 
        <link rel="stylesheet" href="css/subjectLesson.css">
    </head>
    <body>
        <%@include file="Header.jsp" %>
        <div class="">
            <div>

            </div>
            <div>
                <nav class="navbar navbar-light ">
                    <form class="form-inline row filter-form">

                        <div class="col-md-7 row search-bar"><h3 class="col-md-4 subject-name">${requestScope.courseName}</h3>
                            <input class="form-control mr-sm-2 search-lesson col-md-8" name="searchValue" type="search" placeholder="Search" aria-label="Search">
                        </div>
                        <div class="col-md-2 status-select"><label for="dateSelect" class="form-label">Status</label>
                            <select name="select-action" class="form-select select-1">
                                <option value="all" >All</option>
                                <option value="active" >Active</option>
                                <option value="inactive">Inactive</option>
                            </select></div>
                        <div class="col-md-2 topic-select"><label for="dateSelect" class="form-label">Filter by topic</label>
                            <select name="select-action" class="form-select select-2">
                                <option value="all">All</option>
                                <c:forEach items="${lessonType}" var="type">
                                    <option value="active">${type.typeName}</option>
                                </c:forEach>
                            </select></div>
                        <div class="col-md-1 search-btn "><button class="btn btn-outline-success my-2 my-sm-0" type="submit">Search</button></div>
                    </form>
                </nav>
            </div>
        </div>
        <div class="lesson-list">
            <c:forEach items="${lessonType}" var="type">
                <div class="lesson-type mb-1">
                    <div class="btn btn-primary mb-2 btn-size" 
                         type="button" 
                         id="btn${type.typeID}"
                         onclick="toggleContent('collapse${type.typeID}')">
                        <span>${type.typeName}</span><button class="btn btn-sm btn-success" onclick="window.location.href = '#'">+</button>
                    </div>  
                    <div class="collapse" id="collapse${type.typeID}">
                        <div class="card">
                            <ul class="list-group">
                                <c:forEach items="${lessonList}" var="lesson">
                                    <c:if test="${lesson.typeID == type.typeID}">
                                        <li class="list-group-item"><span><a href="#">${lesson.title}</a>
                                            </span>
                                            <c:if test="${lesson.status eq 'Active'}">
                                                <button class="btn btn-danger">Deactive</button>
                                            </c:if>
                                            <c:if test="${lesson.status eq 'Inactive'}">
                                                <button class="btn btn-success">Active</button>
                                            </c:if>
                                        </li>
                                    </c:if>
                                </c:forEach>
                            </ul>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>


        <script>
            function toggleContent(id) {
                const content = document.getElementById(id);
                if (content.classList.contains('show')) {
                    content.classList.remove('show');
                } else {
                    content.classList.add('show');
                }
            }
        </script>
    </body>
</html>