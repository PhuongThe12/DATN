var app = angular.module("app", ["ngRoute", "ui.bootstrap", "ngCookies"]);

app.config(function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');
    $routeProvider
        .when("/gioi-thieu/", {
            templateUrl: '/pages/chinhsach/views/gioithieu.html', controller: 'gioiThieuController'
        }).when("/bao-hanh", {
        templateUrl: '/pages/chinhsach/views/baohanh.html', controller: 'baoHanhController'
        }).when("/bao-mat", {
        templateUrl: '/pages/chinhsach/views/baomat.html', controller: 'baoMatController'
        }).when("/doi-tra", {
        templateUrl: '/pages/chinhsach/views/doitra.html', controller: 'doiTraController'
        }).when("/hinh-thuc-thanh-toan", {
        templateUrl: '/pages/chinhsach/views/hinhthucthanhtoan.html', controller: 'hinhThucThanhToanController'
        }).when("/lien-he", {
        templateUrl: '/pages/chinhsach/views/lienhe.html', controller: 'lienHeController'
        })
        .otherwise({redirectTo: '/gioi-thieu'});
});