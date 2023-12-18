app.factory('httpInterceptor', function ($q, $location) {
    return {
        'responseError': function (rejection) {
            if (rejection.status === 401 || rejection.status === 403) {
                // window.location.href = feHost + '/login.html';
            }
            return $q.reject(rejection);
        }
    };
});

app.config(function ($httpProvider) {
    $httpProvider.interceptors.push('httpInterceptor');
});

app.controller('navbarAdminController', function ($rootScope, $scope, $http, $location, $window, $cookies) {
    $scope.nhanVienSession = {};
    var token = $cookies.get('token');
    if (token) {
        $http.get(host + "/session/get-staff")
            .then(response => {
                $scope.nhanVienSession = response.data;
            })
    } else {
        $window.location.href = '/login';
    }


    $scope.logOutNhanVien = function () {
        Swal.fire({
            text: "Xác nhận đăng xuất?",
            icon: "info",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "Đồng ý",
            cancelButtonText: "Hủy"
        }).then((result) => {
            if (result.isConfirmed) {
                setTokenCookie(null, null);
                $window.localStorage.clear();
                $window.location.href = '/login';
            }
        });
    }

    function setTokenCookie(token, expiryDays) {
        const d = new Date();
        d.setTime(d.getTime() + (expiryDays * 24 * 60 * 60 * 1000));
        const expires = "expires=" + d.toUTCString();
        document.cookie = `token=${token}; ${expires}; path=/`;
    }

    $scope.deleteCookie = function(cookieName) {
        $cookies.remove(cookieName);
    };

});


app.controller("navbarStaffController", function ($scope, $http, $location, $cookies, $rootScope,$window){
    $scope.nhanVienBanHangSession = {};
    var token = $cookies.get('token');
    if (token) {
        $http.get(host + "/session/get-staff")
            .then(response => {
                $scope.nhanVienBanHangSession = response.data;
            })
    } else {
        $window.location.href = '/login';
    }

    $scope.logOutNhanVien = function () {
        Swal.fire({
            text: "Xác nhận đăng xuất?",
            icon: "info",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "Đồng ý",
            cancelButtonText: "Hủy"
        }).then((result) => {
            if (result.isConfirmed) {
                setTokenCookie(null, null);
                $window.localStorage.clear();
                $window.location.href = '/login';
            }
        });
    }

    function setTokenCookie(token, expiryDays) {
        const d = new Date();
        d.setTime(d.getTime() + (expiryDays * 24 * 60 * 60 * 1000));
        const expires = "expires=" + d.toUTCString();
        document.cookie = `token=${token}; ${expires}; path=/`;
    }
});
function getTokenFromCookie() {
    const name = "token=";
    const decodedCookie = decodeURIComponent(document.cookie);
    const cookieArray = decodedCookie.split(';');

    for (let i = 0; i < cookieArray.length; i++) {
        let cookie = cookieArray[i];
        while (cookie.charAt(0) === ' ') {
            cookie = cookie.substring(1);
        }
        if (cookie.indexOf(name) === 0) {
            return cookie.substring(name.length, cookie.length);
        }
    }
    return "";
}


const headers = {
    'Authorization': 'Bearer ' + getTokenFromCookie(),
    'Content-Type': 'application/json',
    'ShopId': 189641,
    'Token': 'ecf11c4a-5d20-11ee-b1d4-92b443b7a897'
};

app.factory('headerInterceptor', function () {
    return {
        request: function (config) {
            angular.extend(config.headers, headers);
            return config;
        }
    };
});


app.config(function ($httpProvider) {
    $httpProvider.interceptors.push('headerInterceptor');
});
