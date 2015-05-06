(function (window, angular) {
    "use strict";

    angular.module('module.mocker')
        .controller('mocker.setting.MainController', [
            '$scope',
            '$state',
            '$stateParams',
            'appConfig',
            function ($scope, $state, $stateParams, appConfig) {

                $scope.mockerId = $stateParams.mid;

                $scope.gotoDashboard = function () {
                    $state.go(appConfig.stateName.mockerDashboard);
                };

                $scope.gotoViewer = function (mocker) {
                    $state.go(appConfig.stateName.mockerViewer, {
                        mid: mocker.id
                    });
                };
            }
        ]);

})(window, window.angular);