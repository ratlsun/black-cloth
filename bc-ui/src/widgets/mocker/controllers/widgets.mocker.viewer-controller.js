(function (window, angular) {
    "use strict";

    angular.module('module.widgets.mocker')
        .controller('widgets.mocker.ViewerController', [
            '$scope',
            'mockerService',
            function ($scope, mockerService) {

                mockerService.getMockerById($scope.mockerId).then(function(resp){
                    $scope.mocker = resp;
                });

            }
        ]);

})(window, window.angular);