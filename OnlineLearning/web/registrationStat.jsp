<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="col-md-9 content-container">

    <div class="content">
        <div class="total-course-card">
            <div class="card-1">
                <p class="card-title">Total registration</p>
                <div class="course-stat">
                    <div class="icon bi bi-pencil-square"></div>
                    <div class="course-number">${requestScope.totalRegistration}</div>
                </div>
            </div>
            <div class="card-2">
                <p class="card-title">New registration</p>
                <div class="course-stat">
                    <div class="icon bi bi-pencil-square"></div>
                    <div class="course-number">${requestScope.newRegistration}</div>
                </div>
            </div>
        </div>
        <div class="course-category-card">
            <div class="card-3">
                <div class="chart-container" style="width: 63%; margin: auto;">
                    <canvas id="subjectChart"></canvas>
                </div>
            </div>
        </div> 
    </div>
    <div class="course-regis-chart">
        <div class="card-4">
            <p class="best-seller">Best seller courses</p>
            <div class="best-seller-content" style="font-weight: bold"><span>Name</span><span>Amount</span></div>
            <c:forEach items="${requestScope.bestSeller}" var="b" varStatus="loop">
                <div class="best-seller-content" ><span>${loop.index + 1}. ${b.category}</span><span>${b.count}</span></div>
            </c:forEach>
        </div>
        <div class="registration-status">
            <div class="card-5">
                <p class="card-title text-success">Success registration</p>
                <div class="course-stat">
                    <div class="icon bi bi-pencil-square"></div>
                    <div class="course-number">${requestScope.successRegistration}</div>
                </div>
            </div>
            <div class="card-5 new-regis">
                <p class="card-title text-dark">Cancelled registration</p>
                <div class="course-stat">
                    <div class="icon bi bi-pencil-square"></div>
                    <div class="course-number">${requestScope.cancelledRegistration}</div>
                </div>
            </div> 
        </div>   
                <div class="card-6">
                <p class="card-title text-warning">Submitted registration</p>
                <div class="course-stat">
                    <div class="icon bi bi-pencil-square"></div>
                    <div class="course-number">${requestScope.submittedRegistration}</div>
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
                                        text: 'Registraion Distribution By Category',
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
    </script>