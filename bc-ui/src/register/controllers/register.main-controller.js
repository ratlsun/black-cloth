(function (window, angular) {
    "use strict";

    angular.module('module.register')
        .controller('register.MainController', [
            '$scope',
            '$state',
            'appConfig',
            function ($scope, $state, appConfig) {

                $scope.gotoLogin = function () {
                    $state.go(appConfig.stateName.login);
                };
            }
        ]);

})(window, window.angular);