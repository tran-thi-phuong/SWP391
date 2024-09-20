document.addEventListener('DOMContentLoaded', function() {
    const popup = document.getElementById('register-popup');
    const openPopupBtn = document.getElementById('open-popup-btn');
    const closeBtn = document.querySelector('.close-btn');

    // Function to open the pop-up
    function openPopup() {
        popup.style.display = 'block';
    }

    // Function to close the pop-up
    function closePopup() {
        popup.style.display = 'none';
    }

    // Event listener to open the pop-up when the button is clicked
    openPopupBtn.addEventListener('click', openPopup);

    // Event listener to close the pop-up when the close button is clicked
    closeBtn.addEventListener('click', closePopup);

    // Optional: Close the pop-up when clicking outside of it
    window.addEventListener('click', function(event) {
        if (event.target === popup) {
            closePopup();
        }
    });
});
