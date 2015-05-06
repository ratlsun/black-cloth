(function (window, angular) {
    "use strict";

    angular.module('module.mocker')
        .controller('mocker.dashboard.MainController', [
            '$scope',
            '$state',
            'appConfig',
            function ($scope, $state, appConfig) {

                $scope.viewMocker = function (mocker) {
                    $state.go(appConfig.stateName.mockerViewer, {
                        mid: mocker.id
                    });
                };

            }
        ]);

})(window, window.angular);