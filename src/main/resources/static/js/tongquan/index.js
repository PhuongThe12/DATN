var app = angular.module("app", ["ngRoute", "ui.bootstrap", "ngCookies"]);

app.config(function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');
    $routeProvider
        .when("/home", {
            templateUrl: '/pages/admin/tongquan/views/home.html',
            controller: 'homeController'
        }).otherwise({redirectTo: '/home'});
});

app.controller("indexController", function ($scope, $http, $location, $cookies) {
    $http.get(host + '/admin/rest/nhan-vien/check-logged')
        .then(function (response) {
            if (response.status == 200) {
                $scope.nhanVienLogged = response.data;
                if ($scope.nhanVienLogged.chucVu == 2) {
                    $scope.chuCuaHangLogged = true;
                } else if ($scope.nhanVienLogged.chucVu == 1) {
                    $scope.chuCuaHangLogged = false;
                }
            }
        }).catch(function (error) {
        toastr["error"]("Không tìm thấy người dùng , vui lòng đăng nhập lại !");
    });
});

app.controller("homeController", function ($scope, $http, $location, $cookies) {
    $http.get(host + '/admin/rest/nhan-vien/logger')
        .then(function (response) {
            $scope.listHanhDong = response.data;
        }).catch(function (error) {
        toastr["error"]("Lấy dữ liệu thất bại");
        $location.path("/home");
    });
    // Chọn ngày hiện tại làm mặc định

    $scope.dates = [];

    // Lấy ngày tháng hiện tại
    var currentDate = new Date();
    var daysInMonth = new Date(currentDate.getFullYear(), currentDate.getMonth() + 1, 0).getDate();

    // Tạo các option cho select
    for (var i = 1; i <= daysInMonth; i++) {
        var date = new Date(currentDate.getFullYear(), currentDate.getMonth(), i);
        $scope.dates.push({
            value: date.toLocaleDateString('vi-VN', { year: 'numeric', month: 'numeric', day: 'numeric' }), // Giá trị có thể sử dụng cho việc xử lý dữ liệu
            display: date.toLocaleDateString('vi-VN', { year: 'numeric', month: 'numeric', day: 'numeric' }) // Hiển thị cho người dùng
        });
    }

    $scope.selectedDate = new Date().toLocaleDateString('vi-VN', { year: 'numeric', month: 'numeric', day: 'numeric' });
    console.log($scope.dates, $scope.selectedDate)


    const ctx = document.getElementById('myChart');

    const data = {
        labels: ['Tháng 1', 'Tháng 2', 'Tháng 3', 'Tháng 4', 'Tháng 5', 'Tháng 6', 'Tháng 7', 'Tháng 8', 'Tháng 9', 'Tháng 10', 'Tháng 11', 'Tháng 12'],
        datasets:
            [{
                label: 'Tổng doanh thu',
                data: [65, 59, 80, 120, 56, 55, 40, 35, 30, 20, 21, 15],
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

    const chartProduct = document.getElementById('myChart2');

    const data1 = {
        labels: ['Sản phẩm 1', 'Sản phẩm 2', 'Sản phẩm 3', 'Sản phẩm 4', 'Sản phẩm 5', 'Sản phẩm 6', 'Sản phẩm 7', 'Sản phẩm 8', 'Sản phẩm 9', 'Sản phẩm 10'],
        datasets:
            [{
                label: 'Số lượng bán',
                data: [65, 59, 80, 81, 56, 55, 40, 35, 30, 20],
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
        }
    });

    const xArray = ["Converse", "Puma", "Nike", "Adidas"];
    const yArray = [30, 25, 25, 20];
    const layout = {title: "Thống kê doanh thu theo thương hiệu"};
    const dataBrand = [{labels: xArray, values: yArray, type: "pie"}];
    Plotly.newPlot("myPlot", dataBrand, layout);

    const xArray1 = ["VIP_1", "VIP_2", "VIP_3"];
    const yArray1 = [30, 50, 20];
    const layout1 = {title: "Thống kê doanh thu theo hạng khách hàng"};
    const dataCustomer = [{labels: xArray1, values: yArray1, type: "pie"}];
    Plotly.newPlot("myPlot1", dataCustomer, layout1);
})