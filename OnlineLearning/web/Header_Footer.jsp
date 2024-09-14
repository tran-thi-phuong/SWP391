<%-- 
    Document   : Homepage
    Created on : Sep 10, 2024, 6:56:34 PM
    Author     : sonna
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!doctype html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <title>Online Learning Web</title>

        <!-- CSS FILES -->    
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;600;700&display=swap" rel="stylesheet">  
         
        <link href="bootstrap.min.css" rel="stylesheet">
        <link href="bootstrap-icons.css" rel="stylesheet">
        <link href="tooplate-crispy-kitchen.css" rel="stylesheet">

      
    </head>

    <body>

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
                            <a class="nav-link active" href="Homepage.jsp">Homepage</a>
                        </li>

                        <li class="nav-item">
                            <a class="nav-link" href="#">Course</a>
                        </li>

                        <li class="nav-item">
                            <a class="nav-link" href="#">News</a>
                        </li>

                        <li class="nav-item">
                            <a class="nav-link" href="#">Support</a>
                        </li>
                    </ul>
                </div>

                <div class="">
                    <button type="button" class="btn btn-danger" onclick="window.location.href='login.jsp'">Login</button>
                    <button type="button" class="btn btn-secondary" >Register</button>
                </div>
            </div>
        </nav>

        
        <div class="container">
            <div class="row my-5">
                <div class="col-md-4">
                    <h3>About Us</h3>
                    <p>We are a leading online learning platform offering a variety of courses designed to enhance your skills and knowledge in multiple fields. Join us today to start your learning journey!</p>
                </div>

                <div class="col-md-4">
                    <h3>Our Mission</h3>
                    <p>Our mission is to make quality education accessible to everyone, anywhere, and at any time. We aim to empower students with knowledge that can help them succeed in their careers.</p>
                </div>

                <div class="col-md-4">
                    <h3>Why Choose Us?</h3>
                    <p>We offer a wide range of courses taught by experienced instructors. Our flexible schedule and user-friendly platform make it easy for you to learn at your own pace.</p>
                </div>
            </div>

            <div class="row my-5">
                <div class="col-md-6">
                    <h4>Upcoming Events</h4>
                    <ul>
                        <li>Free Web Development Workshop - Sep 20, 2024</li>
                        <li>Data Science Bootcamp - Oct 10, 2024</li>
                        <li>Advanced Java Programming Course - Nov 1, 2024</li>
                    </ul>
                </div>

                <div class="col-md-6">
                    <h4>Testimonials</h4>
                    <blockquote class="blockquote">
                        <p>"The learning experience here has been amazing! The courses are well-structured and the instructors are incredibly knowledgeable."</p>
                        <footer class="blockquote-footer">John Doe, Student</footer>
                    </blockquote>

                    <blockquote class="blockquote">
                        <p>"I was able to improve my programming skills significantly thanks to their comprehensive courses."</p>
                        <footer class="blockquote-footer">Jane Smith, Developer</footer>
                    </blockquote>
                </div>
            </div>
        </div>


        <footer class="site-footer section-padding">     
            <div class="container">             
                <div class="row">
                    <div class="col-12">
                        <h4 class="text-white mb-4 me-5">Learning Web</h4>
                    </div>

                    <div class="col-lg-4 col-md-7 col-xs-12 tooplate-mt30">
                        <h6 class="text-white mb-lg-4 mb-3">Hot Course</h6>       
                    </div>

                    <div class="col-lg-4 col-md-5 col-xs-12 tooplate-mt30">
                        <h6 class="text-white mb-lg-4 mb-3">News</h6>
                    </div>

                    <div class="col-lg-4 col-md-6 col-xs-12 tooplate-mt30">
                        <h6 class="text-white mb-lg-4 mb-3">More</h6>
                        <a  href="#">Contact</a>
                        <p></p>
                    </div>
                </div>  
            </div>     
        </footer>
    </body>
</html>
