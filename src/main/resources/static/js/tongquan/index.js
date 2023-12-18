var app = angular.module("app", ["ngRoute", "ui.bootstrap", "ngCookies"]);

app.config(function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');
    $routeProvider
        .when("/home", {
            templateUrl: '/pages/admin/tongquan/views/home.html',
            controller: 'homeController'
        }).otherwise({redirectTo: '/home'});
});

app.controller("homeController", function ($scope, $http, $location, $cookies) {

    $scope.optionChanged = function () {

        var parts = $scope.selectedDate.split('/');
        var formattedDate = parts[2] + '-' + parts[1] + '-' + parts[0];
        filterDate(formattedDate);
    };


    $scope.stringDate1 = '2023-12-17';
    $scope.stringDate2 = '2023-12-18';
    $scope.year = "2023";
    getDataByYear($scope.year);
    // getData($scope.stringDate1, $scope.stringDate2);
    filterDate($scope.stringDate2);

    function filterDate(ngay1) {
        $http.get(host + '/rest/admin/thong-ke/tong-quan?ngay1=' + ngay1)
            .then(function (response) {
                $scope.tongSanPham = response.data.tongSanPham;
                $scope.tongYeuCau = response.data.tongYeuCau;
                $scope.tongDoanhThu = response.data.tongDoanhThu;
                $scope.tongHoaDon = response.data.tongHoaDon;
            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu thất bại");
            });
    }


    $http.get(host + '/rest/admin/thong-ke/hang_khach_hang')
        .then(function (response) {
            const xArray1 = response.data.map(item => item.ten);
            const yArray1 = response.data.map(item => item.tongDoanhThu);
            const layout1 = {title: "Thống kê doanh thu theo hạng khách hàng"};
            const dataCustomer = [{labels: xArray1, values: yArray1, type: "pie"}];
            Plotly.newPlot("myPlot1", dataCustomer, layout1);
        })
        .catch(function (error) {
            toastr["error"]("Lấy dữ liệu thất bại");
        });

    $http.get(host + '/rest/admin/thong-ke/thuong_hieu')
        .then(function (response) {
            const xArray = response.data.map(item => item.ten);
            const yArray = response.data.map(item => item.tongDoanhThu);
            const layout = {title: "Thống kê doanh thu theo thương hiệu"};
            const dataBrand = [{labels: xArray, values: yArray, type: "pie"}];
            Plotly.newPlot("myPlot", dataBrand, layout);
        })
        .catch(function (error) {
            toastr["error"]("Lấy dữ liệu thất bại");
        });

    $http.get(host + '/rest/admin/thong-ke/top-ban-chay')
        .then(function (response) {
            const labels = response.data.map(item => item.ten);
            const datas = response.data.map(item => item.tongDoanhThu);

            const chartProduct = document.getElementById('myChart2');
            const data1 = {
                labels: labels,
                datasets:
                    [{
                        label: 'Số lượng bán',
                        data: datas,
                        backgroundColor: [
                            'rgba(54, 162, 235, 0.6)',
                        ],
                        borderColor: [
                            'rgb(54, 162, 235)'
                        ],
                        borderWidth: 1
                    }]
            };
            new Chart(chartProduct, {
                type: 'bar',
                data: data1,
                options: {
                    indexAxis: 'y',
                    scales: {
                        y: {
                            ticks: {
                                align: 'end', // Căn chữ về bên trái
                                font: {
                                    size: 11 // Đặt kích thước font chữ
                                }
                            }
                        }
                    }
                }
            });
        })
        .catch(function (error) {
            toastr["error"]("Lấy dữ liệu thất bại");
        });

    function getDataByYear(year) {
        $http.get(host + '/rest/admin/thong-ke/doanh-thu-theo-nam?year=' + year)
            .then(function (response) {
                const labels = response.data.map(item => item.ten);
                const datas = response.data.map(item => item.tongDoanhThu);
                const ctx = document.getElementById('myChart');
                const data = {
                    labels: labels,
                    datasets:
                        [{
                            label: 'Tổng doanh thu',
                            data: datas,
                            backgroundColor: [
                                'rgba(255, 99, 132, 0.2)',
                                'rgba(255, 159, 64, 0.2)',
                                'rgba(255, 205, 86, 0.2)',
                                'rgba(75, 192, 192, 0.2)',
                                'rgba(54, 162, 235, 0.2)',
                                'rgba(153, 102, 255, 0.2)',
                                'rgba(201, 203, 207, 0.2)',
                                'rgba(255, 192, 203, 0.2)',
                                'rgba(128, 128, 128, 0.2)',
                                'rgba(165, 42, 42, 0.2)',
                                'rgba(0, 255, 0, 0.2)',
                                'rgba(0, 255, 0, 0.6)'
                            ],
                            borderColor: [
                                'rgb(255, 99, 132)',
                                'rgb(255, 159, 64)',
                                'rgb(255, 205, 86)',
                                'rgb(75, 192, 192)',
                                'rgb(54, 162, 235)',
                                'rgb(153, 102, 255)',
                                'rgb(201, 203, 207)',
                                'rgba(255, 192, 203)',
                                'rgba(128, 128, 128)',
                                'rgba(165, 42, 42)',
                                'rgba(0, 255, 0)',
                                'rgba(0, 255, 0)'
                            ],
                            borderWidth: 1
                        }]
                };

                new Chart(ctx, {
                    type: 'bar',
                    data: data,
                });

            })
            .catch(function (error) {
                toastr["error"]("Lấy dữ liệu thất bại");
            });
    }


    $scope.dates = [];
    // Lấy ngày tháng hiện tại
    var currentDate = new Date();
    var daysInMonth = new Date(currentDate.getFullYear(), currentDate.getMonth() + 1, 0).getDate();

    // Tạo các option cho select
    for (var i = 1; i <= daysInMonth; i++) {
        var date = new Date(currentDate.getFullYear(), currentDate.getMonth(), i);
        $scope.dates.push({
            value: date.toLocaleDateString('vi-VN', {year: 'numeric', month: 'numeric', day: 'numeric'}), // Giá trị có thể sử dụng cho việc xử lý dữ liệu
            display: date.toLocaleDateString('vi-VN', {year: 'numeric', month: 'numeric', day: 'numeric'}) // Hiển thị cho người dùng
        });
    }

    $scope.selectedDate = new Date().toLocaleDateString('vi-VN', {year: 'numeric', month: 'numeric', day: 'numeric'});


})