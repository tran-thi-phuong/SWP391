<c:choose>
    <c:when test="${empty sessionScope.user}">
        <c:redirect url="login.jsp"/>
    </c:when>
    <c:when test="${sessionScope.user.role != 'Admin' && sessionScope.user.role != 'Marketing'}">
        <c:redirect url="/Homepage"/>
    </c:when>
    <c:otherwise>
        <div class="col-md-9 content-container">
            <div class="filter-course">
                <form id="toggleForm" method="post" action="courseStat">
                    <div class="checkBox">
                        <label><input type="checkbox" class="section-toggle" data-target=".total-course-section" checked> Total courses</label><br>
                        <label><input type="checkbox" class="section-toggle" data-target=".new-course-section" checked> New courses</label><br>
                        <label><input type="checkbox" class="section-toggle" data-target=".course-chart-section" checked> Subject distribution by category</label>
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
                <p class="error-mess">${requestScope.error}</p>           
            </div>

            <div class="content">
                <div class="total-course-card">
                    <div class="card-1 total-course-section">
                        <p class="card-title">Total courses</p>
                        <div class="course-stat">
                            <div class="icon bi bi-book"></div>
                            <div class="course-number">${requestScope.totalCourse}</div>
                        </div>
                    </div>
                    <div class="card-2 new-course-section">
                        <p class="card-title">New courses</p>
                        <div class="course-stat">
                            <div class="icon bi bi-book"></div>
                            <div class="course-number">${requestScope.newCourse}</div>
                        </div>
                    </div>
                </div>
                <div class="course-category-card course-chart-section">
                    <div class="card-3">
                        <div class="chart-container" style="width: 63%; margin: auto;">
                            <canvas id="subjectChart"></canvas>
                        </div>
                    </div>
                </div> 
            </div>
        </div>
    </c:otherwise>
</c:choose>

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
                    <c:forEach items="${requestScope.subjectAllocation}" var="category" varStatus="status">
                        '${category.category}'${!status.last ? ',' : ''}
                        </c:forEach>
                ],
                datasets: [{
                        data: [
                            <c:forEach items="${requestScope.subjectAllocation}" var="category" varStatus="status">
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
                        text: 'Subject Distribution By Category',
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
