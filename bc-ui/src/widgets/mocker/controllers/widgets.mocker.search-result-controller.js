(function (window, angular) {
    "use strict";

    angular.module('module.widgets.mocker')
        .controller('widgets.mocker.SearchResultController', [
            '$scope',
            'mockerService',
            'userService',
            'alertService',
            function ($scope, mockerService, userService, alertService) {

                $scope.publicMockers = [];

                var refreshPublicMocker = function () {
                    mockerService.getWatchedMockers().then(function(resp){
                        var watchedMockers = resp;
                        mockerService.getPublicMockers().then(function(resp){
                            _.forEach(resp, function(mocker) {
                                mocker.isCollect = !!_.find(watchedMockers, {'id': mocker.id})
                            });
                            $scope.publicMockers = resp;
                        });
                    });
                };
                refreshPublicMocker();

                $scope.collectMocker = function(id){
                    mockerService.collectMocker(id, 'CollectMocker').then(function(resp){
                        if (resp) {
                            if (resp.result < 0) {
                                alertService.error(appConfig.alertMsg.mockerModule[resp.result.toString()]);
                            } else{
                                alertService.success('成功关注此模拟系统');
                                refreshPublicMocker();
                            };
                        } else{
                            alertService.error(appConfig.alertMsg.mockerModule['-15']);
                        };
                    });
                };

                $scope.cancelCollectMocker = function(id){
                    mockerService.cancelCollectMocker(id, 'CancelCollectMocker').then(function(resp){
                        if (resp) {
                            if (resp.result < 0) {
                                alertService.error(appConfig.alertMsg.mockerModule[resp.result.toString()]);
                            } else{
                                alertService.success('成功取消关注此模拟系统');
                                refreshPublicMocker();
                            };
                        } else{
                            alertService.error(appConfig.alertMsg.mockerModule['-15']);
                        };
                    });
                };
            }
        ]);

})(window, window.angular);