<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Subject Preparation</title>
    <link rel="stylesheet" href="path/to/your/css/file.css">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/axios/0.21.1/axios.min.js"></script>
</head>
<style>
    /* Reset and base styles */
* {
    box-sizing: border-box;
    margin: 0;
    padding: 0;
}

body {
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    line-height: 1.6;
    color: #333;
    background-color: #f4f7f9;
    padding: 20px;
}

/* Container */
.container {
    max-width: 800px;
    margin: 0 auto;
    background-color: #ffffff;
    padding: 30px;
    border-radius: 8px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

/* Headings */
h1 {
    font-size: 28px;
    color: #2c3e50;
    margin-bottom: 20px;
    text-align: center;
}

/* Form styles */
.form-group {
    margin-bottom: 20px;
}

label {
    display: block;
    margin-bottom: 5px;
    font-weight: 600;
    color: #34495e;
}

input[type="text"],
input[type="number"],
textarea,
select {
    width: 100%;
    padding: 10px;
    border: 1px solid #ddd;
    border-radius: 4px;
    font-size: 16px;
    transition: border-color 0.3s ease;
}

input[type="text"]:focus,
input[type="number"]:focus,
textarea:focus,
select:focus {
    outline: none;
    border-color: #3498db;
}

input[type="file"] {
    border: 1px solid #ddd;
    border-radius: 4px;
    padding: 10px;
    width: 100%;
}

input[type="checkbox"] {
    margin-right: 5px;
}

textarea {
    resize: vertical;
    min-height: 100px;
}

/* Buttons */
button {
    background-color: #3498db;
    color: #fff;
    border: none;
    padding: 10px 20px;
    font-size: 16px;
    border-radius: 4px;
    cursor: pointer;
    transition: background-color 0.3s ease;
}

button:hover {
    background-color: #2980b9;
}

button[type="submit"] {
    background-color: #2ecc71;
    font-weight: 600;
}

button[type="submit"]:hover {
    background-color: #27ae60;
}

/* Error message */
.error-message {
    background-color: #e74c3c;
    color: #fff;
    padding: 10px;
    border-radius: 4px;
    margin-bottom: 20px;
    text-align: center;
}

/* Thumbnail preview */
#currentThumbnail {
    display: block;
    margin-top: 10px;
    max-width: 200px;
    border-radius: 4px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

/* Dimensions and Price Packages */
#dimensionsContainer,
#pricePackagesContainer {
    margin-bottom: 10px;
}

#dimensionsContainer input,
.price-package input {
    margin-bottom: 10px;
}

.price-package {
    display: flex;
    gap: 10px;
    margin-bottom: 10px;
}

.price-package input {
    flex: 1;
}

