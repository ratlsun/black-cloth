(function (window, angular) {
    "use strict";

    angular.module('module.mocker')
        .controller('mocker.viewer.MainController', [
            '$scope',
            '$state',
            '$stateParams',
            'appConfig',
            function ($scope, $state, $stateParams, appConfig) {

                $scope.mockerId = $stateParams.mid;

                $scope.editRule = function (rule) {
                    $state.go(appConfig.stateName.ruleEditor, {
                        mid: $scope.mockerId,
                        rid: rule.id
                    });
                };
            }
        ]);

})(window, window.angular);