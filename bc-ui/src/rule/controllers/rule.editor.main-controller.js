(function (window, angular) {
    "use strict";

    angular.module('module.rule')
        .controller('rule.editor.MainController', [
            '$scope',
            '$state',
            '$stateParams',
            'appConfig',
            function ($scope, $state, $stateParams, appConfig) {

                $scope.mockerId = $stateParams.mid;
                $scope.ruleId = $stateParams.rid;

                $scope.gotoMockerViewer = function () {
                    $state.go(appConfig.stateName.mockerViewer, {
                        mid: $scope.mockerId
                    });
                };

            }
        ]);

})(window, window.angular);