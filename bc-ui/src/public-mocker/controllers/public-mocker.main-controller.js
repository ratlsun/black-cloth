(function (window, angular) {
    "use strict";

    angular.module('module.public-mocker')
        .controller('public-mocker.MainController', [
            '$scope',
            '$state',
            'mockerService',
            'userService',
            'alertService',
            'appConfig',
            function ($scope, $state, mockerService, userService, alertService, appConfig) {
                var refreshPublicMocker = function () {
                    $scope.publicMockers = [];
                    mockerService.getCollect().then(function(resp){
                        var collectMockers = resp;
                        mockerService.getByPublic().then(function(resp){
                            _.forEach(resp, function(mocker) {
                                if (_.find(collectMockers, { 'id': mocker.id})) {
                                    mocker.isCollect = true;
                                } else{
                                    mocker.isCollect = false;
                                };
                                $scope.publicMockers.push(mocker);
                            });
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
                $scope.viewMocker = function (mid) {
                    $state.go(appConfig.stateName.mockerViewer, {
                        mid: mid
                    });
                };
            }
        ]);

})(window, window.angular);