(function (window, angular) {
    "use strict";

    angular.module('module.mock-activity')
        .controller('mock-activity.MainController', [
            '$scope',
            '$state',
            '$stateParams',
            'mockActivityService',
            'alertService',
            'appConfig',
            function ($scope, $state, $stateParams, mockActivityService, alertService, appConfig) {

                $scope.selectedMockers = [];
                $scope.disableSelectMocker = false;

                mockActivityService.getMyMockActivity().then(function(resp){
                    $scope.activity = resp;
                    $scope.selectedMockers = resp.mockerIds;
                });

                $scope.$watch('activity', function (nv) {
                    if (nv) {
                        $scope.disableSelectMocker = nv.status === 'Running';
                    }
                });

                $scope.gotoMockerViewer = function (mocker) {
                    $state.go(appConfig.stateName.mockerViewer, {
                        mid: mocker.id
                    });
                };

                $scope.startMock = function () {
                    console.log('start ' + $scope.selectedMockers);
                    if($scope.selectedMockers.length > 0) {
                        mockActivityService.startMockActivity({mockerIds: $scope.selectedMockers}).then(function(resp){
                            if (resp) {
                                alertService.success('模拟环境启动成功，可以进行模拟。');
                                $scope.activity = resp;
                            } else {
                                alertService.error('模拟环境启动失败，请检查模拟系统的设置是否正确！');
                            }
                        });
                    } else {
                        alertService.warning('请先选择需要模拟的场景再点击开始！');
                    }
                };

                $scope.pauseMock = function () {
                    mockActivityService.pauseMockActivity($scope.activity).then(function(resp){
                        if (resp) {
                            alertService.success('模拟环境暂停成功，暂停期间可以修改模拟系统的设置，恢复模拟后生效。');
                            $scope.activity = resp;
                        } else {
                            alertService.error('模拟环境暂停失败，请检查模拟系统的设置是否正确！');
                        }
                    });
                };

                $scope.resumeMock = function () {
                    $scope.activity.mockerIds = $scope.selectedMockers;
                    mockActivityService.resumeMockActivity($scope.activity).then(function(resp){
                        if (resp) {
                            alertService.success('模拟环境恢复运行成功，可以进行模拟。');
                            $scope.activity = resp;
                        } else {
                            alertService.error('模拟环境恢复失败，请检查模拟系统的设置是否正确！');
                        }
                    });
                };

                $scope.stopMock = function () {
                    mockActivityService.stopMockActivity($scope.activity).then(function(resp){
                        if (resp) {
                            alertService.success('模拟环境停止运行成功。');
                            $scope.activity = resp;
                        } else {
                            alertService.error('模拟环境停止运行失败，请检查模拟系统的设置是否正确！');
                        }
                    });
                };
            }
        ]);

})(window, window.angular);