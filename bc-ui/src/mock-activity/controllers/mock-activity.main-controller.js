(function (window, angular) {
    "use strict";

    angular.module('module.mock-activity')
        .controller('mock-activity.MainController', [
            '$scope',
            '$state',
            '$stateParams',
            '$interval',
            'mockActivityService',
            'alertService',
            'pageService',
            'appConfig',
            function ($scope, $state, $stateParams, $interval,
                      mockActivityService, alertService, pageService, appConfig) {

                $scope.selectedMockers = [];
                $scope.disableSelectMocker = false;
                $scope.hits = [];

                mockActivityService.getMyMockActivity().then(function(resp){
                    $scope.activity = resp;
                    if (resp) {
                        $scope.selectedMockers = resp.mockerIds;
                        pageService.mask('hits-list-spinner');
                        $scope.hitsFetching = $interval(function(){
                            mockActivityService.getMockHitByActivity($scope.activity).then(function(hitsResp) {
                                $scope.hits = hitsResp;
                                pageService.unmask('hits-list-spinner');
                            });
                        }, 5000);
                    }
                });

                $scope.$on('$destroy', function() {
                    $interval.cancel($scope.hitsFetching);
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
                            alertService.success('模拟环境暂停成功，暂停期间可以修改设置，恢复模拟后生效。');
                            $scope.activity = resp;
                        } else {
                            alertService.error('模拟环境暂停失败，请检查模拟系统的设置是否正确！');
                        }
                    });
                };

                $scope.resumeMock = function () {
                    if($scope.selectedMockers.length > 0) {
                        $scope.activity.mockerIds = $scope.selectedMockers;
                        mockActivityService.resumeMockActivity($scope.activity).then(function(resp){
                            if (resp) {
                                alertService.success('模拟环境恢复运行成功，可以进行模拟。');
                                $scope.activity = resp;
                            } else {
                                alertService.error('模拟环境恢复失败，请检查模拟系统的设置是否正确！');
                            }
                        });
                    } else {
                        alertService.warning('请先选择需要模拟的场景再点击恢复！');
                    }
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

                $scope.clearLog = function () {
                    if ($scope.hits.length > 0) {
                        $scope.hits = [];
                        mockActivityService.clearMockHitByActivity($scope.activity).then(function(resp){
                            if (resp) {
                                alertService.success('匹配日志清空成功。');
                            } else {
                                alertService.error('匹配日志清空失败。');
                            }
                        });
                    } else {
                        alertService.warning('尚未有匹配日志！');
                    }
                };
            }
        ]);

})(window, window.angular);