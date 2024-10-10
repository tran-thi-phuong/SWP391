<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="col-md-9 content-container">
    <div class="filter-course-regis">
        <form id="toggleForm" method="post" action="registrationStat">
            <div class="checkBox">
                <label><input type="checkbox" class="section-toggle" data-target=".total-registration-section" checked> Total registration</label>
                <label><input type="checkbox" class="section-toggle" data-target=".best-registration-section" checked> Best seller course</label><br>
                <label><input type="checkbox" class="section-toggle" data-target=".new-registration-section" checked> New registration</label>
                <label><input type="checkbox" class="section-toggle" data-target=".success-registration-section" checked> Success registration</label><br>
                <label><input type="checkbox" class="section-toggle" data-target=".submitted-registration-section" checked> Submitted registration</label>
                <label><input type="checkbox" class="section-toggle" data-target=".cancelled-registration-section" checked> Cancelled registration</label><br>
                <label><input type="checkbox" class="section-toggle" data-target=".registration-chart-section" checked> Registration distribution by category</label>
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
    </div>
    <div class="content">
        <div class="total-course-card">
            <div class="card-1 total-registration-section">
                <p class="card-title">Total registration</p>
                <div class="course-stat">
                    <div class="icon bi bi-pencil-square"></div>
                    <div class="course-number">${requestScope.totalRegistration}</div>
                </div>
            </div>
            <div class="card-2 new-registration-section">
                <p class="card-title">New registration</p>
                <div class="course-stat">
                    <div class="icon bi bi-pencil-square"></div>
                    <div class="course-number">${requestScope.newRegistration}</div>
                </div>
            </div>
        </div>
        <div class="course-category-card registration-chart-section">
            <div class="card-3">
                <div class="chart-container" style="width: 63%; margin: auto;">
                    <canvas id="subjectChart"></canvas>
                </div>
            </div>
        </div> 
    </div>
    <div class="course-regis-chart">
        <div class="card-4 best-registration-section">
            <p class="best-seller">Best seller courses</p>
            <div class="best-seller-content" style="font-weight: bold"><span>Name</span><span>Amount</span></div>
            <c:forEach items="${requestScope.bestSeller}" var="b" varStatus="loop">
                <div class="best-seller-content" ><span>${loop.index + 1}. ${b.category}</span><span>${b.count}</span></div>
            </c:forEach>
        </div>
        <div class="registration-status">
            <div class="card-5 success-registration-section">
                <p class="card-title text-success">Success registration</p>
                <div class="course-stat">
                    <div class="icon bi bi-pencil-square"></div>
                    <div class="course-number">${requestScope.successRegistration}</div>
                </div>
            </div>
            <div class="card-5 new-regis cancelled-registration-section">
                <p class="card-title text-dark">Cancelled registration</p>
                <div class="course-stat">
                    <div class="icon bi bi-pencil-square"></div>
                    <div class="course-number">${requestScope.cancelledRegistration}</div>
                </div>
            </div> 
        </div>   
        <div class="card-6 submitted-registration-section">
            <p class="card-title text-warning">Submitted registration</p>
            <div class="course-stat">
                <div class="icon bi bi-pencil-square"></div>
                <div class="course-number">${requestScope.submittedRegistration}</div>
            </div>
        </div>
    </div>

</div>


<script>
    var colors = ['#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0', '#9966FF', '#FF9F40',
            '#FF6384', '#C9CBCF', '#7CFC00', '#008080', '#FF69B4', '#CD5C5C',
            '#40E0D0', '#8A2BE2', '#32CD32', '#FFD700', '#48D1CC', '#FF4500',
            '#00CED1', '#FF1493', '#00FA9A', '#FF6347', '#1E90FF', '#DC143C'];
    document.addEventListener('DOMContentLoaded', function () {
    Chart.register(ChartDataLabels);
    var ctx = document.getElementById('subjectChart').getContext('2d');
    var chart = new Chart(ctx, {
    type: 'pie',
            data: {
            labels: [
    <c:forEach items="${requestScope.registrationAllocation}" var="category" varStatus="status">
            '${category.category}'${!status.last ? ',' : ''}
    </c:forEach>
            ],
                    datasets: [{
                    data: [
    <c:forEach items="${requestScope.registrationAllocation}" var="category" varStatus="status">
        ${category.count}${!status.last ? ',' : ''}
    </c:forEach>
                    ],
                            backgroundColor: colors
                    }]
            },
            options: {
            responsive: true,
                    plugins: {
                    legend: {
                    display: true,
                            position: 'right'
                    },
                            title: {
                            display: true,
                                    text: 'Registration Distribution By Category',
                                    font: {
                                    size: 20
                                    }
                            },
                            datalabels: {

                            color: '#fff',
                                    font: {
                                    weight: 'bold',
                                            size: 8
                                    },
                                    formatter: (value, ctx) => {
                            let datasets = ctx.chart.data.datasets;
                            let sum = datasets[0].data.reduce((a, b) => a + b, 0);
                            let percentage = Math.round((value / sum) * 100) + '%';
                            return ctx.chart.data.labels[ctx.dataIndex] + '\n' + percentage;
                            },
                                    textAlign: 'center'
                            }
                    }
            }
    });
    });
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