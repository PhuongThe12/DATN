
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
    'Authorization': 'Bearer '+getTokenFromCookie(),
    'Content-Type': 'application/json',
    'ShopId' : 189641,
    'Token' : 'ecf11c4a-5d20-11ee-b1d4-92b443b7a897'
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
