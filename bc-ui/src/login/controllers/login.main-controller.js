(function (window, angular) {
    "use strict";

    angular.module('module.login')
        .controller('login.MainController', [
            '$scope',
            '$state',
            'appConfig',
            function ($scope, $state, appConfig) {

                $scope.gotoRegister = function () {
                    $state.go(appConfig.stateName.register);
                };

                $scope.gotoPasswordAdmin = function () {
                    $state.go(appConfig.stateName.passwordAdmin);
                };

                $scope.gotoHome = function () {
                    $state.go(appConfig.stateName.mockerDashboard);
                };

            }
        ]);

})(window, window.angular);