/* Responsive design */
@media (max-width: 600px) {
    .container {
        padding: 20px;
    }

    input[type="text"],
    input[type="number"],
    textarea,
    select {
        font-size: 14px;
    }

    .price-package {
        flex-direction: column;
        gap: 5px;
    }
}
</style>
<body>
     <div class="container">
        <h1>Subject Preparation</h1>
        
        <form id="subjectForm">
            <input type="hidden" id="subjectId">
            
            <div class="form-group">
                <label for="name">Name:</label>
                <input type="text" id="name" required>
            </div>
            
            <div class="form-group">
                <label for="title">Title:</label>
                <input type="text" id="title" required>
            </div>
            
            <div class="form-group">
                <label for="tagline">Tagline:</label>
                <input type="text" id="tagline">
            </div>
            
            <div class="form-group">
                <label for="description">Description:</label>
                <textarea id="description" rows="1"></textarea>
            </div>
            
            <div class="form-group">
                <label for="thumbnailImage">Thumbnail Image:</label>
                <input type="file" id="thumbnailImage" accept="image/*">
                <img id="currentThumbnail" src="" alt="Current Thumbnail" style="display: none;">
            </div>
            
            <div class="form-group">
                <label for="thumbnailImage">Video:</label>
                <input type="file" id="thumbnailImage" accept="image/*">
                <img id="currentThumbnail" src="" alt="Current Thumbnail" style="display: none;">
            </div>
            <div class="form-group">
                <label for="category">Category:</label>
                <select id="category"></select>
            </div>
            
            <div class="form-group">
                <label for="featured">
                    <input type="checkbox" id="featured">
                    Featured
                </label>
            </div>
            
            <div class="form-group">
                <label for="owner">Owner:</label>
                <select id="owner"></select>
            </div>
            
            <div class="form-group">
                <label for="status">Status:</label>
                <select id="status">
                    <option value="Draft">Draft</option>
                    <option value="Published">Published</option>
                    <option value="Unpublished">Unpublished</option>
                </select>
            </div>
            
            <div class="form-group">
                <label>Dimensions:</label>
                <div id="dimensionsContainer"></div>
                <button type="button" onclick="addDimension()">Add Dimension</button>
            </div>
            
            <div id="pricePackagesSection" style="display: none;">
                <div class="form-group">
                    <label>Price Packages:</label>
                    <div id="pricePackagesContainer"></div>
                    <button type="button" onclick="addPricePackage()">Add Price Package</button>
                </div>
            </div>
            
            <div class="form-group">
                <button type="submit">Save Changes</button>
            </div>
        </form>
    </div>
    <script>
        let isAdmin = false;

        // Fetch subject data and populate form
        async function fetchSubjectData() {
            try {
                const response = await axios.get('/api/subjects/1'); // Replace '1' with the actual subject ID
                const subject = response.data;
                populateForm(subject);
            } catch (error) {
                showError('Failed to fetch subject data');
            }
        }

        // Populate form with subject data
        function populateForm(subject) {
            document.getElementById('subjectId').value = subject.id;
            document.getElementById('name').value = subject.name;
            document.getElementById('title').value = subject.title;
            document.getElementById('tagline').value = subject.tagline;
            document.getElementById('description').value = subject.description;
            document.getElementById('featured').checked = subject.featured;
            document.getElementById('status').value = subject.status;

            if (subject.thumbnailUrl) {
                document.getElementById('currentThumbnail').src = subject.thumbnailUrl;
                document.getElementById('currentThumbnail').style.display = 'block';
            }

            populateSelect('category', subject.category.id);
            populateSelect('owner', subject.owner.id);
            populateDimensions(subject.dimensions);
            populatePricePackages(subject.pricePackages);
        }

        // Populate select elements
        async function populateSelect(elementId, selectedValue) {
            try {
                const response = await axios.get(`/api/${elementId}s`);
                const options = response.data;
                const selectElement = document.getElementById(elementId);
                selectElement.innerHTML = '';
                options.forEach(option => {
                    const optionElement = document.createElement('option');
                    optionElement.value = option.id;
                    optionElement.textContent = option.name;
                    if (option.id === selectedValue) {
                        optionElement.selected = true;
                    }
                    selectElement.appendChild(optionElement);
                });
            } catch (error) {
                showError(`Failed to fetch ${elementId} options`);
            }
        }

        // Populate dimensions
        function populateDimensions(dimensions) {
            const container = document.getElementById('dimensionsContainer');
            container.innerHTML = '';
            dimensions.forEach((dimension, index) => {
                container.appendChild(createDimensionInput(index, dimension));
            });
        }

        // Create dimension input
        function createDimensionInput(index, value = '') {
            const input = document.createElement('input');
            input.type = 'text';
            input.name = `dimensions[${index}]`;
            input.value = value;
            return input;
        }

        // Add new dimension input
        function addDimension() {
            const container = document.getElementById('dimensionsContainer');
            const index = container.children.length;
            container.appendChild(createDimensionInput(index));
        }

        // Populate price packages
        function populatePricePackages(packages) {
            const container = document.getElementById('pricePackagesContainer');
            container.innerHTML = '';
            packages.forEach((pkg, index) => {
                container.appendChild(createPricePackageInputs(index, pkg));
            });
        }

        // Create price package inputs
        function createPricePackageInputs(index, pkg = { name: '', listPrice: '', salePrice: '' }) {
            const div = document.createElement('div');
            div.className = 'price-package';
            div.innerHTML = `
                <input type="text" name="packageNames[${index}]" value="${pkg.name}" placeholder="Package Name">
                <input type="number" name="listPrices[${index}]" value="${pkg.listPrice}" placeholder="List Price">
                <input type="number" name="salePrices[${index}]" value="${pkg.salePrice}" placeholder="Sale Price">
            `;
            return div;
        }

        // Add new price package inputs
        function addPricePackage() {
            const container = document.getElementById('pricePackagesContainer');
            const index = container.children.length;
            container.appendChild(createPricePackageInputs(index));
        }

        // Show error message
        function showError(message) {
            const errorElement = document.getElementById('errorMessage');
            errorElement.textContent = message;
            errorElement.style.display = 'block';
        }

        // Check if user is admin
        async function checkAdminStatus() {
            try {
                const response = await axios.get('/api/user/isAdmin');
                isAdmin = response.data.isAdmin;
                if (isAdmin) {
                    document.getElementById('pricePackagesSection').style.display = 'block';
                }
            } catch (error) {
                console.error('Failed to check admin status', error);
            }
        }

        // Submit form
        document.getElementById('subjectForm').addEventListener('submit', async (e) => {
            e.preventDefault();
            const formData = new FormData(e.target);
            try {
                await axios.post('/api/subjects/update', formData, {
                    headers: {
                        'Content-Type': 'multipart/form-data'
                    }
                });
                alert('Subject updated successfully');
            } catch (error) {
                showError('Failed to update subject');
            }
        });

        // Initialize page
        async function init() {
            await checkAdminStatus();
            await fetchSubjectData();
            await populateSelect('category');
            await populateSelect('owner');
        }

        init();
    </script>
</body>
</html>