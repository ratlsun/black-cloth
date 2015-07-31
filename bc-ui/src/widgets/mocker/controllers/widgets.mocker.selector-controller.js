(function (window, angular) {
    "use strict";

    angular.module('module.widgets.mocker')
        .controller('widgets.mocker.SelectorController', [
            '$scope',
            'mockerService',
            'alertService',
            function ($scope, mockerService, alertService) {

                $scope.selected = {};

                mockerService.getMyMockers().then(function(myResp){
                    var myMockers = myResp;
                    mockerService.getWatchedMockers().then(function(watchedResp){
                        $scope.mockers = _.union(myMockers, watchedResp);
                    });
                });

                $scope.$watch('selected', function (nv) {
                    var s = [];
                    _.forEach(nv, function(v, k){
                        if (v) {
                            s.push(parseInt(k));
                        }
                    });
                    $scope.selectedMockers = s;
                }, true);

                $scope.$watch('selectedMockers', function (nv) {
                    if (_.isEmpty($scope.selected)) {
                        var s = {};
                        _.forEach(nv, function(mid){
                            s[mid] = true;
                        });
                        $scope.selected = s;
                    }
                });
            }
        ]);

})(window, window.angular);