(function (window, angular) {
    "use strict";

    angular.module('module.widgets.mocker')
        .controller('widgets.mocker.ListController', [
            '$scope',
            'mockerService',
            'alertService',
            function ($scope, mockerService, alertService) {

                var refreshList = function(){
                    if ($scope.mockerGroup === 'owner') {
                        mockerService.getMyMockers().then(function(resp){
                            $scope.mockers = resp;
                        });
                    } else {  //watched
                        mockerService.getWatchedMockers().then(function(resp){
                            $scope.mockers = resp;
                        });
                    }
                };
                refreshList();

                $scope.unwatch = function(mocker){
                    mockerService.unwatchMocker(mocker.id).then(function(resp){
                        if (resp) {
                            alertService.success('取消关注成功。');
                            refreshList();
                        }
                    });
                };
            }
        ]);

})(window, window.angular);