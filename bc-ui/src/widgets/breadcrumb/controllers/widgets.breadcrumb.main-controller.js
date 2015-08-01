(function (window, angular) {
    "use strict";

    angular.module('module.widgets.breadcrumb')
        .controller('widgets.breadcrumb.MainController', [
            '$scope',
            '$state',
            '$stateParams',
            'appConfig',
            'mockerService',
            'userService',
            function ($scope, $state, $stateParams, appConfig, mockerService, userService) {
                $scope.user =
                $scope.activeMenu = $state.current.name;
                $scope.mockerMode = {
                    view: false,
                    setting: false,
                    create: false,
                    addRule: false,
                    editRule: false,
                    viewOther: false
                };

                if ($scope.lastItemId) {
                    mockerService.getMockerById($scope.lastItemId).then(function(resp) {
                        $scope.mocker = resp;
                        $scope.mockerMode.viewOther = userService.getCurrentUser().name !== $scope.mocker.owner;
                    });
                }

                if ($scope.activeMenu === appConfig.stateName.mockerViewer) {
                    $scope.mockerMode.view = true;
                } else if ($scope.activeMenu === appConfig.stateName.mockerCreator) {
                    $scope.mockerMode.create = true;
                } else if ($scope.activeMenu === appConfig.stateName.mockerSetting) {
                    $scope.mockerMode.setting = true;
                } else if ($stateParams.rid) {
                    $scope.mockerMode.editRule = true;
                } else {
                    $scope.mockerMode.addRule = true;
                }

                $scope.gotoMockerDashboard = function () {
                    $state.go(appConfig.stateName.mockerDashboard);
                };

                $scope.gotoMockerSearch = function () {
                    $state.go(appConfig.stateName.mockerSearch);
                };

                $scope.gotoMockerSetting = function () {
                    $state.go(appConfig.stateName.mockerSetting, {
                        mid: $scope.mocker.id
                    });
                };

                $scope.gotoMockerViewer = function () {
                    $state.go(appConfig.stateName.mockerViewer, {
                        mid: $scope.mocker.id
                    });
                };

                $scope.gotoRuleEditor = function () {
                    $state.go(appConfig.stateName.ruleEditor, {
                        mid: $scope.mocker.id
                    });
                };
            }
        ]);

})(window, window.angular);