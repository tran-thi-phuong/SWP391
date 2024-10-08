<div class="col-md-9 content-container">
    <div>
        <form id="toggleForm">
        <label><input type="checkbox" class="section-toggle" data-target=".total-course-section" checked> Total registration</label>
        <label><input type="checkbox" class="section-toggle" data-target=".new-course-section" checked> New registration</label>
        <label><input type="checkbox" class="section-toggle" data-target=".course-chart-section" checked> Subject distribution by category</label>
    </form>
    </div>

    <div class="content">
        <div class="total-course-card">
            <div class="card-1 total-course-section">
                <p class="card-title">Total courses</p>
                <div class="course-stat">
                    <div class="icon bi bi-book"></div>
                    <div class="course-number">${requestScope.totalCourse}</div>
                </div>
<!--                    <div class="inactive-course">${requestScope.inactiveCourse} courses inactive</div>-->
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
            <div class="card-3 ">
                <div class="chart-container" style="width: 63%; margin: auto;">
                    <canvas id="subjectChart"></canvas>
                </div>
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

        // Checkbox section toggle logic
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
</script>
