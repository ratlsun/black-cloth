(function (window, angular) {
    "use strict";

    angular.module('module.widgets.mocker')
        .controller('widgets.mocker.SelectorController', [
            '$scope',
            'mockerService',
            'alertService',
            function ($scope, mockerService, alertService) {

                $scope.selected = {};

                mockerService.getMyMockers().then(function(resp){
                    $scope.mockers = resp;
                    mockerService.getCollect().then(function(resp){
                        $scope.mockers = _.union($scope.mockers, resp);
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