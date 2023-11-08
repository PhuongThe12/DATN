var app = angular.module("app", ["ngRoute", "ui.bootstrap", "ngCookies"]);

app.controller("indexController", function ($scope, $http, $location, $cookies, $window) {
    $scope.addSuccess = function (){
        Swal.fire({
            text: "Bản ghi xem thêm mới vào hệ thống",
            icon: "info",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "Đồng ý",
            cancelButtonText:"Hủy"
        }).then((result) => {
            if (result.isConfirmed) {
                Swal.fire({
                    text: "Đã chọn oke",
                    icon: "success"
                });
            }else{
                Swal.fire({
                    text: "Đã chọn hủy",
                    icon: "error"
                });
            }

        });
    }

    $scope.addError = function (){
        Swal.fire({
            text: "Bản ghi xem thêm mới vào hệ thống",
            icon: "error",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "Đồng ý",
            cancelButtonText:"Hủy"
        }).then((result) => {
            if (result.isConfirmed) {
                Swal.fire({
                    text: "Đã chọn oke",
                    icon: "success"
                });
            }else{
                Swal.fire({
                    text: "Đã chọn hủy",
                    icon: "error"
                });
            }

        });
    }

    $scope.addInfo = function (){
        Swal.fire({
            text: "Bản ghi xem thêm mới vào hệ thống",
            icon: "info",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "Đồng ý",
            cancelButtonText:"Hủy"
        }).then((result) => {
            if (result.isConfirmed) {
                Swal.fire({
                    text: "Đã chọn oke",
                    icon: "success"
                });
            }else{
                Swal.fire({
                    text: "Đã chọn hủy",
                    icon: "error"
                });
            }

        });
    }

    $scope.addWarning = function (){
        Swal.fire({
            text: "Bản ghi xem thêm mới vào hệ thống",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "Đồng ý",
            cancelButtonText:"Hủy"
        }).then((result) => {
            if (result.isConfirmed) {
                Swal.fire({
                    text: "Đã chọn oke",
                    icon: "success"
                });
            }else{
                Swal.fire({
                    text: "Đã chọn hủy",
                    icon: "error"
                });
            }

        });
    }
});