(function (window, angular) {
    "use strict";

    angular.module('module.password-admin')
        .controller('password-admin.MainController', [
            '$scope',
            '$state',
            '$stateParams',
            'appConfig',
            function ($scope, $state, $stateParams, appConfig) {

                $scope.pwdCode = $stateParams.r;

                $scope.gotoLogin = function () {
                    $state.go(appConfig.stateName.login);
                };
            }
        ]);

})(window, window.angular);