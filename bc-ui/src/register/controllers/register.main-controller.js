(function (window, angular) {
    "use strict";

    angular.module('module.register')
        .controller('register.MainController', [
            '$scope',
            '$state',
            '$stateParams',
            'appConfig',
            function ($scope, $state, $stateParams, appConfig) {

                $scope.codeStep = $stateParams.code ? 'code' : '';

                $scope.gotoLogin = function () {
                    $state.go(appConfig.stateName.login);
                };
            }
        ]);

})(window, window.angular);