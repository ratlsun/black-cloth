(function (window, angular) {
    "use strict";

    angular.module('module.mock-activity')
        .controller('mock-activity.MainController', [
            '$scope',
            '$state',
            '$stateParams',
            'appConfig',
            function ($scope, $state, $stateParams, appConfig) {

                $scope.gotoMockerViewer = function (mocker) {
                    $state.go(appConfig.stateName.mockerViewer, {
                        mid: mocker.id
                    });
                };

            }
        ]);

})(window, window.angular);