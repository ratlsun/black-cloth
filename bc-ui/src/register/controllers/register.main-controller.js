(function (window, angular) {
    "use strict";

    angular.module('module.register')
        .controller('register.MainController', [
            '$scope',
            '$state',
            function ($scope, $state) {

                $scope.gotoLogin = function () {
                    $state.go(appConfig.stateName.login);
                };
            }
        ]);

})(window, window.angular);