
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="sidebar">
    <h5 class="mb-3">Menu</h5>
    <div class="sidebar-item" onclick="window.location.href = 'courseStat'">
        <i class="bi bi-book sidebar-icon"></i>
        Courses
    </div>
    <div class="sidebar-item">
        <i class="bi bi-pencil-square sidebar-icon"></i>
        Registration
    </div>
    <div class="sidebar-item">
        <i class="bi bi-cash sidebar-icon"></i>
        Revenue
    </div>
    <div class="sidebar-item" onclick="window.location.href = 'dashboard.jsp'">
        <i class="bi bi-person sidebar-icon" ></i>
        Customer
    </div>
    <hr>
    <h5 class="mb-3">Campaign</h5>
    <c:forEach items="${sessionScope.campaigns}" var="cam">
        <div class="sidebar-item">
            <i class="bi bi-cash-coin sidebar-icon"></i>
            ${cam.campaignName}
        </div>
    </c:forEach>
</div>

