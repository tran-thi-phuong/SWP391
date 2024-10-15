/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

function openEditPopup(registrationId) {
    // Fetch registration data based on the ID
    console.log('Opening popup for registration:', registrationId);
    fetch(`/getRegistration?id=${registrationId}`)
            .then(response => response.json())
            .then(data => {
                document.getElementById('subjectName').value = data.subjectName;
                document.getElementById('package').value = data.packageId;
                document.getElementById('totalCost').value = data.totalCost;
                document.getElementById('validFrom').value = data.validFrom;
                document.getElementById('validTo').value = data.validTo;
                document.getElementById('status').value = data.status;
                document.getElementById('staffName').value = data.staffName;

                // Show the popup
                document.getElementById('editPopup').style.display = 'block';
            })
            .catch(error => console.error('Error:', error));
}

function closeEditPopup() {
    document.getElementById('editPopup').style.display = 'none';
}

document.getElementById('editForm').addEventListener('submit', function (e) {
    e.preventDefault();

    // Get form data
    const formData = new FormData(this);

    fetch('/updateRegistration', {
        method: 'POST',
        body: formData
    })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert('Registration updated successfully!');
                    closeEditPopup();
                    // Optionally, refresh the page or update the registration card
                    location.reload();
                } else {
                    alert('Failed to update registration. Please try again.');
                }
            })
            .catch(error => console.error('Error:', error));
});

