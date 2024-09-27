<%-- 
    Document   : Header
    Created on : Sep 14, 2024, 10:15:32 AM
    Author     : sonna
--%>

        <nav class="navbar navbar-expand-lg bg-white shadow-lg">
            <div class="container">
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>

                <a class="navbar-brand" href="Homepage.jsp">
                    Learning
                </a>

                <div class="collapse navbar-collapse" id="navbarNav">
                    <ul class="navbar-nav mx-auto">
                        <li class="nav-item">
                            <a class="nav-link active" href="Homepage">Homepage</a>
                        </li>

                        <li class="nav-item">
                            <a class="nav-link" href="#">Course</a>
                        </li>

                        <li class="nav-item">
                            <a class="nav-link" href="blogs">News</a>
                        </li>

                        <li class="nav-item">
                            <a class="nav-link" href="#">Support</a>
                        </li>
                    </ul>
                </div>

                <div class="">
                    <button type="button" class="btn btn-danger" onclick="window.location.href = 'login.jsp'">Login</button>
                    <button type="button" class="btn btn-secondary" onclick="window.location.href = 'register.jsp'">Register</button>
                </div>
            </div>
        </nav>
