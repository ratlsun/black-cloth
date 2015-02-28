(function (window, angular) {
    "use strict";

    angular.module('module.widgets.navbar')
        .controller('widgets.navbar.MainController', [
            '$scope',
            '$state',
            'appConfig',
            function ($scope, $state, appConfig) {

                $scope.activeMenu = $state.current.name;

                $scope.gotoHome = function () {
                    $state.go(appConfig.stateName.home);
                };

                $scope.gotoLogin = function () {
                    $state.go(appConfig.stateName.login);
                };
            }
        ]);

})(window, window.angular);