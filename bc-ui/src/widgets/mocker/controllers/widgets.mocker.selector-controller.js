(function (window, angular) {
    "use strict";

    angular.module('module.widgets.mocker')
        .controller('widgets.mocker.SelectorController', [
            '$scope',
            'mockerService',
            'alertService',
            function ($scope, mockerService, alertService) {

                mockerService.getMyMockers().then(function(resp){
                    $scope.mockers = resp;
                });

                $scope.selectedMockers = [];
            }
        ]);

})(window, window.angular);