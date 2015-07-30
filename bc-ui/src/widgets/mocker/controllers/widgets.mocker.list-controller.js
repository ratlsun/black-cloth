(function (window, angular) {
    "use strict";

    angular.module('module.widgets.mocker')
        .controller('widgets.mocker.ListController', [
            '$scope',
            'mockerService',
            'alertService',
            'appConfig',
            function ($scope, mockerService, alertService, appConfig) {

                mockerService.getMyMockers().then(function(resp){
                    $scope.mockers = resp;
                });

                var refreshCollectMocker = function () {
                    $scope.collectMockers = [];
                    mockerService.getWatchedMockers().then(function(resp){
                        $scope.collectMockers = resp;
                    });
                };
                refreshCollectMocker();

                $scope.cancelCollectMocker = function(id){
                    mockerService.cancelCollectMocker(id, 'CancelCollectMocker').then(function(resp){
                        if (resp) {
                            if (resp.result < 0) {
                                alertService.error(appConfig.alertMsg.mockerModule[resp.result.toString()]);
                            } else{
                                alertService.success('成功取消关注此模拟系统');
                                refreshCollectMocker();
                            };
                        } else{
                            alertService.error(appConfig.alertMsg.mockerModule['-15']);
                        };
                    });
                };
            }
        ]);

})(window, window.angular);