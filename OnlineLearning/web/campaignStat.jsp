
        <div class="col-md-9 content-container">
            <div class="checkBox">
                <label><input type="checkbox" class="section-toggle" data-target=".registration-section" checked>Registration</label>
                <label><input type="checkbox" class="section-toggle" data-target=".revenue-section" checked>Revenue</label>
                <label><input type="checkbox" class="section-toggle" data-target=".new-customer-section" checked>New customer</label><br>
                <label><input type="checkbox" class="section-toggle" data-target=".newly-bought-section" checked>Newly bought customer</label>
                <label><input type="checkbox" class="section-toggle" data-target=".revenue-chart-section" checked>Revenue distribution by category</label>
            </div>
            <div class="campaign-info">
                Campaign: ${requestScope.campaign.campaignName}<br>
                Period: ${requestScope.campaign.startDate} to ${requestScope.campaign.endDate}<br>
                Status: ${requestScope.campaign.status}

            </div>
            <c:if test="${requestScope.campaign.status == 'Not Start'}">
                <div class="status-message">This campaign has not yet started</div>
            </c:if>
            <c:if test="${requestScope.campaign.status != 'Not Start'}">
                <div class="content">
                    <div class="total-course-card">
                        <div class="card-1 registration-section">
                            <p class="card-title">Registration</p>
                            <div class="course-stat">
                                <div class="icon bi bi-pencil-square"></div>
                                <div class="course-number">${requestScope.registration}</div>
                            </div>
                        </div>
                        <div class="card-2 revenue-section">
                            <p class="card-title">Revenue</p>
                            <div class="course-stat">
                                <div class="icon bi bi-currency-dollar"></div>
                                <div class="revenue-number">${requestScope.revenue}</div>
                            </div>
                        </div>
                    </div>
                    <div class="course-category-card revenue-chart-section">
                        <div class="card-3">
                            <div class="chart-container" style="width: 63%; margin: auto;">
                                <canvas id="subjectChart"></canvas>
                            </div>
                        </div>
                    </div> 
                </div>
                <div class="course-regis-chart">

                    <div class="">
                        <div class="card-1-campaign new-customer-section">
                            <p class="card-title text-success">New customer</p>
                            <div class="course-stat">
                                <div class="icon bi bi-pencil-square"></div>
                                <div class="course-number">${requestScope.newCustomer}</div>
                            </div>
                        </div>
                    </div>   
                    <div class="card-1-campaign newly-bought-section">
                        <p class="card-title text-dark">Newly bought customer</p>
                        <div class="course-stat">
                            <div class="icon bi bi-pencil-square"></div>
                            <div class="course-number">${requestScope.newlyBought}</div>
                        </div>
                    </div>
                </div>
            </c:if>

        </div>


<script>
    // define chart properties
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
    <c:forEach items="${requestScope.revenueAllocation}" var="revenue" varStatus="status">
            '${revenue.category}'${!status.last ? ',' : ''}
    </c:forEach>
            ],
                    datasets: [{
                    data: [
    <c:forEach items="${requestScope.revenueAllocation}" var="revenue" varStatus="status">
        ${revenue.revenue}${!status.last ? ',' : ''}
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
                                    text: 'Revenue Distribution By Category',
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
</script>