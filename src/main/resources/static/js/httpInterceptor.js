
app.factory('httpInterceptor', function ($q, $location) {
    return {
        'responseError': function (rejection) {
            if (rejection.status === 401 || rejection.status === 403) {
                window.location.href = feHost + '/login.html';
            }
            return $q.reject(rejection);
        }
    };
});

app.config(function ($httpProvider) {
    $httpProvider.interceptors.push('httpInterceptor');
});

//Cấu hình Spring Security
// var username = 'admin';
// var password = 'admin';
const headers = {
    // Authorization: 'Basic ' + btoa(username + ':' + password),
    'Content-Type': 'application/json'
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
