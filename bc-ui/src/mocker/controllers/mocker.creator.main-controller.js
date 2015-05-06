(function (window, angular) {
    "use strict";

    angular.module('module.mocker')
        .controller('mocker.creator.MainController', [
            '$scope',
            '$state',
            'appConfig',
            function ($scope, $state, appConfig) {

                $scope.gotoViewer = function (mocker) {
                    $state.go(appConfig.stateName.mockerViewer, {
                        mid: mocker.id
                    });
                };

            }
        ]);

})(window, window.angular);