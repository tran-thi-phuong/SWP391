/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

document.addEventListener('DOMContentLoaded', function() {
    const checkboxes = document.querySelectorAll('input[type="checkbox"][data-attr]');
    const registrationCards = document.querySelectorAll('.registration-card');

    function toggleAttribute(attr) {
        console.log('Toggling attribute:', attr);
        const isChecked = document.querySelector(`input[data-attr="${attr}"]`).checked;
        registrationCards.forEach(card => {
            const element = card.querySelector('.' + attr);
            if (element) {
                element.style.display = isChecked ? 'none' : '';
                console.log(`${attr} display:`, element.style.display);
            } else {
                console.log(`Element with class ${attr} not found in card`);
            }
        });
    }

    checkboxes.forEach(checkbox => {
        checkbox.addEventListener('change', function() {
            toggleAttribute(this.getAttribute('data-attr'));
        });
        // Initialize state
        toggleAttribute(checkbox.getAttribute('data-attr'));
    });

    console.log('Total checkboxes:', checkboxes.length);
    console.log('Total registration cards:', registrationCards.length);
});