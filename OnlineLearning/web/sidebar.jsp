<link rel="stylesheet" href="css/sidebar.css"/>
<div class="sidebar">
    <h5 class="mb-3">Menu</h5>
    <div class="sidebar-item">
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
    <div class="sidebar-item">
        <i class="bi bi-person sidebar-icon"></i>
        Customer
    </div>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function () {
        const sidebarItems = document.querySelectorAll('.sidebar-item');
        sidebarItems.forEach(item => {
            item.addEventListener('click', function () {
                sidebarItems.forEach(i => i.classList.remove('active'));
                this.classList.add('active');
            });
        });
    });
</script>