<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${subject.title}" /> - Chi tiết môn học</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <div class="container">
        <header>
            <h1><c:out value="${subject.title}" /></h1>
        </header>
        
        <main class="subject-detail">
            <img src="<c:out value="${subject.thumbnail}" />" alt="<c:out value="${subject.title}" />" class="subject-image">
            <p class="tagline"><c:out value="${subject.tagline}" /></p>
            <div class="description"><c:out value="${subject.description}" /></div>
            
            <section class="lessons">
                <h2>Lesson</h2>
                <ul>
                    <c:forEach items="${subject.lessons}" var="lesson">
                        <li><c:out value="${lesson.title}" /></li>
                    </c:forEach>
                </ul>
            </section>
            
            <section class="pricing">
                <h2>Register package</h2>
                <c:forEach items="${subject.packages}" var="pack">
                    <div class="package">
                        <h3><c:out value="${pack.name}" /></h3>
                        <p class="price">
                            <span class="list-price"><c:out value="${pack.listPrice}" /></span>
                            <span class="sale-price"><c:out value="${pack.salePrice}" /></span>
                        </p>
                        <ul class="features">
                            <c:forEach items="${pack.features}" var="feature">
                                <li><c:out value="${feature}" /></li>
                            </c:forEach>
                        </ul>
                        <a href="register?id=<c:out value="${subject.id}" />&package=<c:out value="${pack.id}" />" class="btn-register">Đăng ký gói này</a>
                    </div>
                </c:forEach>
            </section>
        </main>
        
        <footer>
            <a href="index.jsp" class="btn-back">Back to course list</a>
        </footer>
    </div>
</body>
</html>