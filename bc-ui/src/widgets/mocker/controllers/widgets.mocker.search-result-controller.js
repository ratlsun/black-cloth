(function (window, angular) {
    "use strict";

    angular.module('module.widgets.mocker')
        .controller('widgets.mocker.SearchResultController', [
            '$scope',
            'mockerService',
            'userService',
            'alertService',
            function ($scope, mockerService, userService, alertService) {

                $scope.currentUser = userService.getCurrentUser();
                $scope.publicMockers = [];

                var refreshPublicMocker = function () {
                    mockerService.getWatchedMockers().then(function(resp){
                        var watchedMockers = resp;
                        mockerService.getPublicMockers().then(function(resp){
                            _.forEach(resp, function(mocker) {
                                mocker.watched = !!_.find(watchedMockers, {'id': mocker.id})
                            });
                            $scope.publicMockers = resp;
                        });
                    });
                };
                refreshPublicMocker();

                $scope.watch = function(mocker){
                    mockerService.watchMocker(mocker.id).then(function(resp){
                        if (resp) {
                            alertService.success('关注成功。');
                            mocker.watched = true;
                            mocker.watcherCount++;
                        }
                    });
                };

                $scope.unwatch = function(mocker){
                    mockerService.unwatchMocker(mocker.id).then(function(resp){
                        if (resp) {
                            alertService.success('取消关注成功。');
                            mocker.watched = false;
                            mocker.watcherCount--;
                        }
                    });
                };
            }
        ]);

})(window, window.angular);