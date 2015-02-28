(function (window, angular) {
    "use strict";

    angular.module('module.widgets.user')
        .controller('widgets.user.LoginController', [
            '$scope',
            'Restangular',
            'appConfig',
            function ($scope, Restangular, appConfig) {

                $scope.user = {};

                $scope.login = function () {
                    Restangular.all('login')
                        .customPOST($.param($scope.user), '', {}, {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'});
                };
            }
        ]);

})(window, window.angular);