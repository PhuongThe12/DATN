var app = angular.module("app", ["ngRoute", "ui.bootstrap"]);
app.config(function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');
    $routeProvider
        .when("/list", {
            templateUrl: '/pages/customer/yeucau/views/list.html', controller: 'yeuCauListController'
        }).when("/detail/:id", {
        templateUrl: '/pages/user/yeucau/views/detail.html', controller: 'detailYeuCauController'
        }).when("/update/:id", {
        templateUrl: '/pages/user/yeucau/views/update.html', controller: 'updateYeuCauController'
        }).when("/add/:id", {
        templateUrl: '/pages/user/yeucau/views/add.html', controller: 'addYeuCauController'
        }).otherwise({redirectTo: '/list'});
});

app.controller("yeuCauListController", function ($scope, $http, $window, $location) {

});