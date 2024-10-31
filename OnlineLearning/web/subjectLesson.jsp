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
                        <nav class="navbar navbar-light ">
                            <form class="form-inline row filter-form" method="post" action="subjectLesson">
                                <input type="hidden" name="courseName" value="${requestScope.courseName}">
                                <input type="hidden" name="courseId" value="${requestScope.courseId}">
                                <div class="col-md-7 row search-bar">
                                    <h3 class="col-md-4 subject-name">${requestScope.courseName}<br><span style="font-size: 12px;">Subject ID: ${requestScope.courseId}</span></h3>
                                    <input class="form-control mr-sm-2 search-lesson col-md-8" name="search-value" type="search"
                                           value="${requestScope.searchValue}" placeholder="Search" aria-label="Search">
                                </div>
                                <div class="col-md-2 status-select"><label for="dateSelect" class="form-label">Status</label>
                                    <select name="select-status" class="form-select select-1">
                                        <option value="all" ${requestScope.selectStatus eq 'all' ? "selected" : ""}>All</option>
                                        <option value="Active" ${requestScope.selectStatus eq 'Active' ? "selected" : ""}>Active</option>
                                        <option value="Inactive" ${requestScope.selectStatus eq 'Inactive' ? "selected" : ""}>Inactive</option>
                                    </select></div>
                                <div class="col-md-2 topic-select"><label for="dateSelect" class="form-label">Filter by topic</label>
                                    <select name="select-topic" class="form-select select-2">
                                        <option value="all" ${requestScope.selectTopic eq 'all' ? "selected" : ""}>All</option>
                                        <c:forEach items="${lessonType}" var="type">
                                            <option value="${type.typeID}" ${requestScope.selectTopic eq type.typeID.toString() ? "selected" : ""}>${type.typeName}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="col-md-1 search-btn "><button class="btn btn-outline-success my-2 my-sm-0" type="submit">Search</button></div>
                            </form>
                        </nav>
                    </div>
                </div>
                <c:if test="${not empty requestScope.searchValue}">
                    <%@include file="searchLesson.jsp" %>
                </c:if>
                <c:if test="${empty requestScope.searchValue}">
                    <%@include file="notSearchLesson.jsp" %>
                </c:if>



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