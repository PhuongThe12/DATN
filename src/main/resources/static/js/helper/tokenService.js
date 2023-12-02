angular.module('tokenService', ['ngCookies'])
    .factory('TokenManager', ['$cookies', function($cookies) {
        return {
            setToken: function(token, expiryDays) {
                const expiryDate = new Date();
                expiryDate.setDate(expiryDate.getDate() + expiryDays);
                $cookies.put('token', token, { expires: expiryDate });
            },
            getToken: function() {
                return $cookies.get('token');
            }
        };
    }]);