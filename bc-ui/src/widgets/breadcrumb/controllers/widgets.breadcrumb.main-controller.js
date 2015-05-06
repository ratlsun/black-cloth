(function (window, angular) {
    "use strict";

    angular.module('module.widgets.breadcrumb')
        .controller('widgets.breadcrumb.MainController', [
            '$scope',
            '$state',
            'appConfig',
            'mockerService',
            function ($scope, $state, appConfig, mockerService) {

                $scope.activeMenu = $state.current.name;
                $scope.mockerMode = {
                    view: false,
                    setting: false
                };

                mockerService.getMockerById($scope.lastItemId).then(function(resp){
                    $scope.mocker = resp;
                });

                if ($scope.activeMenu === appConfig.stateName.mockerViewer) {
                    $scope.mockerMode.view = true;
                } else {
                    $scope.mockerMode.setting = true;
                }

                $scope.gotoMockerDashboard = function () {
                    $state.go(appConfig.stateName.mockerDashboard);
                };

                $scope.gotoMockerSetting = function (mocker) {
                    $state.go(appConfig.stateName.mockerSetting, {
                        mid: mocker.id
                    });
                };

                $scope.gotoMockerViewer = function (mocker) {
                    $state.go(appConfig.stateName.mockerViewer, {
                        mid: mocker.id
                    });
                };

                $scope.gotoRuleCreator = function () {
                    $state.go(appConfig.stateName.mockerCreator);
                };
            }
        ]);

})(window, window.angular);