/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

function toggleColumn(columnClass) {
            var elements = document.getElementsByClassName(columnClass);
            for (var i = 0; i < elements.length; i++) {
                if (elements[i].classList.contains("hidden")) {
                    elements[i].classList.remove("hidden");
                } else {
                    elements[i].classList.add("hidden");
                }
            }
        }


