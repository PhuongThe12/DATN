app.controller('giayListController', function ($scope, $http) {

    $scope.giays = [];

    $http.post(host + '/admin/rest/giay/get-all-giay')
        .then(function (response) {
            $scope.giays = response.data.content;
            $scope.numOfPages = response.data.totalPages;
        })
        .catch(function (error) {
            console.log(error);
            toastr["error"]("Lấy dữ liệu thất bại");
            // window.location.href = feHost + '/trang-chu';
        });

    $scope.showVarients = {};
    $scope.toggleVarients = function (index) {
        $scope.showVarients[index] = !$scope.showVarients[index];
        console.log(index, $scope.showVarients[index]);
    }

});
