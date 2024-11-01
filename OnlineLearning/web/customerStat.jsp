
        <div class="col-md-9 content-container">
            <div class="filter-course-regis">
                <form id="toggleForm" method="post" action="customerStat">
                    <div class="checkBox">
                        <label><input type="checkbox" class="section-toggle" data-target=".total-customer-section" checked> Total customer</label>
                        <label><input type="checkbox" class="section-toggle" data-target=".newly-bought-section" checked>Newly bought customer</label><br>
                        <label><input type="checkbox" class="section-toggle" data-target=".new-customer-section" checked>New customer</label>
                        <label><input type="checkbox" class="section-toggle" data-target=".total-bought-section" checked>Total bought customer</label>
                    </div>
                    <div class="select-time">
                        <div id="customDateRange" style="display: none;">
                            <div class="date-input">
                                <label for="timeRange" class="form-label">Select time range</label>
                                <input type="text" id="timeRange" name="timeRange" class="form-control" placeholder="Select time range" required>
                            </div>

                        </div>
                        <div>
                            <label for="dateSelect" class="form-label">Select Date Range</label>
                            <select name="select-action" class="form-select" id="dateSelect" onchange="toggleCustomDateRange()">
                                <option value="7days" ${requestScope.select == "7days" ? "selected" : ""}>Last 7 days</option>
                                <option value="30days" ${requestScope.select == "30days" ? "selected" : ""}>Last 30 days</option>
                                <option value="custom" ${requestScope.select == "custom" ? "selected" : ""}>Custom</option>
                            </select>
                        </div>
                        <div class="apply-btn"><input type="submit" value="Apply"></div>
                    </div>

                </form>
                <p class="error-mess-customer" >${requestScope.error}</p>
            </div>
            <div class="content">
                <div class="total-course-card">
                    <div class="card-1 total-customer-section">
                        <p class="card-title">Total customer</p>
                        <div class="course-stat">
                            <div class="icon bi bi-book"></div>
                            <div class="course-number">${requestScope.totalCustomer}</div>
                        </div>
        <!--                    <div class="inactive-course">${requestScope.inactiveCourse} courses inactive</div>-->
                    </div>
                    <div class="card-2 new-customer-section">
                        <p class="card-title text-success">New customer</p>
                        <div class="course-stat">
                            <div class="icon bi bi-book"></div>
                            <div class="course-number">${requestScope.newCustomer}</div>
                        </div>
                    </div>

                </div>
                <div class="total-course-card newly-bought">
                    <div class="card-1 newly-bought-section">
                        <p class="card-title text-dark">Newly bought customer</p>
                        <div class="course-stat">
                            <div class="icon bi bi-book"></div>
                            <div class="course-number">${requestScope.newlyBoughtCustomer}</div>
                        </div>
        <!--                    <div class="inactive-course">${requestScope.inactiveCourse} courses inactive</div>-->
                    </div>
                    <div class="card-2 total-bought-section">
                        <p class="card-title">Total bought customer</p>
                        <div class="course-stat">
                            <div class="icon bi bi-book"></div>
                            <div class="course-number">${requestScope.totalBoughtCustomer}</div>
                        </div>
                    </div>

                </div>
            </div>

        </div>
    
<script>
    document.querySelectorAll('.section-toggle').forEach(function (checkbox) {
        checkbox.addEventListener('change', function () {
            var target = document.querySelector(this.dataset.target);
            if (this.checked) {
                target.style.display = '';
            } else {
                target.style.display = 'none';
            }
        });
    });
    toggleCustomDateRange();
    function toggleCustomDateRange() {
        const dateRangeSection = document.getElementById("customDateRange");
        const selectElement = document.getElementById("dateSelect");

        if (selectElement.value === "custom") {
            dateRangeSection.style.display = "";
        } else {
            dateRangeSection.style.display = "none";
        }
    }
    flatpickr("#timeRange", {
        mode: "range",
        dateFormat: "Y-m-d",
        onChange: function (selectedDates, dateStr, instance) {
            console.log("First selected date range: ", dateStr);
        }
    });
</script>