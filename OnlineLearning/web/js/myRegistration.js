/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

document.addEventListener('DOMContentLoaded', function () {
    // Khôi phục trạng thái checkbox từ Local Storage khi tải trang
    document.querySelectorAll('.section-toggle').forEach(function (checkbox) {
        var target = checkbox.dataset.target;
        var savedState = localStorage.getItem(target); 
        
        if (savedState !== null) {
            checkbox.checked = savedState === 'true'; 
        }

        var targets = document.querySelectorAll(target);
        targets.forEach(function (element) {
            element.style.display = checkbox.checked ? '' : 'none'; 
        });
    });

    document.querySelectorAll('.section-toggle').forEach(function (checkbox) {
        checkbox.addEventListener('change', function () {
            var target = this.dataset.target;
            var targets = document.querySelectorAll(target);
            var isChecked = this.checked;
            
            // Cập nhật hiển thị phần tử
            targets.forEach(function (element) {
                element.style.display = isChecked ? '' : 'none';
            });

            // Lưu trạng thái checkbox
            localStorage.setItem(target, isChecked);
        });
    });
});

