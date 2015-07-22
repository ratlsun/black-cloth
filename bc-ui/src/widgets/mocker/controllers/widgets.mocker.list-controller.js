(function (window, angular) {
    "use strict";

    angular.module('module.widgets.mocker')
        .controller('widgets.mocker.ListController', [
            '$scope',
            'mockerService',
            'alertService',
            function ($scope, mockerService, alertService) {
                mockerService.getMyMockers().then(function(resp){
                    $scope.mockers = resp;
                });
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
                        if (resp.result === 1) {
                            alertService.success(resp.msg);
                            refreshPublicMocker();
                        } else if (resp.result === -1) {
                            alertService.warning("您已经关注过此模拟系统，请核对信息。");
                        } else {
                            alertService.error(resp.errorMsg);
                        }
                    });
                };

                $scope.cancelCollectMocker = function(id){
                    mockerService.cancelCollectMocker(id, 'CancelCollectMocker').then(function(resp){
                        if (resp.result === 1) {
                            alertService.success(resp.msg);
                            refreshPublicMocker();
                        } else {
                            alertService.error(resp.errorMsg);
                        }
                    });
                };
            }
        ]);

})(window, window.angular);