<%-- 
    Document   : Header
    Created on : Sep 14, 2024, 10:15:32 AM
    Author     : sonna
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<nav class="navbar navbar-expand-lg bg-white shadow-lg">
    <div class="container">
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <!-- S?a l?i link logo, kh�ng c?n l?p l?i -->
        <a class="navbar-brand" href="Homepage">
            Learning
        </a>

        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav mx-auto">
                <li class="nav-item">
                    <c:if test="${sessionScope.user.role == 'Customer' || empty sessionScope.user}">
                        <a class="nav-link active" href="Homepage">Homepage</a>
                    </c:if>
                    <c:if test="${sessionScope.user.role == 'Admin'}">
                        <a class="nav-link active" href="courseStat">Dashboard</a>
                    </c:if>
                    <c:if test="${sessionScope.user.role == 'Marketing'}">
                        <a class="nav-link active" href="registrationStat">Dashboard</a>
                    </c:if>
                </li>

                <li class="nav-item">
                    <c:if test="${sessionScope.user.role == 'Admin'}">
                        <a class="nav-link active" href="UserList">Manage User</a>
                    </c:if>
                </li>

                <li class="nav-item">
                    <c:if test="${sessionScope.user.role == 'Admin'}">
                        <a class="nav-link active" href="RolePermission">Manage System</a>
                    </c:if>
                </li>

                <li class="nav-item">
                    <c:if test="${sessionScope.user.role == 'Instructor'}">
                        <a class="nav-link active" href="QuizList">Quiz List</a>
                    </c:if>
                </li>

                <li class="nav-item">
                    <c:if test="${sessionScope.user.role == 'Instructor'}">
                        <a class="nav-link active" href="QuestionList">Question List</a>
                    </c:if>
                </li>

                <li class="nav-item">
                    <c:if test="${sessionScope.user.role == 'Marketing'}">
                        <a class="nav-link active" href="listRegistration">Manage Registration</a>
                    </c:if>
                </li>
                <li class="nav-item">
                    <c:if test="${sessionScope.user.role == 'Marketing'}">
                        <a class="nav-link active" href="campaignList">Manage Campaign</a>
                    </c:if>
                </li>

                <li class="nav-item">
                    <c:if test="${sessionScope.user.role == 'Customer' || empty sessionScope.user}">
                        <a class="nav-link" href="CourseList">Course</a>
                    </c:if>
                    <c:if test="${sessionScope.user.role == 'Instructor' || sessionScope.user.role == 'Admin'}">
                        <a class="nav-link" href="SubjectList">Manage Course</a>
                    </c:if>
                </li>

                <li class="nav-item">
                    <c:if test="${sessionScope.user.role == 'Customer' || empty sessionScope.user}">
                        <a class="nav-link" href="blogList">News</a>
                    </c:if>
                    <c:if test="${sessionScope.user.role == 'Marketing' || sessionScope.user.role == 'Instructor'}">
                        <a class="nav-link" href="PostDetail">News</a>
                    </c:if>
                </li>

                <li class="nav-item">
                    <c:if test="${sessionScope.user.role == 'Marketing'}">
                        <a class="nav-link active" href="sliderList">Slider</a>
                    </c:if>
                </li>
            </ul>
        </div>

        <!-- Ki?m tra n?u ng??i d�ng ch?a ??ng nh?p -->
        <c:if test="${empty sessionScope.user}">
            <div class="">
                <button type="button" class="btn btn-danger" onclick="window.location.href = 'login.jsp'">Login</button>
                <button type="button" class="btn btn-secondary" onclick="window.location.href = 'register.jsp'">Register</button>
            </div>
        </c:if>

        <!-- N?u ng??i d�ng ?� ??ng nh?p -->
        <c:if test="${not empty sessionScope.user}">
            <div class="dropdown">
                <button class="btn dropdown-toggle" type="button" id="dropdownMenuButton" data-bs-toggle="dropdown" aria-expanded="false">
                    <img src="${sessionScope.user.avatar == null ? 'images/default-avatar.jpg' : sessionScope.user.avatar}" alt="Avatar" class="rounded-circle" style="width: 40px; height: 40px; object-fit: cover; margin-right: 8px;">
                </button>
                <ul class="dropdown-menu" aria-labelledby="dropdownMenuButton">
                    <li><a class="dropdown-item"><strong>${sessionScope.user.username}</strong></a></li>

                    <!-- N?u l� kh�ch h�ng -->
                    <c:if test="${sessionScope.user.role == 'Customer'}">
                        <li><a class="dropdown-item" href="myRegistration">My Registration</a></li>
                        <li><a class="dropdown-item" href="myCourse">My Courses</a></li>
                    </c:if>

                    <li><a class="dropdown-item" href="profile.jsp">Profile</a></li>
                    <li><a class="dropdown-item" href="Logout">Logout</a></li>
                </ul>
            </div>
        </c:if>
    </div>
</nav>

<!-- Th�m c�c script Bootstrap -->
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.min.js"></script>
